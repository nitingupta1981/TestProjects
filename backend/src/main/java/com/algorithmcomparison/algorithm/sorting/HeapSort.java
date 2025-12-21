package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

/**
 * Heap Sort implementation.
 * 
 * Heap Sort uses a binary heap data structure to sort elements. It first builds
 * a max heap from the array, then repeatedly extracts the maximum element and
 * rebuilds the heap until the array is sorted.
 * 
 * Time Complexity: O(n log n) in all cases
 * Space Complexity: O(1) - sorts in place
 * Stable: No - may change relative order of equal elements
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class HeapSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        int n = array.length;
        
        // Build max heap (rearrange array)
        // Start from last non-leaf node and heapify each node
        // Last non-leaf node is at index n/2 - 1
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i, metrics);
        }
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root (maximum element) to end
            metrics.swap(array, 0, i);
            
            // Heapify the reduced heap (excluding sorted elements at end)
            heapify(array, i, 0, metrics);
        }
    }

    @Override
    public void sort(String[] array, MetricsCollector metrics) {
        // Heap Sort for strings is not currently implemented
        throw new UnsupportedOperationException(
            "Heap Sort is not yet implemented for String datasets. " +
            "Please use Bubble Sort, Selection Sort, Insertion Sort, Merge Sort, or Quick Sort for string data.");
    }

    /**
     * Heapifies a subtree rooted at node i.
     * 
     * Heapify ensures the max heap property: parent node is greater than or equal
     * to its children. This is the core operation of Heap Sort.
     * 
     * Process:
     * 1. Assume current node is largest
     * 2. Compare with left child, update largest if needed
     * 3. Compare with right child, update largest if needed
     * 4. If largest is not current node, swap and recursively heapify affected subtree
     * 
     * @param array The array to heapify
     * @param heapSize Size of heap to consider
     * @param rootIndex Index of root of subtree to heapify
     * @param metrics Metrics collector
     */
    private void heapify(int[] array, int heapSize, int rootIndex, MetricsCollector metrics) {
        int largest = rootIndex;  // Initialize largest as root
        int leftChild = 2 * rootIndex + 1;   // Left child index
        int rightChild = 2 * rootIndex + 2;  // Right child index
        
        // If left child exists and is greater than root
        if (leftChild < heapSize && metrics.isGreaterThan(array[leftChild], array[largest])) {
            largest = leftChild;
        }
        
        // If right child exists and is greater than current largest
        if (rightChild < heapSize && metrics.isGreaterThan(array[rightChild], array[largest])) {
            largest = rightChild;
        }
        
        // If largest is not root, swap and continue heapifying
        if (largest != rootIndex) {
            metrics.swap(array, rootIndex, largest);
            
            // Recursively heapify the affected sub-tree
            // After swap, the subtree rooted at 'largest' may violate heap property
            heapify(array, heapSize, largest, metrics);
        }
    }

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public boolean isStable() {
        return false;
    }
}

