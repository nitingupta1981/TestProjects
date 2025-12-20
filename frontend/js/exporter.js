/**
 * Exporter module.
 * Exports results to various formats.
 */

export class Exporter {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    exportResults(results, format) {
        if (format === 'csv') {
            this.exportToCSV(results);
        } else if (format === 'json') {
            this.exportToJSON(results);
        }
    }

    exportToCSV(results) {
        let csv = 'Algorithm,Dataset,Size,Time(ms),Comparisons,Swaps,Complexity\n';
        
        results.forEach(result => {
            csv += `${result.algorithmName},${result.datasetName},${result.datasetSize},`;
            csv += `${result.executionTimeMillis.toFixed(3)},${result.comparisonCount},`;
            csv += `${result.swapCount},${result.complexity}\n`;
        });
        
        this.downloadFile(csv, 'results.csv', 'text/csv');
    }

    exportToJSON(results) {
        const json = JSON.stringify(results, null, 2);
        this.downloadFile(json, 'results.json', 'application/json');
    }

    downloadFile(content, filename, contentType) {
        const blob = new Blob([content], { type: contentType });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    }
}

