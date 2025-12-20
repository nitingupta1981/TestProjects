package com.algorithmcomparison.controller;

import com.algorithmcomparison.model.Dataset;
import com.algorithmcomparison.model.DatasetCharacteristics;
import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.service.DatasetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for dataset operations.
 * 
 * Endpoints:
 * - POST /api/datasets/generate - Generate new dataset
 * - POST /api/datasets/upload - Upload custom dataset
 * - POST /api/datasets/analyze - Analyze dataset characteristics
 * - GET /api/datasets - Get all datasets
 * - GET /api/datasets/{id} - Get specific dataset
 * - DELETE /api/datasets/{id} - Delete dataset
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/datasets")
@CrossOrigin(origins = "*")
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
     * @return Generated dataset
     */
    @PostMapping("/generate")
    public ResponseEntity<Dataset> generateDataset(@RequestBody Map<String, Object> request) {
        try {
            String type = (String) request.get("type");
            int size = (Integer) request.get("size");
            String dataType = (String) request.getOrDefault("dataType", "INTEGER");
            
            Dataset dataset;
            if (request.containsKey("minValue") && request.containsKey("maxValue")) {
                int minValue = (Integer) request.get("minValue");
                int maxValue = (Integer) request.get("maxValue");
                dataset = datasetService.generateDataset(type, size, minValue, maxValue, dataType);
            } else {
                dataset = datasetService.generateDataset(type, size, 1, 10000, dataType);
            }
            
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Uploads a custom dataset.
     * 
     * Request body example:
     * {
     *   "data": [5, 2, 8, 1, 9],
     *   "name": "My Custom Dataset"
     * }
     * 
     * @param request Map containing dataset data and name
     * @return Stored dataset
     */
    @PostMapping("/upload")
    public ResponseEntity<Dataset> uploadDataset(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> dataList = (List<Integer>) request.get("data");
            String name = (String) request.getOrDefault("name", "Custom");
            
            int[] data = dataList.stream().mapToInt(Integer::intValue).toArray();
            Dataset dataset = datasetService.storeCustomDataset(data, name);
            
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
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
     * @return Map with characteristics and recommendation
     */
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeDataset(@RequestBody Map<String, String> request) {
        try {
            String datasetId = request.get("datasetId");
            String operationType = request.getOrDefault("operationType", "SORT");
            
            DatasetCharacteristics characteristics = datasetService.analyzeDataset(datasetId);
            AlgorithmRecommendation recommendation = datasetService.getRecommendation(datasetId, operationType);
            
            Map<String, Object> response = Map.of(
                "characteristics", characteristics,
                "recommendation", recommendation
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets all datasets.
     * 
     * @return List of all datasets
     */
    @GetMapping
    public ResponseEntity<List<Dataset>> getAllDatasets() {
        List<Dataset> datasets = datasetService.getAllDatasets();
        return ResponseEntity.ok(datasets);
    }

    /**
     * Gets a specific dataset by ID.
     * 
     * @param id Dataset ID
     * @return Dataset if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dataset> getDataset(@PathVariable String id) {
        Dataset dataset = datasetService.getDataset(id);
        if (dataset != null) {
            return ResponseEntity.ok(dataset);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a dataset.
     * 
     * @param id Dataset ID
     * @return Success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataset(@PathVariable String id) {
        boolean deleted = datasetService.deleteDataset(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Exports a dataset in specified format.
     * 
     * @param id Dataset ID
     * @param format Export format (json or csv)
     * @return Dataset data in requested format
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<Map<String, Object>> exportDataset(
            @PathVariable String id,
            @RequestParam(defaultValue = "json") String format) {
        Dataset dataset = datasetService.getDataset(id);
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

