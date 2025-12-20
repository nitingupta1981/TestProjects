package com.algorithmcomparison.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an algorithm recommendation with reasoning.
 * 
 * This model contains the recommended algorithm name, explanation of why
 * it's recommended, expected complexity, confidence level, and alternatives.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class AlgorithmRecommendation {
    
    private String algorithmName;
    private String reason;
    private String expectedComplexity;
    private ConfidenceLevel confidence;
    private List<String> alternatives;
    private List<String> warnings;

    /**
     * Enum representing confidence levels for recommendations.
     */
    public enum ConfidenceLevel {
        HIGH,    // Strong recommendation based on clear dataset characteristics
        MEDIUM,  // Good recommendation but other options may also work well
        LOW      // Tentative recommendation, dataset characteristics are ambiguous
    }

    /**
     * Default constructor.
     */
    public AlgorithmRecommendation() {
        this.alternatives = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

    /**
     * Constructor with main fields.
     * 
     * @param algorithmName The recommended algorithm name
     * @param reason Explanation of why this algorithm is recommended
     * @param expectedComplexity Big-O notation for expected performance
     * @param confidence Confidence level of the recommendation
     */
    public AlgorithmRecommendation(String algorithmName, String reason, 
                                   String expectedComplexity, ConfidenceLevel confidence) {
        this();
        this.algorithmName = algorithmName;
        this.reason = reason;
        this.expectedComplexity = expectedComplexity;
        this.confidence = confidence;
    }

    // Getters and Setters

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExpectedComplexity() {
        return expectedComplexity;
    }

    public void setExpectedComplexity(String expectedComplexity) {
        this.expectedComplexity = expectedComplexity;
    }

    public ConfidenceLevel getConfidence() {
        return confidence;
    }

    public void setConfidence(ConfidenceLevel confidence) {
        this.confidence = confidence;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    /**
     * Adds an alternative algorithm to the list.
     * 
     * @param algorithmName Name of the alternative algorithm
     */
    public void addAlternative(String algorithmName) {
        this.alternatives.add(algorithmName);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    /**
     * Adds a warning about algorithms to avoid.
     * 
     * @param warning The warning message
     */
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    @Override
    public String toString() {
        return "AlgorithmRecommendation{" +
                "algorithmName='" + algorithmName + '\'' +
                ", reason='" + reason + '\'' +
                ", expectedComplexity='" + expectedComplexity + '\'' +
                ", confidence=" + confidence +
                ", alternatives=" + alternatives +
                ", warnings=" + warnings +
                '}';
    }
}

