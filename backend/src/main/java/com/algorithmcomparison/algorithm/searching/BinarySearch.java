package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import java.util.Arrays;

/**
 * Binary Search implementation.
 * 
 * Binary Search is an efficient searching algorithm that works on sorted arrays.
 * It repeatedly divides the search interval in half by comparing the target value
 * with the middle element, eliminating half of the remaining elements each time.
 * 
 * Algorithm Steps:
 * 1. Find the middle element of the current search range
 * 2. Compare middle element with target
 * 3. If equal, target is found
 * 4. If target is less than middle, search in left half
 * 5. If target is greater than middle, search in right half
 * 6. Repeat until target is found or search range is empty
 * 
 * Time Complexity: O(log n) - halves the search space each iteration
 * Space Complexity: O(1) for iterative implementation
 * 
 * Requirements:
 * - Array MUST be sorted in ascending order
 * 
 * Best Use Cases:
 * - Large sorted datasets
 * - Multiple searches on the same dataset
 * - When fast search performance is critical
 * 
 * @author Algorithm Comparison Team
 * @version 2.0 (Refactored to use AbstractSearchingAlgorithm)
 */
public class BinarySearch extends AbstractSearchingAlgorithm {

    @Override
    public int searchWithSteps(int[] array, int target, MetricsCollector metrics, StepCollector stepCollector) {
        // For visualization, ensure array is sorted
        if (stepCollector != null) {
            if (!isSorted(array)) {
                Arrays.sort(array);
            }
            stepCollector.recordInitial(array, 
                "Binary Search requires sorted data. Starting search for target: " + target);
        }
        
        // Box the array and call searchGeneric with stepCollector
        Integer[] boxedArray = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            boxedArray[i] = array[i];
        }
        return searchGeneric(boxedArray, target, metrics, stepCollector);
    }

    @Override
    public int searchWithSteps(String[] array, String target, MetricsCollector metrics, StepCollector stepCollector) {
        // For visualization, ensure array is sorted
        if (stepCollector != null) {
            if (!isSorted(array)) {
                Arrays.sort(array);
            }
            stepCollector.recordInitial(array, 
                "Binary Search requires sorted data. Starting search for target: " + target);
        }
        
        // Call searchGeneric with stepCollector
        return searchGeneric(array, target, metrics, stepCollector);
    }

    @Override
    protected <T extends Comparable<T>> int searchGeneric(
            T[] array, 
            T target,
            MetricsCollector metrics, 
            StepCollector stepCollector) {
        
        // Initialize search boundaries
        int left = 0;
        int right = array.length - 1;

        // Continue searching while search space is valid
        while (left <= right) {
            // Calculate middle index
            // Using (left + right) / 2 can cause integer overflow for large values
            // So we use left + (right - left) / 2 which is mathematically equivalent
            int mid = left + (right - left) / 2;
            
            metrics.recordArrayAccess(1); // Access array[mid]
            T midValue = array[mid];

            // Record this search iteration if collecting steps
            recordRange(array, stepCollector, left, right, mid,
                String.format("Searching range [%d, %d]. Checking middle index %d: Is %s == %s?", 
                    left, right, mid, midValue, target));

            // Compare middle element with target
            if (isEqual(midValue, target, metrics)) {
                // Target found at middle index
                recordFound(array, stepCollector, mid,
                    "Target " + target + " found at index " + mid + "!");
                return mid;
            } else if (isLessThan(midValue, target, metrics)) {
                // Target is in the right half
                // Eliminate left half by moving left boundary
                recordCheck(array, stepCollector, mid,
                    midValue + " < " + target + ". Target is in the right half. Moving left boundary to " + (mid + 1));
                left = mid + 1;
            } else {
                // Target is in the left half
                // Eliminate right half by moving right boundary
                recordCheck(array, stepCollector, mid,
                    midValue + " > " + target + ". Target is in the left half. Moving right boundary to " + (mid - 1));
                right = mid - 1;
            }
        }

        // Target not found in array
        recordNotFound(array, stepCollector,
            "Target " + target + " not found in the array. Search space exhausted.");
        
        return -1;
    }

    @Override
    public String getName() {
        return "Binary Search";
    }

    @Override
    public String getTimeComplexity() {
        return "O(log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public boolean requiresSortedArray() {
        // Binary search REQUIRES sorted array to work correctly
        return true;
    }

    // ==================== Helper Methods ====================

    /**
     * Checks if integer array is sorted.
     */
    private boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if string array is sorted.
     */
    private boolean isSorted(String[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(array[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }
}
