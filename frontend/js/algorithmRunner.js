/**
 * Algorithm Runner module.
 * Executes algorithms and collects results.
 */

export class AlgorithmRunner {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async runSortComparison(datasetIds, algorithmNames) {
        const response = await fetch(`${this.apiBaseUrl}/algorithms/sort/compare`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                datasetIds,
                algorithmNames,
                operationType: 'SORT'
            })
        });
        
        if (!response.ok) {
            throw new Error('Failed to run sorting comparison');
        }
        
        return await response.json();
    }

    async runSearchComparison(datasetIds, algorithmNames, target) {
        const response = await fetch(`${this.apiBaseUrl}/algorithms/search/compare`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                datasetIds,
                algorithmNames,
                operationType: 'SEARCH',
                searchTarget: target
            })
        });
        
        if (!response.ok) {
            throw new Error('Failed to run searching comparison');
        }
        
        return await response.json();
    }
}

