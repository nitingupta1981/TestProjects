package com.algorithmcomparison.controller;

import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.ComparisonRequest;
import com.algorithmcomparison.model.VisualizationStep;
import com.algorithmcomparison.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for algorithm operations.
 * 
 * Endpoints:
 * - POST /api/algorithms/sort/compare - Compare sorting algorithms
 * - POST /api/algorithms/search/compare - Compare searching algorithms
 * - POST /api/algorithms/visualize - Get visualization steps
 * - POST /api/algorithms/recommend - Get algorithm recommendations
 * - GET /api/algorithms/sort - List available sorting algorithms
 * - GET /api/algorithms/search - List available searching algorithms
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/algorithms")
@CrossOrigin(origins = "*")
public class AlgorithmController {

    private final SortingService sortingService;
    private final SearchingService searchingService;
    private final VisualizationService visualizationService;
    private final DatasetService datasetService;

    /**
     * Constructor with dependency injection.
     */
    public AlgorithmController(SortingService sortingService,
                              SearchingService searchingService,
                              VisualizationService visualizationService,
                              DatasetService datasetService) {
        this.sortingService = sortingService;
        this.searchingService = searchingService;
        this.visualizationService = visualizationService;
        this.datasetService = datasetService;
    }

    /**
     * Compares multiple sorting algorithms.
     * 
     * Request body example:
     * {
     *   "datasetIds": ["dataset-1", "dataset-2"],
     *   "algorithmNames": ["Quick Sort", "Merge Sort"],
     *   "includeVisualization": false
     * }
     * 
     * @param request Comparison request
     * @return List of algorithm results
     */
    @PostMapping("/sort/compare")
    public ResponseEntity<?> compareSortingAlgorithms(
            @RequestBody ComparisonRequest request) {
        try {
            if (request.getDatasetIds().size() == 1) {
                // Single dataset comparison
                List<AlgorithmResult> results = sortingService.compareAlgorithms(
                    request.getDatasetIds().get(0), 
                    request.getAlgorithmNames()
                );
                return ResponseEntity.ok(results);
            } else {
                // Multiple dataset comparison
                List<AlgorithmResult> results = sortingService.compareOnMultipleDatasets(
                    request.getDatasetIds(), 
                    request.getAlgorithmNames()
                );
                return ResponseEntity.ok(results);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Compares multiple searching algorithms.
     * 
     * Request body example:
     * {
     *   "datasetIds": ["dataset-1"],
     *   "algorithmNames": ["Linear Search", "Binary Search"],
     *   "searchTarget": 42  // for INTEGER datasets
     *   OR
     *   "searchTargetString": "apple"  // for STRING datasets
     * }
     * 
     * @param request Comparison request
     * @return List of algorithm results
     */
    @PostMapping("/search/compare")
    public ResponseEntity<?> compareSearchingAlgorithms(
            @RequestBody ComparisonRequest request) {
        try {
            if (request.getDatasetIds().size() == 1) {
                // Single dataset comparison
                List<AlgorithmResult> results;
                if (request.getSearchTargetString() != null) {
                    // String search
                    results = searchingService.compareAlgorithms(
                        request.getDatasetIds().get(0), 
                        request.getAlgorithmNames(),
                        request.getSearchTargetString()
                    );
                } else {
                    // Integer search
                    int target = request.getSearchTarget() != null ? request.getSearchTarget() : 0;
                    results = searchingService.compareAlgorithms(
                        request.getDatasetIds().get(0), 
                        request.getAlgorithmNames(),
                        target
                    );
                }
                return ResponseEntity.ok(results);
            } else {
                // Multiple dataset comparison
                List<AlgorithmResult> results;
                if (request.getSearchTargetString() != null) {
                    // String search
                    results = searchingService.compareOnMultipleDatasets(
                        request.getDatasetIds(), 
                        request.getAlgorithmNames(),
                        request.getSearchTargetString()
                    );
                } else {
                    // Integer search
                    int target = request.getSearchTarget() != null ? request.getSearchTarget() : 0;
                    results = searchingService.compareOnMultipleDatasets(
                        request.getDatasetIds(), 
                        request.getAlgorithmNames(),
                        target
                    );
                }
                return ResponseEntity.ok(results);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Gets visualization steps for an algorithm.
     * 
     * Request body example:
     * {
     *   "datasetId": "dataset-1",
     *   "algorithmName": "Bubble Sort"
     * }
     * 
     * @param request Map containing datasetId and algorithmName
     * @return List of visualization steps
     */
    @PostMapping("/visualize")
    public ResponseEntity<List<VisualizationStep>> visualizeAlgorithm(
            @RequestBody Map<String, String> request) {
        try {
            String datasetId = request.get("datasetId");
            String algorithmName = request.get("algorithmName");
            String targetStr = request.get("target"); // For search algorithms
            
            Integer target = null;
            if (targetStr != null && !targetStr.isEmpty()) {
                try {
                    target = Integer.parseInt(targetStr);
                } catch (NumberFormatException e) {
                    // Invalid target, will use default
                }
            }
            
            List<VisualizationStep> steps = visualizationService.visualizeAlgorithm(
                datasetId, algorithmName, target);
            
            return ResponseEntity.ok(steps);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets algorithm recommendations for a dataset.
     * 
     * Request body example:
     * {
     *   "datasetId": "dataset-1",
     *   "operationType": "SORT"
     * }
     * 
     * @param request Map containing datasetId and operationType
     * @return Algorithm recommendation
     */
    @PostMapping("/recommend")
    public ResponseEntity<AlgorithmRecommendation> recommendAlgorithm(
            @RequestBody Map<String, String> request) {
        try {
            String datasetId = request.get("datasetId");
            String operationType = request.getOrDefault("operationType", "SORT");
            
            AlgorithmRecommendation recommendation = datasetService.getRecommendation(
                datasetId, operationType);
            
            return ResponseEntity.ok(recommendation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets list of available sorting algorithms.
     * 
     * @return List of algorithm names
     */
    @GetMapping("/sort")
    public ResponseEntity<List<String>> getAvailableSortingAlgorithms() {
        List<String> algorithms = sortingService.getAvailableAlgorithms();
        return ResponseEntity.ok(algorithms);
    }

    /**
     * Gets list of available searching algorithms.
     * 
     * @return List of algorithm names
     */
    @GetMapping("/search")
    public ResponseEntity<List<String>> getAvailableSearchingAlgorithms() {
        List<String> algorithms = searchingService.getAvailableAlgorithms();
        return ResponseEntity.ok(algorithms);
    }
}

