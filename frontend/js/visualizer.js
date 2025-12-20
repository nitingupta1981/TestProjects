/**
 * Visualizer module.
 * Visualizes algorithm execution step-by-step.
 */

export class Visualizer {
    constructor() {
        this.canvas = document.getElementById('visualization-canvas');
        this.ctx = this.canvas.getContext('2d');
        this.steps = [];
        this.currentStep = 0;
        this.isPlaying = false;
        this.speed = 5;
        
        this.setupControls();
    }

    setupControls() {
        document.getElementById('play-btn').addEventListener('click', () => this.play());
        document.getElementById('pause-btn').addEventListener('click', () => this.pause());
        document.getElementById('reset-btn').addEventListener('click', () => this.reset());
        document.getElementById('speed-slider').addEventListener('input', (e) => {
            this.speed = parseInt(e.target.value);
        });
    }

    async visualizeAlgorithm(datasetId, algorithmName) {
        // For now, create simple visualization
        // In production, this would fetch steps from backend
        this.steps = this.createMockSteps();
        this.currentStep = 0;
        this.drawStep(this.steps[0]);
    }

    createMockSteps() {
        // Create mock visualization steps for demonstration
        const steps = [];
        let array = [5, 2, 8, 1, 9, 3];
        
        steps.push({
            stepNumber: 0,
            arrayState: [...array],
            operation: 'INIT',
            description: 'Initial array state',
            highlightedIndices: []
        });
        
        // Add some simple steps
        for (let i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                steps.push({
                    stepNumber: steps.length,
                    arrayState: [...array],
                    operation: 'COMPARE',
                    description: `Comparing ${array[i]} and ${array[i + 1]}`,
                    highlightedIndices: [i, i + 1]
                });
                
                [array[i], array[i + 1]] = [array[i + 1], array[i]];
                
                steps.push({
                    stepNumber: steps.length,
                    arrayState: [...array],
                    operation: 'SWAP',
                    description: `Swapped elements`,
                    highlightedIndices: [i, i + 1]
                });
            }
        }
        
        return steps;
    }

    drawStep(step) {
        const ctx = this.ctx;
        const canvas = this.canvas;
        
        // Clear canvas
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        
        const array = step.arrayState;
        const barWidth = canvas.width / array.length;
        const maxValue = Math.max(...array);
        
        // Draw bars
        array.forEach((value, index) => {
            const barHeight = (value / maxValue) * (canvas.height - 40);
            const x = index * barWidth;
            const y = canvas.height - barHeight;
            
            // Color based on highlighting
            if (step.highlightedIndices.includes(index)) {
                ctx.fillStyle = step.operation === 'SWAP' ? '#ffd700' : '#ff6b6b';
            } else {
                ctx.fillStyle = '#4ecdc4';
            }
            
            ctx.fillRect(x, y, barWidth - 2, barHeight);
            
            // Draw value
            ctx.fillStyle = '#000';
            ctx.font = '12px Arial';
            ctx.textAlign = 'center';
            ctx.fillText(value, x + barWidth / 2, canvas.height - 5);
        });
        
        // Update description
        document.getElementById('visualization-description').textContent = step.description;
    }

    play() {
        this.isPlaying = true;
        this.animate();
    }

    pause() {
        this.isPlaying = false;
    }

    reset() {
        this.currentStep = 0;
        this.isPlaying = false;
        if (this.steps.length > 0) {
            this.drawStep(this.steps[0]);
        }
    }

    animate() {
        if (!this.isPlaying || this.currentStep >= this.steps.length) {
            this.isPlaying = false;
            return;
        }
        
        this.drawStep(this.steps[this.currentStep]);
        this.currentStep++;
        
        const delay = 1000 / this.speed;
        setTimeout(() => this.animate(), delay);
    }
}

