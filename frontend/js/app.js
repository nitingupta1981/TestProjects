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
    loadDatasets();
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
 * Handles dataset generation.
 */
async function handleGenerateDataset() {
    const type = document.getElementById('dataset-type').value;
    const size = parseInt(document.getElementById('dataset-size').value);
    
    try {
        const dataset = await datasetManager.generateDataset(type, size);
        state.datasets.push(dataset);
        renderDatasets();
        showMessage('Dataset generated successfully!', 'success');
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
        
        item.innerHTML = `
            <strong>${dataset.name}</strong><br>
            Type: ${dataset.type}<br>
            Size: ${dataset.size}<br>
            <small>ID: ${dataset.id.substring(0, 8)}...</small>
        `;
        
        item.addEventListener('click', () => toggleDatasetSelection(dataset.id));
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
        const analysis = await datasetAnalyzer.analyzeDataset(datasetId, state.operationType);
        
        // Display characteristics
        displayCharacteristics(analysis.characteristics);
        
        // Display recommendation
        displayRecommendation(analysis.recommendation);
        
        showMessage('Analysis complete!', 'success');
    } catch (error) {
        showMessage('Error analyzing dataset: ' + error.message, 'error');
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
    if (state.selectedDatasets.length === 0) {
        showMessage('Please select a dataset', 'error');
        return;
    }
    
    const selectedAlgorithms = getSelectedAlgorithms();
    if (selectedAlgorithms.length === 0) {
        showMessage('Please select an algorithm to visualize', 'error');
        return;
    }
    
    try {
        const section = document.getElementById('visualization-section');
        section.style.display = 'block';
        
        await visualizer.visualizeAlgorithm(
            state.selectedDatasets[0],
            selectedAlgorithms[0]
        );
    } catch (error) {
        showMessage('Error visualizing algorithm: ' + error.message, 'error');
    }
}

/**
 * Loads existing datasets.
 */
async function loadDatasets() {
    try {
        state.datasets = await datasetManager.getAllDatasets();
        renderDatasets();
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

// Initialize the application when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeApp);
} else {
    initializeApp();
}

export { state, API_BASE_URL };

