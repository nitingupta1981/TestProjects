/**
 * Recommendation Engine module.
 * Gets and displays algorithm recommendations.
 */

export class RecommendationEngine {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async getRecommendation(datasetId, operationType = 'SORT') {
        const response = await fetch(`${this.apiBaseUrl}/algorithms/recommend`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ datasetId, operationType })
        });
        
        if (!response.ok) {
            throw new Error('Failed to get recommendation');
        }
        
        return await response.json();
    }
}

