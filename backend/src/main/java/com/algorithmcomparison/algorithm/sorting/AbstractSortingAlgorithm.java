package com.algorithmcomparison.algorithm.sorting;

import com.algorithmcomparison.util.MetricsCollector;
import com.algorithmcomparison.util.StepCollector;
import java.util.function.BiPredicate;

/**
 * Abstract base class for sorting algorithms that eliminates code duplication
 * between Integer and String array implementations.
 * 
 * This class provides:
 * - Generic sorting infrastructure using Comparable<T>
 * - Unified StepCollector integration
 * - Consistent MetricsCollector usage
 * - Template methods for common operations
 * 
 * Subclasses only need to implement the core sorting logic once.
 * 
 * @author Algorithm Comparison Team
 * @version 2.0
 */
public abstract class AbstractSortingAlgorithm implements SortingAlgorithm {

    @Override
    public void sort(int[] array, MetricsCollector metrics) {
        sortInternal(array, metrics, null);
    }

    @Override
    public void sort(String[] array, MetricsCollector metrics) {
        sortStringInternal(array, metrics, null);
    }

    /**
     * Sorts integer array with optional step collection for visualization.
     * Subclasses should override if they support visualization.
     */
    public void sortWithSteps(int[] array, MetricsCollector metrics, StepCollector stepCollector) {
        sortInternal(array, metrics, stepCollector);
    }

    /**
     * Sorts string array with optional step collection for visualization.
     * Subclasses should override if they support visualization.
     */
    public void sortWithSteps(String[] array, MetricsCollector metrics, StepCollector stepCollector) {
        sortStringInternal(array, metrics, stepCollector);
    }

    /**
     * Internal method to sort integer arrays.
     * Delegates to the generic implementation.
     */
    private void sortInternal(int[] array, MetricsCollector metrics, StepCollector stepCollector) {
        Integer[] boxedArray = boxIntArray(array);
        sortGeneric(boxedArray, metrics, stepCollector, (a, b) -> a.compareTo(b) > 0);
        unboxIntArray(boxedArray, array);
    }

    /**
     * Internal method to sort string arrays.
     * Delegates to the generic implementation.
     */
    private void sortStringInternal(String[] array, MetricsCollector metrics, StepCollector stepCollector) {
        sortGeneric(array, metrics, stepCollector, (a, b) -> a.compareTo(b) > 0);
    }

    /**
     * Generic sorting method that subclasses must implement.
     * This method contains the core algorithm logic and works with any Comparable type.
     * 
     * @param array The array to sort (Integer[] or String[])
     * @param metrics The metrics collector
     * @param stepCollector Optional step collector for visualization (can be null)
     * @param isGreater Comparison function to determine if first element is greater than second
     */
    protected abstract <T extends Comparable<T>> void sortGeneric(
        T[] array, 
        MetricsCollector metrics, 
        StepCollector stepCollector,
        BiPredicate<T, T> isGreater
    );

    // ==================== Helper Methods ====================

    /**
     * Records initial state for visualization.
     */
    protected void recordInitialState(Object array, StepCollector stepCollector, String message) {
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
     * Records comparison for visualization.
     */
    protected void recordComparison(Object array, StepCollector stepCollector, int i, int j, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordCompare((int[]) array, i, j, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordCompare(unboxToIntArray((Integer[]) array), i, j, message);
            } else if (array instanceof String[]) {
                stepCollector.recordCompare((String[]) array, i, j, message);
            }
        }
    }

    /**
     * Records swap for visualization.
     */
    protected void recordSwap(Object array, StepCollector stepCollector, int i, int j, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordSwap((int[]) array, i, j, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordSwap(unboxToIntArray((Integer[]) array), i, j, message);
            } else if (array instanceof String[]) {
                stepCollector.recordSwap((String[]) array, i, j, message);
            }
        }
    }

    /**
     * Records completion for visualization.
     */
    protected void recordComplete(Object array, StepCollector stepCollector, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordComplete((int[]) array, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordComplete(unboxToIntArray((Integer[]) array), message);
            } else if (array instanceof String[]) {
                stepCollector.recordComplete((String[]) array, message);
            }
        }
    }

    /**
     * Records a custom step with highlighted indices for visualization.
     * Useful for divide-and-conquer algorithms like Merge Sort.
     */
    protected void recordCustomStep(Object array, StepCollector stepCollector, int[] highlightIndices, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordStep((int[]) array, highlightIndices, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordStep(unboxToIntArray((Integer[]) array), highlightIndices, message);
            } else if (array instanceof String[]) {
                stepCollector.recordStep((String[]) array, highlightIndices, message);
            }
        }
    }

    /**
     * Records a step with active region highlighting for divide-and-conquer algorithms.
     * Highlights the active region and dims inactive regions.
     */
    protected void recordRegionStep(Object array, StepCollector stepCollector, int activeLeft, int activeRight, int[] highlightIndices, String message) {
        if (stepCollector != null) {
            if (array instanceof int[]) {
                stepCollector.recordRegionStep((int[]) array, activeLeft, activeRight, highlightIndices, message);
            } else if (array instanceof Integer[]) {
                stepCollector.recordRegionStep(unboxToIntArray((Integer[]) array), activeLeft, activeRight, highlightIndices, message);
            } else if (array instanceof String[]) {
                stepCollector.recordRegionStep((String[]) array, activeLeft, activeRight, highlightIndices, message);
            }
        }
    }

    /**
     * Unified comparison method that uses MetricsCollector.
     */
    protected <T extends Comparable<T>> boolean isGreaterThan(T a, T b, MetricsCollector metrics, BiPredicate<T, T> isGreater) {
        if (a instanceof Integer) {
            return metrics.isGreaterThan((Integer) a, (Integer) b);
        } else {
            metrics.recordComparison(1);
            return isGreater.test(a, b);
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
     * Unified swap method.
     */
    protected <T> void swap(T[] array, int i, int j, MetricsCollector metrics) {
        if (array instanceof String[]) {
            metrics.swap((String[]) array, i, j);
        } else {
            // For Integer[] or other types, do manual swap and record metrics
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            metrics.recordSwap(1);
            metrics.recordArrayAccess(2); // Two array accesses for the swap
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
     * Copies Integer[] results back to int[].
     */
    private void unboxIntArray(Integer[] source, int[] dest) {
        for (int i = 0; i < source.length; i++) {
            dest[i] = source[i];
        }
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

