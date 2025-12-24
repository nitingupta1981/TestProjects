package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;

/**
 * Abstract base class for searching algorithms that eliminates code duplication
 * between Integer and String array implementations.
 * 
 * This class provides:
 * - Generic searching infrastructure using Comparable<T>
 * - Unified StepCollector integration
 * - Consistent MetricsCollector usage
 * - Template methods for common operations
 * 
 * Subclasses only need to implement the core searching logic once.
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
public abstract class AbstractSearchingAlgorithm implements SearchingAlgorithm {

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
        return searchInternal(array, target, metrics, null);
    }

    @Override
    public int search(String[] array, String target, MetricsCollector metrics) {
        return searchStringInternal(array, target, metrics, null);
    }

    /**
     * Searches integer array with optional step collection for visualization.
     * Subclasses should override if they support visualization.
     */
    public int searchWithSteps(int[] array, int target, MetricsCollector metrics, StepCollector stepCollector) {
        return searchInternal(array, target, metrics, stepCollector);
    }

    /**
     * Searches string array with optional step collection for visualization.
     * Subclasses should override if they support visualization.
     */
    public int searchWithSteps(String[] array, String target, MetricsCollector metrics, StepCollector stepCollector) {
        return searchStringInternal(array, target, metrics, stepCollector);
    }

    /**
     * Internal method to search integer arrays.
     * Delegates to the generic implementation.
     */
    private int searchInternal(int[] array, int target, MetricsCollector metrics, StepCollector stepCollector) {
        Integer[] boxedArray = boxIntArray(array);
        return searchGeneric(boxedArray, target, metrics, stepCollector);
    }

    /**
     * Internal method to search string arrays.
     * Delegates to the generic implementation.
     */
    private int searchStringInternal(String[] array, String target, MetricsCollector metrics, StepCollector stepCollector) {
        return searchGeneric(array, target, metrics, stepCollector);
    }

    /**
     * Generic searching method that subclasses must implement.
     * This method contains the core algorithm logic and works with any Comparable type.
     * 
     * @param array The array to search (Integer[] or String[])
     * @param target The target value to find
     * @param metrics The metrics collector
     * @param stepCollector Optional step collector for visualization (can be null)
     * @return Index of target if found, -1 otherwise
     */
    protected abstract <T extends Comparable<T>> int searchGeneric(
        T[] array, 
        T target,
        MetricsCollector metrics, 
        StepCollector stepCollector
    );

    // ==================== Helper Methods ====================

    /**
     * Records initial state for visualization.
     */
    protected void recordInitial(Object array, StepCollector stepCollector, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordInitial((int[]) array, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordInitial(unboxToIntArray((Integer[]) array), message);
            } else if (array instanceof String[]) {
                stepCollector.recordInitial((String[]) array, message);
            }
        }
    }

    /**
     * Records checking an element for visualization.
     */
    protected void recordCheck(Object array, StepCollector stepCollector, int index, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordCheck((int[]) array, index, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordCheck(unboxToIntArray((Integer[]) array), index, message);
            } else if (array instanceof String[]) {
                stepCollector.recordCheck((String[]) array, index, message);
            }
        }
    }

    /**
     * Records range search for visualization (used in binary search).
     */
    protected void recordRange(Object array, StepCollector stepCollector, int left, int right, int mid, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordRange((int[]) array, left, right, mid, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordRange(unboxToIntArray((Integer[]) array), left, right, mid, message);
            } else if (array instanceof String[]) {
                stepCollector.recordRange((String[]) array, left, right, mid, message);
            }
        }
    }

    /**
     * Records found target for visualization.
     */
    protected void recordFound(Object array, StepCollector stepCollector, int index, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordFound((int[]) array, index, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordFound(unboxToIntArray((Integer[]) array), index, message);
            } else if (array instanceof String[]) {
                stepCollector.recordFound((String[]) array, index, message);
            }
        }
    }

    /**
     * Records target not found for visualization.
     */
    protected void recordNotFound(Object array, StepCollector stepCollector, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordNotFound((int[]) array, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordNotFound(unboxToIntArray((Integer[]) array), message);
            } else if (array instanceof String[]) {
                stepCollector.recordNotFound((String[]) array, message);
            }
        }
    }

    /**
     * Unified equality comparison.
     */
    protected <T extends Comparable<T>> boolean isEqual(T a, T b, MetricsCollector metrics) {
        if (a instanceof Integer) {
            return metrics.isEqual((Integer) a, (Integer) b);
        } else {
            metrics.recordComparison(1);
            return a.equals(b);
        }
    }

    /**
     * Unified less-than comparison.
     */
    protected <T extends Comparable<T>> boolean isLessThan(T a, T b, MetricsCollector metrics) {
        if (a instanceof Integer) {
            return metrics.isLessThan((Integer) a, (Integer) b);
        } else {
            metrics.recordComparison(1);
            return a.compareTo(b) < 0;
        }
    }

    /**
     * Unified less-than-or-equal comparison.
     */
    protected <T extends Comparable<T>> boolean isLessThanOrEqual(T a, T b, MetricsCollector metrics) {
        if (a instanceof Integer) {
            return metrics.isLessThanOrEqual((Integer) a, (Integer) b);
        } else {
            metrics.recordComparison(1);
            return a.compareTo(b) <= 0;
        }
    }

    // ==================== Boxing/Unboxing Utilities ====================

    /**
     * Converts int[] to Integer[] for generic processing.
     */
    private Integer[] boxIntArray(int[] array) {
        Integer[] boxed = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            boxed[i] = array[i];
        }
        return boxed;
    }

    /**
     * Converts Integer[] to int[] for step recording.
     */
    private int[] unboxToIntArray(Integer[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
}

