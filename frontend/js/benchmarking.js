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
        // Extract data type from report name or dataset names
        let dataType = 'N/A';
        let datasetSizes = new Set();
        
        // First, try to extract from report name (e.g., "Sorting Benchmark - RANDOM (STRING)")
        if (report.reportName) {
            const match = report.reportName.match(/\(([A-Z]+)\)$/);
            if (match) {
                dataType = match[1];
            }
        }
        
        // If not found in report name, try to extract from dataset names
        if (dataType === 'N/A' && report.results && report.results.length > 0) {
            // Check if dataset names contain _STR or _INT
            const firstDatasetName = report.results[0].datasetName || '';
            if (firstDatasetName.includes('_STR')) {
                dataType = 'STRING';
            } else if (firstDatasetName.includes('_INT')) {
                dataType = 'INTEGER';
            }
        }
        
        // Collect unique dataset sizes
        if (report.results && report.results.length > 0) {
            report.results.forEach(result => {
                if (result.datasetSize) {
                    datasetSizes.add(result.datasetSize);
                }
            });
        }
        
        const datasetSizesStr = datasetSizes.size > 0 
            ? Array.from(datasetSizes).sort((a, b) => a - b).join(', ')
            : 'N/A';
        
        let html = `
            <div class="benchmark-info">
                <h3>${report.reportName}</h3>
                <div class="benchmark-metadata">
                    <p><strong>Data Type:</strong> <span class="badge">${dataType}</span></p>
                    <p><strong>Dataset Sizes:</strong> ${datasetSizesStr} elements</p>
                    <p><strong>Total Runs:</strong> ${report.totalRuns}</p>
                    <p><strong>Duration:</strong> ${report.totalDurationMillis} ms</p>
                </div>
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

