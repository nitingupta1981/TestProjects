package com.algorithmcomparison.service;

import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.model.DatasetCharacteristics;
import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.util.DatasetGenerator;
import com.algorithmcomparison.util.DatasetAnalyzer;
import com.algorithmcomparison.util.AlgorithmRecommendationEngine;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for managing datasets.
 * 
 * Provides functionality for:
 * - Generating datasets with various characteristics
 * - Storing and retrieving datasets
 * - Analyzing dataset properties
 * - Getting algorithm recommendations
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@Service
public class DatasetService {

    // In-memory storage for datasets (would use database in production)
    private final Map<String, Dataset> datasetStore = new HashMap<>();

    /**
     * Generates a new dataset based on type and size.
     * 
     * @param type Dataset type: RANDOM, SORTED, or REVERSE_SORTED
     * @param size Number of elements
     * @param minValue Minimum value range (for integers)
     * @param maxValue Maximum value range (for integers)
     * @param dataType Data type: INTEGER or STRING
     * @return Generated dataset
     */
    public Dataset generateDataset(String type, int size, int minValue, int maxValue, String dataType) {
        Dataset dataset;
        
        if ("STRING".equalsIgnoreCase(dataType)) {
            String[] data;
            switch (type.toUpperCase()) {
                case "SORTED":
                    data = DatasetGenerator.generateSortedStrings(size);
                    break;
                case "REVERSE_SORTED":
                    data = DatasetGenerator.generateReverseSortedStrings(size);
                    break;
                case "RANDOM":
                default:
                    data = DatasetGenerator.generateRandomStrings(size);
                    break;
            }
            dataset = new Dataset(data, type.toUpperCase());
        } else {
            // Default to INTEGER
            int[] data;
            switch (type.toUpperCase()) {
                case "SORTED":
                    data = DatasetGenerator.generateSorted(size, minValue, maxValue);
                    break;
                case "REVERSE_SORTED":
                    data = DatasetGenerator.generateReverseSorted(size, minValue, maxValue);
                    break;
                case "RANDOM":
                default:
                    data = DatasetGenerator.generateRandom(size, minValue, maxValue);
                    break;
            }
            dataset = new Dataset(data, type.toUpperCase());
        }

        datasetStore.put(dataset.getId(), dataset);
        return dataset;
    }

    /**
     * Generates a new dataset based on type and size (backwards compatible).
     * 
     * @param type Dataset type: RANDOM, SORTED, or REVERSE_SORTED
     * @param size Number of elements
     * @param minValue Minimum value range
     * @param maxValue Maximum value range
     * @return Generated dataset
     */
    public Dataset generateDataset(String type, int size, int minValue, int maxValue) {
        return generateDataset(type, size, minValue, maxValue, "INTEGER");
    }

    /**
     * Generates a dataset with default value range [1, 10000].
     * 
     * @param type Dataset type
     * @param size Number of elements
     * @return Generated dataset
     */
    public Dataset generateDataset(String type, int size) {
        return generateDataset(type, size, 1, 10000);
    }

    /**
     * Stores a custom dataset from user-provided data.
     * 
     * @param data Array of integers
     * @param name Optional name for the dataset
     * @return Stored dataset
     */
    public Dataset storeCustomDataset(int[] data, String name) {
        String datasetName = (name != null && !name.isEmpty()) ? name : "CUSTOM";
        Dataset dataset = new Dataset(datasetName, data, "CUSTOM");
        datasetStore.put(dataset.getId(), dataset);
        return dataset;
    }

    /**
     * Retrieves a dataset by ID.
     * 
     * @param datasetId The dataset ID
     * @return Dataset if found, null otherwise
     */
    public Dataset getDataset(String datasetId) {
        return datasetStore.get(datasetId);
    }

    /**
     * Gets all stored datasets.
     * 
     * @return List of all datasets
     */
    public List<Dataset> getAllDatasets() {
        return new ArrayList<>(datasetStore.values());
    }

    /**
     * Deletes a dataset by ID.
     * 
     * @param datasetId The dataset ID
     * @return true if deleted, false if not found
     */
    public boolean deleteDataset(String datasetId) {
        return datasetStore.remove(datasetId) != null;
    }

    /**
     * Analyzes dataset characteristics.
     * 
     * @param datasetId The dataset ID
     * @return DatasetCharacteristics with analysis results
     */
    public DatasetCharacteristics analyzeDataset(String datasetId) {
        Dataset dataset = getDataset(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        return DatasetAnalyzer.analyze(dataset.getData());
    }

    /**
     * Gets algorithm recommendation for a dataset.
     * 
     * @param datasetId The dataset ID
     * @param operationType "SORT" or "SEARCH"
     * @return Algorithm recommendation
     */
    public AlgorithmRecommendation getRecommendation(String datasetId, String operationType) {
        DatasetCharacteristics characteristics = analyzeDataset(datasetId);
        
        if ("SEARCH".equalsIgnoreCase(operationType)) {
            return AlgorithmRecommendationEngine.recommendSearchingAlgorithm(characteristics);
        } else {
            return AlgorithmRecommendationEngine.recommendSortingAlgorithm(characteristics);
        }
    }

    /**
     * Clears all stored datasets.
     */
    public void clearAllDatasets() {
        datasetStore.clear();
    }

    /**
     * Gets the total number of stored datasets.
     * 
     * @return Dataset count
     */
    public int getDatasetCount() {
        return datasetStore.size();
    }
}

