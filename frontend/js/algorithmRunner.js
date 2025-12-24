/**
 * Algorithm Runner module.
 * Executes algorithms and collects results.
 */

export class AlgorithmRunner {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async runSortComparison(datasetIds, algorithmNames, sortOrder = 'ASCENDING') {
        const response = await fetch(`${this.apiBaseUrl}/algorithms/sort/compare`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // Enable cookies for session management
            body: JSON.stringify({
                datasetIds,
                algorithmNames,
                operationType: 'SORT',
                sortOrder
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
        // Determine if target is a number or string
        const requestBody = {
            datasetIds,
            algorithmNames,
            operationType: 'SEARCH'
        };
        
        // Add the appropriate target field based on type
        if (typeof target === 'number') {
            requestBody.searchTarget = target;
        } else {
            requestBody.searchTargetString = target;
        }
        
        const response = await fetch(`${this.apiBaseUrl}/algorithms/search/compare`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // Enable cookies for session management
            body: JSON.stringify(requestBody)
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

