package com.algorithmcomparison.service;

import com.algorithmcomparison.model.VisualizationStep;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.algorithm.sorting.BubbleSort;
import com.algorithmcomparison.algorithm.sorting.InsertionSort;
import com.algorithmcomparison.algorithm.sorting.SelectionSort;
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
     * @param datasetId The dataset ID
     * @return List of visualization steps
     * @throws IllegalArgumentException if dataset not found
     */
    public List<VisualizationStep> visualizeBubbleSort(String datasetId) {
        Dataset dataset = datasetService.getDataset(datasetId);
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
     * @param datasetId The dataset ID
     * @param algorithmName The algorithm name
     * @param target Optional target value for search algorithms (null for sorting)
     * @return List of visualization steps
     * @throws IllegalArgumentException if dataset not found
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public List<VisualizationStep> visualizeAlgorithm(String datasetId, String algorithmName, Integer target) {
        // Check if algorithm has full visualization support
        if (algorithmName.equalsIgnoreCase("Bubble Sort")) {
            return visualizeBubbleSort(datasetId);
        } else if (algorithmName.equalsIgnoreCase("Insertion Sort")) {
            return visualizeInsertionSort(datasetId);
        } else if (algorithmName.equalsIgnoreCase("Selection Sort")) {
            return visualizeSelectionSort(datasetId);
        } else if (algorithmName.equalsIgnoreCase("Linear Search")) {
            return visualizeLinearSearch(datasetId, target);
        } else if (algorithmName.equalsIgnoreCase("Binary Search")) {
            return visualizeBinarySearch(datasetId, target);
        }

        // Return basic visualization for other algorithms
        Dataset dataset = datasetService.getDataset(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

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
            String[] sortedArray = Arrays.copyOf(dataset.getStringData(), dataset.getStringData().length);
            Arrays.sort(sortedArray);
            
            VisualizationStep finalStep = new VisualizationStep(1, sortedArray, "COMPLETE");
            finalStep.setDescription(algorithmName + " complete");
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
            
            VisualizationStep finalStep = new VisualizationStep(1, sortedArray, "COMPLETE");
            finalStep.setDescription(algorithmName + " complete");
            steps.add(finalStep);
        }

        return steps;
    }

    /**
     * Generates visualization steps for Insertion Sort algorithm.
     * 
     * @param datasetId The dataset ID
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeInsertionSort(String datasetId) {
        Dataset dataset = datasetService.getDataset(datasetId);
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
     * @param datasetId The dataset ID
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeSelectionSort(String datasetId) {
        Dataset dataset = datasetService.getDataset(datasetId);
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
     * Generates visualization steps for Linear Search algorithm.
     * 
     * @param datasetId The dataset ID
     * @param target Optional target value to search for (if null, uses middle element)
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeLinearSearch(String datasetId, Integer target) {
        Dataset dataset = datasetService.getDataset(datasetId);
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
            String searchTarget = (target != null) ? String.valueOf(target) : 
                                 (array.length > 0 ? array[array.length / 2] : "");
            linearSearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            // Use provided target or default to middle element
            int searchTarget = (target != null) ? target : (array.length > 0 ? array[array.length / 2] : 0);
            linearSearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        }
        
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for Binary Search algorithm.
     * 
     * @param datasetId The dataset ID
     * @param target Optional target value to search for (if null, uses middle element of sorted array)
     * @return List of visualization steps
     */
    private List<VisualizationStep> visualizeBinarySearch(String datasetId, Integer target) {
        Dataset dataset = datasetService.getDataset(datasetId);
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
            String searchTarget = (target != null) ? String.valueOf(target) : 
                                 (array.length > 0 ? array[array.length / 2] : "");
            binarySearch.searchWithSteps(array, searchTarget, metrics, stepCollector);
        } else {
            if (dataset.getData() == null) {
                throw new IllegalStateException("Dataset has no data to visualize");
            }
            int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
            // Sort the array for binary search
            Arrays.sort(array);
            // Use provided target or default to middle element of sorted array
            int searchTarget = (target != null) ? target : (array.length > 0 ? array[array.length / 2] : 0);
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
        return 50; // Limit to 50 elements for smooth visualization
    }
}

