package com.algorithmcomparison.model;

import java.util.List;

/**
 * Represents a request to compare multiple algorithms on datasets.
 * 
 * This model is used as input for comparison endpoints, specifying
 * which algorithms to run on which datasets.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class ComparisonRequest {
    
    private List<String> datasetIds;
    private List<String> algorithmNames;
    private String operationType; // "SORT" or "SEARCH"
    private Integer searchTarget; // For search operations (INTEGER datasets)
    private String searchTargetString; // For search operations (STRING datasets)
    private boolean includeVisualization;

    /**
     * Default constructor.
     */
    public ComparisonRequest() {
    }

    /**
     * Constructor for sorting comparison.
     * 
     * @param datasetIds List of dataset IDs to compare
     * @param algorithmNames List of algorithm names to run
     */
    public ComparisonRequest(List<String> datasetIds, List<String> algorithmNames) {
        this.datasetIds = datasetIds;
        this.algorithmNames = algorithmNames;
        this.operationType = "SORT";
        this.includeVisualization = false;
    }

    // Getters and Setters

    public List<String> getDatasetIds() {
        return datasetIds;
    }

    public void setDatasetIds(List<String> datasetIds) {
        this.datasetIds = datasetIds;
    }

    public List<String> getAlgorithmNames() {
        return algorithmNames;
    }

    public void setAlgorithmNames(List<String> algorithmNames) {
        this.algorithmNames = algorithmNames;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getSearchTarget() {
        return searchTarget;
    }

    public void setSearchTarget(Integer searchTarget) {
        this.searchTarget = searchTarget;
    }

    public String getSearchTargetString() {
        return searchTargetString;
    }

    public void setSearchTargetString(String searchTargetString) {
        this.searchTargetString = searchTargetString;
    }

    public boolean isIncludeVisualization() {
        return includeVisualization;
    }

    public void setIncludeVisualization(boolean includeVisualization) {
        this.includeVisualization = includeVisualization;
    }

    /**
     * Validates the comparison request.
     * 
     * @return true if the request is valid, false otherwise
     */
    public boolean isValid() {
        if (datasetIds == null || datasetIds.isEmpty()) {
            return false;
        }
        if (algorithmNames == null || algorithmNames.isEmpty()) {
            return false;
        }
        if (operationType == null || 
            (!operationType.equals("SORT") && !operationType.equals("SEARCH"))) {
            return false;
        }
        if (operationType.equals("SEARCH") && searchTarget == null && searchTargetString == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ComparisonRequest{" +
                "datasetIds=" + datasetIds +
                ", algorithmNames=" + algorithmNames +
                ", operationType='" + operationType + '\'' +
                ", searchTarget=" + searchTarget +
                ", searchTargetString='" + searchTargetString + '\'' +
                ", includeVisualization=" + includeVisualization +
                '}';
    }
}

