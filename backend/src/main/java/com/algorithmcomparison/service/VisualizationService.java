package com.algorithmcomparison.service;

import com.algorithmcomparison.model.VisualizationStep;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.algorithm.sorting.BubbleSort;
import com.algorithmcomparison.algorithm.sorting.InsertionSort;
import com.algorithmcomparison.algorithm.sorting.SelectionSort;
import com.algorithmcomparison.algorithm.sorting.MergeSort;
import com.algorithmcomparison.algorithm.searching.LinearSearch;
import com.algorithmcomparison.algorithm.searching.BinarySearch;
import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for generating algorithm visualization steps.
 * 
 * This service calls the actual algorithm implementations with a StepCollector
 * to gather visualization steps. No algorithm logic is duplicated here.
 * 
 * Supports both sorting and searching algorithms.
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
@Service
public class VisualizationService {

    private final DatasetService datasetService;

    /**
     * Constructor with dependency injection.
     * 
     * @param datasetService Service for dataset management
     */
    public VisualizationService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    /**
     * Generates visualization steps for Bubble Sort algorithm.
     * 
     * This method calls the actual BubbleSort implementation with a StepCollector.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return List of visualization steps
     * @throws IllegalArgumentException if dataset not found
     */
    public List<VisualizationStep> visualizeBubbleSort(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        // Create collectors
        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        
        // Call the actual BubbleSort algorithm with step collection
        BubbleSort bubbleSort = new BubbleSort();
        
        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            // Create a copy of the data to avoid modifying the original
            String[] array = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            bubbleSort.sortWithSteps(array, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            // Create a copy of the data to avoid modifying the original
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            bubbleSort.sortWithSteps(array, metrics, stepCollector);
        }
        
        // Return the collected steps
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for a generic algorithm.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @param algorithmName The algorithm name
     * @param target Optional target value for search algorithms (null for sorting)
     * @param sortOrder "ASCENDING" or "DESCENDING" for sorting algorithms
     * @return List of visualization steps
     * @throws IllegalArgumentException if dataset not found or too large
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public List<VisualizationStep> visualizeAlgorithm(String sessionId, String datasetId, String algorithmName, String target, String sortOrder) {
        boolean isDescending = "DESCENDING".equalsIgnoreCase(sortOrder);
        
        // Get dataset and check size
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }
        
        // Check dataset size limit for visualization
        int maxSize = getMaxVisualizationSize();
        if (dataset.getSize() > maxSize) {
            throw new IllegalArgumentException(
                "Dataset too large for visualization (" + dataset.getSize() + " elements). " +
                "Maximum recommended size is " + maxSize + " elements. " +
                "Large datasets generate too many visualization steps and may cause performance issues."
            );
        }
        
        // Check if algorithm has full visualization support
        if (algorithmName.equalsIgnoreCase("Bubble Sort")) {
            List<VisualizationStep> steps = visualizeBubbleSort(sessionId, datasetId);
            if (isDescending) {
                reverseVisualizationSteps(steps);
            }
            return steps;
        } else if (algorithmName.equalsIgnoreCase("Insertion Sort")) {
            List<VisualizationStep> steps = visualizeInsertionSort(sessionId, datasetId);
            if (isDescending) {
                reverseVisualizationSteps(steps);
            }
            return steps;
        } else if (algorithmName.equalsIgnoreCase("Selection Sort")) {
            List<VisualizationStep> steps = visualizeSelectionSort(sessionId, datasetId);
            if (isDescending) {
                reverseVisualizationSteps(steps);
            }
            return steps;
        } else if (algorithmName.equalsIgnoreCase("Merge Sort")) {
            List<VisualizationStep> steps = visualizeMergeSort(sessionId, datasetId);
            if (isDescending) {
                reverseVisualizationSteps(steps);
            }
            return steps;
        } else if (algorithmName.equalsIgnoreCase("Linear Search")) {
            return visualizeLinearSearch(sessionId, datasetId, target);
        } else if (algorithmName.equalsIgnoreCase("Binary Search")) {
            return visualizeBinarySearch(sessionId, datasetId, target);
        }

        // Return basic visualization for other algorithms (only initial and final states)
        List<VisualizationStep> steps = new ArrayList<>();
        
        // Handle both INTEGER and STRING datasets for basic visualization
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            
            // Initial state
            VisualizationStep initialStep = new VisualizationStep(0, dataset.getStringData(), "INIT");
            initialStep.setDescription("Initial state - " + algorithmName);
            steps.add(initialStep);

            // Sorted state (assuming algorithm completes successfully)
            String[] sortedArray = new String[dataset.getStringData().length];
            System.arraycopy(dataset.getStringData(), 0, sortedArray, 0, dataset.getStringData().length);
            Arrays.sort(sortedArray);
            
            // Reverse if descending
            if (isDescending) {
                reverseArray(sortedArray);
            }
            
            VisualizationStep finalStep = new VisualizationStep(1, sortedArray, "COMPLETE");
            finalStep.setDescription(algorithmName + " complete" + (isDescending ? " (descending)" : ""));
            steps.add(finalStep);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            
            // Initial state
            VisualizationStep initialStep = new VisualizationStep(0, dataset.getData(), "INIT");
            initialStep.setDescription("Initial state - " + algorithmName);
            steps.add(initialStep);

            // Sorted state (assuming algorithm completes successfully)
            int[] sortedArray = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            Arrays.sort(sortedArray);
            
            // Reverse if descending
            if (isDescending) {
                reverseArray(sortedArray);
            }
            
            VisualizationStep finalStep = new VisualizationStep(1, sortedArray, "COMPLETE");
            finalStep.setDescription(algorithmName + " complete" + (isDescending ? " (descending)" : ""));
            steps.add(finalStep);
        }

        return steps;
    }
    
    /**
     * Reverses all array states in visualization steps for descending order.
     * Also adjusts highlighted indices to match the reversed positions.
     * 
     * @param steps List of visualization steps to reverse
     */
    private void reverseVisualizationSteps(List<VisualizationStep> steps) {
        for (VisualizationStep step : steps) {
            Object[] arrayState = step.getArrayState();
            if (arrayState != null && arrayState.length > 0) {
                int arrayLength = arrayState.length;
                
                // Reverse the array state
                reverseArrayState(arrayState);
                
                // Reverse the highlighted indices to match new positions
                List<Integer> oldIndices = step.getHighlightedIndices();
                if (oldIndices != null && !oldIndices.isEmpty()) {
                    List<Integer> newIndices = new ArrayList<>();
                    
                    for (int oldIndex : oldIndices) {
                        // Calculate the reversed index: newIndex = arrayLength - 1 - oldIndex
                        int newIndex = arrayLength - 1 - oldIndex;
                        newIndices.add(newIndex);
                    }
                    
                    step.setHighlightedIndices(newIndices);
                }
            }
        }
    }
    
    /**
     * Reverses an Object array in place.
     * 
     * @param array Array to reverse
     */
    private void reverseArrayState(Object[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            Object temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }
    
    /**
     * Reverses an integer array in place.
     * 
     * @param array Array to reverse
     */
    private void reverseArray(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }
    
    /**
     * Reverses a string array in place.
     * 
     * @param array Array to reverse
     */
    private void reverseArray(String[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            String temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Generates visualization steps for Insertion Sort algorithm.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeInsertionSort(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        
        InsertionSort insertionSort = new InsertionSort();
        
        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            String[] array = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            insertionSort.sortWithSteps(array, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            insertionSort.sortWithSteps(array, metrics, stepCollector);
        }
        
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for Selection Sort algorithm.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeSelectionSort(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        
        SelectionSort selectionSort = new SelectionSort();
        
        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            String[] array = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            selectionSort.sortWithSteps(array, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            selectionSort.sortWithSteps(array, metrics, stepCollector);
        }
        
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for Merge Sort algorithm.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeMergeSort(String sessionId, String datasetId) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        
        MergeSort mergeSort = new MergeSort();
        
        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            String[] array = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            mergeSort.sortWithSteps(array, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            mergeSort.sortWithSteps(array, metrics, stepCollector);
        }
        
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for Linear Search algorithm.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @param target Optional target value to search for (if null, uses middle element)
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeLinearSearch(String sessionId, String datasetId, String target) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        LinearSearch linearSearch = new LinearSearch();
        
        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            String[] array = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            // Use provided target or default to middle element
            String searchTarget = (target != null && !target.isEmpty()) ? target : 
                                 (array.length > 0 ? array[array.length / 2] : "");
            linearSearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            // Parse target as integer or use default to middle element
            int searchTarget;
            if (target != null && !target.isEmpty()) {
                try {
                    searchTarget = Integer.parseInt(target);
                } catch (NumberFormatException e) {
                    // If parsing fails, use middle element
                    searchTarget = array.length > 0 ? array[array.length / 2] : 0;
                }
            } else {
                searchTarget = array.length > 0 ? array[array.length / 2] : 0;
            }
            linearSearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        }
        
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for Binary Search algorithm.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @param target Optional target value to search for (if null, uses middle element of sorted array)
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeBinarySearch(String sessionId, String datasetId, String target) {
        Dataset dataset = datasetService.getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        BinarySearch binarySearch = new BinarySearch();
        
        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            if (dataset.getStringData() == null) {
                throw new IllegalStateException("Dataset has no string data to visualize");
            }
            String[] array = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            // Sort the array for binary search
            Arrays.sort(array);
            // Use provided target or default to middle element of sorted array
            String searchTarget = (target != null && !target.isEmpty()) ? target : 
                                 (array.length > 0 ? array[array.length / 2] : "");
            binarySearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            // Sort the array for binary search
            Arrays.sort(array);
            // Parse target as integer or use default to middle element of sorted array
            int searchTarget;
            if (target != null && !target.isEmpty()) {
                try {
                    searchTarget = Integer.parseInt(target);
                } catch (NumberFormatException e) {
                    // If parsing fails, use middle element
                    searchTarget = array.length > 0 ? array[array.length / 2] : 0;
                }
            } else {
                searchTarget = array.length > 0 ? array[array.length / 2] : 0;
            }
            binarySearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        }
        
        return stepCollector.getSteps();
    }

    /**
     * Gets the maximum recommended dataset size for visualization.
     * Large datasets produce too many steps and slow down visualization.
     * 
     * @return Maximum recommended size
     */
    public int getMaxVisualizationSize() {
        return 100; // Limit to 100 elements for smooth visualization
    }
}

