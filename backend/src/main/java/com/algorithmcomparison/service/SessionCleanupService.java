package com.algorithmcomparison.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for cleaning up expired session data.
 * 
 * Runs periodically to monitor and cleanup session data.
 * Provides logging and statistics for memory management.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
@Service
public class SessionCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(SessionCleanupService.class);

    private final DatasetService datasetService;

    /**
     * Constructor with dependency injection.
     * 
     * @param datasetService Service for dataset management
     */
    public SessionCleanupService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    /**
     * Logs session statistics periodically.
     * Runs every 10 minutes to provide insight into memory usage.
     */
    @Scheduled(fixedRate = 600000) // Every 10 minutes
    public void logSessionStatistics() {
        int activeSessions = datasetService.getActiveSessionCount();
        int totalDatasets = datasetService.getTotalDatasetCount();
        
        logger.info("Session Statistics - Active Sessions: {}, Total Datasets: {}", 
                   activeSessions, totalDatasets);
        
        // Log warning if memory usage is high
        if (totalDatasets > 1000) {
            logger.warn("High dataset count detected: {}. Consider session timeout adjustment.", 
                       totalDatasets);
        }
    }

    /**
     * Manual cleanup method that can be called via admin endpoint.
     * Clears all session data (use with caution).
     */
    public void clearAllSessions() {
        logger.info("Manual cleanup initiated - clearing all sessions");
        datasetService.clearAllDatasets();
        logger.info("All sessions cleared successfully");
    }
}

