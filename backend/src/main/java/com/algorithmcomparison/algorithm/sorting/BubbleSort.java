package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

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
        int n = array.length;
        
        // Outer loop: iterate through all elements
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Flag to optimize for nearly sorted arrays
            
            // Inner loop: compare adjacent elements and bubble up the largest
            // Each pass ensures the largest unsorted element reaches its final position
            for (int j = 0; j < n - i - 1; j++) {
                // Compare adjacent elements
                if (metrics.isGreaterThan(array[j], array[j + 1])) {
                    // Swap if elements are in wrong order
                    metrics.swap(array, j, j + 1);
                    swapped = true;
                }
            }
            
            // Optimization: If no swaps occurred, array is already sorted
            if (!swapped) {
                break;
            }
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
}

