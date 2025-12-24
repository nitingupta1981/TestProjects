/**
 * Dataset Manager module.
 * Handles dataset generation, upload, and management.
 */

export class DatasetManager {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async generateDataset(type, size, minValue = 1, maxValue = 10000, dataType = 'INTEGER') {
        const response = await fetch(`${this.apiBaseUrl}/datasets/generate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // Enable cookies for session management
            body: JSON.stringify({ type, size, minValue, maxValue, dataType })
        });
        
        if (!response.ok) {
            throw new Error('Failed to generate dataset');
        }
        
        return await response.json();
    }

    async uploadDataset(data, name, dataType = 'INTEGER') {
        const response = await fetch(`${this.apiBaseUrl}/datasets/upload`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // Enable cookies for session management
            body: JSON.stringify({ data, name, dataType })
        });
        
        if (!response.ok) {
            throw new Error('Failed to upload dataset');
        }
        
        return await response.json();
    }

    async getAllDatasets() {
        const response = await fetch(`${this.apiBaseUrl}/datasets`, {
            credentials: 'include' // Enable cookies for session management
        });
        
        if (!response.ok) {
            throw new Error('Failed to load datasets');
        }
        
        return await response.json();
    }

    async getDataset(datasetId) {
        const response = await fetch(`${this.apiBaseUrl}/datasets/${datasetId}`, {
            credentials: 'include' // Enable cookies for session management
        });
        
        if (!response.ok) {
            throw new Error('Failed to load dataset');
        }
        
        return await response.json();
    }

    async deleteDataset(datasetId) {
        const response = await fetch(`${this.apiBaseUrl}/datasets/${datasetId}`, {
            method: 'DELETE',
            credentials: 'include' // Enable cookies for session management
        });
        
        return response.ok;
    }

    async exportDataset(datasetId, format = 'json') {
        const response = await fetch(`${this.apiBaseUrl}/datasets/${datasetId}/export?format=${format}`, {
            credentials: 'include' // Enable cookies for session management
        });
        
        if (!response.ok) {
            throw new Error('Failed to export dataset');
        }
        
        return await response.json();
    }
}

