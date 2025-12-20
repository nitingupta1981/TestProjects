/**
 * Comparator module.
 * Creates comparison tables and charts.
 */

export class Comparator {
    createResultsTable(results) {
        const table = document.createElement('table');
        table.className = 'results-table';
        
        // Create header
        const thead = document.createElement('thead');
        thead.innerHTML = `
            <tr>
                <th>Algorithm</th>
                <th>Dataset</th>
                <th>Size</th>
                <th>Time (ms)</th>
                <th>Comparisons</th>
                <th>Swaps</th>
                <th>Complexity</th>
            </tr>
        `;
        table.appendChild(thead);
        
        // Find best time
        const minTime = Math.min(...results.map(r => r.executionTimeMillis));
        
        // Create body
        const tbody = document.createElement('tbody');
        results.forEach(result => {
            const row = document.createElement('tr');
            if (result.executionTimeMillis === minTime) {
                row.classList.add('best-time');
            }
            
            row.innerHTML = `
                <td>${result.algorithmName}</td>
                <td>${result.datasetName}</td>
                <td>${result.datasetSize}</td>
                <td>${result.executionTimeMillis.toFixed(3)}</td>
                <td>${result.comparisonCount}</td>
                <td>${result.swapCount}</td>
                <td>${result.complexity}</td>
            `;
            tbody.appendChild(row);
        });
        table.appendChild(tbody);
        
        return table;
    }
}

