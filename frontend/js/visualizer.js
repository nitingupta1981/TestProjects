/**
 * Visualizer module.
 * Visualizes algorithm execution step-by-step with full backend integration.
 */

import { API_BASE_URL } from './app.js';

export class Visualizer {
    constructor() {
        this.canvas = null;
        this.ctx = null;
        this.steps = [];
        this.currentStep = 0;
        this.isPlaying = false;
        this.speed = 5;
        this.animationTimeout = null;
        
        this.sortingAlgorithms = [
            'Bubble Sort', 'Selection Sort', 'Insertion Sort', 'Quick Sort',
            'Merge Sort', 'Heap Sort', 'Shell Sort', 'Counting Sort'
        ];
        
        this.searchingAlgorithms = [
            'Linear Search', 'Binary Search', 'Depth First Search', 'Breadth First Search'
        ];
    }

    initialize() {
        this.canvas = document.getElementById('visualization-canvas');
        if (this.canvas) {
            this.ctx = this.canvas.getContext('2d');
        }
        this.setupControls();
        this.setupAlgorithmTypeChange();
        this.loadDatasetOptions();
    }

    setupControls() {
        const loadBtn = document.getElementById('load-visualization-btn');
        const playBtn = document.getElementById('play-btn');
        const pauseBtn = document.getElementById('pause-btn');
        const resetBtn = document.getElementById('reset-btn');
        const stepForwardBtn = document.getElementById('step-forward-btn');
        const stepBackwardBtn = document.getElementById('step-backward-btn');
        const restartBtn = document.getElementById('restart-vis-btn');
        const speedSlider = document.getElementById('speed-slider');
        
        if (loadBtn) loadBtn.addEventListener('click', () => this.loadVisualization());
        if (playBtn) playBtn.addEventListener('click', () => this.play());
        if (pauseBtn) pauseBtn.addEventListener('click', () => this.pause());
        if (resetBtn) resetBtn.addEventListener('click', () => this.reset());
        if (stepForwardBtn) stepForwardBtn.addEventListener('click', () => this.stepForward());
        if (stepBackwardBtn) stepBackwardBtn.addEventListener('click', () => this.stepBackward());
        if (restartBtn) restartBtn.addEventListener('click', () => this.restart());
        
        if (speedSlider) {
            speedSlider.addEventListener('input', (e) => {
                this.speed = parseInt(e.target.value);
                const speedValue = document.getElementById('speed-value');
                if (speedValue) {
                    speedValue.textContent = this.speed;
                }
            });
        }
    }

    setupAlgorithmTypeChange() {
        const typeSelect = document.getElementById('vis-algorithm-type');
        const algorithmSelect = document.getElementById('vis-algorithm-select');
        const searchTargetGroup = document.getElementById('vis-search-target-group');
        
        if (typeSelect && algorithmSelect) {
            typeSelect.addEventListener('change', () => {
                const type = typeSelect.value;
                algorithmSelect.innerHTML = '';
                
                const algorithms = type === 'SEARCH' ? this.searchingAlgorithms : this.sortingAlgorithms;
                algorithms.forEach(algo => {
                    const option = document.createElement('option');
                    option.value = algo;
                    option.textContent = algo;
                    algorithmSelect.appendChild(option);
                });
                
                if (searchTargetGroup) {
                    searchTargetGroup.style.display = type === 'SEARCH' ? 'block' : 'none';
                }
            });
            
            // Trigger initial population
            typeSelect.dispatchEvent(new Event('change'));
        }
    }

    async loadDatasetOptions() {
        try {
            const response = await fetch(`${API_BASE_URL}/datasets`);
            const datasets = await response.json();
            
            const select = document.getElementById('vis-dataset-select');
            if (select) {
                select.innerHTML = '';
                datasets.forEach(dataset => {
                    const option = document.createElement('option');
                    option.value = dataset.id;
                    option.textContent = `${dataset.name} (${dataset.size} elements)`;
                    select.appendChild(option);
                });
            }
        } catch (error) {
            console.error('Error loading datasets:', error);
        }
    }

    async loadVisualization() {
        const datasetId = document.getElementById('vis-dataset-select')?.value;
        const algorithmName = document.getElementById('vis-algorithm-select')?.value;
        const algorithmType = document.getElementById('vis-algorithm-type')?.value;
        const searchTarget = algorithmType === 'SEARCH' ? 
            parseInt(document.getElementById('vis-search-target')?.value || '50') : undefined;
        
        if (!datasetId || !algorithmName) {
            alert('Please select a dataset and algorithm');
            return;
        }
        
        try {
            // Show loading message
            const desc = document.getElementById('visualization-description');
            if (desc) desc.textContent = 'Loading visualization...';
            
            // Call backend visualization API
            const response = await fetch(`${API_BASE_URL}/algorithms/visualize`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    datasetId,
                    algorithmName,
                    searchTarget
                })
            });
            
            if (!response.ok) {
                throw new Error('Failed to load visualization');
            }
            
            this.steps = await response.json();
            this.currentStep = 0;
            
            // Show the player, hide setup
            document.getElementById('visualization-setup').style.display = 'none';
            document.getElementById('visualization-player').style.display = 'block';
            
            this.updateStepCounter();
            this.drawStep(this.steps[0]);
            
        } catch (error) {
            console.error('Error loading visualization:', error);
            alert('Error loading visualization: ' + error.message);
        }
    }

    drawStep(step) {
        if (!this.ctx || !this.canvas || !step) return;
        
        const ctx = this.ctx;
        const canvas = this.canvas;
        
        // Clear canvas
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        
        const array = step.arrayState || [];
        if (array.length === 0) return;
        
        const barWidth = Math.min(canvas.width / array.length, 80);
        const maxValue = Math.max(...array.map(v => typeof v === 'number' ? v : 0));
        const padding = 20;
        
        // Draw bars
        array.forEach((value, index) => {
            const numValue = typeof value === 'number' ? value : 0;
            const barHeight = maxValue > 0 ? (numValue / maxValue) * (canvas.height - 80) : 0;
            const x = padding + index * barWidth;
            const y = canvas.height - barHeight - 30;
            
            // Color based on highlighting and operation
            let color = '#4ecdc4'; // Default color
            
            if (step.highlightedIndices && step.highlightedIndices.includes(index)) {
                if (step.operation === 'SWAP') {
                    color = '#ffd700'; // Gold for swap
                } else if (step.operation === 'COMPARE') {
                    color = '#ff6b6b'; // Red for compare
                } else if (step.operation === 'FOUND') {
                    color = '#51cf66'; // Green for found
                } else {
                    color = '#ff6b6b'; // Default highlight
                }
            }
            
            // Draw bar
            ctx.fillStyle = color;
            ctx.fillRect(x, y, barWidth - 4, barHeight);
            
            // Draw border
            ctx.strokeStyle = '#333';
            ctx.lineWidth = 1;
            ctx.strokeRect(x, y, barWidth - 4, barHeight);
            
            // Draw value
            ctx.fillStyle = '#000';
            ctx.font = 'bold 14px Arial';
            ctx.textAlign = 'center';
            ctx.fillText(value, x + (barWidth - 4) / 2, canvas.height - 10);
            
            // Draw index
            ctx.fillStyle = '#666';
            ctx.font = '10px Arial';
            ctx.fillText(index, x + (barWidth - 4) / 2, canvas.height - 2);
        });
        
        // Update description
        const desc = document.getElementById('visualization-description');
        if (desc && step.description) {
            desc.innerHTML = `
                <strong>Step ${step.stepNumber}:</strong> ${step.description}<br>
                <em>Operation: ${step.operation || 'N/A'}</em>
            `;
        }
        
        this.updateStepCounter();
    }

    updateStepCounter() {
        const counter = document.getElementById('step-counter');
        if (counter) {
            counter.textContent = `Step: ${this.currentStep + 1} / ${this.steps.length}`;
        }
    }

    play() {
        this.isPlaying = true;
        this.animate();
    }

    pause() {
        this.isPlaying = false;
        if (this.animationTimeout) {
            clearTimeout(this.animationTimeout);
            this.animationTimeout = null;
        }
    }

    reset() {
        this.currentStep = 0;
        this.isPlaying = false;
        if (this.animationTimeout) {
            clearTimeout(this.animationTimeout);
            this.animationTimeout = null;
        }
        if (this.steps.length > 0) {
            this.drawStep(this.steps[0]);
        }
    }

    stepForward() {
        this.pause();
        if (this.currentStep < this.steps.length - 1) {
            this.currentStep++;
            this.drawStep(this.steps[this.currentStep]);
        }
    }

    stepBackward() {
        this.pause();
        if (this.currentStep > 0) {
            this.currentStep--;
            this.drawStep(this.steps[this.currentStep]);
        }
    }

    restart() {
        // Hide player, show setup
        document.getElementById('visualization-player').style.display = 'none';
        document.getElementById('visualization-setup').style.display = 'block';
        this.reset();
        this.loadDatasetOptions();
    }

    animate() {
        if (!this.isPlaying || this.currentStep >= this.steps.length) {
            this.isPlaying = false;
            return;
        }
        
        this.drawStep(this.steps[this.currentStep]);
        this.currentStep++;
        
        // If we reached the end, stop
        if (this.currentStep >= this.steps.length) {
            this.isPlaying = false;
            this.currentStep = this.steps.length - 1;
            return;
        }
        
        const delay = 1000 / this.speed;
        this.animationTimeout = setTimeout(() => this.animate(), delay);
    }

    async visualizeAlgorithm(datasetId, algorithmName) {
        // Legacy method for compatibility
        document.getElementById('vis-dataset-select').value = datasetId;
        document.getElementById('vis-algorithm-select').value = algorithmName;
        await this.loadVisualization();
    }
}
