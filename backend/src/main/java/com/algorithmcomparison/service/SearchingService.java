package com.algorithmcomparison.service;

import com.algorithmcomparison.algorithm.searching.*;
import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.util.MetricsCollector;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * Optimized service for executing searching algorithms with unified data type handling.
 * 
 * @author Algorithm Comparison Team
 * @version 3.0 (Highly Optimized)
 */
@Service
public class SearchingService {

    private final DatasetService datasetService;

    public SearchingService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    // ==================== Public API ====================

    public AlgorithmResult executeSearchingAlgorithm(String sessionId, String datasetId, 
                                                     String algorithmName, int target) {
        return executeSearch(sessionId, datasetId, algorithmName, target, String.valueOf(target));
    }

    public AlgorithmResult executeSearchingAlgorithm(String sessionId, String datasetId, 
                                                     String algorithmName, String target) {
        return executeSearch(sessionId, datasetId, algorithmName, null, target);
    }

    public List<AlgorithmResult> compareAlgorithms(String sessionId, String datasetId, 
                                                    List<String> algorithmNames, int target) {
        return executeWithErrorHandling(algorithmNames, alg -> 
            executeSearchingAlgorithm(sessionId, datasetId, alg, target));
    }

    public List<AlgorithmResult> compareAlgorithms(String sessionId, String datasetId, 
                                                    List<String> algorithmNames, String target) {
        return executeWithErrorHandling(algorithmNames, alg -> 
            executeSearchingAlgorithm(sessionId, datasetId, alg, target));
    }

    public List<AlgorithmResult> compareOnMultipleDatasets(String sessionId, List<String> datasetIds, 
                                                           List<String> algorithmNames, int target) {
        return datasetIds.stream()
            .flatMap(id -> compareAlgorithms(sessionId, id, algorithmNames, target).stream())
            .toList();
    }

    public List<AlgorithmResult> compareOnMultipleDatasets(String sessionId, List<String> datasetIds, 
                                                           List<String> algorithmNames, String target) {
        return datasetIds.stream()
            .flatMap(id -> compareAlgorithms(sessionId, id, algorithmNames, target).stream())
            .toList();
    }

    public int selectTargetValue(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null || dataset.getData() == null || dataset.getData().length == 0) {
            return 0;
        }
        if ("STRING".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException(
                "Target selection only supported for INTEGER datasets. Dataset '" + 
                dataset.getName() + "' is STRING.");
        }
        return dataset.getData()[dataset.getData().length / 2];
    }

    public List<String> getAvailableAlgorithms() {
        return Arrays.asList("Linear Search", "Binary Search", "Trie Search", 
                           "Depth First Search", "Breadth First Search");
    }

    // ==================== Core Unified Logic ====================

    /**
     * Unified search execution for both String and Integer datasets.
     * Eliminates all duplication between data type handling.
     */
    private AlgorithmResult executeSearch(String sessionId, String datasetId, String algorithmName,
                                         Integer intTarget, String stringTarget) {
        Dataset dataset = getValidatedDataset(sessionId, datasetId);
        SearchingAlgorithm algorithm = getValidatedAlgorithm(algorithmName, dataset);
        MetricsCollector metrics = new MetricsCollector();

        int resultIndex = "STRING".equals(dataset.getDataType()) 
            ? performSearch(dataset.getStringData(), stringTarget, algorithm, metrics, "string data")
            : performSearch(dataset.getData(), intTarget != null ? intTarget : 0, algorithm, metrics, "data");

        return buildSearchResult(algorithm, dataset, metrics, resultIndex, 
                                intTarget != null ? intTarget : 0);
    }

    /**
     * Generic search operation template for any data type.
     * Handles validation, sorting (if needed), and execution.
     * 
     * @param <T> Array type (int[] or String[])
     * @param <V> Value type (Integer or String)
     */
    private <T, V> int performSearch(T data, V target, SearchingAlgorithm algorithm, 
                                     MetricsCollector metrics, String typeName) {
        validateData(data, typeName);
        
        T searchData = algorithm.requiresSortedArray() ? sortedCopy(data) : data;
        
        return executeSearchWithTiming(searchData, target, algorithm, metrics, typeName);
    }

    /**
     * Executes search with timing and proper error handling.
     */
    private <T, V> int executeSearchWithTiming(T data, V target, SearchingAlgorithm algorithm,
                                               MetricsCollector metrics, String typeName) {
        metrics.startTiming();
        try {
            int result = invokeSearch(algorithm, data, target, metrics);
            metrics.stopTiming();
            return result;
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException(
                algorithm.getName() + " does not support " + typeName.toUpperCase() + " datasets", e);
        }
    }

    /**
     * Invokes the appropriate search method based on data type.
     * Type-safe dispatch to algorithm methods.
     */
    @SuppressWarnings("unchecked")
    private <T, V> int invokeSearch(SearchingAlgorithm algorithm, T data, V target, 
                                    MetricsCollector metrics) {
        if (data instanceof String[] stringData && target instanceof String stringTarget) {
            return algorithm.search(stringData, stringTarget, metrics);
        } else if (data instanceof int[] intData && target instanceof Integer intTarget) {
            return algorithm.search(intData, intTarget, metrics);
        }
        throw new IllegalArgumentException("Unsupported data/target type combination");
    }

    // ==================== Helper Methods ====================

    private Dataset getValidatedDataset(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }
        return dataset;
    }

    private SearchingAlgorithm getValidatedAlgorithm(String algorithmName, Dataset dataset) {
        SearchingAlgorithm algorithm = createSearchingAlgorithm(algorithmName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unknown searching algorithm: " + algorithmName);
        }
        if ("Trie Search".equals(algorithmName) && "INTEGER".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException("Trie Search only supports STRING datasets.");
        }
        return algorithm;
    }

    private void validateData(Object data, String typeName) {
        if (data == null) {
            throw new IllegalStateException("Dataset has no " + typeName + " to search");
        }
    }

    /**
     * Creates a sorted copy of any array type if needed.
     */
    @SuppressWarnings("unchecked")
    private <T> T sortedCopy(T original) {
        if (original instanceof int[] intArray) {
            int[] copy = Arrays.copyOf(intArray, intArray.length);
            Arrays.sort(copy);
            return (T) copy;
        } else if (original instanceof String[] stringArray) {
            String[] copy = Arrays.copyOf(stringArray, stringArray.length);
            Arrays.sort(copy);
            return (T) copy;
        }
        throw new IllegalArgumentException("Unsupported array type for sorting");
    }

    private <T> List<T> executeWithErrorHandling(List<String> algorithmNames, 
                                                  Function<String, T> executor) {
        List<T> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        algorithmNames.forEach(algorithmName -> {
            try {
                results.add(executor.apply(algorithmName));
            } catch (Exception e) {
                errors.add(algorithmName + ": " + e.getMessage());
                System.err.println("Error executing " + algorithmName + ": " + e.getMessage());
            }
        });

        if (results.isEmpty() && !errors.isEmpty()) {
            throw new IllegalStateException(
                "All algorithms failed. Errors: " + String.join("; ", errors));
        }

        return results;
    }

    private AlgorithmResult buildSearchResult(SearchingAlgorithm algorithm, Dataset dataset, 
                                             MetricsCollector metrics, int resultIndex, int target) {
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
            .complexity(algorithm.getTimeComplexity())
            .resultType("SEARCH")
            .build();
    }

    private SearchingAlgorithm createSearchingAlgorithm(String algorithmName) {
        return switch (algorithmName.toLowerCase().replace(" ", "")) {
            case "linearsearch" -> new LinearSearch();
            case "binarysearch" -> new BinarySearch();
            case "triesearch" -> new TrieSearch();
            case "depthfirstsearch", "dfs" -> new DepthFirstSearch();
            case "breadthfirstsearch", "bfs" -> new BreadthFirstSearch();
            default -> null;
        };
    }
}
