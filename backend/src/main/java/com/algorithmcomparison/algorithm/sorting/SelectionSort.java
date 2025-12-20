package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;

/**
 * Selection Sort implementation.
 * 
 * Selection Sort divides the array into a sorted and unsorted region.
 * It repeatedly selects the smallest (or largest) element from the unsorted
 * region and moves it to the end of the sorted region.
 * 
 * Time Complexity: O(n²) in all cases
 * Space Complexity: O(1) - sorts in place
 * Stable: No - may change relative order of equal elements
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class SelectionSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        sortInternal(array, metrics, null);
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
     * Internal sort implementation that optionally collects steps.
     */
    private void sortInternal(int[] array, MetricsCollector metrics, StepCollector stepCollector) {
        int n = array.length;
        
        // Record initial state if collecting steps
        if (stepCollector != null) {
            stepCollector.recordInitial(array, "Initial array state");
        }
        
        // Outer loop: expand the sorted region one element at a time
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted region
            int minIndex = i;
            
            // Record starting to search for minimum if collecting steps
            if (stepCollector != null) {
                stepCollector.recordCompare(array, i, i,
                    "Starting pass " + (i + 1) + ", looking for minimum in unsorted region");
            }
            
            // Inner loop: search for the minimum in remaining unsorted elements
            for (int j = i + 1; j < n; j++) {
                // Record comparison if collecting steps
                if (stepCollector != null) {
                    stepCollector.recordCompare(array, j, minIndex,
                        "Comparing " + array[j] + " with current minimum " + array[minIndex]);
                }
                
                // Compare current element with current minimum
                if (metrics.isLessThan(array[j], array[minIndex])) {
                    minIndex = j; // Update minimum index
                }
            }
            
            // Swap the found minimum element with the first unsorted element
            // This expands the sorted region by one element
            if (minIndex != i) {
                metrics.swap(array, i, minIndex);
                
                // Record swap if collecting steps
                if (stepCollector != null) {
                    stepCollector.recordSwap(array, i, minIndex,
                        "Swapping minimum " + array[i] + " to sorted position " + i);
                }
            } else {
                // Record that no swap was needed if collecting steps
                if (stepCollector != null) {
                    stepCollector.recordCompare(array, i, i,
                        "Element " + array[i] + " already in correct position");
                }
            }
        }
        
        // Record completion if collecting steps
        if (stepCollector != null) {
            stepCollector.recordComplete(array, "Sorting complete!");
        }
    }

    @Override
    public String getName() {
        return "Selection Sort";
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
        return false;
    }
}

