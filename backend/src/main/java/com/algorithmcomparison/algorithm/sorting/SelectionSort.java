package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

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
        int n = array.length;
        
        // Outer loop: expand the sorted region one element at a time
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted region
            int minIndex = i;
            
            // Inner loop: search for the minimum in remaining unsorted elements
            for (int j = i + 1; j < n; j++) {
                // Compare current element with current minimum
                if (metrics.isLessThan(array[j], array[minIndex])) {
                    minIndex = j; // Update minimum index
                }
            }
            
            // Swap the found minimum element with the first unsorted element
            // This expands the sorted region by one element
            if (minIndex != i) {
                metrics.swap(array, i, minIndex);
            }
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

