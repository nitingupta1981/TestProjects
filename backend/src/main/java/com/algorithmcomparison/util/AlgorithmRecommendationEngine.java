package com.algorithmcomparison.util;

import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.model.AlgorithmRecommendation.ConfidenceLevel;
import com.algorithmcomparison.model.DatasetCharacteristics;

import java.util.ArrayList;
import java.util.List;

/**
 * Engine for recommending optimal algorithms based on dataset characteristics.
 * 
 * This engine analyzes dataset properties and provides intelligent recommendations
 * for which algorithms will perform best, along with explanations and warnings.
 * 
 * Recommendation Logic:
 * - Considers dataset size, sortedness, duplicates, range, and distribution
 * - Provides primary recommendation with confidence level
 * - Suggests alternative algorithms
 * - Warns about algorithms to avoid
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class AlgorithmRecommendationEngine {

    /**
     * Recommends the best sorting algorithm for the given dataset.
     * 
     * @param characteristics Dataset characteristics from analysis
     * @return AlgorithmRecommendation with best sorting algorithm and reasoning
     */
    public static AlgorithmRecommendation recommendSortingAlgorithm(DatasetCharacteristics characteristics) {
        // Check for already sorted data (sortedness > 95%)
        if (characteristics.getSortedness() > 95) {
            return createSortedDataRecommendation(characteristics);
        }

        // Check for reverse sorted data
        if (characteristics.isReverseSorted()) {
            return createReverseSortedRecommendation(characteristics);
        }

        // Check for nearly sorted data (70-95% sorted)
        if (characteristics.getSortedness() >= 70) {
            return createNearlySortedRecommendation(characteristics);
        }

        // Check for small dataset (< 100 elements)
        if (characteristics.getSize() < 100) {
            return createSmallDatasetRecommendation(characteristics);
        }

        // Check for large dataset with limited range (good for counting sort)
        if (characteristics.getRangeSize() < characteristics.getSize() * 10 &&
            characteristics.getMinValue() >= 0) {
            return createLimitedRangeRecommendation(characteristics);
        }

        // Check for many duplicates (> 30%)
        if (characteristics.getDuplicatePercentage() > 30) {
            return createManyDuplicatesRecommendation(characteristics);
        }

        // Default: Large random dataset
        return createLargeRandomRecommendation(characteristics);
    }

    /**
     * Recommends the best searching algorithm for the given dataset.
     * 
     * @param characteristics Dataset characteristics from analysis
     * @return AlgorithmRecommendation with best searching algorithm and reasoning
     */
    public static AlgorithmRecommendation recommendSearchingAlgorithm(DatasetCharacteristics characteristics) {
        // If data is sorted, binary search is optimal
        if (characteristics.isSorted() || characteristics.getSortedness() > 95) {
            AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
                "Binary Search",
                "Your dataset is sorted. Binary Search provides O(log n) performance, " +
                "significantly faster than linear search for large datasets.",
                "O(log n)",
                ConfidenceLevel.HIGH
            );
            recommendation.addAlternative("Linear Search (if only one search needed)");
            recommendation.addWarning("Avoid: Linear Search for multiple searches (inefficient for sorted data)");
            return recommendation;
        }

        // For unsorted data, linear search is the only reliable option
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Linear Search",
            "Your dataset is unsorted. Linear Search is the only guaranteed method " +
            "to find elements. Consider sorting first if multiple searches are planned.",
            "O(n)",
            ConfidenceLevel.HIGH
        );
        
        recommendation.addAlternative("BFS or DFS for graph-based exploration");
        
        if (characteristics.getSize() > 100) {
            recommendation.addWarning("Note: For multiple searches, consider sorting once O(n log n) + binary searches O(log n)");
        }
        
        return recommendation;
    }

    /**
     * Creates recommendation for already sorted data.
     */
    private static AlgorithmRecommendation createSortedDataRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Insertion Sort",
            String.format("Your dataset is %.1f%% sorted. Insertion Sort performs optimally (O(n) linear time) " +
                         "on already sorted or nearly sorted data.", chars.getSortedness()),
            "O(n)",
            ConfidenceLevel.HIGH
        );
        recommendation.addAlternative("Bubble Sort (with early termination flag)");
        recommendation.addWarning("Avoid: Quick Sort (degrades to O(n²) on sorted data without randomization)");
        return recommendation;
    }

    /**
     * Creates recommendation for reverse sorted data.
     */
    private static AlgorithmRecommendation createReverseSortedRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Merge Sort",
            "Your dataset is reverse sorted. Merge Sort provides consistent O(n log n) performance " +
            "regardless of input order, avoiding the O(n²) worst case of Quick Sort.",
            "O(n log n)",
            ConfidenceLevel.HIGH
        );
        recommendation.addAlternative("Heap Sort (also consistent O(n log n))");
        recommendation.addWarning("Avoid: Quick Sort without randomization (O(n²) on reverse sorted data)");
        return recommendation;
    }

    /**
     * Creates recommendation for nearly sorted data.
     */
    private static AlgorithmRecommendation createNearlySortedRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Insertion Sort",
            String.format("Your dataset is %.1f%% sorted (nearly sorted). Insertion Sort and Shell Sort " +
                         "perform well on nearly sorted data with significantly better than O(n²) performance.",
                         chars.getSortedness()),
            "O(n) to O(n²)",
            ConfidenceLevel.HIGH
        );
        recommendation.addAlternative("Shell Sort (better for larger nearly-sorted datasets)");
        recommendation.addAlternative("Bubble Sort (with optimization flag)");
        return recommendation;
    }

    /**
     * Creates recommendation for small datasets.
     */
    private static AlgorithmRecommendation createSmallDatasetRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Insertion Sort",
            String.format("Your dataset is small (%d elements). Insertion Sort is optimal for small datasets " +
                         "due to its simplicity, low overhead, and good cache performance.",
                         chars.getSize()),
            "O(n²)",
            ConfidenceLevel.HIGH
        );
        recommendation.addAlternative("Selection Sort (simpler but slightly slower)");
        recommendation.addWarning("Note: For small datasets, algorithm choice has minimal impact on performance");
        return recommendation;
    }

    /**
     * Creates recommendation for datasets with limited value range.
     */
    private static AlgorithmRecommendation createLimitedRangeRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Counting Sort",
            String.format("Your dataset has a limited range (%d values over size %d). Counting Sort " +
                         "provides O(n+k) linear time performance, faster than comparison-based sorts.",
                         chars.getRangeSize(), chars.getSize()),
            "O(n+k)",
            ConfidenceLevel.HIGH
        );
        recommendation.addAlternative("Quick Sort (good fallback if range increases)");
        recommendation.addWarning("Note: Counting Sort only works with non-negative integers");
        return recommendation;
    }

    /**
     * Creates recommendation for datasets with many duplicates.
     */
    private static AlgorithmRecommendation createManyDuplicatesRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Quick Sort",
            String.format("Your dataset has %.1f%% duplicate values. Quick Sort with 3-way partitioning " +
                         "handles duplicates efficiently, grouping equal elements together.",
                         chars.getDuplicatePercentage()),
            "O(n log n)",
            ConfidenceLevel.MEDIUM
        );
        recommendation.addAlternative("Merge Sort (stable and consistent performance)");
        recommendation.addWarning("Note: Basic Quick Sort may be slower with many duplicates");
        return recommendation;
    }

    /**
     * Creates recommendation for large random datasets.
     */
    private static AlgorithmRecommendation createLargeRandomRecommendation(DatasetCharacteristics chars) {
        AlgorithmRecommendation recommendation = new AlgorithmRecommendation(
            "Quick Sort",
            String.format("Your dataset is large (%d elements) and randomly distributed. Quick Sort " +
                         "provides excellent average O(n log n) performance with good cache efficiency.",
                         chars.getSize()),
            "O(n log n)",
            ConfidenceLevel.HIGH
        );
        recommendation.addAlternative("Merge Sort (guaranteed O(n log n), but uses more memory)");
        recommendation.addAlternative("Heap Sort (in-place with guaranteed O(n log n))");
        recommendation.addWarning("Note: Use randomized pivot selection to avoid worst-case scenarios");
        return recommendation;
    }

    /**
     * Gets a list of all available sorting algorithms.
     * 
     * @return List of sorting algorithm names
     */
    public static List<String> getAllSortingAlgorithms() {
        List<String> algorithms = new ArrayList<>();
        algorithms.add("Bubble Sort");
        algorithms.add("Selection Sort");
        algorithms.add("Insertion Sort");
        algorithms.add("Quick Sort");
        algorithms.add("Merge Sort");
        algorithms.add("Heap Sort");
        algorithms.add("Shell Sort");
        algorithms.add("Counting Sort");
        return algorithms;
    }

    /**
     * Gets a list of all available searching algorithms.
     * 
     * @return List of searching algorithm names
     */
    public static List<String> getAllSearchingAlgorithms() {
        List<String> algorithms = new ArrayList<>();
        algorithms.add("Linear Search");
        algorithms.add("Binary Search");
        algorithms.add("Depth First Search");
        algorithms.add("Breadth First Search");
        return algorithms;
    }

    /**
     * Checks if a given algorithm is appropriate for the dataset.
     * Returns warnings if the algorithm may perform poorly.
     * 
     * @param algorithmName The algorithm to check
     * @param characteristics Dataset characteristics
     * @return Warning message if algorithm is not recommended, null otherwise
     */
    public static String getAlgorithmWarning(String algorithmName, DatasetCharacteristics characteristics) {
        // Quick Sort warnings
        if (algorithmName.equalsIgnoreCase("Quick Sort")) {
            if (characteristics.isSorted() || characteristics.isReverseSorted()) {
                return "⚠️ Warning: Quick Sort may degrade to O(n²) on sorted/reverse sorted data without pivot randomization";
            }
        }

        // Binary Search warnings
        if (algorithmName.equalsIgnoreCase("Binary Search")) {
            if (!characteristics.isSorted() && characteristics.getSortedness() < 95) {
                return "⚠️ Warning: Binary Search requires sorted data. Current sortedness: " + 
                       String.format("%.1f%%", characteristics.getSortedness());
            }
        }

        // Counting Sort warnings
        if (algorithmName.equalsIgnoreCase("Counting Sort")) {
            if (characteristics.getMinValue() < 0) {
                return "⚠️ Warning: Counting Sort implementation requires non-negative integers";
            }
            if (characteristics.getRangeSize() > characteristics.getSize() * 10) {
                return "⚠️ Warning: Counting Sort may use excessive memory. Range size: " + 
                       characteristics.getRangeSize();
            }
        }

        // Bubble Sort / Selection Sort on large datasets
        if ((algorithmName.equalsIgnoreCase("Bubble Sort") || 
             algorithmName.equalsIgnoreCase("Selection Sort")) &&
            characteristics.getSize() > 1000) {
            return "⚠️ Warning: " + algorithmName + " has O(n²) complexity. May be slow on large datasets (size: " +
                   characteristics.getSize() + ")";
        }

        return null; // No warnings
    }
}

