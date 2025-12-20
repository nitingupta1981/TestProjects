package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

/**
 * Insertion Sort implementation.
 * 
 * Insertion Sort builds the final sorted array one item at a time.
 * It takes each element and inserts it into its correct position in the
 * already sorted portion of the array.
 * 
 * Time Complexity: O(n²) worst/average case, O(n) best case (already sorted)
 * Space Complexity: O(1) - sorts in place
 * Stable: Yes - maintains relative order of equal elements
 * 
 * Best Performance: Nearly sorted arrays, small arrays
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class InsertionSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        int n = array.length;
        
        // Outer loop: pick each element starting from index 1
        // The first element (index 0) is considered already sorted
        for (int i = 1; i < n; i++) {
            int key = array[i]; // Element to be inserted
            int j = i - 1;
            
            // Inner loop: shift elements of sorted portion that are greater than key
            // Move elements one position to the right to make space for key
            while (j >= 0 && metrics.isGreaterThan(array[j], key)) {
                // Shift element to the right
                array[j + 1] = array[j];
                metrics.recordArrayAccess(2); // One read, one write
                j--;
            }
            
            // Insert the key at its correct position
            array[j + 1] = key;
            metrics.recordArrayAccess(1);
            
            // If key wasn't moved, no swap occurred
            if (j + 1 != i) {
                metrics.recordSwap(1);
            }
        }
    }

    @Override
    public String getName() {
        return "Insertion Sort";
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

