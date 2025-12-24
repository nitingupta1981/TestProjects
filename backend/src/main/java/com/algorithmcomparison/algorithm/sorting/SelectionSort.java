package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import java.util.function.BiPredicate;

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
 * @version 2.0 (Refactored to use AbstractSortingAlgorithm)
 */
public class SelectionSort extends AbstractSortingAlgorithm {

    @Override
    protected <T extends Comparable<T>> void sortGeneric(
            T[] array, 
            MetricsCollector metrics, 
            StepCollector stepCollector,
            BiPredicate<T, T> isGreater) {
        
        int n = array.length;
        
        // Record initial state if collecting steps
        recordInitialState(array, stepCollector, "Initial array state");
        
        // Outer loop: expand the sorted region one element at a time
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted region
            int minIndex = i;
            
            // Record starting to search for minimum if collecting steps
            recordComparison(array, stepCollector, i, i,
                "Starting pass " + (i + 1) + ", looking for minimum in unsorted region");
            
            // Inner loop: search for the minimum in remaining unsorted elements
            for (int j = i + 1; j < n; j++) {
                // Record comparison if collecting steps
                recordComparison(array, stepCollector, j, minIndex,
                    "Comparing " + array[j] + " with current minimum " + array[minIndex]);
                
                // Compare current element with current minimum
                if (isLessThan(array[j], array[minIndex], metrics)) {
                    minIndex = j; // Update minimum index
                }
            }
            
            // Swap the found minimum element with the first unsorted element
            // This expands the sorted region by one element
            if (minIndex != i) {
                swap(array, i, minIndex, metrics);
                
                // Record swap if collecting steps
                recordSwap(array, stepCollector, i, minIndex,
                    "Swapping minimum " + array[i] + " to sorted position " + i);
            } else {
                // Record that no swap was needed if collecting steps
                recordComparison(array, stepCollector, i, i,
                    "Element " + array[i] + " already in correct position");
            }
        }
        
        // Record completion if collecting steps
        recordComplete(array, stepCollector, "Sorting complete!");
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
