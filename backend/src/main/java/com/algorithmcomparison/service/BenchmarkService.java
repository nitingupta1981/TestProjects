package com.algorithmcomparison.service;

import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.BenchmarkReport;
import com.algorithmcomparison.model.BenchmarkReport.BenchmarkStatistics;
import com.algorithmcomparison.model.Dataset;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for running comprehensive benchmarks on algorithms.
 * 
 * Provides functionality for:
 * - Running algorithms across multiple dataset sizes
 * - Collecting statistical summaries (min, max, avg, median)
 * - Generating benchmark reports
 * - Analyzing algorithm scalability
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@Service
public class BenchmarkService {

    private final DatasetService datasetService;
    private final SortingService sortingService;
    private final SearchingService searchingService;

    // In-memory storage for benchmark reports (session-based)
    private final Map<String, Map<String, BenchmarkReport>> sessionReportStore = new ConcurrentHashMap<>();

    /**
     * Constructor with dependency injection.
     * 
     * @param datasetService Service for dataset management
     * @param sortingService Service for sorting algorithms
     * @param searchingService Service for searching algorithms
     */
    public BenchmarkService(DatasetService datasetService, 
                           SortingService sortingService,
                           SearchingService searchingService) {
        this.datasetService = datasetService;
        this.sortingService = sortingService;
        this.searchingService = searchingService;
    }

    /**
     * Runs a comprehensive benchmark for sorting algorithms.
     * Tests algorithms across multiple dataset sizes.
     * 
     * @param sessionId The user's session ID
     * @param algorithmNames List of algorithms to benchmark
     * @param datasetSizes List of dataset sizes to test
     * @param datasetType Type of dataset (RANDOM, SORTED, REVERSE_SORTED)
     * @param dataType Data type (INTEGER or STRING)
     * @return BenchmarkReport with all results and statistics
     */
    public BenchmarkReport runSortingBenchmark(String sessionId, List<String> algorithmNames, 
                                              List<Integer> datasetSizes,
                                              String datasetType,
                                              String dataType) {
        BenchmarkReport report = new BenchmarkReport("Sorting Benchmark - " + datasetType + " (" + dataType + ")");
        
        // Generate datasets for each size with benchmark naming
        List<String> datasetIds = new ArrayList<>();
        for (int size : datasetSizes) {
            Dataset dataset = datasetService.generateBenchmarkDataset(sessionId, datasetType, size, dataType);
            datasetIds.add(dataset.getId());
        }

        // Run each algorithm on each dataset
        for (String algorithmName : algorithmNames) {
            for (String datasetId : datasetIds) {
                try {
                    // Use default ascending order for benchmarks
                    AlgorithmResult result = sortingService.executeSortingAlgorithm(sessionId, datasetId, algorithmName, "ASCENDING");
                    report.addResult(result);
                } catch (Exception e) {
                    System.err.println("Benchmark error for " + algorithmName + ": " + e.getMessage());
                }
            }
        }

        // Calculate statistics for each algorithm
        for (String algorithmName : algorithmNames) {
            BenchmarkStatistics stats = calculateStatistics(report.getResults(), algorithmName);
            // Only add statistics if the algorithm had successful runs
            if (stats.getRunsCount() > 0) {
                report.addStatistics(stats);
            }
        }

        report.setEndTime(System.currentTimeMillis());
        
        // Store report in session-based store
        Map<String, BenchmarkReport> sessionStore = sessionReportStore.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        sessionStore.put(report.getId(), report);
        
        return report;
    }

    /**
     * Runs a benchmark with default dataset sizes (backwards compatible, INTEGER only).
     * 
     * @param sessionId The user's session ID
     * @param algorithmNames List of algorithms to benchmark
     * @param datasetType Type of dataset
     * @return BenchmarkReport
     */
    public BenchmarkReport runSortingBenchmark(String sessionId, List<String> algorithmNames, String datasetType) {
        List<Integer> defaultSizes = Arrays.asList(10, 100, 1000, 5000);
        return runSortingBenchmark(sessionId, algorithmNames, defaultSizes, datasetType, "INTEGER");
    }

    /**
     * Runs a sorting benchmark with custom sizes (backwards compatible, INTEGER only).
     * 
     * @param sessionId The user's session ID
     * @param algorithmNames List of algorithms to benchmark
     * @param datasetSizes List of dataset sizes to test
     * @param datasetType Type of dataset (RANDOM, SORTED, REVERSE_SORTED)
     * @return BenchmarkReport with all results and statistics
     */
    public BenchmarkReport runSortingBenchmark(String sessionId, List<String> algorithmNames, 
                                              List<Integer> datasetSizes,
                                              String datasetType) {
        return runSortingBenchmark(sessionId, algorithmNames, datasetSizes, datasetType, "INTEGER");
    }

    /**
     * Runs a comprehensive benchmark for searching algorithms (INTEGER).
     * 
     * @param sessionId The user's session ID
     * @param algorithmNames List of algorithms to benchmark
     * @param datasetSizes List of dataset sizes to test
     * @param target Target value to search for
     * @param dataType Data type (INTEGER or STRING)
     * @return BenchmarkReport
     */
    public BenchmarkReport runSearchingBenchmark(String sessionId, List<String> algorithmNames,
                                                 List<Integer> datasetSizes,
                                                 int target,
                                                 String dataType) {
        BenchmarkReport report = new BenchmarkReport("Searching Benchmark (" + dataType + ")");

        // Generate datasets for each size with benchmark naming
        List<String> datasetIds = new ArrayList<>();
        for (int size : datasetSizes) {
            Dataset dataset = datasetService.generateBenchmarkDataset(sessionId, "RANDOM", size, dataType);
            datasetIds.add(dataset.getId());
        }

        // Run each algorithm on each dataset
        for (String algorithmName : algorithmNames) {
            for (String datasetId : datasetIds) {
                try {
                    AlgorithmResult result = searchingService.executeSearchingAlgorithm(
                        sessionId, datasetId, algorithmName, target);
                    report.addResult(result);
                } catch (Exception e) {
                    System.err.println("Benchmark error for " + algorithmName + ": " + e.getMessage());
                }
            }
        }

        // Calculate statistics
        for (String algorithmName : algorithmNames) {
            BenchmarkStatistics stats = calculateStatistics(report.getResults(), algorithmName);
            // Only add statistics if the algorithm had successful runs
            if (stats.getRunsCount() > 0) {
                report.addStatistics(stats);
            }
        }

        report.setEndTime(System.currentTimeMillis());
        
        // Store report in session-based store
        Map<String, BenchmarkReport> sessionStore = sessionReportStore.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        sessionStore.put(report.getId(), report);
        
        return report;
    }

    /**
     * Runs a comprehensive benchmark for searching algorithms (STRING).
     * 
     * @param sessionId The user's session ID
     * @param algorithmNames List of algorithms to benchmark
     * @param datasetSizes List of dataset sizes to test
     * @param target Target string to search for
     * @param dataType Data type (should be STRING)
     * @return BenchmarkReport
     */
    public BenchmarkReport runSearchingBenchmark(String sessionId, List<String> algorithmNames,
                                                 List<Integer> datasetSizes,
                                                 String target,
                                                 String dataType) {
        BenchmarkReport report = new BenchmarkReport("Searching Benchmark (" + dataType + ")");

        // Generate datasets for each size with benchmark naming
        List<String> datasetIds = new ArrayList<>();
        for (int size : datasetSizes) {
            Dataset dataset = datasetService.generateBenchmarkDataset(sessionId, "RANDOM", size, dataType);
            datasetIds.add(dataset.getId());
        }

        // Run each algorithm on each dataset
        for (String algorithmName : algorithmNames) {
            for (String datasetId : datasetIds) {
                try {
                    AlgorithmResult result = searchingService.executeSearchingAlgorithm(
                        sessionId, datasetId, algorithmName, target);
                    report.addResult(result);
                } catch (Exception e) {
                    System.err.println("Benchmark error for " + algorithmName + ": " + e.getMessage());
                }
            }
        }

        // Calculate statistics
        for (String algorithmName : algorithmNames) {
            BenchmarkStatistics stats = calculateStatistics(report.getResults(), algorithmName);
            // Only add statistics if the algorithm had successful runs
            if (stats.getRunsCount() > 0) {
                report.addStatistics(stats);
            }
        }

        report.setEndTime(System.currentTimeMillis());
        
        // Store report in session-based store
        Map<String, BenchmarkReport> sessionStore = sessionReportStore.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        sessionStore.put(report.getId(), report);
        
        return report;
    }

    /**
     * Runs a benchmark for searching algorithms (backwards compatible, INTEGER only).
     * 
     * @param sessionId The user's session ID
     * @param algorithmNames List of algorithms to benchmark
     * @param datasetSizes List of dataset sizes to test
     * @param target Target value to search for
     * @return BenchmarkReport
     */
    public BenchmarkReport runSearchingBenchmark(String sessionId, List<String> algorithmNames,
                                                 List<Integer> datasetSizes,
                                                 int target) {
        return runSearchingBenchmark(sessionId, algorithmNames, datasetSizes, target, "INTEGER");
    }

    /**
     * Calculates statistical summary for an algorithm's benchmark results.
     * 
     * @param allResults All benchmark results
     * @param algorithmName Algorithm to analyze
     * @return BenchmarkStatistics with min, max, avg, median
     */
    private BenchmarkStatistics calculateStatistics(List<AlgorithmResult> allResults, 
                                                    String algorithmName) {
        // Filter results for this algorithm
        // Note: Algorithm names in results may have " (ASC)" or " (DESC)" suffix, so we need to strip it for matching
        List<AlgorithmResult> algorithmResults = allResults.stream()
            .filter(r -> {
                String resultAlgoName = r.getAlgorithmName();
                // Remove " (ASC)" or " (DESC)" suffix if present
                String normalizedName = resultAlgoName.replaceAll(" \\((ASC|DESC)\\)$", "");
                return normalizedName.equals(algorithmName);
            })
            .collect(Collectors.toList());

        if (algorithmResults.isEmpty()) {
            return new BenchmarkStatistics();
        }

        BenchmarkStatistics stats = new BenchmarkStatistics();
        stats.setAlgorithmName(algorithmName);
        stats.setRunsCount(algorithmResults.size());

        // Extract execution times
        List<Double> times = algorithmResults.stream()
            .map(AlgorithmResult::getExecutionTimeMillis)
            .sorted()
            .collect(Collectors.toList());

        // Calculate time statistics
        stats.setMinTimeMillis(times.get(0));
        stats.setMaxTimeMillis(times.get(times.size() - 1));
        stats.setAvgTimeMillis(times.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        stats.setMedianTimeMillis(calculateMedian(times));

        // Extract comparison counts
        List<Long> comparisons = algorithmResults.stream()
            .map(AlgorithmResult::getComparisonCount)
            .collect(Collectors.toList());

        if (!comparisons.isEmpty()) {
            stats.setMinComparisons(Collections.min(comparisons));
            stats.setMaxComparisons(Collections.max(comparisons));
            stats.setAvgComparisons(comparisons.stream().mapToLong(Long::longValue).average().orElse(0.0));
        }

        // Extract swap counts
        List<Long> swaps = algorithmResults.stream()
            .map(AlgorithmResult::getSwapCount)
            .collect(Collectors.toList());

        if (!swaps.isEmpty()) {
            stats.setMinSwaps(Collections.min(swaps));
            stats.setMaxSwaps(Collections.max(swaps));
            stats.setAvgSwaps(swaps.stream().mapToLong(Long::longValue).average().orElse(0.0));
        }

        // Collect dataset sizes
        List<Integer> sizes = algorithmResults.stream()
            .map(AlgorithmResult::getDatasetSize)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        stats.setDatasetSizes(sizes);

        return stats;
    }

    /**
     * Calculates median value from a sorted list of doubles.
     * 
     * @param sortedValues Sorted list of values
     * @return Median value
     */
    private double calculateMedian(List<Double> sortedValues) {
        int size = sortedValues.size();
        if (size == 0) {
            return 0.0;
        }
        
        if (size % 2 == 0) {
            // Even number of elements - average of middle two
            return (sortedValues.get(size / 2 - 1) + sortedValues.get(size / 2)) / 2.0;
        } else {
            // Odd number of elements - middle element
            return sortedValues.get(size / 2);
        }
    }

    /**
     * Retrieves a stored benchmark report.
     * 
     * @param sessionId The user's session ID
     * @param reportId The report ID
     * @return BenchmarkReport if found, null otherwise
     */
    public BenchmarkReport getReport(String sessionId, String reportId) {
        Map<String, BenchmarkReport> sessionStore = sessionReportStore.get(sessionId);
        if (sessionStore == null) {
            return null;
        }
        return sessionStore.get(reportId);
    }

    /**
     * Gets all stored benchmark reports for a session.
     * 
     * @param sessionId The user's session ID
     * @return List of all reports in this session
     */
    public List<BenchmarkReport> getAllReports(String sessionId) {
        Map<String, BenchmarkReport> sessionStore = sessionReportStore.get(sessionId);
        if (sessionStore == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sessionStore.values());
    }

    /**
     * Deletes a benchmark report from a session.
     * 
     * @param sessionId The user's session ID
     * @param reportId The report ID
     * @return true if deleted, false if not found
     */
    public boolean deleteReport(String sessionId, String reportId) {
        Map<String, BenchmarkReport> sessionStore = sessionReportStore.get(sessionId);
        if (sessionStore == null) {
            return false;
        }
        return sessionStore.remove(reportId) != null;
    }

    /**
     * Clears all stored benchmark reports across all sessions.
     */
    public void clearAllReports() {
        sessionReportStore.clear();
    }

    /**
     * Clears all benchmark reports for a specific session.
     * Called when a session expires.
     * 
     * @param sessionId The session ID to clear
     */
    public void clearSessionReports(String sessionId) {
        sessionReportStore.remove(sessionId);
    }

    /**
     * Gets the total number of reports across all sessions.
     * Used for monitoring memory usage.
     * 
     * @return Total count of benchmark reports
     */
    public int getTotalReportCount() {
        return sessionReportStore.values().stream()
            .mapToInt(Map::size)
            .sum();
    }
}

