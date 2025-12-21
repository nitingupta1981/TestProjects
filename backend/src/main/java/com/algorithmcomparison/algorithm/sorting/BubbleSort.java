package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;

/**
 * Bubble Sort implementation.
 * 
 * Bubble Sort is a simple comparison-based sorting algorithm that repeatedly
 * steps through the list, compares adjacent elements and swaps them if they
 * are in the wrong order. The pass through the list is repeated until the list is sorted.
 * 
 * Time Complexity: O(n²) in worst and average cases, O(n) best case (already sorted)
 * Space Complexity: O(1) - sorts in place
 * Stable: Yes - equal elements maintain their relative order
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class BubbleSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        sortInternal(array, metrics, null);
    }

    @Override
    public void sort(String[] array, MetricsCollector metrics) {
        sortStringInternal(array, metrics, null);
    }

    /**
     * Sorts the array with optional step collection for visualization.
     * 
     * @param array The array to sort
     * @param metrics The metrics collector
     * @param stepCollector Optional step collector for visualization (null for normal sorting)
     */
    public void sortWithSteps(int[] array, MetricsCollector metrics, StepCollector stepCollector) {
        sortInternal(array, metrics, stepCollector);
    }

    /**
     * Sorts the string array with optional step collection for visualization.
     * 
     * @param array The string array to sort
     * @param metrics The metrics collector
     * @param stepCollector Optional step collector for visualization (null for normal sorting)
     */
    public void sortWithSteps(String[] array, MetricsCollector metrics, StepCollector stepCollector) {
        sortStringInternal(array, metrics, stepCollector);
    }

    /**
     * Internal sort implementation that optionally collects steps.
     */
    private void sortInternal(int[] array, MetricsCollector metrics, StepCollector stepCollector) {
        int n = array.length;
        
        // Record initial state if collecting steps
        if (stepCollector != null) {
            stepCollector.recordInitial(array, "Initial array state");
        }
        
        // Outer loop: iterate through all elements
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Flag to optimize for nearly sorted arrays
            
            // Inner loop: compare adjacent elements and bubble up the largest
            // Each pass ensures the largest unsorted element reaches its final position
            for (int j = 0; j < n - i - 1; j++) {
                // Record comparison if collecting steps
                if (stepCollector != null) {
                    stepCollector.recordCompare(array, j, j + 1, 
                        "Comparing elements at index " + j + " and " + (j + 1));
                }
                
                // Compare adjacent elements
                if (metrics.isGreaterThan(array[j], array[j + 1])) {
                    // Swap if elements are in wrong order
                    metrics.swap(array, j, j + 1);
                    swapped = true;
                    
                    // Record swap if collecting steps
                    if (stepCollector != null) {
                        stepCollector.recordSwap(array, j, j + 1,
                            "Swapped elements at index " + j + " and " + (j + 1));
                    }
                }
            }
            
            // Optimization: If no swaps occurred, array is already sorted
            if (!swapped) {
                break;
            }
        }
        
        // Record completion if collecting steps
        if (stepCollector != null) {
            stepCollector.recordComplete(array, "Sorting complete!");
        }
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    /**
     * Internal sort implementation for String arrays that optionally collects steps.
     */
    private void sortStringInternal(String[] array, MetricsCollector metrics, StepCollector stepCollector) {
        int n = array.length;
        
        // Record initial state if collecting steps
        if (stepCollector != null) {
            stepCollector.recordInitial(array, "Initial array state");
        }
        
        // Outer loop: iterate through all elements
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Flag to optimize for nearly sorted arrays
            
            // Inner loop: compare adjacent elements and bubble up the largest
            for (int j = 0; j < n - i - 1; j++) {
                // Record comparison if collecting steps
                if (stepCollector != null) {
                    stepCollector.recordCompare(array, j, j + 1, 
                        "Comparing elements at index " + j + " and " + (j + 1));
                }
                
                // Compare adjacent strings lexicographically
                if (metrics.isGreaterThan(array[j], array[j + 1])) {
                    // Swap if elements are in wrong order
                    metrics.swap(array, j, j + 1);
                    swapped = true;
                    
                    // Record swap if collecting steps
                    if (stepCollector != null) {
                        stepCollector.recordSwap(array, j, j + 1,
                            "Swapped elements at index " + j + " and " + (j + 1));
                    }
                }
            }
            
            // Optimization: If no swaps occurred, array is already sorted
            if (!swapped) {
                break;
            }
        }
        
        // Record completion if collecting steps
        if (stepCollector != null) {
            stepCollector.recordComplete(array, "Sorting complete!");
        }
    }
}

