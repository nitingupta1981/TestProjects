package com.algorithmcomparison.util;

import com.algorithmcomparison.model.DatasetCharacteristics;
import com.algorithmcomparison.model.DatasetCharacteristics.DistributionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for analyzing dataset characteristics.
 * 
 * This analyzer examines datasets to determine properties such as sortedness,
 * duplicates, range, and distribution patterns. These characteristics are used
 * by the recommendation engine to suggest optimal algorithms.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class DatasetAnalyzer {

    /**
     * Performs comprehensive analysis on a dataset.
     * 
     * Analyzes all characteristics including:
     * - Size and size category
     * - Sortedness (ascending, descending, partial)
     * - Duplicates and unique element count
     * - Value range (min, max, range size)
     * - Distribution pattern
     * 
     * @param data The array to analyze
     * @return DatasetCharacteristics containing all analysis results
     */
    public static DatasetCharacteristics analyze(int[] data) {
        DatasetCharacteristics characteristics = new DatasetCharacteristics();

        if (data == null || data.length == 0) {
            characteristics.setSize(0);
            return characteristics;
        }

        // Basic size information
        characteristics.setSize(data.length);

        // Analyze sortedness
        analyzeSortedness(data, characteristics);

        // Analyze duplicates
        analyzeDuplicates(data, characteristics);

        // Analyze range
        analyzeRange(data, characteristics);

        // Analyze distribution
        analyzeDistribution(data, characteristics);

        return characteristics;
    }

    /**
     * Performs comprehensive analysis on a String dataset.
     * 
     * Analyzes characteristics including:
     * - Size and size category
     * - Sortedness (lexicographic order)
     * - Duplicates and unique element count
     * 
     * Note: Range and distribution analysis are not applicable for String data.
     * 
     * @param data The String array to analyze
     * @return DatasetCharacteristics containing all analysis results
     */
    public static DatasetCharacteristics analyze(String[] data) {
        DatasetCharacteristics characteristics = new DatasetCharacteristics();

        if (data == null || data.length == 0) {
            characteristics.setSize(0);
            return characteristics;
        }

        // Basic size information
        characteristics.setSize(data.length);

        // Analyze sortedness
        analyzeSortedness(data, characteristics);

        // Analyze duplicates
        analyzeDuplicates(data, characteristics);

        // For String data, range and distribution are not meaningful
        // Set defaults
        characteristics.setMinValue(0);
        characteristics.setMaxValue(0);
        characteristics.setRangeSize(0);
        characteristics.setDistribution(DistributionType.RANDOM);

        return characteristics;
    }

    /**
     * Analyzes sortedness of the dataset.
     * 
     * Checks if array is:
     * - Fully sorted (ascending)
     * - Fully reverse sorted (descending)
     * - Partially sorted (calculates percentage)
     * 
     * Sortedness percentage is calculated as the proportion of adjacent
     * pairs that are in correct sorted order.
     * 
     * @param data The array to analyze
     * @param characteristics The characteristics object to update
     */
    private static void analyzeSortedness(int[] data, DatasetCharacteristics characteristics) {
        if (data.length <= 1) {
            characteristics.setSorted(true);
            characteristics.setSortedness(100.0);
            return;
        }

        int correctOrderPairs = 0;
        int reverseOrderPairs = 0;
        int totalPairs = data.length - 1;

        // Check each adjacent pair
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] <= data[i + 1]) {
                correctOrderPairs++;
            }
            if (data[i] >= data[i + 1]) {
                reverseOrderPairs++;
            }
        }

        // Calculate sortedness percentage
        double sortedness = (correctOrderPairs * 100.0) / totalPairs;
        characteristics.setSortedness(sortedness);

        // Check if fully sorted
        if (correctOrderPairs == totalPairs) {
            characteristics.setSorted(true);
        }

        // Check if fully reverse sorted
        if (reverseOrderPairs == totalPairs) {
            characteristics.setReverseSorted(true);
        }
    }

    /**
     * Analyzes sortedness of the String dataset (lexicographic order).
     * 
     * @param data The String array to analyze
     * @param characteristics The characteristics object to update
     */
    private static void analyzeSortedness(String[] data, DatasetCharacteristics characteristics) {
        if (data.length <= 1) {
            characteristics.setSorted(true);
            characteristics.setSortedness(100.0);
            return;
        }

        int correctOrderPairs = 0;
        int reverseOrderPairs = 0;
        int totalPairs = data.length - 1;

        // Check each adjacent pair using lexicographic comparison
        for (int i = 0; i < data.length - 1; i++) {
            int comparison = data[i].compareTo(data[i + 1]);
            if (comparison <= 0) {
                correctOrderPairs++;
            }
            if (comparison >= 0) {
                reverseOrderPairs++;
            }
        }

        // Calculate sortedness percentage
        double sortedness = (correctOrderPairs * 100.0) / totalPairs;
        characteristics.setSortedness(sortedness);

        // Check if fully sorted
        if (correctOrderPairs == totalPairs) {
            characteristics.setSorted(true);
        }

        // Check if fully reverse sorted
        if (reverseOrderPairs == totalPairs) {
            characteristics.setReverseSorted(true);
        }
    }

    /**
     * Analyzes duplicate elements in the dataset.
     * 
     * Determines:
     * - Whether duplicates exist
     * - Percentage of elements that are duplicates
     * - Count of unique elements
     * 
     * @param data The array to analyze
     * @param characteristics The characteristics object to update
     */
    private static void analyzeDuplicates(int[] data, DatasetCharacteristics characteristics) {
        // Use HashSet to find unique elements efficiently
        Set<Integer> uniqueElements = new HashSet<>();
        
        for (int value : data) {
            uniqueElements.add(value);
        }

        int uniqueCount = uniqueElements.size();
        characteristics.setUniqueCount(uniqueCount);

        // Check if duplicates exist
        boolean hasDuplicates = uniqueCount < data.length;
        characteristics.setHasDuplicates(hasDuplicates);

        // Calculate duplicate percentage
        if (hasDuplicates) {
            int duplicateCount = data.length - uniqueCount;
            double duplicatePercentage = (duplicateCount * 100.0) / data.length;
            characteristics.setDuplicatePercentage(duplicatePercentage);
        } else {
            characteristics.setDuplicatePercentage(0.0);
        }
    }

    /**
     * Analyzes duplicate elements in the String dataset.
     * 
     * @param data The String array to analyze
     * @param characteristics The characteristics object to update
     */
    private static void analyzeDuplicates(String[] data, DatasetCharacteristics characteristics) {
        // Use HashSet to find unique elements efficiently
        Set<String> uniqueElements = new HashSet<>();
        
        for (String value : data) {
            uniqueElements.add(value);
        }

        int uniqueCount = uniqueElements.size();
        characteristics.setUniqueCount(uniqueCount);

        // Check if duplicates exist
        boolean hasDuplicates = uniqueCount < data.length;
        characteristics.setHasDuplicates(hasDuplicates);

        // Calculate duplicate percentage
        if (hasDuplicates) {
            int duplicateCount = data.length - uniqueCount;
            double duplicatePercentage = (duplicateCount * 100.0) / data.length;
            characteristics.setDuplicatePercentage(duplicatePercentage);
        } else {
            characteristics.setDuplicatePercentage(0.0);
        }
    }

    /**
     * Analyzes the range of values in the dataset.
     * 
     * Determines:
     * - Minimum value
     * - Maximum value
     * - Range size (max - min + 1)
     * 
     * Range size is important for determining if counting sort is appropriate.
     * 
     * @param data The array to analyze
     * @param characteristics The characteristics object to update
     */
    private static void analyzeRange(int[] data, DatasetCharacteristics characteristics) {
        int min = data[0];
        int max = data[0];

        // Find min and max values
        for (int i = 1; i < data.length; i++) {
            if (data[i] < min) {
                min = data[i];
            }
            if (data[i] > max) {
                max = data[i];
            }
        }

        characteristics.setMinValue(min);
        characteristics.setMaxValue(max);
        characteristics.setRangeSize(max - min + 1);
    }

    /**
     * Analyzes the distribution pattern of values.
     * 
     * Classifies distribution as:
     * - UNIFORM: Values evenly distributed across range
     * - NORMAL: Bell curve-like distribution
     * - SKEWED: Values concentrated at one end
     * - RANDOM: No clear pattern
     * 
     * This is a simplified heuristic-based approach.
     * For more accurate analysis, statistical tests would be needed.
     * 
     * @param data The array to analyze
     * @param characteristics The characteristics object to update
     */
    private static void analyzeDistribution(int[] data, DatasetCharacteristics characteristics) {
        if (data.length < 10) {
            // Too small for meaningful distribution analysis
            characteristics.setDistribution(DistributionType.RANDOM);
            return;
        }

        int min = characteristics.getMinValue();
        int max = characteristics.getMaxValue();
        int range = characteristics.getRangeSize();

        // Divide range into buckets
        int bucketCount = Math.min(10, range);
        int[] buckets = new int[bucketCount];

        // Count elements in each bucket
        for (int value : data) {
            int bucketIndex = ((value - min) * bucketCount) / range;
            if (bucketIndex >= bucketCount) {
                bucketIndex = bucketCount - 1; // Handle edge case where value == max
            }
            buckets[bucketIndex]++;
        }

        // Analyze bucket distribution
        double avgCount = data.length / (double) bucketCount;
        int variance = 0;
        
        for (int count : buckets) {
            variance += Math.pow(count - avgCount, 2);
        }
        variance /= bucketCount;

        // Classify distribution based on variance and bucket patterns
        if (variance < avgCount * 0.5) {
            // Low variance suggests uniform distribution
            characteristics.setDistribution(DistributionType.UNIFORM);
        } else if (isNormalDistribution(buckets)) {
            // Peak in middle buckets suggests normal distribution
            characteristics.setDistribution(DistributionType.NORMAL);
        } else if (isSkewedDistribution(buckets)) {
            // Concentration at one end suggests skewed distribution
            characteristics.setDistribution(DistributionType.SKEWED);
        } else {
            // No clear pattern
            characteristics.setDistribution(DistributionType.RANDOM);
        }
    }

    /**
     * Checks if bucket distribution resembles a normal (bell curve) distribution.
     * 
     * @param buckets The bucket counts
     * @return true if distribution appears normal
     */
    private static boolean isNormalDistribution(int[] buckets) {
        if (buckets.length < 3) {
            return false;
        }

        int midIndex = buckets.length / 2;
        int midValue = buckets[midIndex];

        // Check if middle buckets have more elements than edge buckets
        int edgeAvg = (buckets[0] + buckets[buckets.length - 1]) / 2;
        
        return midValue > edgeAvg * 1.5;
    }

    /**
     * Checks if bucket distribution is skewed (concentrated at one end).
     * 
     * @param buckets The bucket counts
     * @return true if distribution appears skewed
     */
    private static boolean isSkewedDistribution(int[] buckets) {
        if (buckets.length < 3) {
            return false;
        }

        int leftSum = 0;
        int rightSum = 0;
        int midPoint = buckets.length / 2;

        // Sum left and right halves
        for (int i = 0; i < midPoint; i++) {
            leftSum += buckets[i];
        }
        for (int i = midPoint; i < buckets.length; i++) {
            rightSum += buckets[i];
        }

        // Check if one side has significantly more elements
        double ratio = Math.max(leftSum, rightSum) / (double) Math.min(leftSum, rightSum);
        
        return ratio > 2.0; // Skewed if one side has 2x more elements
    }

    /**
     * Quick check if array is sorted (ascending).
     * 
     * @param data The array to check
     * @return true if sorted in ascending order
     */
    public static boolean isSorted(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] > data[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Quick check if array is reverse sorted (descending).
     * 
     * @param data The array to check
     * @return true if sorted in descending order
     */
    public static boolean isReverseSorted(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] < data[i + 1]) {
                return false;
            }
        }
        return true;
    }
}

