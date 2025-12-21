/**
 * Main application module that coordinates all components.
 * 
 * This module:
 * - Initializes all sub-modules
 * - Sets up event listeners
 * - Coordinates communication between modules
 * - Manages application state
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */

import { DatasetManager } from './datasetManager.js';
import { DatasetAnalyzer } from './datasetAnalyzer.js';
import { AlgorithmRunner } from './algorithmRunner.js';
import { RecommendationEngine } from './recommendationEngine.js';
import { Visualizer } from './visualizer.js';
import { Comparator } from './comparator.js';
import { Benchmarking } from './benchmarking.js';
import { Exporter } from './exporter.js';

// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Application State
const state = {
    datasets: [],
    selectedDatasets: [],
    currentResults: [],
    currentBenchmark: null,
    operationType: 'SORT'
};

// Initialize modules
const datasetManager = new DatasetManager(API_BASE_URL);
const datasetAnalyzer = new DatasetAnalyzer(API_BASE_URL);
const algorithmRunner = new AlgorithmRunner(API_BASE_URL);
const recommendationEngine = new RecommendationEngine(API_BASE_URL);
const visualizer = new Visualizer();
const comparator = new Comparator();
const benchmarking = new Benchmarking(API_BASE_URL);
const exporter = new Exporter(API_BASE_URL);

/**
 * Initializes the application.
 */
function initializeApp() {
    setupEventListeners();
    setupTabs();
    loadDatasets();
    visualizer.initialize();
}

/**
 * Sets up all event listeners.
 */
function setupEventListeners() {
    // Dataset generation
    document.getElementById('generate-dataset-btn').addEventListener('click', handleGenerateDataset);
    
    // Operation type change
    document.getElementById('operation-type').addEventListener('change', handleOperationTypeChange);
    
    // Analysis and recommendations
    document.getElementById('analyze-dataset-btn').addEventListener('click', handleAnalyzeDataset);
    
    // Run comparison
    document.getElementById('run-comparison-btn').addEventListener('click', handleRunComparison);
    
    // Run benchmark
    document.getElementById('run-benchmark-btn').addEventListener('click', handleRunBenchmark);
    
    // Export buttons
    document.getElementById('export-csv-btn').addEventListener('click', () => handleExport('csv'));
    document.getElementById('export-json-btn').addEventListener('click', () => handleExport('json'));
    
    // Visualization
    document.getElementById('visualize-btn').addEventListener('click', handleVisualize);
}

/**
 * Sets up tab navigation.
 */
function setupTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    
    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetTab = btn.dataset.tab;
            
            // Update active button
            tabBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            // Show/hide content based on tab
            const allContent = document.querySelectorAll('[data-tab-content]');
            allContent.forEach(content => {
                if (content.dataset.tabContent === targetTab) {
                    content.classList.add('active');
                    // Don't hide sections that need conditional display
                    if (!content.id || !['analysis-section', 'recommendation-section', 'results-section', 'benchmark-section'].includes(content.id)) {
                        content.style.display = 'block';
                    }
                } else {
                    content.classList.remove('active');
                    // Only hide if not in main tab or is visualization section
                    if (targetTab !== 'main' || content.dataset.tabContent === 'visualization') {
                        content.style.display = 'none';
                    }
                }
            });
            
            // Refresh dataset options when switching to visualization tab
            if (targetTab === 'visualization') {
                visualizer.loadDatasetOptions();
                visualizer.populateAlgorithmOptions();
            }
        });
    });
    
    // Activate first tab (main)
    const firstTab = document.querySelector('.tab-btn[data-tab="main"]');
    if (firstTab) {
        firstTab.click();
    }
}

/**
 * Handles dataset generation.
 */
async function handleGenerateDataset() {
    const type = document.getElementById('dataset-type').value;
    const size = parseInt(document.getElementById('dataset-size').value);
    const dataType = document.getElementById('data-type').value;
    
    try {
        const dataset = await datasetManager.generateDataset(type, size, 1, 10000, dataType);
        state.datasets.push(dataset);
        
        // Clear previous selections and select only the newly generated dataset
        state.selectedDatasets = [dataset.id];
        
        renderDatasets();
        
        // Update visualization dropdown with new dataset
        visualizer.loadDatasetOptions();
        
        showMessage('Dataset generated and selected successfully!', 'success');
    } catch (error) {
        showMessage('Error generating dataset: ' + error.message, 'error');
    }
}

/**
 * Renders the datasets list.
 */
function renderDatasets() {
    const container = document.getElementById('datasets-items');
    container.innerHTML = '';
    
    state.datasets.forEach(dataset => {
        const item = document.createElement('div');
        item.className = 'dataset-item';
        if (state.selectedDatasets.includes(dataset.id)) {
            item.classList.add('selected');
        }
        
        const isString = dataset.dataType === 'STRING';
        const analysisNote = isString ? 
            '<br><small style="color: #ff9800;">⚠️ Analysis not available for STRING datasets</small>' : '';
        
        item.innerHTML = `
            <div class="dataset-info">
                <strong>${dataset.name}</strong><br>
                Type: ${dataset.type} | Data: ${dataset.dataType || 'INTEGER'}<br>
                Size: ${dataset.size}${analysisNote}<br>
                <small>ID: ${dataset.id.substring(0, 8)}...</small>
            </div>
            <div class="dataset-actions">
                <button class="btn-small btn-info" onclick="viewDataset('${dataset.id}')">View</button>
                <button class="btn-small btn-warning" onclick="exportDataset('${dataset.id}')">Export</button>
                <button class="btn-small btn-danger" onclick="deleteDataset('${dataset.id}')">Delete</button>
            </div>
        `;
        
        item.addEventListener('click', (e) => {
            // Don't toggle selection if clicking on buttons
            if (!e.target.classList.contains('btn-small')) {
                toggleDatasetSelection(dataset.id);
            }
        });
        container.appendChild(item);
    });
}

/**
 * Toggles dataset selection.
 */
function toggleDatasetSelection(datasetId) {
    const index = state.selectedDatasets.indexOf(datasetId);
    if (index > -1) {
        state.selectedDatasets.splice(index, 1);
    } else {
        state.selectedDatasets.push(datasetId);
    }
    renderDatasets();
}

/**
 * Handles operation type change (SORT/SEARCH).
 */
function handleOperationTypeChange() {
    state.operationType = document.getElementById('operation-type').value;
    
    const sortingAlgos = document.getElementById('sorting-algorithms');
    const searchingAlgos = document.getElementById('searching-algorithms');
    const searchTargetGroup = document.getElementById('search-target-group');
    
    if (state.operationType === 'SEARCH') {
        sortingAlgos.style.display = 'none';
        searchingAlgos.style.display = 'block';
        searchTargetGroup.style.display = 'block';
    } else {
        sortingAlgos.style.display = 'block';
        searchingAlgos.style.display = 'none';
        searchTargetGroup.style.display = 'none';
    }
}

/**
 * Handles dataset analysis and recommendations.
 */
async function handleAnalyzeDataset() {
    if (state.selectedDatasets.length === 0) {
        showMessage('Please select at least one dataset', 'error');
        return;
    }
    
    try {
        const datasetId = state.selectedDatasets[0];
        
        // Check if selected dataset is STRING type
        const dataset = state.datasets.find(d => d.id === datasetId);
        if (dataset && dataset.dataType === 'STRING') {
            showMessage('Analysis is currently only supported for INTEGER datasets. ' +
                       'Please select an integer dataset or generate a new one.', 'error');
            return;
        }
        
        const analysis = await datasetAnalyzer.analyzeDataset(datasetId, state.operationType);
        
        // Display characteristics
        displayCharacteristics(analysis.characteristics);
        
        // Display recommendation
        displayRecommendation(analysis.recommendation);
        
        showMessage('Analysis complete!', 'success');
    } catch (error) {
        // Handle backend error message
        const errorMsg = error.message || 'Unknown error occurred';
        showMessage('Error analyzing dataset: ' + errorMsg, 'error');
    }
}

/**
 * Displays dataset characteristics.
 */
function displayCharacteristics(characteristics) {
    const section = document.getElementById('analysis-section');
    const container = document.getElementById('dataset-characteristics');
    
    container.innerHTML = `
        <div class="char-grid">
            <div class="char-item">
                <strong>Size:</strong> ${characteristics.size}
            </div>
            <div class="char-item">
                <strong>Sorted:</strong> ${characteristics.sorted ? 'Yes' : 'No'}
            </div>
            <div class="char-item">
                <strong>Sortedness:</strong> ${characteristics.sortedness.toFixed(2)}%
            </div>
            <div class="char-item">
                <strong>Duplicates:</strong> ${characteristics.hasDuplicates ? 'Yes' : 'No'}
            </div>
            <div class="char-item">
                <strong>Duplicate %:</strong> ${characteristics.duplicatePercentage.toFixed(2)}%
            </div>
            <div class="char-item">
                <strong>Unique Count:</strong> ${characteristics.uniqueCount}
            </div>
            <div class="char-item">
                <strong>Range:</strong> [${characteristics.minValue}, ${characteristics.maxValue}]
            </div>
            <div class="char-item">
                <strong>Range Size:</strong> ${characteristics.rangeSize}
            </div>
            <div class="char-item">
                <strong>Distribution:</strong> ${characteristics.distribution}
            </div>
            <div class="char-item">
                <strong>Size Category:</strong> ${characteristics.sizeCategory}
            </div>
        </div>
    `;
    
    section.style.display = 'block';
}

/**
 * Displays algorithm recommendation.
 */
function displayRecommendation(recommendation) {
    const section = document.getElementById('recommendation-section');
    const container = document.getElementById('recommendation-details');
    
    let alternativesHtml = '';
    if (recommendation.alternatives && recommendation.alternatives.length > 0) {
        alternativesHtml = `
            <div class="alternatives">
                <strong>Alternatives:</strong>
                <ul>
                    ${recommendation.alternatives.map(alt => `<li>${alt}</li>`).join('')}
                </ul>
            </div>
        `;
    }
    
    let warningsHtml = '';
    if (recommendation.warnings && recommendation.warnings.length > 0) {
        warningsHtml = `
            <div class="warnings">
                <strong>Warnings:</strong>
                <ul>
                    ${recommendation.warnings.map(warn => `<li>${warn}</li>`).join('')}
                </ul>
            </div>
        `;
    }
    
    container.innerHTML = `
        <h3>Recommended: ${recommendation.algorithmName}</h3>
        <p><strong>Reason:</strong> ${recommendation.reason}</p>
        <p class="complexity"><strong>Expected Complexity:</strong> ${recommendation.expectedComplexity}</p>
        <p><strong>Confidence:</strong> ${recommendation.confidence}</p>
        ${alternativesHtml}
        ${warningsHtml}
    `;
    
    section.style.display = 'block';
}

/**
 * Handles running comparison.
 */
async function handleRunComparison() {
    if (state.selectedDatasets.length === 0) {
        showMessage('Please select at least one dataset', 'error');
        return;
    }
    
    const selectedAlgorithms = getSelectedAlgorithms();
    if (selectedAlgorithms.length === 0) {
        showMessage('Please select at least one algorithm', 'error');
        return;
    }
    
    try {
        let results;
        if (state.operationType === 'SEARCH') {
            const target = parseInt(document.getElementById('search-target').value);
            results = await algorithmRunner.runSearchComparison(
                state.selectedDatasets,
                selectedAlgorithms,
                target
            );
        } else {
            results = await algorithmRunner.runSortComparison(
                state.selectedDatasets,
                selectedAlgorithms
            );
        }
        
        state.currentResults = results;
        
        displayResults(results);
        showMessage('Comparison complete!', 'success');
    } catch (error) {
        showMessage('Error running comparison: ' + error.message, 'error');
    }
}

/**
 * Gets selected algorithms from checkboxes.
 */
function getSelectedAlgorithms() {
    const container = state.operationType === 'SEARCH' ? 
        'searching-algorithms' : 'sorting-algorithms';
    
    const checkboxes = document.querySelectorAll(`#${container} input[type="checkbox"]:checked`);
    return Array.from(checkboxes).map(cb => cb.value);
}

/**
 * Displays comparison results.
 */
function displayResults(results) {
    const section = document.getElementById('results-section');
    const container = document.getElementById('results-table-container');
    
    const table = comparator.createResultsTable(results);
    container.innerHTML = '';
    container.appendChild(table);
    
    section.style.display = 'block';
}

/**
 * Handles running benchmark.
 */
async function handleRunBenchmark() {
    const selectedAlgorithms = getSelectedAlgorithms();
    if (selectedAlgorithms.length === 0) {
        showMessage('Please select at least one algorithm', 'error');
        return;
    }
    
    try {
        const report = await benchmarking.runBenchmark(
            state.operationType,
            selectedAlgorithms,
            [100, 1000, 5000]
        );
        
        state.currentBenchmark = report;
        displayBenchmarkResults(report);
        showMessage('Benchmark complete!', 'success');
    } catch (error) {
        showMessage('Error running benchmark: ' + error.message, 'error');
    }
}

/**
 * Displays benchmark results.
 */
function displayBenchmarkResults(report) {
    const section = document.getElementById('benchmark-section');
    const container = document.getElementById('benchmark-results-container');
    
    container.innerHTML = benchmarking.formatBenchmarkReport(report);
    section.style.display = 'block';
}

/**
 * Handles export.
 */
function handleExport(format) {
    if (state.currentResults.length === 0) {
        showMessage('No results to export', 'error');
        return;
    }
    
    exporter.exportResults(state.currentResults, format);
    showMessage(`Results exported as ${format.toUpperCase()}`, 'success');
}

/**
 * Handles visualization.
 */
async function handleVisualize() {
    // Switch to visualization tab
    const visTab = document.querySelector('.tab-btn[data-tab="visualization"]');
    if (visTab) {
        visTab.click();
    }
    
    // If datasets and algorithms are selected, pre-populate
    if (state.selectedDatasets.length > 0) {
        const datasetSelect = document.getElementById('vis-dataset-select');
        if (datasetSelect) {
            datasetSelect.value = state.selectedDatasets[0];
        }
    }
    
    const selectedAlgorithms = getSelectedAlgorithms();
    if (selectedAlgorithms.length > 0) {
        const algorithmSelect = document.getElementById('vis-algorithm-select');
        if (algorithmSelect) {
            algorithmSelect.value = selectedAlgorithms[0];
        }
    }
}

/**
 * Loads existing datasets.
 */
async function loadDatasets() {
    try {
        state.datasets = await datasetManager.getAllDatasets();
        renderDatasets();
        
        // Update visualization dropdown
        visualizer.loadDatasetOptions();
    } catch (error) {
        console.error('Error loading datasets:', error);
    }
}

/**
 * Shows a message to the user.
 */
function showMessage(message, type) {
    // Simple alert for now - could be enhanced with a toast notification
    if (type === 'error') {
        alert('Error: ' + message);
    } else {
        alert(message);
    }
}

/**
 * Views a dataset's contents.
 */
async function viewDataset(datasetId) {
    try {
        const data = await datasetManager.exportDataset(datasetId);
        const content = JSON.stringify(data, null, 2);
        
        // Create modal to display dataset
        const modal = document.createElement('div');
        modal.style.cssText = 'position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);background:white;padding:20px;border:2px solid #333;z-index:1000;max-height:80vh;overflow:auto;box-shadow:0 4px 6px rgba(0,0,0,0.3);';
        modal.innerHTML = `
            <h3>Dataset: ${data.name}</h3>
            <p>Type: ${data.type} | Data Type: ${data.dataType} | Size: ${data.size}</p>
            <pre style="background:#f5f5f5;padding:10px;border-radius:4px;max-height:400px;overflow:auto;">${JSON.stringify(data.data, null, 2)}</pre>
            <button onclick="this.parentElement.remove();document.getElementById('modal-overlay').remove();" class="btn btn-secondary">Close</button>
        `;
        
        const overlay = document.createElement('div');
        overlay.id = 'modal-overlay';
        overlay.style.cssText = 'position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);z-index:999;';
        overlay.onclick = () => { modal.remove(); overlay.remove(); };
        
        document.body.appendChild(overlay);
        document.body.appendChild(modal);
    } catch (error) {
        showMessage('Error viewing dataset: ' + error.message, 'error');
    }
}

/**
 * Exports a dataset.
 */
async function exportDataset(datasetId) {
    try {
        const data = await datasetManager.exportDataset(datasetId);
        const jsonStr = JSON.stringify(data, null, 2);
        const blob = new Blob([jsonStr], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${data.name}_${data.id.substring(0, 8)}.json`;
        a.click();
        URL.revokeObjectURL(url);
        showMessage('Dataset exported successfully!', 'success');
    } catch (error) {
        showMessage('Error exporting dataset: ' + error.message, 'error');
    }
}

/**
 * Deletes a dataset.
 */
async function deleteDataset(datasetId) {
    if (!confirm('Are you sure you want to delete this dataset?')) {
        return;
    }
    
    try {
        const success = await datasetManager.deleteDataset(datasetId);
        if (success) {
            state.datasets = state.datasets.filter(d => d.id !== datasetId);
            state.selectedDatasets = state.selectedDatasets.filter(id => id !== datasetId);
            renderDatasets();
            
            // Update visualization dropdown
            visualizer.loadDatasetOptions();
            
            showMessage('Dataset deleted successfully!', 'success');
        } else {
            showMessage('Failed to delete dataset', 'error');
        }
    } catch (error) {
        showMessage('Error deleting dataset: ' + error.message, 'error');
    }
}

// Make functions available globally for onclick handlers
window.viewDataset = viewDataset;
window.exportDataset = exportDataset;
window.deleteDataset = deleteDataset;

// Initialize the application when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeApp);
} else {
    initializeApp();
}

export { state, API_BASE_URL };

