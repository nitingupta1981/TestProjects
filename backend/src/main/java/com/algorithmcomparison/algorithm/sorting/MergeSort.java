package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;

/**
 * Merge Sort implementation.
 * 
 * Merge Sort is a divide-and-conquer algorithm that divides the array into
 * two halves, recursively sorts them, and then merges the sorted halves.
 * 
 * Time Complexity: O(n log n) in all cases
 * Space Complexity: O(n) - requires auxiliary array for merging
 * Stable: Yes - maintains relative order of equal elements
 * 
 * Best Performance: Guaranteed O(n log n) performance, works well on linked lists
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class MergeSort implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        if (array.length > 1) {
            mergeSort(array, 0, array.length - 1, metrics);
        }
    }

    @Override
    public void sort(String[] array, MetricsCollector metrics) {
        if (array.length > 1) {
            mergeSortString(array, 0, array.length - 1, metrics);
        }
    }

    /**
     * Recursive merge sort method.
     * 
     * @param array The array to sort
     * @param left Starting index
     * @param right Ending index
     * @param metrics Metrics collector
     */
    private void mergeSort(int[] array, int left, int right, MetricsCollector metrics) {
        if (left < right) {
            // Find the middle point to divide the array into two halves
            int mid = left + (right - left) / 2;
            
            // Recursively sort first half
            mergeSort(array, left, mid, metrics);
            
            // Recursively sort second half
            mergeSort(array, mid + 1, right, metrics);
            
            // Merge the sorted halves
            merge(array, left, mid, right, metrics);
        }
    }

    /**
     * Merges two sorted subarrays into a single sorted array.
     * 
     * This is the core operation of Merge Sort:
     * 1. Create temporary arrays for left and right halves
     * 2. Compare elements from both halves and place smaller one in main array
     * 3. Copy any remaining elements
     * 
     * @param array The array containing the subarrays
     * @param left Starting index of left subarray
     * @param mid Ending index of left subarray (mid+1 is start of right)
     * @param right Ending index of right subarray
     * @param metrics Metrics collector
     */
    private void merge(int[] array, int left, int mid, int right, MetricsCollector metrics) {
        // Calculate sizes of two subarrays to be merged
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        // Create temporary arrays
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];
        
        // Copy data to temporary arrays
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
            metrics.recordArrayAccess(1);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
            metrics.recordArrayAccess(1);
        }
        
        // Merge the temporary arrays back into array[left...right]
        int i = 0; // Initial index of first subarray
        int j = 0; // Initial index of second subarray
        int k = left; // Initial index of merged subarray
        
        // Compare elements from both subarrays and place smaller one in main array
        while (i < n1 && j < n2) {
            // Compare elements from left and right subarrays
            if (metrics.isLessThanOrEqual(leftArray[i], rightArray[j])) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            metrics.recordArrayAccess(1); // Writing to array
            metrics.recordSwap(1); // Consider this a move operation
            k++;
        }
        
        // Copy remaining elements of leftArray, if any
        while (i < n1) {
            array[k] = leftArray[i];
            metrics.recordArrayAccess(1);
            i++;
            k++;
        }
        
        // Copy remaining elements of rightArray, if any
        while (j < n2) {
            array[k] = rightArray[j];
            metrics.recordArrayAccess(1);
            j++;
            k++;
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    /**
     * Recursive merge sort for String arrays.
     */
    private void mergeSortString(String[] array, int left, int right, MetricsCollector metrics) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortString(array, left, mid, metrics);
            mergeSortString(array, mid + 1, right, metrics);
            mergeString(array, left, mid, right, metrics);
        }
    }

    /**
     * Merges two sorted String subarrays.
     */
    private void mergeString(String[] array, int left, int mid, int right, MetricsCollector metrics) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        String[] leftArray = new String[n1];
        String[] rightArray = new String[n2];
        
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
            metrics.recordArrayAccess(1);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
            metrics.recordArrayAccess(1);
        }
        
        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            metrics.recordComparison(1);
            if (leftArray[i].compareTo(rightArray[j]) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            metrics.recordArrayAccess(1);
            k++;
        }
        
        while (i < n1) {
            array[k] = leftArray[i];
            metrics.recordArrayAccess(1);
            i++;
            k++;
        }
        
        while (j < n2) {
            array[k] = rightArray[j];
            metrics.recordArrayAccess(1);
            j++;
            k++;
        }
    }
}

