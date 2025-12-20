package com.algorithmcomparison.service;

import com.algorithmcomparison.model.VisualizationStep;
import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.algorithm.sorting.BubbleSort;
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
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public List<VisualizationStep> visualizeBubbleSort(String datasetId) {
        Dataset dataset = datasetService.getDataset(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        // Check dataset type
        if ("STRING".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException(
                "Visualization is currently only supported for INTEGER datasets. " +
                "Dataset '" + dataset.getName() + "' is of type STRING."
            );
        }

        if (dataset.getData() == null) {
            throw new IllegalStateException("Dataset has no data to visualize");
        }

        // Create a copy of the data to avoid modifying the original
        int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
        
        // Create collectors
        MetricsCollector metrics = new MetricsCollector();
        StepCollector stepCollector = new StepCollector();
        
        // Call the actual BubbleSort algorithm with step collection
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.sortWithSteps(array, metrics, stepCollector);
        
        // Return the collected steps
        return stepCollector.getSteps();
    }

    /**
     * Generates visualization steps for a generic algorithm.
     * Placeholder method that returns basic steps.
     * 
     * @param datasetId The dataset ID
     * @param algorithmName The algorithm name
     * @return List of visualization steps
     * @throws IllegalArgumentException if dataset not found
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public List<VisualizationStep> visualizeAlgorithm(String datasetId, String algorithmName) {
        // For now, only Bubble Sort is fully implemented
        if (algorithmName.equalsIgnoreCase("Bubble Sort")) {
            return visualizeBubbleSort(datasetId);
        }

        // Return basic visualization for other algorithms
        Dataset dataset = datasetService.getDataset(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        // Check dataset type
        if ("STRING".equals(dataset.getDataType())) {
            throw new UnsupportedOperationException(
                "Visualization is currently only supported for INTEGER datasets. " +
                "Dataset '" + dataset.getName() + "' is of type STRING."
            );
        }

        if (dataset.getData() == null) {
            throw new IllegalStateException("Dataset has no data to visualize");
        }

        List<VisualizationStep> steps = new ArrayList<>();
        
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

        return steps;
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

