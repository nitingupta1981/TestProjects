package com.algorithmcomparison.util;

import java.util.Arrays;
import java.util.Random;

/**
 * Utility class for generating test datasets.
 * 
 * Provides methods to generate datasets with various characteristics:
 * - Random: Randomly generated integers
 * - Sorted: Pre-sorted in ascending order
 * - Reverse Sorted: Pre-sorted in descending order
 * 
 * All generated datasets use integers in a configurable range.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class DatasetGenerator {

    private static final Random random = new Random();
    private static final int DEFAULT_MIN = 1;
    private static final int DEFAULT_MAX = 10000;

    /**
     * Generates a random dataset of specified size.
     * 
     * Creates an array filled with random integers between minValue and maxValue.
     * 
     * @param size Number of elements to generate
     * @param minValue Minimum value (inclusive)
     * @param maxValue Maximum value (inclusive)
     * @return Array of random integers
     */
    public static int[] generateRandom(int size, int minValue, int maxValue) {
        if (size <= 0) {
            return new int[0];
        }

        int[] data = new int[size];
        int range = maxValue - minValue + 1;

        for (int i = 0; i < size; i++) {
            data[i] = minValue + random.nextInt(range);
        }

        return data;
    }

    /**
     * Generates a random dataset with default range [1, 10000].
     * 
     * @param size Number of elements to generate
     * @return Array of random integers
     */
    public static int[] generateRandom(int size) {
        return generateRandom(size, DEFAULT_MIN, DEFAULT_MAX);
    }

    /**
     * Generates a sorted dataset (ascending order).
     * 
     * Creates an array filled with random integers, then sorts it.
     * This produces a realistic sorted dataset (not just sequential numbers).
     * 
     * @param size Number of elements to generate
     * @param minValue Minimum value (inclusive)
     * @param maxValue Maximum value (inclusive)
     * @return Sorted array of integers
     */
    public static int[] generateSorted(int size, int minValue, int maxValue) {
        int[] data = generateRandom(size, minValue, maxValue);
        Arrays.sort(data);
        return data;
    }

    /**
     * Generates a sorted dataset with default range [1, 10000].
     * 
     * @param size Number of elements to generate
     * @return Sorted array of integers
     */
    public static int[] generateSorted(int size) {
        return generateSorted(size, DEFAULT_MIN, DEFAULT_MAX);
    }

    /**
     * Generates a reverse sorted dataset (descending order).
     * 
     * Creates a sorted array and then reverses it.
     * 
     * @param size Number of elements to generate
     * @param minValue Minimum value (inclusive)
     * @param maxValue Maximum value (inclusive)
     * @return Reverse sorted array of integers
     */
    public static int[] generateReverseSorted(int size, int minValue, int maxValue) {
        int[] data = generateSorted(size, minValue, maxValue);
        reverse(data);
        return data;
    }

    /**
     * Generates a reverse sorted dataset with default range [1, 10000].
     * 
     * @param size Number of elements to generate
     * @return Reverse sorted array of integers
     */
    public static int[] generateReverseSorted(int size) {
        return generateReverseSorted(size, DEFAULT_MIN, DEFAULT_MAX);
    }

    /**
     * Generates a nearly sorted dataset.
     * 
     * Creates a sorted array and performs a small number of random swaps
     * to make it "nearly sorted" but not perfectly sorted.
     * 
     * @param size Number of elements to generate
     * @param swapPercentage Percentage of elements to swap (0-100)
     * @return Nearly sorted array
     */
    public static int[] generateNearlySorted(int size, int swapPercentage) {
        int[] data = generateSorted(size);
        
        int swapCount = (size * swapPercentage) / 100;
        for (int i = 0; i < swapCount; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            swap(data, idx1, idx2);
        }
        
        return data;
    }

    /**
     * Generates a dataset with many duplicate values.
     * 
     * Creates an array where values are limited to a small range,
     * resulting in many duplicate elements.
     * 
     * @param size Number of elements to generate
     * @param uniqueValuesCount Number of unique values
     * @return Array with many duplicates
     */
    public static int[] generateWithDuplicates(int size, int uniqueValuesCount) {
        if (uniqueValuesCount <= 0 || uniqueValuesCount > size) {
            uniqueValuesCount = size / 10; // Default to 10% unique
        }

        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt(uniqueValuesCount);
        }

        return data;
    }

    /**
     * Reverses an array in place.
     * 
     * @param data Array to reverse
     */
    private static void reverse(int[] data) {
        int left = 0;
        int right = data.length - 1;

        while (left < right) {
            swap(data, left, right);
            left++;
            right--;
        }
    }

    /**
     * Swaps two elements in an array.
     * 
     * @param data Array containing elements
     * @param i First index
     * @param j Second index
     */
    private static void swap(int[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Sets the random seed for reproducible dataset generation.
     * Useful for testing and debugging.
     * 
     * @param seed The random seed
     */
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }
}

