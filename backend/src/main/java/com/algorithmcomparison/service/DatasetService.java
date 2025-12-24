package com.algorithmcomparison.service;

import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.model.DatasetCharacteristics;
import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.util.DatasetGenerator;
import com.algorithmcomparison.util.DatasetAnalyzer;
import com.algorithmcomparison.util.AlgorithmRecommendationEngine;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing datasets with session-based isolation.
 * 
 * Provides functionality for:
 * - Generating datasets with various characteristics
 * - Storing and retrieving datasets per user session
 * - Analyzing dataset properties
 * - Getting algorithm recommendations
 * 
 * Each user session has its own isolated dataset storage.
 * Datasets are automatically cleaned up when sessions expire.
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
@Service
public class DatasetService {

    // Session-based storage: sessionId -> (datasetId -> Dataset)
    // Using ConcurrentHashMap for thread-safety
    private final Map<String, Map<String, Dataset>> sessionDatasetStore = new ConcurrentHashMap<>();

    /**
     * Gets or creates a dataset store for a session.
     * 
     * @param sessionId The session ID
     * @return Map of datasets for this session
     */
    private Map<String, Dataset> getSessionStore(String sessionId) {
        return sessionDatasetStore.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
    }

    /**
     * Generates a new dataset based on type and size.
     * 
     * @param sessionId The user's session ID
     * @param type Dataset type: RANDOM, SORTED, or REVERSE_SORTED
     * @param size Number of elements
     * @param minValue Minimum value range (for integers)
     * @param maxValue Maximum value range (for integers)
     * @param dataType Data type: INTEGER or STRING
     * @return Generated dataset
     */
    public Dataset generateDataset(String sessionId, String type, int size, int minValue, int maxValue, String dataType) {
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

        Map<String, Dataset> sessionStore = getSessionStore(sessionId);
        sessionStore.put(dataset.getId(), dataset);
        return dataset;
    }

    /**
     * Generates a new dataset based on type and size (backwards compatible).
     * 
     * @param sessionId The user's session ID
     * @param type Dataset type: RANDOM, SORTED, or REVERSE_SORTED
     * @param size Number of elements
     * @param minValue Minimum value range
     * @param maxValue Maximum value range
     * @return Generated dataset
     */
    public Dataset generateDataset(String sessionId, String type, int size, int minValue, int maxValue) {
        return generateDataset(sessionId, type, size, minValue, maxValue, "INTEGER");
    }

    /**
     * Generates a dataset with default value range [1, 10000].
     * 
     * @param sessionId The user's session ID
     * @param type Dataset type
     * @param size Number of elements
     * @return Generated dataset
     */
    public Dataset generateDataset(String sessionId, String type, int size) {
        return generateDataset(sessionId, type, size, 1, 10000);
    }

    /**
     * Generates a dataset specifically for benchmarking with a descriptive name.
     * This helps users identify auto-generated benchmark datasets.
     * 
     * @param sessionId The user's session ID
     * @param type Dataset type
     * @param size Number of elements
     * @return Generated dataset with benchmark naming
     */
    public Dataset generateBenchmarkDataset(String sessionId, String type, int size) {
        Dataset dataset = generateDataset(sessionId, type, size, 1, 10000);
        // Update the name to indicate it's a benchmark dataset
        String benchmarkName = String.format("[Benchmark] %s - Size %d", type, size);
        dataset.setName(benchmarkName);
        
        // Re-store the dataset with updated name
        Map<String, Dataset> sessionStore = getSessionStore(sessionId);
        sessionStore.put(dataset.getId(), dataset);
        
        return dataset;
    }

    /**
     * Stores a custom dataset from user-provided data.
     * 
     * @param sessionId The user's session ID
     * @param data Array of integers
     * @param name Optional name for the dataset
     * @return Stored dataset
     */
    public Dataset storeCustomDataset(String sessionId, int[] data, String name) {
        String datasetName = (name != null && !name.isEmpty()) ? name : "CUSTOM";
        Dataset dataset = new Dataset(datasetName, data, "CUSTOM");
        
        Map<String, Dataset> sessionStore = getSessionStore(sessionId);
        sessionStore.put(dataset.getId(), dataset);
        return dataset;
    }

    /**
     * Stores a custom dataset from user-provided string data.
     * 
     * @param sessionId The user's session ID
     * @param data Array of strings
     * @param name Optional name for the dataset
     * @return Stored dataset
     */
    public Dataset storeCustomDataset(String sessionId, String[] data, String name) {
        String datasetName = (name != null && !name.isEmpty()) ? name : "CUSTOM";
        Dataset dataset = new Dataset(datasetName, data, "CUSTOM");
        
        Map<String, Dataset> sessionStore = getSessionStore(sessionId);
        sessionStore.put(dataset.getId(), dataset);
        return dataset;
    }

    /**
     * Retrieves a dataset by ID from the user's session.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return Dataset if found, null otherwise
     */
    public Dataset getDataset(String sessionId, String datasetId) {
        Map<String, Dataset> sessionStore = sessionDatasetStore.get(sessionId);
        if (sessionStore == null) {
            return null;
        }
        return sessionStore.get(datasetId);
    }

    /**
     * Gets all stored datasets for a user's session.
     * 
     * @param sessionId The user's session ID
     * @return List of all datasets in this session
     */
    public List<Dataset> getAllDatasets(String sessionId) {
        Map<String, Dataset> sessionStore = sessionDatasetStore.get(sessionId);
        if (sessionStore == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sessionStore.values());
    }

    /**
     * Deletes a dataset by ID from the user's session.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return true if deleted, false if not found
     */
    public boolean deleteDataset(String sessionId, String datasetId) {
        Map<String, Dataset> sessionStore = sessionDatasetStore.get(sessionId);
        if (sessionStore == null) {
            return false;
        }
        return sessionStore.remove(datasetId) != null;
    }

    /**
     * Analyzes dataset characteristics.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @return DatasetCharacteristics with analysis results
     * @throws IllegalArgumentException if dataset not found
     * @throws UnsupportedOperationException if dataset type is not INTEGER
     */
    public DatasetCharacteristics analyzeDataset(String sessionId, String datasetId) {
        Dataset dataset = getDataset(sessionId, datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset not found: " + datasetId);
        }

        // Handle both INTEGER and STRING datasets
        if ("STRING".equals(dataset.getDataType())) {
            // Verify string data is not null
            if (dataset.getStringData() == null) {
                throw new IllegalStateException(
                    "Dataset '" + dataset.getName() + "' has no string data to analyze."
                );
            }
            return DatasetAnalyzer.analyze(dataset.getStringData());
        } else {
            // Verify integer data is not null
            if (dataset.getData() == null) {
                throw new IllegalStateException(
                    "Dataset '" + dataset.getName() + "' has no data to analyze."
                );
            }
            return DatasetAnalyzer.analyze(dataset.getData());
        }
    }

    /**
     * Gets algorithm recommendation for a dataset.
     * 
     * @param sessionId The user's session ID
     * @param datasetId The dataset ID
     * @param operationType "SORT" or "SEARCH"
     * @return Algorithm recommendation
     */
    public AlgorithmRecommendation getRecommendation(String sessionId, String datasetId, String operationType) {
        DatasetCharacteristics characteristics = analyzeDataset(sessionId, datasetId);
        
        if ("SEARCH".equalsIgnoreCase(operationType)) {
            return AlgorithmRecommendationEngine.recommendSearchingAlgorithm(characteristics);
        } else {
            return AlgorithmRecommendationEngine.recommendSortingAlgorithm(characteristics);
        }
    }

    /**
     * Clears all datasets for a specific session.
     * 
     * @param sessionId The user's session ID
     */
    public void clearSessionDatasets(String sessionId) {
        sessionDatasetStore.remove(sessionId);
    }

    /**
     * Clears all stored datasets (admin function).
     */
    public void clearAllDatasets() {
        sessionDatasetStore.clear();
    }

    /**
     * Gets the total number of stored datasets for a session.
     * 
     * @param sessionId The user's session ID
     * @return Dataset count for this session
     */
    public int getDatasetCount(String sessionId) {
        Map<String, Dataset> sessionStore = sessionDatasetStore.get(sessionId);
        return sessionStore != null ? sessionStore.size() : 0;
    }

    /**
     * Gets the total number of active sessions.
     * 
     * @return Number of sessions with datasets
     */
    public int getActiveSessionCount() {
        return sessionDatasetStore.size();
    }

    /**
     * Gets the total number of datasets across all sessions.
     * 
     * @return Total dataset count
     */
    public int getTotalDatasetCount() {
        return sessionDatasetStore.values().stream()
                .mapToInt(Map::size)
                .sum();
    }
}

