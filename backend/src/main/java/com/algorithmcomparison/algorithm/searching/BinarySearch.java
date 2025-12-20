package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.util.MetricsCollector;

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
 * @version 1.0
 */
public class BinarySearch implements SearchingAlgorithm {

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
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
            int midValue = array[mid];

            // Compare middle element with target
            if (metrics.isEqual(midValue, target)) {
                // Target found at middle index
                return mid;
            } else if (metrics.isLessThan(midValue, target)) {
                // Target is in the right half
                // Eliminate left half by moving left boundary
                left = mid + 1;
            } else {
                // Target is in the left half
                // Eliminate right half by moving right boundary
                right = mid - 1;
            }
        }

        // Target not found in array
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
}

