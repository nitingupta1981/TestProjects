package com.algorithmcomparison.service;

import com.algorithmcomparison.algorithm.sorting.*;
import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.util.MetricsCollector;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Optimized service for executing sorting algorithms with unified data type handling.
 * 
 * @author Algorithm Comparison Team
 * @version 3.0 (Highly Optimized)
 */
@Service
public class SortingService {

    private final DatasetService datasetService;

    public SortingService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    // ==================== Public API ====================

    public AlgorithmResult executeSortingAlgorithm(String sessionId, String datasetId, 
                                                   String algorithmName, String sortOrder) {
        Dataset dataset = getValidatedDataset(sessionId, datasetId);
        SortingAlgorithm algorithm = getValidatedAlgorithm(algorithmName);
        
        // Validate algorithm supports the dataset type
        if ("STRING".equals(dataset.getDataType()) && isIntegerOnlyAlgorithm(algorithmName)) {
            throw new UnsupportedOperationException(
                algorithmName + " does not support STRING datasets. " +
                "Dataset '" + dataset.getName() + "' is of type STRING. " +
                "Please use: Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, or Merge Sort for STRING data.");
        }
        
        boolean isDescending = "DESCENDING".equalsIgnoreCase(sortOrder);

        System.out.println("DEBUG: Executing " + algorithmName + " on dataset " + datasetId + 
                         " (type: " + dataset.getDataType() + ", order: " + sortOrder + ")");

        MetricsCollector metrics = new MetricsCollector();
        executeSort(dataset, algorithm, metrics, isDescending);
        
        return buildResult(algorithm, dataset, metrics, isDescending);
    }

    public List<AlgorithmResult> compareAlgorithms(String sessionId, String datasetId, 
                                                    List<String> algorithmNames, String sortOrder) {
        System.out.println("DEBUG: compareAlgorithms - session=" + sessionId + ", dataset=" + datasetId);
        return executeWithErrorHandling(algorithmNames, alg -> 
            executeSortingAlgorithm(sessionId, datasetId, alg, sortOrder));
    }

    public List<AlgorithmResult> compareOnMultipleDatasets(String sessionId, List<String> datasetIds, 
                                                           List<String> algorithmNames, String sortOrder) {
        return datasetIds.stream()
            .flatMap(id -> compareAlgorithms(sessionId, id, algorithmNames, sortOrder).stream())
            .toList();
    }

    public List<String> getAvailableAlgorithms() {
        return Arrays.asList("Bubble Sort", "Selection Sort", "Insertion Sort", 
                           "Quick Sort", "Merge Sort", "Heap Sort", "Shell Sort", "Counting Sort");
    }

    public boolean verifySorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) return false;
        }
        return true;
    }

    // ==================== Core Unified Logic ====================

    /**
     * Unified sort execution for both String and Integer datasets.
     * Uses strategy pattern to eliminate code duplication.
     */
    private void executeSort(Dataset dataset, SortingAlgorithm algorithm, 
                            MetricsCollector metrics, boolean isDescending) {
        if ("STRING".equals(dataset.getDataType())) {
            executeSortForType(
                dataset.getStringData(),
                "string data",
                data -> algorithm.sort(data, metrics),
                this::reverseArray,
                metrics,
                isDescending
            );
        } else {
            executeSortForType(
                dataset.getData(),
                "data",
                data -> algorithm.sort(data, metrics),
                this::reverseArray,
                metrics,
                isDescending
            );
        }
    }

    /**
     * Generic sort execution template that works for any array type.
     * Eliminates duplication between String and Integer handling.
     * 
     * @param <T> Array type (int[] or String[])
     */
    private <T> void executeSortForType(T originalData, String dataTypeName,
                                        Consumer<T> sortOperation,
                                        Consumer<T> reverseOperation,
                                        MetricsCollector metrics,
                                        boolean isDescending) {
        validateData(originalData, dataTypeName);
        
        T dataCopy = copyArray(originalData);
        
        metrics.startTiming();
        try {
            sortOperation.accept(dataCopy);
            if (isDescending) {
                reverseOperation.accept(dataCopy);
            }
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException(
                "Algorithm does not support " + dataTypeName.toUpperCase() + " datasets", e);
        } finally {
            metrics.stopTiming();
        }
        
        System.out.println("DEBUG: " + dataTypeName.toUpperCase() + " sort completed. " +
                         "Comparisons: " + metrics.getComparisonCount() + 
                         ", Swaps: " + metrics.getSwapCount());
    }

    // ==================== Helper Methods ====================

    private Dataset getValidatedDataset(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }
        return dataset;
        }

    private SortingAlgorithm getValidatedAlgorithm(String algorithmName) {
        SortingAlgorithm algorithm = createSortingAlgorithm(algorithmName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Unknown sorting algorithm: " + algorithmName);
        }
        return algorithm;
    }

    private void validateData(Object data, String typeName) {
        if (data == null) {
            throw new IllegalStateException("Dataset has no " + typeName + " to sort");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T copyArray(T original) {
        if (original instanceof int[] intArray) {
            return (T) Arrays.copyOf(intArray, intArray.length);
        } else if (original instanceof String[] stringArray) {
            return (T) Arrays.copyOf(stringArray, stringArray.length);
        }
        throw new IllegalArgumentException("Unsupported array type");
    }

    private <T> List<T> executeWithErrorHandling(List<String> algorithmNames, 
                                                  Function<String, T> executor) {
        List<T> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        algorithmNames.forEach(algorithmName -> {
            try {
                results.add(executor.apply(algorithmName));
                System.out.println("DEBUG: Added result for " + algorithmName);
            } catch (Exception e) {
                String errorMsg = algorithmName + ": " + e.getMessage();
                System.err.println("Error executing " + errorMsg);
                errors.add(errorMsg);
            }
        });

        if (results.isEmpty() && !errors.isEmpty()) {
            throw new IllegalStateException(
                "All algorithms failed to execute. Errors: " + String.join("; ", errors));
        }

        System.out.println("DEBUG: Total results: " + results.size() + ", errors: " + errors.size());
        return results;
    }

    private AlgorithmResult buildResult(SortingAlgorithm algorithm, Dataset dataset, 
                                       MetricsCollector metrics, boolean isDescending) {
        return new AlgorithmResult.Builder()
            .algorithmName(algorithm.getName() + (isDescending ? " (DESC)" : " (ASC)"))
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

    private SortingAlgorithm createSortingAlgorithm(String algorithmName) {
        return switch (algorithmName.toLowerCase().replace(" ", "")) {
            case "bubblesort" -> new BubbleSort();
            case "selectionsort" -> new SelectionSort();
            case "insertionsort" -> new InsertionSort();
            case "quicksort" -> new QuickSort();
            case "mergesort" -> new MergeSort();
            case "heapsort" -> new HeapSort();
            case "shellsort" -> new ShellSort();
            case "countingsort" -> new CountingSort();
            default -> null;
        };
    }

    private void reverseArray(int[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private void reverseArray(String[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    /**
     * Checks if a sorting algorithm only supports integer datasets.
     * 
     * @param algorithmName Name of the algorithm
     * @return true if algorithm only supports integers
     */
    private boolean isIntegerOnlyAlgorithm(String algorithmName) {
        String normalized = algorithmName.toLowerCase().replace(" ", "");
        return normalized.equals("heapsort") || 
               normalized.equals("shellsort") ||
               normalized.equals("countingsort");
    }
}
