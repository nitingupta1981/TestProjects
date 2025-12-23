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
        const sortOrderGroup = document.getElementById('vis-sort-order-group');
        
        if (typeSelect && algorithmSelect) {
            typeSelect.addEventListener('change', () => {
                this.populateAlgorithmOptions();
                this.updateVisControls();
            });
            
            // Trigger initial population
            this.populateAlgorithmOptions();
            this.updateVisControls();
        }
    }

    updateVisControls() {
        const typeSelect = document.getElementById('vis-algorithm-type');
        const searchTargetGroup = document.getElementById('vis-search-target-group');
        const sortOrderGroup = document.getElementById('vis-sort-order-group');
        
        if (!typeSelect) return;
        
        const type = typeSelect.value;
        
        // Show/hide controls based on algorithm type
        if (searchTargetGroup) {
            searchTargetGroup.style.display = type === 'SEARCH' ? 'block' : 'none';
        }
        if (sortOrderGroup) {
            sortOrderGroup.style.display = type === 'SORT' ? 'block' : 'none';
        }
    }

    populateAlgorithmOptions() {
        const typeSelect = document.getElementById('vis-algorithm-type');
        const algorithmSelect = document.getElementById('vis-algorithm-select');
        
        if (!typeSelect || !algorithmSelect) return;
        
        const type = typeSelect.value;
        algorithmSelect.innerHTML = '';
        
        // Get all algorithms based on type (no filtering)
        const algorithms = type === 'SEARCH' ? this.searchingAlgorithms : this.sortingAlgorithms;
        
        // Populate dropdown with all algorithms
        algorithms.forEach((algo, index) => {
            const option = document.createElement('option');
            option.value = algo;
            option.textContent = algo;
            algorithmSelect.appendChild(option);
        });
        
        // Explicitly set the first option as selected
        if (algorithms.length > 0) {
            algorithmSelect.selectedIndex = 0;
        }
    }

    async loadDatasetOptions() {
        try {
            const response = await fetch(`${API_BASE_URL}/datasets`);
            const datasets = await response.json();
            
            const select = document.getElementById('vis-dataset-select');
            if (select) {
                select.innerHTML = '';
                
                if (datasets.length === 0) {
                    // No datasets available
                    const option = document.createElement('option');
                    option.value = '';
                    option.textContent = 'No datasets available - Generate one first';
                    option.disabled = true;
                    option.selected = true;
                    select.appendChild(option);
                } else {
                    // Add datasets to dropdown
                    datasets.forEach(dataset => {
                        const option = document.createElement('option');
                        option.value = dataset.id;
                        option.textContent = `${dataset.name} (${dataset.size} elements)`;
                        option.dataset.dataType = dataset.dataType; // Store data type
                        select.appendChild(option);
                    });
                    
                    // Add change listener to update search input type
                    select.addEventListener('change', () => {
                        this.updateVisSearchInputType();
                    });
                    
                    // Initial update
                    this.updateVisSearchInputType();
                }
            }
        } catch (error) {
            console.error('Error loading datasets:', error);
        }
    }

    updateVisSearchInputType() {
        const select = document.getElementById('vis-dataset-select');
        const searchInput = document.getElementById('vis-search-target');
        
        if (!select || !searchInput) return;
        
        const selectedOption = select.options[select.selectedIndex];
        if (!selectedOption) return;
        
        const dataType = selectedOption.dataset.dataType;
        
        if (dataType === 'STRING') {
            searchInput.type = 'text';
            searchInput.value = 'apple';
            searchInput.placeholder = 'Enter string to search';
        } else {
            searchInput.type = 'number';
            searchInput.value = '50';
            searchInput.placeholder = 'Enter number to search';
        }
    }

    async loadVisualization() {
        const datasetId = document.getElementById('vis-dataset-select')?.value;
        const algorithmName = document.getElementById('vis-algorithm-select')?.value;
        const algorithmType = document.getElementById('vis-algorithm-type')?.value;
        const sortOrder = document.getElementById('vis-sort-order')?.value || 'ASCENDING';
        
        // Get search target and handle both string and number types
        let searchTarget;
        if (algorithmType === 'SEARCH') {
            const searchInput = document.getElementById('vis-search-target');
            const inputValue = searchInput?.value;
            
            if (searchInput?.type === 'number') {
                searchTarget = parseInt(inputValue || '50');
            } else {
                searchTarget = inputValue || 'apple';
            }
        }
        
        if (!datasetId || !algorithmName) {
            alert('Please select a dataset and algorithm');
            return;
        }
        
        try {
            // Show loading message
            const desc = document.getElementById('visualization-description');
            if (desc) desc.textContent = 'Loading visualization...';
            
            // Build request body
            const requestBody = {
                datasetId,
                algorithmName,
                target: searchTarget !== undefined ? searchTarget.toString() : undefined
            };
            
            // Add sortOrder for sorting algorithms
            if (algorithmType === 'SORT') {
                requestBody.sortOrder = sortOrder;
            }
            
            // Call backend visualization API
            const response = await fetch(`${API_BASE_URL}/algorithms/visualize`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestBody)
            });
            
            if (!response.ok) {
                throw new Error('Failed to load visualization');
            }
            
            this.steps = await response.json();
            this.currentStep = 0;
            
            // Check if this is a limited visualization (only 2 steps = start and end)
            const supportedAlgorithms = [
                'Bubble Sort', 'Insertion Sort', 'Selection Sort', 
                'Linear Search', 'Binary Search'
            ];
            
            if (this.steps.length <= 2 && !supportedAlgorithms.includes(algorithmName)) {
                const continueVis = confirm(
                    `Note: Full step-by-step visualization is currently available for:\n` +
                    `  • Sorting: Bubble Sort, Insertion Sort, Selection Sort\n` +
                    `  • Searching: Linear Search, Binary Search\n\n` +
                    `${algorithmName} will show only the initial and final states.\n\n` +
                    `Continue with limited visualization?`
                );
                if (!continueVis) {
                    return;
                }
            }
            
            // Show the player, hide setup
            document.getElementById('visualization-setup').style.display = 'none';
            document.getElementById('visualization-player').style.display = 'block';
            
            this.updateStepCounter();
            this.drawStep(this.steps[0]);
            this.updateButtonStates(); // Initialize button states
            
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
        
        // Detect if we're visualizing strings or numbers
        const isStringArray = array.length > 0 && typeof array[0] === 'string';
        
        if (isStringArray) {
            this.drawStringVisualization(step, array);
        } else {
            this.drawNumberVisualization(step, array);
        }
        
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

    drawNumberVisualization(step, array) {
        const ctx = this.ctx;
        const canvas = this.canvas;
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
            let color = this.getHighlightColor(step, index);
            
            // Draw bar
            ctx.fillStyle = color;
            ctx.fillRect(x, y, barWidth - 4, barHeight);
            
            // Draw border
            ctx.strokeStyle = '#333';
            ctx.lineWidth = 1;
            ctx.strokeRect(x, y, barWidth - 4, barHeight);
            
            // Draw value on top of bar (only if bar is wide enough)
            if (barWidth > 30) {
                ctx.fillStyle = '#000';
                ctx.font = 'bold 14px Arial';
                ctx.textAlign = 'center';
                ctx.fillText(value, x + (barWidth - 4) / 2, canvas.height - barHeight - 35);
            }
        });
    }

    drawStringVisualization(step, array) {
        const ctx = this.ctx;
        const canvas = this.canvas;
        
        // Fixed box width for consistent appearance
        const boxWidth = 50;
        const padding = 10;
        
        // Calculate box height based on longest string (with character height)
        const charHeight = 16; // Height per character
        const maxStringLength = Math.max(...array.map(s => s.toString().length));
        const boxHeight = Math.max(maxStringLength * charHeight + 20, 80); // Minimum 80px
        
        // Position boxes to fill canvas width
        const totalWidth = array.length * (boxWidth + padding);
        const startX = (canvas.width - totalWidth) / 2 + padding;
        const startY = (canvas.height - boxHeight) / 2; // Center vertically
        
        // Draw string boxes
        array.forEach((value, index) => {
            const x = startX + index * (boxWidth + padding);
            const y = startY;
            
            // Color based on highlighting and operation
            let color = this.getHighlightColor(step, index);
            const isHighlighted = step.highlightedIndices && step.highlightedIndices.includes(index);
            
            // Only draw filled box if highlighted, otherwise just draw border
            if (isHighlighted) {
                ctx.fillStyle = color;
                ctx.fillRect(x, y, boxWidth, boxHeight);
            }
            
            // Draw border (always) - subtle for inactive, bold for active
            ctx.strokeStyle = isHighlighted ? '#333' : '#ddd';
            ctx.lineWidth = isHighlighted ? 2 : 0.5;
            ctx.strokeRect(x, y, boxWidth, boxHeight);
            
            // Draw string value vertically (bottom to top)
            ctx.save(); // Save current context state
            
            // Move to bottom center of the box
            ctx.translate(x + boxWidth / 2, y + boxHeight - 10);
            
            // Rotate 90 degrees counter-clockwise
            ctx.rotate(-Math.PI / 2);
            
            // Draw text
            ctx.fillStyle = '#000';
            ctx.font = '18px Arial';
            ctx.textAlign = 'left';
            ctx.textBaseline = 'middle';
            
            const displayValue = value.toString();
            ctx.fillText(displayValue, 0, 0);
            
            ctx.restore(); // Restore context state
            
            // Draw index below box
            ctx.fillStyle = '#666';
            ctx.font = '12px Arial';
            ctx.textAlign = 'center';
            ctx.fillText(index, x + boxWidth / 2, y + boxHeight + 15);
        });
    }

    getHighlightColor(step, index) {
        let color = '#4ecdc4'; // Default color (teal)
        
        if (step.highlightedIndices && step.highlightedIndices.includes(index)) {
            if (step.operation === 'SWAP') {
                color = '#ffd700'; // Gold for swap
            } else if (step.operation === 'COMPARE') {
                color = '#ff6b6b'; // Red for compare
            } else if (step.operation === 'FOUND') {
                color = '#51cf66'; // Green for found
            } else if (step.operation === 'RANGE') {
                // For binary search: yellow for boundaries, blue for mid
                const isLastHighlighted = index === step.highlightedIndices[step.highlightedIndices.length - 1];
                color = isLastHighlighted ? '#4dabf7' : '#ffd700';
            } else if (step.operation === 'CHECK') {
                color = '#4dabf7'; // Blue for checking
            } else if (step.operation === 'SET') {
                color = '#a78bfa'; // Purple for set/insert
            } else {
                //color = '#ff6b6b'; // Default highlight (red)
                color = '#51cf66'; // Default highlight (green)
            }
        }
        
        return color;
    }

    updateStepCounter() {
        const counter = document.getElementById('step-counter');
        if (counter) {
            counter.textContent = `Step: ${this.currentStep + 1} / ${this.steps.length}`;
        }
    }

    updateButtonStates() {
        const playBtn = document.getElementById('play-btn');
        const pauseBtn = document.getElementById('pause-btn');
        const stepForwardBtn = document.getElementById('step-forward-btn');
        const stepBackwardBtn = document.getElementById('step-backward-btn');
        const resetBtn = document.getElementById('reset-btn');
        
        if (this.isPlaying) {
            // During playback
            if (playBtn) playBtn.disabled = true;
            if (pauseBtn) pauseBtn.disabled = false;
            if (stepForwardBtn) stepForwardBtn.disabled = true;
            if (stepBackwardBtn) stepBackwardBtn.disabled = true;
            if (resetBtn) resetBtn.disabled = true;
        } else {
            // When paused or stopped
            if (playBtn) playBtn.disabled = false;
            if (pauseBtn) pauseBtn.disabled = true;
            if (stepForwardBtn) stepForwardBtn.disabled = false;
            if (stepBackwardBtn) stepBackwardBtn.disabled = false;
            if (resetBtn) resetBtn.disabled = false;
        }
    }

    play() {
        if (this.isPlaying) return; // Prevent multiple simultaneous plays
        
        // If we're at the end, restart from the beginning
        if (this.currentStep >= this.steps.length - 1) {
            this.currentStep = 0;
        }
        
        this.isPlaying = true;
        this.updateButtonStates();
        this.animate();
    }

    pause() {
        this.isPlaying = false;
        if (this.animationTimeout) {
            clearTimeout(this.animationTimeout);
            this.animationTimeout = null;
        }
        this.updateButtonStates();
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
        this.updateButtonStates();
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
            this.updateButtonStates();
            return;
        }
        
        this.drawStep(this.steps[this.currentStep]);
        this.currentStep++;
        
        // If we reached the end, stop
        if (this.currentStep >= this.steps.length) {
            this.isPlaying = false;
            this.currentStep = this.steps.length - 1;
            this.updateButtonStates();
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
