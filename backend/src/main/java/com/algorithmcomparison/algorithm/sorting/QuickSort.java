package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import java.util.Random;
import java.util.function.BiPredicate;

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
 * @version 2.0 (Refactored to use AbstractSortingAlgorithm)
 */
public class QuickSort extends AbstractSortingAlgorithm {

    private Random random = new Random();

    @Override
    protected <T extends Comparable<T>> void sortGeneric(
            T[] array, 
            MetricsCollector metrics, 
            StepCollector stepCollector,
            BiPredicate<T, T> isGreater) {
        
        if (array.length > 0) {
            quickSort(array, 0, array.length - 1, metrics, isGreater);
        }
    }

    /**
     * Recursive quick sort method.
     * 
     * @param array The array to sort
     * @param low Starting index of the partition
     * @param high Ending index of the partition
     * @param metrics Metrics collector
     * @param isGreater Comparison function
     */
    private <T extends Comparable<T>> void quickSort(
            T[] array, int low, int high, 
            MetricsCollector metrics, 
            BiPredicate<T, T> isGreater) {
        
        if (low < high) {
            // Partition the array and get the pivot index
            // All elements before pivot are smaller, all after are larger
            int pivotIndex = partition(array, low, high, metrics, isGreater);
            
            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1, metrics, isGreater);
            quickSort(array, pivotIndex + 1, high, metrics, isGreater);
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
     * @param isGreater Comparison function
     * @return The final position of the pivot element
     */
    private <T extends Comparable<T>> int partition(
            T[] array, int low, int high, 
            MetricsCollector metrics, 
            BiPredicate<T, T> isGreater) {
        
        // Randomize pivot selection to avoid O(n²) on sorted arrays
        // Swap random element with last element
        int randomIndex = low + random.nextInt(high - low + 1);
        swap(array, randomIndex, high, metrics);
        
        // Use last element as pivot after randomization
        T pivot = array[high];
        metrics.recordArrayAccess(1);
        
        // Index of smaller element - indicates the right position of pivot found so far
        int i = low - 1;
        
        // Traverse through array and move smaller elements to left of pivot
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (isLessThanOrEqual(array[j], pivot, metrics)) {
                i++; // Increment index of smaller element
                swap(array, i, j, metrics); // Swap to move smaller element left
            }
        }
        
        // Place pivot in its correct position (between smaller and larger elements)
        swap(array, i + 1, high, metrics);
        
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
