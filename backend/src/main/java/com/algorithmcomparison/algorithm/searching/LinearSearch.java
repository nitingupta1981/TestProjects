package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;

/**
 * Linear Search implementation.
 * 
 * Linear Search (also called Sequential Search) is the simplest searching algorithm
 * that checks every element in the array sequentially until the target is found
 * or the end of the array is reached.
 * 
 * Algorithm Steps:
 * 1. Start from the first element
 * 2. Compare each element with the target value
 * 3. If match is found, return the index
 * 4. If end of array is reached without finding target, return -1
 * 
 * Time Complexity: O(n) - must check all elements in worst case
 * Space Complexity: O(1) - uses only a constant amount of extra space
 * 
 * Best Use Cases:
 * - Small datasets
 * - Unsorted arrays (no preprocessing needed)
 * - When only one search is needed (sorting overhead not justified)
 * 
 * @author Algorithm Comparison Team
 * @version 2.0 (Refactored to use AbstractSearchingAlgorithm)
 */
public class LinearSearch extends AbstractSearchingAlgorithm {

    @Override
    protected <T extends Comparable<T>> int searchGeneric(
            T[] array, 
            T target,
            MetricsCollector metrics, 
            StepCollector stepCollector) {
        
        // Record initial state if collecting steps
        recordInitial(array, stepCollector, "Starting Linear Search for target: " + target);
        
        // Iterate through each element sequentially
        for (int i = 0; i < array.length; i++) {
            metrics.recordArrayAccess(1); // Count array access
            
            // Record checking this element if collecting steps
            recordCheck(array, stepCollector, i, 
                "Checking index " + i + ": Is " + array[i] + " == " + target + "?");
            
            // Compare current element with target
            if (isEqual(array[i], target, metrics)) {
                // Target found at index i
                recordFound(array, stepCollector, i,
                    "Target " + target + " found at index " + i + "!");
                return i;
            }
        }
        
        // Target not found in array
        recordNotFound(array, stepCollector,
            "Target " + target + " not found in the array");
        
        return -1;
    }

    @Override
    public String getName() {
        return "Linear Search";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public boolean requiresSortedArray() {
        // Linear search works on both sorted and unsorted arrays
        return false;
    }
}
