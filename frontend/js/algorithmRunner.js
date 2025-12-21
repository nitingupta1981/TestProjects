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
            // Try to extract error message from response
            try {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Failed to run sorting comparison');
            } catch (e) {
                if (e.message && e.message !== 'Failed to run sorting comparison') {
                    throw e;
                }
                throw new Error('Failed to run sorting comparison');
            }
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
            // Try to extract error message from response
            try {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Failed to run searching comparison');
            } catch (e) {
                if (e.message && e.message !== 'Failed to run searching comparison') {
                    throw e;
                }
                throw new Error('Failed to run searching comparison');
            }
        }
        
        return await response.json();
    }
}

