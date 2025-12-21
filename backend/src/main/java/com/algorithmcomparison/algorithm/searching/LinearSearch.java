package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;

/**
 * Linear Search implementation.
 * 
 * Linear Search (also called Sequential Search) is the simplest searching algorithm
 * that checks every element in the array sequentially until the target is found
 * or the end of the array is reached.
 * 
 * Algorithm Steps:
 * 1. Start from the first element
 * 2. Compare each element with the target value
 * 3. If match is found, return the index
 * 4. If end of array is reached without finding target, return -1
 * 
 * Time Complexity: O(n) - must check all elements in worst case
 * Space Complexity: O(1) - uses only a constant amount of extra space
 * 
 * Best Use Cases:
 * - Small datasets
 * - Unsorted arrays (no preprocessing needed)
 * - When only one search is needed (sorting overhead not justified)
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class LinearSearch implements SearchingAlgorithm {

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
        return searchInternal(array, target, metrics, null);
    }

    @Override
    public int search(String[] array, String target, MetricsCollector metrics) {
        return searchStringInternal(array, target, metrics, null);
    }

    /**
     * Searches the array with optional step collection for visualization.
     * 
     * @param array The array to search
     * @param target The target value to find
     * @param metrics The metrics collector
     * @param stepCollector Optional step collector for visualization (null for normal search)
     * @return Index of target if found, -1 otherwise
     */
    public int searchWithSteps(int[] array, int target, MetricsCollector metrics, StepCollector stepCollector) {
        return searchInternal(array, target, metrics, stepCollector);
    }

    /**
     * Searches the string array with optional step collection for visualization.
     */
    public int searchWithSteps(String[] array, String target, MetricsCollector metrics, StepCollector stepCollector) {
        return searchStringInternal(array, target, metrics, stepCollector);
    }

    /**
     * Internal search implementation that optionally collects steps.
     */
    private int searchInternal(int[] array, int target, MetricsCollector metrics, StepCollector stepCollector) {
        // Record initial state if collecting steps
        if (stepCollector != null) {
            stepCollector.recordInitial(array, "Starting Linear Search for target: " + target);
        }
        
        // Iterate through each element sequentially
        for (int i = 0; i < array.length; i++) {
            metrics.recordArrayAccess(1); // Count array access
            
            // Record checking this element if collecting steps
            if (stepCollector != null) {
                stepCollector.recordCheck(array, i, 
                    "Checking index " + i + ": Is " + array[i] + " == " + target + "?");
            }
            
            // Compare current element with target
            if (metrics.isEqual(array[i], target)) {
                // Target found at index i
                if (stepCollector != null) {
                    stepCollector.recordFound(array, i,
                        "Target " + target + " found at index " + i + "!");
                }
                return i;
            }
        }
        
        // Target not found in array
        if (stepCollector != null) {
            stepCollector.recordNotFound(array,
                "Target " + target + " not found in the array");
        }
        
        return -1;
    }

    @Override
    public String getName() {
        return "Linear Search";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public boolean requiresSortedArray() {
        // Linear search works on both sorted and unsorted arrays
        return false;
    }

    /**
     * Internal search implementation for String arrays that optionally collects steps.
     */
    private int searchStringInternal(String[] array, String target, MetricsCollector metrics, StepCollector stepCollector) {
        // Record initial state if collecting steps
        if (stepCollector != null) {
            stepCollector.recordInitial(array, "Starting Linear Search for target: " + target);
        }
        
        // Iterate through each element sequentially
        for (int i = 0; i < array.length; i++) {
            metrics.recordArrayAccess(1);
            
            // Record checking this element if collecting steps
            if (stepCollector != null) {
                stepCollector.recordCheck(array, i, 
                    "Checking index " + i + ": Is " + array[i] + " == " + target + "?");
            }
            
            // Compare current element with target
            metrics.recordComparison(1);
            if (array[i].equals(target)) {
                // Target found at index i
                if (stepCollector != null) {
                    stepCollector.recordFound(array, i,
                        "Target " + target + " found at index " + i + "!");
                }
                return i;
            }
        }
        
        // Target not found in array
        if (stepCollector != null) {
            stepCollector.recordNotFound(array,
                "Target " + target + " not found in the array");
        }
        
        return -1;
    }
}

