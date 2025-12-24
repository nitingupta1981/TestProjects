package com.algorithmcomparison.controller;

import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.BenchmarkReport;
import com.algorithmcomparison.service.BenchmarkService;
import com.algorithmcomparison.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for export and benchmark operations.
 * 
 * Endpoints:
 * - POST /api/benchmark/run - Run comprehensive benchmark
 * - GET /api/benchmark/results/{id} - Get benchmark results
 * - GET /api/benchmark/results - Get all benchmarks
 * - GET /api/results/export/{format} - Export results
 * - POST /api/results/export - Export with custom data
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
@RestController
@RequestMapping("/api")
public class ExportController {

    private final ExportService exportService;
    private final BenchmarkService benchmarkService;

    /**
     * Constructor with dependency injection.
     */
    public ExportController(ExportService exportService, BenchmarkService benchmarkService) {
        this.exportService = exportService;
        this.benchmarkService = benchmarkService;
    }

    /**
     * Runs a comprehensive benchmark.
     * 
     * Request body example:
     * {
     *   "operationType": "SORT",
     *   "algorithmNames": ["Quick Sort", "Merge Sort"],
     *   "datasetSizes": [100, 1000, 5000],
     *   "datasetType": "RANDOM"
     * }
     * 
     * @param request Benchmark configuration
     * @param session HTTP session for user isolation
     * @return Benchmark report
     */
    @PostMapping("/benchmark/run")
    public ResponseEntity<BenchmarkReport> runBenchmark(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            String sessionId = session.getId();
            String operationType = (String) request.getOrDefault("operationType", "SORT");
            
            @SuppressWarnings("unchecked")
            List<String> algorithmNames = (List<String>) request.get("algorithmNames");
            
            if (algorithmNames == null || algorithmNames.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            BenchmarkReport report;
            
            if ("SORT".equalsIgnoreCase(operationType)) {
                String datasetType = (String) request.getOrDefault("datasetType", "RANDOM");
                
                if (request.containsKey("datasetSizes")) {
                    @SuppressWarnings("unchecked")
                    List<Integer> datasetSizes = (List<Integer>) request.get("datasetSizes");
                    report = benchmarkService.runSortingBenchmark(sessionId, algorithmNames, datasetSizes, datasetType);
                } else {
                    report = benchmarkService.runSortingBenchmark(sessionId, algorithmNames, datasetType);
                }
            } else {
                int target = (Integer) request.getOrDefault("target", 50);
                @SuppressWarnings("unchecked")
                List<Integer> datasetSizes = (List<Integer>) request.getOrDefault("datasetSizes", 
                    List.of(100, 1000, 5000));
                report = benchmarkService.runSearchingBenchmark(sessionId, algorithmNames, datasetSizes, target);
            }
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets a specific benchmark report.
     * 
     * @param id Benchmark report ID
     * @param session HTTP session for user isolation
     * @return Benchmark report
     */
    @GetMapping("/benchmark/results/{id}")
    public ResponseEntity<BenchmarkReport> getBenchmarkReport(@PathVariable String id, HttpSession session) {
        String sessionId = session.getId();
        BenchmarkReport report = benchmarkService.getReport(sessionId, id);
        if (report != null) {
            return ResponseEntity.ok(report);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Gets all benchmark reports for the current session.
     * 
     * @param session HTTP session for user isolation
     * @return List of all reports in this session
     */
    @GetMapping("/benchmark/results")
    public ResponseEntity<List<BenchmarkReport>> getAllBenchmarkReports(HttpSession session) {
        String sessionId = session.getId();
        List<BenchmarkReport> reports = benchmarkService.getAllReports(sessionId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Exports results in specified format.
     * 
     * Request body example:
     * {
     *   "results": [...],
     *   "format": "csv"
     * }
     * 
     * @param request Export request
     * @return Exported data as string
     */
    @PostMapping("/results/export")
    public ResponseEntity<String> exportResults(@RequestBody Map<String, Object> request) {
        try {
            String format = (String) request.getOrDefault("format", "csv");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resultMaps = (List<Map<String, Object>>) request.get("results");
            
            // Convert maps to AlgorithmResult objects
            List<AlgorithmResult> results = resultMaps.stream()
                .map(this::mapToAlgorithmResult)
                .toList();
            
            String exportedData;
            MediaType mediaType;
            String filename;
            
            if ("json".equalsIgnoreCase(format)) {
                exportedData = exportService.exportToJSON(results);
                mediaType = MediaType.APPLICATION_JSON;
                filename = "results.json";
            } else {
                exportedData = exportService.exportToCSV(results);
                mediaType = MediaType.parseMediaType("text/csv");
                filename = "results.csv";
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(exportedData);
                
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Exports a benchmark report.
     * 
     * @param id Benchmark report ID
     * @param format Export format (csv or json)
     * @param session HTTP session for user isolation
     * @return Exported data
     */
    @GetMapping("/benchmark/export/{id}")
    public ResponseEntity<String> exportBenchmark(@PathVariable String id, 
                                                  @RequestParam(defaultValue = "csv") String format,
                                                  HttpSession session) {
        try {
            String sessionId = session.getId();
            BenchmarkReport report = benchmarkService.getReport(sessionId, id);
            if (report == null) {
                return ResponseEntity.notFound().build();
            }
            
            String exportedData;
            MediaType mediaType;
            String filename;
            
            if ("json".equalsIgnoreCase(format)) {
                exportedData = exportService.exportBenchmarkToJSON(report);
                mediaType = MediaType.APPLICATION_JSON;
                filename = "benchmark_" + id + ".json";
            } else {
                exportedData = exportService.exportBenchmarkToCSV(report);
                mediaType = MediaType.parseMediaType("text/csv");
                filename = "benchmark_" + id + ".csv";
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(exportedData);
                
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Helper method to convert map to AlgorithmResult.
     */
    private AlgorithmResult mapToAlgorithmResult(Map<String, Object> map) {
        AlgorithmResult result = new AlgorithmResult();
        result.setAlgorithmName((String) map.get("algorithmName"));
        result.setDatasetName((String) map.get("datasetName"));
        result.setDatasetSize((Integer) map.get("datasetSize"));
        result.setExecutionTimeMillis(((Number) map.get("executionTimeMillis")).doubleValue());
        result.setComparisonCount(((Number) map.get("comparisonCount")).longValue());
        result.setSwapCount(((Number) map.get("swapCount")).longValue());
        result.setArrayAccessCount(((Number) map.getOrDefault("arrayAccessCount", 0)).longValue());
        result.setComplexity((String) map.get("complexity"));
        return result;
    }
}

