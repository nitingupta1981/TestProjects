package com.algorithmcomparison.service;

import com.algorithmcomparison.algorithm.searching.*;
import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.util.MetricsCollector;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for executing searching algorithms and collecting results.
 * 
 * Provides functionality for:
 * - Running individual searching algorithms
 * - Comparing multiple searching algorithms
 * - Factory pattern for algorithm instantiation
 * - Handling both array-based and graph-based searches
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@Service
public class SearchingService {

    private final DatasetService datasetService;

    /**
     * Constructor with dependency injection.
     * 
     * @param datasetService Service for dataset management
     */
    public SearchingService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    /**
     * Executes a searching algorithm on a dataset and collects metrics.
     * 
     * @param datasetId ID of the dataset to search
     * @param algorithmName Name of the searching algorithm
     * @param target Value to search for
     * @return AlgorithmResult with performance metrics
     * @throws IllegalArgumentException if dataset not found or algorithm unknown
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public AlgorithmResult executeSearchingAlgorithm(String datasetId, String algorithmName, int target) {
        // Get dataset
        Dataset dataset = datasetService.getDataset(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        // Check dataset type
        if ("STRING".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException(
                "Searching algorithms currently only support INTEGER datasets. " +
                "Dataset '" + dataset.getName() + "' is of type STRING."
            );
        }

        if (dataset.getData() == null) {
            throw new IllegalStateException("Dataset has no data to search");
        }

        // Get algorithm instance
        SearchingAlgorithm algorithm = createSearchingAlgorithm(algorithmName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unknown searching algorithm: " + algorithmName);
        }

        // For binary search, verify array is sorted
        if (algorithm.requiresSortedArray()) {
            int[] dataCopy = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            Arrays.sort(dataCopy);
            return executeSearch(algorithm, dataCopy, target, dataset);
        } else {
            return executeSearch(algorithm, dataset.getData(), target, dataset);
        }
    }

    /**
     * Helper method to execute search and collect metrics.
     * 
     * @param algorithm The searching algorithm
     * @param data The data to search
     * @param target The target value
     * @param dataset The original dataset
     * @return AlgorithmResult with metrics
     */
    private AlgorithmResult executeSearch(SearchingAlgorithm algorithm, int[] data, 
                                         int target, Dataset dataset) {
        // Create metrics collector
        MetricsCollector metrics = new MetricsCollector();

        // Execute algorithm with timing
        metrics.startTiming();
        int resultIndex = algorithm.search(data, target, metrics);
        metrics.stopTiming();

        // Build and return result
        return new AlgorithmResult.Builder()
                .algorithmName(algorithm.getName())
                .datasetId(dataset.getId())
                .datasetName(dataset.getName())
                .datasetSize(dataset.getSize())
                .executionTimeNanos(metrics.getExecutionTimeNanos())
                .comparisonCount(metrics.getComparisonCount())
                .arrayAccessCount(metrics.getArrayAccessCount())
                .foundTarget(resultIndex != -1)
                .targetIndex(resultIndex != -1 ? resultIndex : null)
                .nodesVisited(metrics.getArrayAccessCount()) // For graph searches
                .complexity(algorithm.getTimeComplexity())
                .build();
    }

    /**
     * Compares multiple searching algorithms on a single dataset.
     * 
     * @param datasetId ID of the dataset
     * @param algorithmNames List of algorithm names to compare
     * @param target Value to search for
     * @return List of AlgorithmResults, one for each algorithm
     */
    public List<AlgorithmResult> compareAlgorithms(String datasetId, List<String> algorithmNames, 
                                                   int target) {
        List<AlgorithmResult> results = new ArrayList<>();

        for (String algorithmName : algorithmNames) {
            try {
                AlgorithmResult result = executeSearchingAlgorithm(datasetId, algorithmName, target);
                results.add(result);
            } catch (Exception e) {
                System.err.println("Error executing " + algorithmName + ": " + e.getMessage());
            }
        }

        return results;
    }

    /**
     * Compares multiple searching algorithms across multiple datasets.
     * 
     * @param datasetIds List of dataset IDs
     * @param algorithmNames List of algorithm names
     * @param target Value to search for
     * @return List of all AlgorithmResults
     */
    public List<AlgorithmResult> compareOnMultipleDatasets(List<String> datasetIds, 
                                                           List<String> algorithmNames,
                                                           int target) {
        List<AlgorithmResult> allResults = new ArrayList<>();

        for (String datasetId : datasetIds) {
            List<AlgorithmResult> datasetResults = compareAlgorithms(datasetId, algorithmNames, target);
            allResults.addAll(datasetResults);
        }

        return allResults;
    }

    /**
     * Automatically selects an appropriate target value from the dataset.
     * Uses a value that exists in the dataset for meaningful search results.
     * 
     * @param datasetId The dataset ID
     * @return A target value from the dataset
     * @throws IllegalArgumentException if dataset not found
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public int selectTargetValue(String datasetId) {
        Dataset dataset = datasetService.getDataset(datasetId);
        
        if (dataset == null) {
            return 0;
        }

        // Check dataset type
        if ("STRING".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException(
                "Target selection is currently only supported for INTEGER datasets. " +
                "Dataset '" + dataset.getName() + "' is of type STRING."
            );
        }

        if (dataset.getData() == null || dataset.getData().length == 0) {
            return 0;
        }

        // Select middle element as target
        int middleIndex = dataset.getData().length / 2;
        return dataset.getData()[middleIndex];
    }

    /**
     * Gets a list of all available searching algorithms.
     * 
     * @return List of algorithm names
     */
    public List<String> getAvailableAlgorithms() {
        return Arrays.asList(
            "Linear Search",
            "Binary Search",
            "Depth First Search",
            "Breadth First Search"
        );
    }

    /**
     * Factory method to create searching algorithm instances.
     * Uses the Factory Pattern to instantiate algorithms by name.
     * 
     * @param algorithmName Name of the algorithm
     * @return SearchingAlgorithm instance, or null if unknown
     */
    private SearchingAlgorithm createSearchingAlgorithm(String algorithmName) {
        switch (algorithmName.toLowerCase().replace(" ", "")) {
            case "linearsearch":
                return new LinearSearch();
            case "binarysearch":
                return new BinarySearch();
            case "depthfirstsearch":
            case "dfs":
                return new DepthFirstSearch();
            case "breadthfirstsearch":
            case "bfs":
                return new BreadthFirstSearch();
            default:
                return null;
        }
    }
}

