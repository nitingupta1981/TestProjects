package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import java.util.function.BiPredicate;

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
 * @version 2.0 (Refactored to use AbstractSortingAlgorithm)
 */
public class BubbleSort extends AbstractSortingAlgorithm {

    @Override
    protected <T extends Comparable<T>> void sortGeneric(
            T[] array, 
            MetricsCollector metrics, 
            StepCollector stepCollector,
            BiPredicate<T, T> isGreater) {
        
        int n = array.length;
        
        // Record initial state if collecting steps
        recordInitialState(array, stepCollector, "Initial array state");
        
        // Outer loop: iterate through all elements
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Flag to optimize for nearly sorted arrays
            
            // Inner loop: compare adjacent elements and bubble up the largest
            // Each pass ensures the largest unsorted element reaches its final position
            for (int j = 0; j < n - i - 1; j++) {
                // Record comparison if collecting steps
                recordComparison(array, stepCollector, j, j + 1, 
                    "Comparing elements at index " + j + " and " + (j + 1));
                
                // Compare adjacent elements
                if (isGreaterThan(array[j], array[j + 1], metrics, isGreater)) {
                    // Swap if elements are in wrong order
                    swap(array, j, j + 1, metrics);
                    swapped = true;
                    
                    // Record swap if collecting steps
                    recordSwap(array, stepCollector, j, j + 1,
                        "Swapped elements at index " + j + " and " + (j + 1));
                }
            }
            
            // Optimization: If no swaps occurred, array is already sorted
            if (!swapped) {
                break;
            }
        }
        
        // Record completion if collecting steps
        recordComplete(array, stepCollector, "Sorting complete!");
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
}
