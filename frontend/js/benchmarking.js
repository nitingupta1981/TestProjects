/**
 * Benchmarking module.
 * Runs and displays benchmark results.
 */

export class Benchmarking {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async runBenchmark(operationType, algorithmNames, datasetSizes, dataType = 'INTEGER') {
        const requestBody = {
            operationType,
            algorithmNames,
            datasetSizes,
            datasetType: 'RANDOM',
            dataType
        };
        
        // For searching with string data type, add a sample target
        if (operationType === 'SEARCH' && dataType === 'STRING') {
            requestBody.targetString = 'apple'; // Default string target
        }
        
        const response = await fetch(`${this.apiBaseUrl}/benchmark/run`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // Enable cookies for session management
            body: JSON.stringify(requestBody)
        });
        
        if (!response.ok) {
            throw new Error('Failed to run benchmark');
        }
        
        return await response.json();
    }

    formatBenchmarkReport(report) {
        let html = `
            <div class="benchmark-info">
                <h3>${report.reportName}</h3>
                <p>Total Runs: ${report.totalRuns}</p>
                <p>Duration: ${report.totalDurationMillis} ms</p>
            </div>
            <div class="benchmark-stats">
        `;
        
        report.statistics.forEach(stats => {
            html += `
                <div class="stat-card">
                    <h4>${stats.algorithmName}</h4>
                    <div class="stat-row">
                        <span>Runs:</span>
                        <span>${stats.runsCount}</span>
                    </div>
                    <div class="stat-row">
                        <span>Min Time:</span>
                        <span>${stats.minTimeMillis.toFixed(3)} ms</span>
                    </div>
                    <div class="stat-row">
                        <span>Max Time:</span>
                        <span>${stats.maxTimeMillis.toFixed(3)} ms</span>
                    </div>
                    <div class="stat-row">
                        <span>Avg Time:</span>
                        <span>${stats.avgTimeMillis.toFixed(3)} ms</span>
                    </div>
                    <div class="stat-row">
                        <span>Median Time:</span>
                        <span>${stats.medianTimeMillis.toFixed(3)} ms</span>
                    </div>
                    <div class="stat-row">
                        <span>Avg Comparisons:</span>
                        <span>${stats.avgComparisons.toFixed(0)}</span>
                    </div>
                </div>
            `;
        });
        
        html += '</div>';
        return html;
    }
}

