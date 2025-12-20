package com.algorithmcomparison.util;

import com.algorithmcomparison.model.VisualizationStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple utility to collect visualization steps during algorithm execution.
 * 
 * Algorithms can optionally use this to record their steps for visualization.
 * The collector just stores snapshots - it doesn't modify anything.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class StepCollector {
    
    private List<VisualizationStep> steps;
    private int stepNumber;
    
    public StepCollector() {
        this.steps = new ArrayList<>();
        this.stepNumber = 0;
    }
    
    /**
     * Records the initial state.
     */
    public void recordInitial(int[] array, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "INIT"
        );
        step.setDescription(description);
        steps.add(step);
    }
    
    /**
     * Records a comparison.
     */
    public void recordCompare(int[] array, int index1, int index2, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "COMPARE"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index1, "RED");
        step.addHighlightedIndex(index2, "RED");
        steps.add(step);
    }
    
    /**
     * Records a swap (call AFTER the swap is done).
     */
    public void recordSwap(int[] array, int index1, int index2, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "SWAP"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index1, "YELLOW");
        step.addHighlightedIndex(index2, "YELLOW");
        steps.add(step);
    }
    
    /**
     * Records completion.
     */
    public void recordComplete(int[] array, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "COMPLETE"
        );
        step.setDescription(description);
        // Highlight all in green
        for (int i = 0; i < array.length; i++) {
            step.addHighlightedIndex(i, "GREEN");
        }
        steps.add(step);
    }
    
    /**
     * Gets all collected steps.
     */
    public List<VisualizationStep> getSteps() {
        return steps;
    }
}

