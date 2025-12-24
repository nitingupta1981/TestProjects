package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import java.util.function.BiPredicate;

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
 * @version 2.0 (Refactored to use AbstractSortingAlgorithm)
 */
public class InsertionSort extends AbstractSortingAlgorithm {

    @Override
    protected <T extends Comparable<T>> void sortGeneric(
            T[] array, 
            MetricsCollector metrics, 
            StepCollector stepCollector,
            BiPredicate<T, T> isGreater) {
        
        int n = array.length;
        
        // Record initial state if collecting steps
        recordInitialState(array, stepCollector, "Initial array state");
        
        // Outer loop: pick each element starting from index 1
        // The first element (index 0) is considered already sorted
        for (int i = 1; i < n; i++) {
            T key = array[i]; // Element to be inserted
            int j = i - 1;
            
            // Record picking element if collecting steps
            recordComparison(array, stepCollector, i, i, 
                "Picking element " + key + " at index " + i + " to insert into sorted portion");
            
            // Inner loop: shift elements of sorted portion that are greater than key
            // Move elements one position to the right to make space for key
            while (j >= 0 && isGreaterThan(array[j], key, metrics, isGreater)) {
                // Shift element to the right
                array[j + 1] = array[j];
                metrics.recordArrayAccess(2); // One read, one write
                
                // Record the shift if collecting steps
                recordSwap(array, stepCollector, j + 1, j + 1,
                    "Shifting " + array[j + 1] + " right to position " + (j + 1));
                
                j--;
            }
            
            // Insert the key at its correct position
            array[j + 1] = key;
            metrics.recordArrayAccess(1);
            
            // Record insertion if collecting steps
            recordSwap(array, stepCollector, j + 1, j + 1,
                "Inserted " + key + " at position " + (j + 1));
            
            // If key wasn't moved, no swap occurred
            if (j + 1 != i) {
                metrics.recordSwap(1);
            }
        }
        
        // Record completion if collecting steps
        recordComplete(array, stepCollector, "Sorting complete!");
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
