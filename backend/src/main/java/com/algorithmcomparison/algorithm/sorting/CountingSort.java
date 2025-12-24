package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

/**
 * Counting Sort implementation.
 * 
 * Counting Sort is a non-comparison-based sorting algorithm that counts the frequency
 * of each distinct element in the input array and uses this information to place
 * elements in their correct sorted position. It works efficiently when the range of
 * input values (k) is not significantly larger than the number of elements (n).
 * 
 * Algorithm Steps:
 * 1. Find the range of input values (min and max)
 * 2. Create a counting array to store frequency of each value
 * 3. Modify counting array to store cumulative counts
 * 4. Build output array by placing elements at their correct positions
 * 5. Copy output array back to original array
 * 
 * Time Complexity: O(n + k) where k is the range of input values
 * Space Complexity: O(n + k) for the counting and output arrays
 * Stable: Yes - maintains relative order of equal elements
 * 
 * Best Use Cases:
 * - Small range of integer values
 * - Range size (k) is close to or less than array size (n)
 * - When linear time complexity is needed
 * 
 * Limitations:
 * - Only works with non-negative integers (or requires offset for negative values)
 * - Memory inefficient when range is very large
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class CountingSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        // Step 1: Find the range of values (min and max)
        // This determines the size of our counting array
        int min = array[0];
        int max = array[0];
        
        for (int i = 1; i < n; i++) {
            metrics.recordArrayAccess(1); // Count direct array access
            if (array[i] < min) {
                min = array[i];
            }
            if (array[i] > max) {
                max = array[i];
            }
        }

        // Calculate the range size
        int range = max - min + 1;

        // Step 2: Create counting array to store frequency of each value
        // Index i in countArray represents value (i + min)
        int[] countArray = new int[range];
        
        // Count frequency of each element
        for (int i = 0; i < n; i++) {
            metrics.recordArrayAccess(1); // Array read
            countArray[array[i] - min]++; // Adjust index by min offset
        }

        // Step 3: Modify countArray to store cumulative counts
        // After this step, countArray[i] contains the position where
        // elements with value (i + min) should end in the output array
        for (int i = 1; i < range; i++) {
            countArray[i] += countArray[i - 1];
        }

        // Step 4: Build the output array
        // Traverse input array from right to left to maintain stability
        int[] outputArray = new int[n];
        
        for (int i = n - 1; i >= 0; i--) {
            metrics.recordArrayAccess(1); // Read from original array
            int value = array[i];
            int position = countArray[value - min] - 1; // Get correct position
            outputArray[position] = value; // Place element in sorted position
            countArray[value - min]--; // Decrement count for next occurrence
            
            // Record this as a "move" operation (similar to swap but one-directional)
            metrics.recordSwap(1);
        }

        // Step 5: Copy sorted elements back to original array
        for (int i = 0; i < n; i++) {
            metrics.recordArrayAccess(1); // Array write
            array[i] = outputArray[i];
        }
    }

    @Override
    public void sort(String[] array, MetricsCollector metrics) {
        throw new UnsupportedOperationException(
            "Counting Sort does not support STRING datasets. " +
            "This algorithm only works with INTEGER data that has a limited range.");
    }

    @Override
    public String getName() {
        return "Counting Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n+k)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n+k)";
    }

    @Override
    public boolean isComparisonBased() {
        // Counting Sort does not use comparisons; it uses array indexing
        return false;
    }

    @Override
    public boolean isStable() {
        // Counting Sort is stable when implemented correctly
        // (by traversing input from right to left)
        return true;
    }
}

