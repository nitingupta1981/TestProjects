package com.algorithmcomparison.controller;

import com.algorithmcomparison.model.AlgorithmRecommendation;
import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.ComparisonRequest;
import com.algorithmcomparison.model.VisualizationStep;
import com.algorithmcomparison.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for algorithm operations with session-based isolation.
 * 
 * All algorithm operations are performed within the context of a user session.
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
 * @version 2.0
 */
@RestController
@RequestMapping("/api/algorithms")
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
     *   "sortOrder": "ASCENDING",  // or "DESCENDING"
     *   "includeVisualization": false
     * }
     * 
     * @param request Comparison request
     * @param session HTTP session for user isolation
     * @return List of algorithm results
     */
    @PostMapping("/sort/compare")
    public ResponseEntity<?> compareSortingAlgorithms(
            @RequestBody ComparisonRequest request,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            String sortOrder = request.getSortOrder() != null ? request.getSortOrder() : "ASCENDING";
            
            if (request.getDatasetIds().size() == 1) {
                // Single dataset comparison
                List<AlgorithmResult> results = sortingService.compareAlgorithms(
                    sessionId,
                    request.getDatasetIds().get(0), 
                    request.getAlgorithmNames(),
                    sortOrder
                );
                return ResponseEntity.ok(results);
            } else {
                // Multiple dataset comparison
                List<AlgorithmResult> results = sortingService.compareOnMultipleDatasets(
                    sessionId,
                    request.getDatasetIds(), 
                    request.getAlgorithmNames(),
                    sortOrder
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
     * @param session HTTP session for user isolation
     * @return List of algorithm results
     */
    @PostMapping("/search/compare")
    public ResponseEntity<?> compareSearchingAlgorithms(
            @RequestBody ComparisonRequest request,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            
            // Check if string search is requested
            if (request.getSearchTargetString() != null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "String search is currently not supported. Searching algorithms only support INTEGER datasets."
                ));
            }
            
            // Integer search
            int target = request.getSearchTarget() != null ? request.getSearchTarget() : 0;
            
            if (request.getDatasetIds().size() == 1) {
                // Single dataset comparison
                List<AlgorithmResult> results = searchingService.compareAlgorithms(
                    sessionId,
                    request.getDatasetIds().get(0), 
                    request.getAlgorithmNames(),
                    target
                );
                return ResponseEntity.ok(results);
            } else {
                // Multiple dataset comparison
                List<AlgorithmResult> results = searchingService.compareOnMultipleDatasets(
                    sessionId,
                    request.getDatasetIds(), 
                    request.getAlgorithmNames(),
                    target
                );
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
     *   "algorithmName": "Bubble Sort",
     *   "sortOrder": "DESCENDING"  // Optional for sorting algorithms
     * }
     * 
     * @param request Map containing datasetId, algorithmName, and optional sortOrder
     * @param session HTTP session for user isolation
     * @return List of visualization steps
     */
    @PostMapping("/visualize")
    public ResponseEntity<List<VisualizationStep>> visualizeAlgorithm(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            String datasetId = request.get("datasetId");
            String algorithmName = request.get("algorithmName");
            String target = request.get("target"); // For search algorithms - keep as String
            String sortOrder = request.getOrDefault("sortOrder", "ASCENDING"); // Default to ascending
            
            List<VisualizationStep> steps = visualizationService.visualizeAlgorithm(
                sessionId, datasetId, algorithmName, target, sortOrder);
            
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
     * @param session HTTP session for user isolation
     * @return Algorithm recommendation
     */
    @PostMapping("/recommend")
    public ResponseEntity<AlgorithmRecommendation> recommendAlgorithm(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            String datasetId = request.get("datasetId");
            String operationType = request.getOrDefault("operationType", "SORT");
            
            AlgorithmRecommendation recommendation = datasetService.getRecommendation(
                sessionId, datasetId, operationType);
            
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

