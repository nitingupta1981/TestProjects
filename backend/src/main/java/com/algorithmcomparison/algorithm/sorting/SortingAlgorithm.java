package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.util.MetricsCollector;

/**
 * Interface for all sorting algorithms.
 * 
 * This interface uses the Strategy Pattern to allow interchangeable
 * sorting algorithm implementations. All sorting algorithms must implement
 * this interface to be used in the comparison framework.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public interface SortingAlgorithm {
    
    /**
     * Sorts the given array in ascending order.
     * 
     * This method modifies the input array in place and uses the provided
     * MetricsCollector to track performance metrics.
     * 
     * @param array The array to sort (will be modified)
     * @param metrics The metrics collector for tracking performance
     */
    void sort(int[] array, MetricsCollector metrics);

    /**
     * Gets the name of the sorting algorithm.
     * 
     * @return The algorithm name (e.g., "Quick Sort", "Merge Sort")
     */
    String getName();

    /**
     * Gets the Big-O time complexity of the algorithm.
     * 
     * @return The time complexity notation (e.g., "O(n log n)", "O(n²)")
     */
    String getTimeComplexity();

    /**
     * Gets the Big-O space complexity of the algorithm.
     * 
     * @return The space complexity notation (e.g., "O(1)", "O(n)")
     */
    String getSpaceComplexity();

    /**
     * Indicates whether this is a comparison-based sorting algorithm.
     * 
     * @return true if the algorithm uses comparisons, false otherwise
     */
    default boolean isComparisonBased() {
        return true;
    }

    /**
     * Indicates whether this sorting algorithm is stable.
     * A stable sort maintains the relative order of equal elements.
     * 
     * @return true if the algorithm is stable, false otherwise
     */
    boolean isStable();
}

