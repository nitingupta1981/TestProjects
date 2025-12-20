package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import java.util.Random;

/**
 * Quick Sort implementation with randomized pivot selection.
 * 
 * Quick Sort is a divide-and-conquer algorithm that selects a 'pivot' element
 * and partitions the array around the pivot, placing smaller elements before it
 * and larger elements after it. It then recursively sorts the sub-arrays.
 * 
 * Time Complexity: O(n log n) average case, O(n²) worst case (already sorted with bad pivot)
 * Space Complexity: O(log n) due to recursion stack
 * Stable: No - may change relative order of equal elements
 * 
 * Note: Using randomized pivot selection helps avoid O(n²) worst case on sorted data
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class QuickSort implements SortingAlgorithm {

    private Random random = new Random();

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        if (array.length > 0) {
            quickSort(array, 0, array.length - 1, metrics);
        }
    }

    /**
     * Recursive quick sort method.
     * 
     * @param array The array to sort
     * @param low Starting index of the partition
     * @param high Ending index of the partition
     * @param metrics Metrics collector
     */
    private void quickSort(int[] array, int low, int high, MetricsCollector metrics) {
        if (low < high) {
            // Partition the array and get the pivot index
            // All elements before pivot are smaller, all after are larger
            int pivotIndex = partition(array, low, high, metrics);
            
            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1, metrics);
            quickSort(array, pivotIndex + 1, high, metrics);
        }
    }

    /**
     * Partitions the array using the last element as pivot (after randomization).
     * 
     * This is the core of Quick Sort:
     * 1. Select a pivot element (randomized for better performance)
     * 2. Rearrange array so elements < pivot are on left, > pivot are on right
     * 3. Place pivot in its final sorted position
     * 
     * @param array The array to partition
     * @param low Starting index
     * @param high Ending index
     * @param metrics Metrics collector
     * @return The final position of the pivot element
     */
    private int partition(int[] array, int low, int high, MetricsCollector metrics) {
        // Randomize pivot selection to avoid O(n²) on sorted arrays
        // Swap random element with last element
        int randomIndex = low + random.nextInt(high - low + 1);
        metrics.swap(array, randomIndex, high);
        
        // Use last element as pivot after randomization
        int pivot = array[high];
        metrics.recordArrayAccess(1);
        
        // Index of smaller element - indicates the right position of pivot found so far
        int i = low - 1;
        
        // Traverse through array and move smaller elements to left of pivot
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (metrics.isLessThanOrEqual(array[j], pivot)) {
                i++; // Increment index of smaller element
                metrics.swap(array, i, j); // Swap to move smaller element left
            }
        }
        
        // Place pivot in its correct position (between smaller and larger elements)
        metrics.swap(array, i + 1, high);
        
        return i + 1; // Return the partitioning index
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(log n)";
    }

    @Override
    public boolean isStable() {
        return false;
    }
}

