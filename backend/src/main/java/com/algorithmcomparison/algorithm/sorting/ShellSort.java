package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

/**
 * Shell Sort implementation.
 * 
 * Shell Sort is an optimization over Insertion Sort that allows the exchange of
 * items that are far apart. It starts by sorting pairs of elements far apart from
 * each other, then progressively reducing the gap between elements to be compared.
 * 
 * The algorithm uses a gap sequence (Shell's original: N/2, N/4, ..., 1) to
 * perform "gapped" insertion sorts. By the time gap = 1, the array is nearly sorted,
 * making the final insertion sort very efficient.
 * 
 * Time Complexity: O(n²) worst case, but much better than O(n²) in practice
 *                   Depends on gap sequence: O(n log²n) for Shell's sequence
 * Space Complexity: O(1) - sorts in place
 * Stable: No - may change relative order of equal elements due to long-distance swaps
 * 
 * Note: Currently only supports integer arrays.
 * 
 * @author Algorithm Comparison Team
 * @version 1.1 (Cleaned up, integer-only)
 */
public class ShellSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        int n = array.length;
        
        // Start with a large gap and reduce it
        // Shell's original sequence: n/2, n/4, n/8, ..., 1
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // Perform gapped insertion sort for this gap size
            // The first gap elements array[0..gap-1] are already in gapped order
            
            // Add elements one by one to the gap-sorted portion
            for (int i = gap; i < n; i++) {
                // Save current element to insert
                int temp = array[i];
                metrics.recordArrayAccess(1);
                
                // Shift earlier gap-sorted elements up until the correct location
                // for array[i] is found
                int j = i;
                
                // Compare elements that are 'gap' distance apart
                // This is like insertion sort, but with a larger step size
                while (j >= gap && metrics.isGreaterThan(array[j - gap], temp)) {
                    // Shift element at position (j-gap) to position j
                    array[j] = array[j - gap];
                    metrics.recordArrayAccess(2); // One read, one write
                    metrics.recordSwap(1); // Count as a swap/move
                    j -= gap;
                }
                
                // Insert temp at its correct position
                array[j] = temp;
                metrics.recordArrayAccess(1);
            }
            
            // As gap decreases, the array becomes more sorted
            // When gap = 1, this becomes regular insertion sort on a nearly sorted array
        }
    }

    @Override
    public void sort(String[] array, MetricsCollector metrics) {
        throw new UnsupportedOperationException(
            "Shell Sort does not support STRING datasets. " +
            "Please use: Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, or Merge Sort for string data.");
    }

    @Override
    public String getName() {
        return "Shell Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log²n)";
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
