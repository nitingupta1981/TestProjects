package com.algorithmcomparison.model;

/**
 * Represents the result of running an algorithm on a dataset.
 * 
 * This immutable model captures all performance metrics including
 * execution time, comparisons, swaps, and other algorithm-specific metrics.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class AlgorithmResult {
    
    private String algorithmName;
    private String datasetId;
    private String datasetName;
    private int datasetSize;
    private long executionTimeNanos;
    private double executionTimeMillis;
    private long comparisonCount;
    private long swapCount;
    private long arrayAccessCount;
    private long nodesVisited; // For graph-based searches
    private boolean foundTarget; // For searching algorithms
    private Integer targetIndex; // For searching algorithms
    private String complexity; // Big-O notation
    private long timestamp;

    /**
     * Default constructor for JSON serialization.
     */
    public AlgorithmResult() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Builder pattern for creating AlgorithmResult instances.
     */
    public static class Builder {
        private AlgorithmResult result = new AlgorithmResult();

        public Builder algorithmName(String algorithmName) {
            result.algorithmName = algorithmName;
            return this;
        }

        public Builder datasetId(String datasetId) {
            result.datasetId = datasetId;
            return this;
        }

        public Builder datasetName(String datasetName) {
            result.datasetName = datasetName;
            return this;
        }

        public Builder datasetSize(int datasetSize) {
            result.datasetSize = datasetSize;
            return this;
        }

        public Builder executionTimeNanos(long executionTimeNanos) {
            result.executionTimeNanos = executionTimeNanos;
            result.executionTimeMillis = executionTimeNanos / 1_000_000.0;
            return this;
        }

        public Builder comparisonCount(long comparisonCount) {
            result.comparisonCount = comparisonCount;
            return this;
        }

        public Builder swapCount(long swapCount) {
            result.swapCount = swapCount;
            return this;
        }

        public Builder arrayAccessCount(long arrayAccessCount) {
            result.arrayAccessCount = arrayAccessCount;
            return this;
        }

        public Builder nodesVisited(long nodesVisited) {
            result.nodesVisited = nodesVisited;
            return this;
        }

        public Builder foundTarget(boolean foundTarget) {
            result.foundTarget = foundTarget;
            return this;
        }

        public Builder targetIndex(Integer targetIndex) {
            result.targetIndex = targetIndex;
            return this;
        }

        public Builder complexity(String complexity) {
            result.complexity = complexity;
            return this;
        }

        public AlgorithmResult build() {
            return result;
        }
    }

    // Getters and Setters

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public void setDatasetSize(int datasetSize) {
        this.datasetSize = datasetSize;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
        this.executionTimeMillis = executionTimeNanos / 1_000_000.0;
    }

    public double getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public void setExecutionTimeMillis(double executionTimeMillis) {
        this.executionTimeMillis = executionTimeMillis;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public void setComparisonCount(long comparisonCount) {
        this.comparisonCount = comparisonCount;
    }

    public long getSwapCount() {
        return swapCount;
    }

    public void setSwapCount(long swapCount) {
        this.swapCount = swapCount;
    }

    public long getArrayAccessCount() {
        return arrayAccessCount;
    }

    public void setArrayAccessCount(long arrayAccessCount) {
        this.arrayAccessCount = arrayAccessCount;
    }

    public long getNodesVisited() {
        return nodesVisited;
    }

    public void setNodesVisited(long nodesVisited) {
        this.nodesVisited = nodesVisited;
    }

    public boolean isFoundTarget() {
        return foundTarget;
    }

    public void setFoundTarget(boolean foundTarget) {
        this.foundTarget = foundTarget;
    }

    public Integer getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(Integer targetIndex) {
        this.targetIndex = targetIndex;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AlgorithmResult{" +
                "algorithmName='" + algorithmName + '\'' +
                ", datasetSize=" + datasetSize +
                ", executionTime=" + String.format("%.3f", executionTimeMillis) + "ms" +
                ", comparisons=" + comparisonCount +
                ", swaps=" + swapCount +
                ", complexity='" + complexity + '\'' +
                '}';
    }
}

