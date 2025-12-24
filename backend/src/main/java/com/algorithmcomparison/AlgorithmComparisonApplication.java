package com.algorithmcomparison;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Spring Boot application class for Algorithm Comparison Tool.
 * 
 * This application provides a REST API for comparing sorting and searching algorithms,
 * analyzing datasets, and generating performance benchmarks.
 * 
 * Features:
 * - Session-based user isolation
 * - Automatic session cleanup
 * - CORS configuration for frontend
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
@SpringBootApplication
@EnableScheduling
public class AlgorithmComparisonApplication {

    /**
     * Main entry point for the Spring Boot application.
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AlgorithmComparisonApplication.class, args);
        System.out.println("Algorithm Comparison Tool is running on http://localhost:8080");
    }

    /**
     * Configure CORS to allow frontend to communicate with backend.
     * Allows all origin patterns with credentials for session management.
     * 
     * @return WebMvcConfigurer with CORS settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")  // Allow all origins with credentials
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);  // Enable credentials for session cookies
            }
        };
    }
}

