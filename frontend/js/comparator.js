/**
 * Comparator module.
 * Creates comparison tables and charts.
 */

export class Comparator {
    createResultsTable(results) {
        const table = document.createElement('table');
        table.className = 'results-table';
        
        // Determine if these are search results (check if foundTarget property exists)
        const isSearchResults = results.length > 0 && results[0].hasOwnProperty('foundTarget');
        
        // Create header based on operation type
        const thead = document.createElement('thead');
        if (isSearchResults) {
            thead.innerHTML = `
                <tr>
                    <th>Algorithm</th>
                    <th>Dataset</th>
                    <th>Size</th>
                    <th>Time (ms)</th>
                    <th>Comparisons</th>
                    <th>Found?</th>
                    <th>Index</th>
                    <th>Complexity</th>
                </tr>
            `;
        } else {
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
        }
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
            
            if (isSearchResults) {
                // Search results row
                const foundIcon = result.foundTarget ? '✓' : '✗';
                const foundClass = result.foundTarget ? 'found-yes' : 'found-no';
                const indexDisplay = result.foundTarget ? result.targetIndex : 'N/A';
                
                row.innerHTML = `
                    <td>${result.algorithmName}</td>
                    <td>${result.datasetName}</td>
                    <td>${result.datasetSize}</td>
                    <td>${result.executionTimeMillis.toFixed(3)}</td>
                    <td>${result.comparisonCount}</td>
                    <td class="${foundClass}">${foundIcon}</td>
                    <td>${indexDisplay}</td>
                    <td>${result.complexity}</td>
                `;
            } else {
                // Sorting results row
                row.innerHTML = `
                    <td>${result.algorithmName}</td>
                    <td>${result.datasetName}</td>
                    <td>${result.datasetSize}</td>
                    <td>${result.executionTimeMillis.toFixed(3)}</td>
                    <td>${result.comparisonCount}</td>
                    <td>${result.swapCount}</td>
                    <td>${result.complexity}</td>
                `;
            }
            tbody.appendChild(row);
        });
        table.appendChild(tbody);
        
        return table;
    }
}

