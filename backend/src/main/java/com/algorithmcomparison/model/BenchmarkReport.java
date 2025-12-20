package com.algorithmcomparison.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a comprehensive benchmark report with statistical analysis.
 * 
 * Contains results from multiple algorithm runs with aggregated statistics
 * including min, max, average, and median execution times.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class BenchmarkReport {
    
    private String id;
    private String reportName;
    private List<AlgorithmResult> results;
    private List<BenchmarkStatistics> statistics;
    private long startTime;
    private long endTime;
    private long totalDurationMillis;
    private int totalRuns;

    /**
     * Inner class representing statistical summary for an algorithm.
     */
    public static class BenchmarkStatistics {
        private String algorithmName;
        private int runsCount;
        private double minTimeMillis;
        private double maxTimeMillis;
        private double avgTimeMillis;
        private double medianTimeMillis;
        private long minComparisons;
        private long maxComparisons;
        private double avgComparisons;
        private long minSwaps;
        private long maxSwaps;
        private double avgSwaps;
        private List<Integer> datasetSizes;

        public BenchmarkStatistics() {
            this.datasetSizes = new ArrayList<>();
        }

        // Getters and Setters

        public String getAlgorithmName() {
            return algorithmName;
        }

        public void setAlgorithmName(String algorithmName) {
            this.algorithmName = algorithmName;
        }

        public int getRunsCount() {
            return runsCount;
        }

        public void setRunsCount(int runsCount) {
            this.runsCount = runsCount;
        }

        public double getMinTimeMillis() {
            return minTimeMillis;
        }

        public void setMinTimeMillis(double minTimeMillis) {
            this.minTimeMillis = minTimeMillis;
        }

        public double getMaxTimeMillis() {
            return maxTimeMillis;
        }

        public void setMaxTimeMillis(double maxTimeMillis) {
            this.maxTimeMillis = maxTimeMillis;
        }

        public double getAvgTimeMillis() {
            return avgTimeMillis;
        }

        public void setAvgTimeMillis(double avgTimeMillis) {
            this.avgTimeMillis = avgTimeMillis;
        }

        public double getMedianTimeMillis() {
            return medianTimeMillis;
        }

        public void setMedianTimeMillis(double medianTimeMillis) {
            this.medianTimeMillis = medianTimeMillis;
        }

        public long getMinComparisons() {
            return minComparisons;
        }

        public void setMinComparisons(long minComparisons) {
            this.minComparisons = minComparisons;
        }

        public long getMaxComparisons() {
            return maxComparisons;
        }

        public void setMaxComparisons(long maxComparisons) {
            this.maxComparisons = maxComparisons;
        }

        public double getAvgComparisons() {
            return avgComparisons;
        }

        public void setAvgComparisons(double avgComparisons) {
            this.avgComparisons = avgComparisons;
        }

        public long getMinSwaps() {
            return minSwaps;
        }

        public void setMinSwaps(long minSwaps) {
            this.minSwaps = minSwaps;
        }

        public long getMaxSwaps() {
            return maxSwaps;
        }

        public void setMaxSwaps(long maxSwaps) {
            this.maxSwaps = maxSwaps;
        }

        public double getAvgSwaps() {
            return avgSwaps;
        }

        public void setAvgSwaps(double avgSwaps) {
            this.avgSwaps = avgSwaps;
        }

        public List<Integer> getDatasetSizes() {
            return datasetSizes;
        }

        public void setDatasetSizes(List<Integer> datasetSizes) {
            this.datasetSizes = datasetSizes;
        }

        @Override
        public String toString() {
            return "BenchmarkStatistics{" +
                    "algorithmName='" + algorithmName + '\'' +
                    ", runsCount=" + runsCount +
                    ", avgTimeMillis=" + String.format("%.3f", avgTimeMillis) +
                    ", medianTimeMillis=" + String.format("%.3f", medianTimeMillis) +
                    '}';
        }
    }

    /**
     * Default constructor.
     */
    public BenchmarkReport() {
        this.results = new ArrayList<>();
        this.statistics = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Constructor with report name.
     * 
     * @param reportName Name of the benchmark report
     */
    public BenchmarkReport(String reportName) {
        this();
        this.reportName = reportName;
        this.id = "BENCH_" + System.currentTimeMillis();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<AlgorithmResult> getResults() {
        return results;
    }

    public void setResults(List<AlgorithmResult> results) {
        this.results = results;
        this.totalRuns = results.size();
    }

    public void addResult(AlgorithmResult result) {
        this.results.add(result);
        this.totalRuns = results.size();
    }

    public List<BenchmarkStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<BenchmarkStatistics> statistics) {
        this.statistics = statistics;
    }

    public void addStatistics(BenchmarkStatistics stats) {
        this.statistics.add(stats);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.totalDurationMillis = endTime - startTime;
    }

    public long getTotalDurationMillis() {
        return totalDurationMillis;
    }

    public void setTotalDurationMillis(long totalDurationMillis) {
        this.totalDurationMillis = totalDurationMillis;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    @Override
    public String toString() {
        return "BenchmarkReport{" +
                "id='" + id + '\'' +
                ", reportName='" + reportName + '\'' +
                ", totalRuns=" + totalRuns +
                ", totalDurationMillis=" + totalDurationMillis +
                '}';
    }
}

