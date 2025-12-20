package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.util.MetricsCollector;

/**
 * Interface for all searching algorithms.
 * 
 * This interface uses the Strategy Pattern to allow interchangeable
 * searching algorithm implementations. All searching algorithms must implement
 * this interface to be used in the comparison framework.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public interface SearchingAlgorithm {
    
    /**
     * Searches for a target value in the given array.
     * 
     * This method searches for the target value and uses the provided
     * MetricsCollector to track performance metrics.
     * 
     * @param array The array to search in
     * @param target The value to search for
     * @param metrics The metrics collector for tracking performance
     * @return The index where target was found, or -1 if not found
     */
    int search(int[] array, int target, MetricsCollector metrics);

    /**
     * Gets the name of the searching algorithm.
     * 
     * @return The algorithm name (e.g., "Linear Search", "Binary Search")
     */
    String getName();

    /**
     * Gets the Big-O time complexity of the algorithm.
     * 
     * @return The time complexity notation (e.g., "O(n)", "O(log n)")
     */
    String getTimeComplexity();

    /**
     * Gets the Big-O space complexity of the algorithm.
     * 
     * @return The space complexity notation (e.g., "O(1)", "O(n)")
     */
    String getSpaceComplexity();

    /**
     * Indicates whether this algorithm requires the array to be sorted.
     * 
     * @return true if the algorithm requires sorted input, false otherwise
     */
    boolean requiresSortedArray();

    /**
     * Indicates whether this is a graph-based search algorithm.
     * 
     * @return true if the algorithm uses graph structures, false otherwise
     */
    default boolean isGraphBased() {
        return false;
    }
}

