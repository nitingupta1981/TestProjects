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
 * Supports both sorting and searching algorithms.
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
     * Records the initial state (for integer arrays).
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
     * Records the initial state (for string arrays).
     */
    public void recordInitial(String[] array, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "INIT"
        );
        step.setDescription(description);
        steps.add(step);
    }
    
    /**
     * Records a comparison (for sorting integer arrays).
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
     * Records a comparison (for sorting string arrays).
     */
    public void recordCompare(String[] array, int index1, int index2, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "COMPARE"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index1, "RED");
        step.addHighlightedIndex(index2, "RED");
        steps.add(step);
    }
    
    /**
     * Records a swap for integer arrays (call AFTER the swap is done).
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
     * Records a swap for string arrays (call AFTER the swap is done).
     */
    public void recordSwap(String[] array, int index1, int index2, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "SWAP"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index1, "YELLOW");
        step.addHighlightedIndex(index2, "YELLOW");
        steps.add(step);
    }
    
    /**
     * Records completion for integer arrays.
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
     * Records completion for string arrays.
     */
    public void recordComplete(String[] array, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
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
     * Records a set/insertion operation for integer arrays.
     */
    public void recordSet(int[] array, int index, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "SET"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index, "BLUE");
        steps.add(step);
    }
    
    /**
     * Records a set/insertion operation for string arrays.
     */
    public void recordSet(String[] array, int index, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "SET"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index, "BLUE");
        steps.add(step);
    }
    
    /**
     * Records a check operation (for searching) on integer arrays.
     */
    public void recordCheck(int[] array, int index, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "CHECK"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index, "ORANGE");
        steps.add(step);
    }
    
    /**
     * Records a check operation (for searching) on string arrays.
     */
    public void recordCheck(String[] array, int index, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "CHECK"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index, "ORANGE");
        steps.add(step);
    }
    
    /**
     * Records a range being examined (for binary search) on integer arrays.
     */
    public void recordRange(int[] array, int left, int right, int mid, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "RANGE"
        );
        step.setDescription(description);
        // Highlight the range in light blue and the middle in orange
        for (int i = left; i <= right; i++) {
            step.addHighlightedIndex(i, "LIGHTBLUE");
        }
        step.addHighlightedIndex(mid, "ORANGE");
        steps.add(step);
    }
    
    /**
     * Records a range being examined (for binary search) on string arrays.
     */
    public void recordRange(String[] array, int left, int right, int mid, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "RANGE"
        );
        step.setDescription(description);
        // Highlight the range in light blue and the middle in orange
        for (int i = left; i <= right; i++) {
            step.addHighlightedIndex(i, "LIGHTBLUE");
        }
        step.addHighlightedIndex(mid, "ORANGE");
        steps.add(step);
    }
    
    /**
     * Records when target is found (for searching) on integer arrays.
     */
    public void recordFound(int[] array, int index, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "FOUND"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index, "GREEN");
        steps.add(step);
    }
    
    /**
     * Records when target is found (for searching) on string arrays.
     */
    public void recordFound(String[] array, int index, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "FOUND"
        );
        step.setDescription(description);
        step.addHighlightedIndex(index, "GREEN");
        steps.add(step);
    }
    
    /**
     * Records when target is not found (for searching) on integer arrays.
     */
    public void recordNotFound(int[] array, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "NOT_FOUND"
        );
        step.setDescription(description);
        steps.add(step);
    }
    
    /**
     * Records when target is not found (for searching) on string arrays.
     */
    public void recordNotFound(String[] array, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "NOT_FOUND"
        );
        step.setDescription(description);
        steps.add(step);
    }
    
    /**
     * Records a custom step with specific highlighted indices (for integer arrays).
     * Useful for divide-and-conquer algorithms like Merge Sort.
     */
    public void recordStep(int[] array, int[] highlightIndices, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "CUSTOM"
        );
        step.setDescription(description);
        for (int index : highlightIndices) {
            step.addHighlightedIndex(index, "BLUE");
        }
        steps.add(step);
    }
    
    /**
     * Records a custom step with specific highlighted indices (for string arrays).
     * Useful for divide-and-conquer algorithms like Merge Sort.
     */
    public void recordStep(String[] array, int[] highlightIndices, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "CUSTOM"
        );
        step.setDescription(description);
        for (int index : highlightIndices) {
            step.addHighlightedIndex(index, "BLUE");
        }
        steps.add(step);
    }
    
    /**
     * Records a step with active region highlighting for divide-and-conquer algorithms.
     * Elements in the active region are highlighted, others are dimmed.
     * 
     * @param array The current array state
     * @param activeLeft Left boundary of active region (inclusive)
     * @param activeRight Right boundary of active region (inclusive)
     * @param highlightIndices Specific indices to highlight within the active region
     * @param description Step description
     */
    public void recordRegionStep(int[] array, int activeLeft, int activeRight, int[] highlightIndices, String description) {
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            Arrays.copyOf(array, array.length), 
            "REGION"
        );
        step.setDescription(description);
        step.setActiveRegion(activeLeft, activeRight);
        
        // Highlight active indices in bright color
        for (int index : highlightIndices) {
            if (index >= activeLeft && index <= activeRight) {
                step.addHighlightedIndex(index, "BLUE");
            }
        }
        
        // Mark inactive regions with grey
        for (int i = 0; i < array.length; i++) {
            if (i < activeLeft || i > activeRight) {
                step.addHighlightedIndex(i, "GREY");
            }
        }
        
        steps.add(step);
    }
    
    /**
     * Records a step with active region highlighting for divide-and-conquer algorithms (string arrays).
     */
    public void recordRegionStep(String[] array, int activeLeft, int activeRight, int[] highlightIndices, String description) {
        String[] arrayCopy = new String[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        
        VisualizationStep step = new VisualizationStep(
            stepNumber++, 
            arrayCopy, 
            "REGION"
        );
        step.setDescription(description);
        step.setActiveRegion(activeLeft, activeRight);
        
        // Highlight active indices in bright color
        for (int index : highlightIndices) {
            if (index >= activeLeft && index <= activeRight) {
                step.addHighlightedIndex(index, "BLUE");
            }
        }
        
        // Mark inactive regions with grey
        for (int i = 0; i < array.length; i++) {
            if (i < activeLeft || i > activeRight) {
                step.addHighlightedIndex(i, "GREY");
            }
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

