package com.algorithmcomparison.util;

/**
 * Utility class for collecting algorithm performance metrics.
 * 
 * This class wraps array operations to track comparisons, swaps, and array accesses.
 * It provides a centralized way to monitor algorithm behavior without modifying
 * the core algorithm logic with counting statements.
 * 
 * Usage:
 * <pre>
 * MetricsCollector metrics = new MetricsCollector();
 * // Use metrics.compare() instead of direct comparisons
 * // Use metrics.swap() instead of direct swaps
 * </pre>
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class MetricsCollector {
    
    private long comparisonCount;
    private long swapCount;
    private long arrayAccessCount;
    private long startTime;
    private long endTime;
    private boolean timing;

    /**
     * Default constructor initializes all counters to zero.
     */
    public MetricsCollector() {
        this.comparisonCount = 0;
        this.swapCount = 0;
        this.arrayAccessCount = 0;
        this.timing = false;
    }

    /**
     * Starts timing the algorithm execution.
     * Uses System.nanoTime() for precise measurement.
     */
    public void startTiming() {
        this.startTime = System.nanoTime();
        this.timing = true;
    }

    /**
     * Stops timing the algorithm execution.
     */
    public void stopTiming() {
        this.endTime = System.nanoTime();
        this.timing = false;
    }

    /**
     * Compares two integer values and increments the comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return negative if a < b, zero if a == b, positive if a > b
     */
    public int compare(int a, int b) {
        comparisonCount++;
        return Integer.compare(a, b);
    }

    /**
     * Checks if first value is less than second and increments comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return true if a < b
     */
    public boolean isLessThan(int a, int b) {
        comparisonCount++;
        return a < b;
    }

    /**
     * Checks if first value is greater than second and increments comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return true if a > b
     */
    public boolean isGreaterThan(int a, int b) {
        comparisonCount++;
        return a > b;
    }

    /**
     * Checks if first string is greater than second (lexicographically) and increments comparison counter.
     * 
     * @param a First string
     * @param b Second string
     * @return true if a > b lexicographically
     */
    public boolean isGreaterThan(String a, String b) {
        comparisonCount++;
        return a.compareTo(b) > 0;
    }

    /**
     * Checks if first value is less than or equal to second and increments comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return true if a <= b
     */
    public boolean isLessThanOrEqual(int a, int b) {
        comparisonCount++;
        return a <= b;
    }

    /**
     * Checks if first value is greater than or equal to second and increments comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return true if a >= b
     */
    public boolean isGreaterThanOrEqual(int a, int b) {
        comparisonCount++;
        return a >= b;
    }

    /**
     * Checks if two values are equal and increments comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return true if a == b
     */
    public boolean isEqual(int a, int b) {
        comparisonCount++;
        return a == b;
    }

    /**
     * Swaps two elements in an integer array and increments the swap counter.
     * 
     * @param array The array containing the elements
     * @param i First index
     * @param j Second index
     */
    public void swap(int[] array, int i, int j) {
        swapCount++;
        arrayAccessCount += 4; // 2 reads + 2 writes
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Swaps two elements in a string array and increments the swap counter.
     * 
     * @param array The array containing the elements
     * @param i First index
     * @param j Second index
     */
    public void swap(String[] array, int i, int j) {
        swapCount++;
        arrayAccessCount += 4; // 2 reads + 2 writes
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Gets an array element and increments the access counter.
     * 
     * @param array The array
     * @param index The index
     * @return The value at the index
     */
    public int get(int[] array, int index) {
        arrayAccessCount++;
        return array[index];
    }

    /**
     * Sets an array element and increments the access counter.
     * 
     * @param array The array
     * @param index The index
     * @param value The value to set
     */
    public void set(int[] array, int index, int value) {
        arrayAccessCount++;
        array[index] = value;
    }

    /**
     * Increments array access count without performing actual access.
     * Useful when direct array access is unavoidable.
     * 
     * @param count Number of accesses to record
     */
    public void recordArrayAccess(int count) {
        arrayAccessCount += count;
    }

    /**
     * Increments comparison count without performing actual comparison.
     * 
     * @param count Number of comparisons to record
     */
    public void recordComparison(int count) {
        comparisonCount += count;
    }

    /**
     * Increments swap count without performing actual swap.
     * 
     * @param count Number of swaps to record
     */
    public void recordSwap(int count) {
        swapCount += count;
    }

    /**
     * Resets all counters to zero.
     */
    public void reset() {
        this.comparisonCount = 0;
        this.swapCount = 0;
        this.arrayAccessCount = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.timing = false;
    }

    // Getters

    /**
     * Gets the total number of comparisons performed.
     * 
     * @return The comparison count
     */
    public long getComparisonCount() {
        return comparisonCount;
    }

    /**
     * Gets the total number of swaps performed.
     * 
     * @return The swap count
     */
    public long getSwapCount() {
        return swapCount;
    }

    /**
     * Gets the total number of array accesses.
     * 
     * @return The array access count
     */
    public long getArrayAccessCount() {
        return arrayAccessCount;
    }

    /**
     * Gets the execution time in nanoseconds.
     * 
     * @return The execution time, or 0 if timing hasn't been stopped
     */
    public long getExecutionTimeNanos() {
        if (timing) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }

    /**
     * Gets the execution time in milliseconds.
     * 
     * @return The execution time in milliseconds
     */
    public double getExecutionTimeMillis() {
        return getExecutionTimeNanos() / 1_000_000.0;
    }

    @Override
    public String toString() {
        return "MetricsCollector{" +
                "comparisons=" + comparisonCount +
                ", swaps=" + swapCount +
                ", arrayAccesses=" + arrayAccessCount +
                ", executionTime=" + String.format("%.3f", getExecutionTimeMillis()) + "ms" +
                '}';
    }
}

