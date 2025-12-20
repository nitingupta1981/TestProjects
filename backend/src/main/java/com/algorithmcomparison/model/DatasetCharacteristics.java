package com.algorithmcomparison.model;

/**
 * Encapsulates the analysis results of a dataset.
 * 
 * This model contains all characteristics determined by the DatasetAnalyzer,
 * including sortedness, duplicates, range, and distribution information.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class DatasetCharacteristics {
    
    private int size;
    private boolean isSorted;
    private boolean isReverseSorted;
    private double sortedness; // Percentage (0-100)
    private boolean hasDuplicates;
    private double duplicatePercentage;
    private int uniqueCount;
    private int minValue;
    private int maxValue;
    private int rangeSize;
    private DistributionType distribution;
    private String sizeCategory; // SMALL, MEDIUM, LARGE, VERY_LARGE

    /**
     * Enum representing different types of data distributions.
     */
    public enum DistributionType {
        UNIFORM,    // Values evenly distributed
        NORMAL,     // Bell curve distribution
        SKEWED,     // Skewed distribution
        RANDOM      // No clear pattern
    }

    /**
     * Default constructor.
     */
    public DatasetCharacteristics() {
    }

    // Getters and Setters

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        // Automatically set size category
        if (size < 100) {
            this.sizeCategory = "SMALL";
        } else if (size < 1000) {
            this.sizeCategory = "MEDIUM";
        } else if (size < 10000) {
            this.sizeCategory = "LARGE";
        } else {
            this.sizeCategory = "VERY_LARGE";
        }
    }

    public boolean isSorted() {
        return isSorted;
    }

    public void setSorted(boolean sorted) {
        isSorted = sorted;
    }

    public boolean isReverseSorted() {
        return isReverseSorted;
    }

    public void setReverseSorted(boolean reverseSorted) {
        isReverseSorted = reverseSorted;
    }

    public double getSortedness() {
        return sortedness;
    }

    public void setSortedness(double sortedness) {
        this.sortedness = sortedness;
    }

    public boolean isHasDuplicates() {
        return hasDuplicates;
    }

    public void setHasDuplicates(boolean hasDuplicates) {
        this.hasDuplicates = hasDuplicates;
    }

    public double getDuplicatePercentage() {
        return duplicatePercentage;
    }

    public void setDuplicatePercentage(double duplicatePercentage) {
        this.duplicatePercentage = duplicatePercentage;
    }

    public int getUniqueCount() {
        return uniqueCount;
    }

    public void setUniqueCount(int uniqueCount) {
        this.uniqueCount = uniqueCount;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getRangeSize() {
        return rangeSize;
    }

    public void setRangeSize(int rangeSize) {
        this.rangeSize = rangeSize;
    }

    public DistributionType getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionType distribution) {
        this.distribution = distribution;
    }

    public String getSizeCategory() {
        return sizeCategory;
    }

    public void setSizeCategory(String sizeCategory) {
        this.sizeCategory = sizeCategory;
    }

    @Override
    public String toString() {
        return "DatasetCharacteristics{" +
                "size=" + size +
                ", isSorted=" + isSorted +
                ", isReverseSorted=" + isReverseSorted +
                ", sortedness=" + String.format("%.2f", sortedness) + "%" +
                ", hasDuplicates=" + hasDuplicates +
                ", duplicatePercentage=" + String.format("%.2f", duplicatePercentage) + "%" +
                ", uniqueCount=" + uniqueCount +
                ", range=[" + minValue + ", " + maxValue + "]" +
                ", rangeSize=" + rangeSize +
                ", distribution=" + distribution +
                ", sizeCategory=" + sizeCategory +
                '}';
    }
}

