/**
 * Dataset Analyzer module.
 * Analyzes dataset characteristics and gets recommendations.
 */

export class DatasetAnalyzer {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async analyzeDataset(datasetId, operationType = 'SORT') {
        const response = await fetch(`${this.apiBaseUrl}/datasets/analyze`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // Enable cookies for session management
            body: JSON.stringify({ datasetId, operationType })
        });
        
        if (!response.ok) {
            throw new Error('Failed to analyze dataset');
        }
        
        return await response.json();
    }
}

