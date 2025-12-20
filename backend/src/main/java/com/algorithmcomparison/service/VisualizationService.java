package com.algorithmcomparison.service;

import com.algorithmcomparison.model.VisualizationStep;
import com.algorithmcomparison.model.Dataset;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for generating algorithm visualization steps.
 * 
 * Provides functionality for:
 * - Capturing step-by-step algorithm execution
 * - Creating visualization data for frontend
 * - Tracking array state changes
 * 
 * Note: This is a simplified visualization service.
 * Full implementation would integrate deeply with algorithm execution.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
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
     * This is a demonstration implementation.
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

        int[] array = Arrays.copyOf(dataset.getData(), dataset.getData().length);
        List<VisualizationStep> steps = new ArrayList<>();
        
        int stepNumber = 0;
        int n = array.length;

        // Initial state
        VisualizationStep initialStep = new VisualizationStep(stepNumber++, array, "INIT");
        initialStep.setDescription("Initial array state");
        steps.add(initialStep);

        // Bubble sort with visualization
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare step
                VisualizationStep compareStep = new VisualizationStep(stepNumber++, array, "COMPARE");
                compareStep.addHighlightedIndex(j, "RED");
                compareStep.addHighlightedIndex(j + 1, "RED");
                compareStep.setDescription("Comparing elements at index " + j + " and " + (j + 1));
                steps.add(compareStep);

                // Swap if needed
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    VisualizationStep swapStep = new VisualizationStep(stepNumber++, array, "SWAP");
                    swapStep.addHighlightedIndex(j, "YELLOW");
                    swapStep.addHighlightedIndex(j + 1, "YELLOW");
                    swapStep.setDescription("Swapped elements at index " + j + " and " + (j + 1));
                    steps.add(swapStep);
                }
            }
        }

        // Final sorted state
        VisualizationStep finalStep = new VisualizationStep(stepNumber++, array, "COMPLETE");
        for (int i = 0; i < array.length; i++) {
            finalStep.addHighlightedIndex(i, "GREEN");
        }
        finalStep.setDescription("Sorting complete!");
        steps.add(finalStep);

        return steps;
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

