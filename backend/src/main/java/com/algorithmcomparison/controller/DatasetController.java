package com.algorithmcomparison.controller;

import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.model.DatasetCharacteristics;
import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.service.DatasetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for dataset operations with session-based isolation.
 * 
 * Each user session has its own isolated dataset storage.
 * Sessions automatically expire after 1 hour of inactivity.
 * 
 * Endpoints:
 * - POST /api/datasets/generate - Generate new dataset
 * - POST /api/datasets/upload - Upload custom dataset
 * - POST /api/datasets/analyze - Analyze dataset characteristics
 * - GET /api/datasets - Get all datasets for current session
 * - GET /api/datasets/{id} - Get specific dataset
 * - DELETE /api/datasets/{id} - Delete dataset
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

    private final DatasetService datasetService;

    /**
     * Constructor with dependency injection.
     * 
     * @param datasetService Service for dataset management
     */
    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }
    
    /**
     * Helper method to log session information for debugging.
     */
    private void logSessionInfo(HttpSession session, String operation) {
        System.out.println(String.format("[SESSION-DEBUG] Operation: %s | Session ID: %s | IsNew: %s | CreationTime: %s",
            operation, session.getId(), session.isNew(), session.getCreationTime()));
    }

    /**
     * Generates a new dataset.
     * 
     * Request body example:
     * {
     *   "type": "RANDOM",
     *   "size": 1000,
     *   "minValue": 1,
     *   "maxValue": 10000,
     *   "dataType": "INTEGER"
     * }
     * 
     * @param request Map containing generation parameters
     * @param session HTTP session for user isolation
     * @return Generated dataset
     */
    @PostMapping("/generate")
    public ResponseEntity<Dataset> generateDataset(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            logSessionInfo(session, "generateDataset");
            String sessionId = session.getId();
            String type = (String) request.get("type");
            int size = (Integer) request.get("size");
            String dataType = (String) request.getOrDefault("dataType", "INTEGER");
            
            Dataset dataset;
            if (request.containsKey("minValue") && request.containsKey("maxValue")) {
                int minValue = (Integer) request.get("minValue");
                int maxValue = (Integer) request.get("maxValue");
                dataset = datasetService.generateDataset(sessionId, type, size, minValue, maxValue, dataType);
            } else {
                dataset = datasetService.generateDataset(sessionId, type, size, 1, 10000, dataType);
            }
            
            System.out.println(String.format("[SESSION-DEBUG] Dataset generated: %s for session: %s", dataset.getId(), sessionId));
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
            System.err.println("[SESSION-DEBUG] Error generating dataset: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Uploads a custom dataset.
     * 
     * Request body example for integers:
     * {
     *   "data": [5, 2, 8, 1, 9],
     *   "name": "My Custom Dataset",
     *   "dataType": "INTEGER"
     * }
     * 
     * Request body example for strings:
     * {
     *   "data": ["apple", "banana", "cherry"],
     *   "name": "My Custom String Dataset",
     *   "dataType": "STRING"
     * }
     * 
     * @param request Map containing dataset data, name, and dataType
     * @param session HTTP session for user isolation
     * @return Stored dataset
     */
    @PostMapping("/upload")
    public ResponseEntity<Dataset> uploadDataset(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            logSessionInfo(session, "uploadDataset");
            String sessionId = session.getId();
            String name = (String) request.getOrDefault("name", "Custom");
            String dataType = (String) request.getOrDefault("dataType", "INTEGER");
            
            Dataset dataset;
            if ("STRING".equalsIgnoreCase(dataType)) {
                @SuppressWarnings("unchecked")
                List<String> dataList = (List<String>) request.get("data");
                String[] data = dataList.toArray(new String[0]);
                dataset = datasetService.storeCustomDataset(sessionId, data, name);
            } else {
                @SuppressWarnings("unchecked")
                List<Integer> dataList = (List<Integer>) request.get("data");
                int[] data = dataList.stream().mapToInt(Integer::intValue).toArray();
                dataset = datasetService.storeCustomDataset(sessionId, data, name);
            }
            
            System.out.println(String.format("[SESSION-DEBUG] Dataset uploaded: %s for session: %s", dataset.getId(), sessionId));
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
            System.err.println("[SESSION-DEBUG] Error uploading dataset: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Analyzes a dataset and optionally returns algorithm recommendations.
     * 
     * Request body example:
     * {
     *   "datasetId": "abc-123",
     *   "operationType": "SORT"
     * }
     * 
     * @param request Map containing datasetId and operationType
     * @param session HTTP session for user isolation
     * @return Map with characteristics and recommendation
     */
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeDataset(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            logSessionInfo(session, "analyzeDataset");
            String sessionId = session.getId();
            String datasetId = request.get("datasetId");
            String operationType = request.getOrDefault("operationType", "SORT");
            
            System.out.println(String.format("[SESSION-DEBUG] Analyzing dataset: %s for session: %s", datasetId, sessionId));
            DatasetCharacteristics characteristics = datasetService.analyzeDataset(sessionId, datasetId);
            AlgorithmRecommendation recommendation = datasetService.getRecommendation(sessionId, datasetId, operationType);
            
            Map<String, Object> response = Map.of(
                "characteristics", characteristics,
                "recommendation", recommendation
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.err.println("[SESSION-DEBUG] Dataset not found for analysis: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("[SESSION-DEBUG] Error analyzing dataset: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets all datasets for the current session.
     * 
     * @param session HTTP session for user isolation
     * @return List of all datasets in this session
     */
    @GetMapping
    public ResponseEntity<List<Dataset>> getAllDatasets(HttpSession session) {
        logSessionInfo(session, "getAllDatasets");
        String sessionId = session.getId();
        List<Dataset> datasets = datasetService.getAllDatasets(sessionId);
        System.out.println(String.format("[SESSION-DEBUG] Found %d datasets for session: %s", datasets.size(), sessionId));
        return ResponseEntity.ok(datasets);
    }

    /**
     * Gets a specific dataset by ID from the current session.
     * 
     * @param id Dataset ID
     * @param session HTTP session for user isolation
     * @return Dataset if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dataset> getDataset(@PathVariable String id, HttpSession session) {
        logSessionInfo(session, "getDataset");
        String sessionId = session.getId();
        Dataset dataset = datasetService.getDataset(sessionId, id);
        if (dataset != null) {
            System.out.println(String.format("[SESSION-DEBUG] Retrieved dataset: %s for session: %s", id, sessionId));
            return ResponseEntity.ok(dataset);
        }
        System.err.println(String.format("[SESSION-DEBUG] Dataset not found: %s for session: %s", id, sessionId));
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a dataset from the current session.
     * 
     * @param id Dataset ID
     * @param session HTTP session for user isolation
     * @return Success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataset(@PathVariable String id, HttpSession session) {
        logSessionInfo(session, "deleteDataset");
        String sessionId = session.getId();
        boolean deleted = datasetService.deleteDataset(sessionId, id);
        if (deleted) {
            System.out.println(String.format("[SESSION-DEBUG] Deleted dataset: %s for session: %s", id, sessionId));
            return ResponseEntity.ok().build();
        }
        System.err.println(String.format("[SESSION-DEBUG] Dataset not found for deletion: %s for session: %s", id, sessionId));
        return ResponseEntity.notFound().build();
    }

    /**
     * Exports a dataset in specified format.
     * 
     * @param id Dataset ID
     * @param format Export format (json or csv)
     * @param session HTTP session for user isolation
     * @return Dataset data in requested format
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<Map<String, Object>> exportDataset(
            @PathVariable String id,
            @RequestParam(defaultValue = "json") String format,
            HttpSession session) {
        String sessionId = session.getId();
        Dataset dataset = datasetService.getDataset(sessionId, id);
        if (dataset == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = Map.of(
            "id", dataset.getId(),
            "name", dataset.getName(),
            "type", dataset.getType(),
            "dataType", dataset.getDataType(),
            "size", dataset.getSize(),
            "data", "STRING".equals(dataset.getDataType()) ? dataset.getStringData() : dataset.getData()
        );

        return ResponseEntity.ok(response);
    }
}

