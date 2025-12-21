package com.algorithmcomparison.service;

import com.algorithmcomparison.algorithm.sorting.*;
import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.util.MetricsCollector;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for executing sorting algorithms and collecting results.
 * 
 * Provides functionality for:
 * - Running individual sorting algorithms
 * - Comparing multiple sorting algorithms
 * - Factory pattern for algorithm instantiation
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@Service
public class SortingService {

    private final DatasetService datasetService;

    /**
     * Constructor with dependency injection.
     * 
     * @param datasetService Service for dataset management
     */
    public SortingService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    /**
     * Executes a sorting algorithm on a dataset and collects metrics.
     * 
     * @param datasetId ID of the dataset to sort
     * @param algorithmName Name of the sorting algorithm
     * @return AlgorithmResult with performance metrics
     * @throws IllegalArgumentException if dataset not found or algorithm unknown
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public AlgorithmResult executeSortingAlgorithm(String datasetId, String algorithmName) {
        // Get dataset
        Dataset dataset = datasetService.getDataset(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        // Check dataset type
        if ("STRING".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException(
                "Sorting algorithms currently only support INTEGER datasets. " +
                "Dataset '" + dataset.getName() + "' is of type STRING."
            );
        }

        if (dataset.getData() == null) {
            throw new IllegalStateException("Dataset has no data to sort");
        }

        // Create a copy of the data to avoid modifying original
        int[] dataCopy = Arrays.copyOf(dataset.getData(), dataset.getData().length);

        // Get algorithm instance
        SortingAlgorithm algorithm = createSortingAlgorithm(algorithmName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unknown sorting algorithm: " + algorithmName);
        }

        // Create metrics collector
        MetricsCollector metrics = new MetricsCollector();

        // Execute algorithm with timing
        metrics.startTiming();
        algorithm.sort(dataCopy, metrics);
        metrics.stopTiming();

        // Build and return result
        return new AlgorithmResult.Builder()
                .algorithmName(algorithm.getName())
                .datasetId(dataset.getId())
                .datasetName(dataset.getName())
                .datasetSize(dataset.getSize())
                .executionTimeNanos(metrics.getExecutionTimeNanos())
                .comparisonCount(metrics.getComparisonCount())
                .swapCount(metrics.getSwapCount())
                .arrayAccessCount(metrics.getArrayAccessCount())
                .complexity(algorithm.getTimeComplexity())
                .resultType("SORT")
                .build();
    }

    /**
     * Compares multiple sorting algorithms on a single dataset.
     * 
     * @param datasetId ID of the dataset
     * @param algorithmNames List of algorithm names to compare
     * @return List of AlgorithmResults, one for each algorithm
     */
    public List<AlgorithmResult> compareAlgorithms(String datasetId, List<String> algorithmNames) {
        List<AlgorithmResult> results = new ArrayList<>();

        for (String algorithmName : algorithmNames) {
            try {
                AlgorithmResult result = executeSortingAlgorithm(datasetId, algorithmName);
                results.add(result);
            } catch (Exception e) {
                System.err.println("Error executing " + algorithmName + ": " + e.getMessage());
            }
        }

        return results;
    }

    /**
     * Compares multiple sorting algorithms across multiple datasets.
     * 
     * @param datasetIds List of dataset IDs
     * @param algorithmNames List of algorithm names
     * @return List of all AlgorithmResults
     */
    public List<AlgorithmResult> compareOnMultipleDatasets(List<String> datasetIds, 
                                                           List<String> algorithmNames) {
        List<AlgorithmResult> allResults = new ArrayList<>();

        for (String datasetId : datasetIds) {
            List<AlgorithmResult> datasetResults = compareAlgorithms(datasetId, algorithmNames);
            allResults.addAll(datasetResults);
        }

        return allResults;
    }

    /**
     * Gets a list of all available sorting algorithms.
     * 
     * @return List of algorithm names
     */
    public List<String> getAvailableAlgorithms() {
        return Arrays.asList(
            "Bubble Sort",
            "Selection Sort",
            "Insertion Sort",
            "Quick Sort",
            "Merge Sort",
            "Heap Sort",
            "Shell Sort",
            "Counting Sort"
        );
    }

    /**
     * Factory method to create sorting algorithm instances.
     * Uses the Factory Pattern to instantiate algorithms by name.
     * 
     * @param algorithmName Name of the algorithm
     * @return SortingAlgorithm instance, or null if unknown
     */
    private SortingAlgorithm createSortingAlgorithm(String algorithmName) {
        switch (algorithmName.toLowerCase().replace(" ", "")) {
            case "bubblesort":
                return new BubbleSort();
            case "selectionsort":
                return new SelectionSort();
            case "insertionsort":
                return new InsertionSort();
            case "quicksort":
                return new QuickSort();
            case "mergesort":
                return new MergeSort();
            case "heapsort":
                return new HeapSort();
            case "shellsort":
                return new ShellSort();
            case "countingsort":
                return new CountingSort();
            default:
                return null;
        }
    }

    /**
     * Verifies if an array is correctly sorted.
     * Useful for testing and validation.
     * 
     * @param array Array to verify
     * @return true if sorted in ascending order
     */
    public boolean verifySorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}

