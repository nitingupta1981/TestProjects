package com.algorithmcomparison.service;

import com.algorithmcomparison.model.AlgorithmResult;
import com.algorithmcomparison.model.BenchmarkReport;
import com.algorithmcomparison.model.BenchmarkReport.BenchmarkStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for exporting algorithm results and benchmark reports.
 * 
 * Provides functionality for:
 * - Exporting results as CSV format
 * - Exporting results as JSON format
 * - Exporting benchmark reports
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@Service
public class ExportService {

    /**
     * Exports algorithm results to CSV format.
     * 
     * Creates a CSV with columns for all result metrics:
     * Algorithm, Dataset, Size, Time(ms), Comparisons, Swaps, Complexity
     * 
     * @param results List of algorithm results
     * @return CSV string
     */
    public String exportToCSV(List<AlgorithmResult> results) {
        StringBuilder csv = new StringBuilder();
        
        // CSV Header
        csv.append("Algorithm,Dataset,Size,ExecutionTime(ms),Comparisons,Swaps,ArrayAccesses,Complexity,Found,Index\n");
        
        // CSV Rows
        for (AlgorithmResult result : results) {
            csv.append(escapeCsv(result.getAlgorithmName())).append(",");
            csv.append(escapeCsv(result.getDatasetName())).append(",");
            csv.append(result.getDatasetSize()).append(",");
            csv.append(String.format("%.3f", result.getExecutionTimeMillis())).append(",");
            csv.append(result.getComparisonCount()).append(",");
            csv.append(result.getSwapCount()).append(",");
            csv.append(result.getArrayAccessCount()).append(",");
            csv.append(escapeCsv(result.getComplexity())).append(",");
            csv.append(result.isFoundTarget()).append(",");
            csv.append(result.getTargetIndex() != null ? result.getTargetIndex() : "N/A");
            csv.append("\n");
        }
        
        return csv.toString();
    }

    /**
     * Exports algorithm results to JSON format.
     * 
     * @param results List of algorithm results
     * @return JSON string
     */
    public String exportToJSON(List<AlgorithmResult> results) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"results\": [\n");
        
        for (int i = 0; i < results.size(); i++) {
            AlgorithmResult result = results.get(i);
            json.append("    {\n");
            json.append("      \"algorithmName\": \"").append(escapeJson(result.getAlgorithmName())).append("\",\n");
            json.append("      \"datasetName\": \"").append(escapeJson(result.getDatasetName())).append("\",\n");
            json.append("      \"datasetSize\": ").append(result.getDatasetSize()).append(",\n");
            json.append("      \"executionTimeMillis\": ").append(String.format("%.3f", result.getExecutionTimeMillis())).append(",\n");
            json.append("      \"comparisonCount\": ").append(result.getComparisonCount()).append(",\n");
            json.append("      \"swapCount\": ").append(result.getSwapCount()).append(",\n");
            json.append("      \"arrayAccessCount\": ").append(result.getArrayAccessCount()).append(",\n");
            json.append("      \"complexity\": \"").append(escapeJson(result.getComplexity())).append("\",\n");
            json.append("      \"foundTarget\": ").append(result.isFoundTarget()).append(",\n");
            json.append("      \"targetIndex\": ").append(result.getTargetIndex() != null ? result.getTargetIndex() : "null").append("\n");
            json.append("    }");
            
            if (i < results.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ],\n");
        json.append("  \"totalResults\": ").append(results.size()).append("\n");
        json.append("}\n");
        
        return json.toString();
    }

    /**
     * Exports benchmark report to CSV format.
     * 
     * Creates a CSV with statistical summaries for each algorithm.
     * 
     * @param report Benchmark report
     * @return CSV string
     */
    public String exportBenchmarkToCSV(BenchmarkReport report) {
        StringBuilder csv = new StringBuilder();
        
        // CSV Header
        csv.append("Algorithm,Runs,MinTime(ms),MaxTime(ms),AvgTime(ms),MedianTime(ms),");
        csv.append("MinComparisons,MaxComparisons,AvgComparisons,MinSwaps,MaxSwaps,AvgSwaps\n");
        
        // CSV Rows
        for (BenchmarkStatistics stats : report.getStatistics()) {
            csv.append(escapeCsv(stats.getAlgorithmName())).append(",");
            csv.append(stats.getRunsCount()).append(",");
            csv.append(String.format("%.3f", stats.getMinTimeMillis())).append(",");
            csv.append(String.format("%.3f", stats.getMaxTimeMillis())).append(",");
            csv.append(String.format("%.3f", stats.getAvgTimeMillis())).append(",");
            csv.append(String.format("%.3f", stats.getMedianTimeMillis())).append(",");
            csv.append(stats.getMinComparisons()).append(",");
            csv.append(stats.getMaxComparisons()).append(",");
            csv.append(String.format("%.2f", stats.getAvgComparisons())).append(",");
            csv.append(stats.getMinSwaps()).append(",");
            csv.append(stats.getMaxSwaps()).append(",");
            csv.append(String.format("%.2f", stats.getAvgSwaps()));
            csv.append("\n");
        }
        
        return csv.toString();
    }

    /**
     * Exports benchmark report to JSON format.
     * 
     * @param report Benchmark report
     * @return JSON string
     */
    public String exportBenchmarkToJSON(BenchmarkReport report) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"reportId\": \"").append(report.getId()).append("\",\n");
        json.append("  \"reportName\": \"").append(escapeJson(report.getReportName())).append("\",\n");
        json.append("  \"totalRuns\": ").append(report.getTotalRuns()).append(",\n");
        json.append("  \"totalDurationMillis\": ").append(report.getTotalDurationMillis()).append(",\n");
        json.append("  \"statistics\": [\n");
        
        List<BenchmarkStatistics> statsList = report.getStatistics();
        for (int i = 0; i < statsList.size(); i++) {
            BenchmarkStatistics stats = statsList.get(i);
            json.append("    {\n");
            json.append("      \"algorithmName\": \"").append(escapeJson(stats.getAlgorithmName())).append("\",\n");
            json.append("      \"runsCount\": ").append(stats.getRunsCount()).append(",\n");
            json.append("      \"minTimeMillis\": ").append(String.format("%.3f", stats.getMinTimeMillis())).append(",\n");
            json.append("      \"maxTimeMillis\": ").append(String.format("%.3f", stats.getMaxTimeMillis())).append(",\n");
            json.append("      \"avgTimeMillis\": ").append(String.format("%.3f", stats.getAvgTimeMillis())).append(",\n");
            json.append("      \"medianTimeMillis\": ").append(String.format("%.3f", stats.getMedianTimeMillis())).append(",\n");
            json.append("      \"minComparisons\": ").append(stats.getMinComparisons()).append(",\n");
            json.append("      \"maxComparisons\": ").append(stats.getMaxComparisons()).append(",\n");
            json.append("      \"avgComparisons\": ").append(String.format("%.2f", stats.getAvgComparisons())).append(",\n");
            json.append("      \"minSwaps\": ").append(stats.getMinSwaps()).append(",\n");
            json.append("      \"maxSwaps\": ").append(stats.getMaxSwaps()).append(",\n");
            json.append("      \"avgSwaps\": ").append(String.format("%.2f", stats.getAvgSwaps())).append("\n");
            json.append("    }");
            
            if (i < statsList.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ]\n");
        json.append("}\n");
        
        return json.toString();
    }

    /**
     * Escapes special characters for CSV format.
     * 
     * @param value String to escape
     * @return Escaped string
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // If contains comma, quote, or newline, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }

    /**
     * Escapes special characters for JSON format.
     * 
     * @param value String to escape
     * @return Escaped string
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}

