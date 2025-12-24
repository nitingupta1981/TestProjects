package com.algorithmcomparison.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a single step in algorithm visualization.
 * 
 * Captures the state of the array/data structure at a specific point
 * during algorithm execution, along with highlighted elements and operation type.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class VisualizationStep {
    
    private int stepNumber;
    private Object[] arrayState;  // Changed to Object[] to support both int and String
    private String operation; // e.g., "COMPARE", "SWAP", "INSERT", "PIVOT", "FOUND"
    private List<Integer> highlightedIndices;
    private String description;
    private List<String> colors; // Color codes for highlighted indices
    
    // Metadata for divide-and-conquer algorithms like Merge Sort
    private Integer activeLeft;  // Left boundary of active region
    private Integer activeRight; // Right boundary of active region

    /**
     * Default constructor.
     */
    public VisualizationStep() {
        this.highlightedIndices = new ArrayList<>();
        this.colors = new ArrayList<>();
    }

    /**
     * Constructor with step number and integer array state.
     * 
     * @param stepNumber The step number in the visualization sequence
     * @param arrayState Current state of the integer array
     * @param operation Type of operation being performed
     */
    public VisualizationStep(int stepNumber, int[] arrayState, String operation) {
        this();
        this.stepNumber = stepNumber;
        this.arrayState = new Object[arrayState.length];
        for (int i = 0; i < arrayState.length; i++) {
            this.arrayState[i] = arrayState[i];
        }
        this.operation = operation;
    }

    /**
     * Constructor with step number and string array state.
     * 
     * @param stepNumber The step number in the visualization sequence
     * @param arrayState Current state of the string array
     * @param operation Type of operation being performed
     */
    public VisualizationStep(int stepNumber, String[] arrayState, String operation) {
        this();
        this.stepNumber = stepNumber;
        this.arrayState = Arrays.copyOf(arrayState, arrayState.length, Object[].class);
        this.operation = operation;
    }

    /**
     * Constructor with description (for integer arrays).
     * 
     * @param stepNumber The step number
     * @param arrayState Current array state
     * @param operation Operation type
     * @param description Human-readable description of the step
     */
    public VisualizationStep(int stepNumber, int[] arrayState, String operation, String description) {
        this(stepNumber, arrayState, operation);
        this.description = description;
    }

    /**
     * Constructor with description (for string arrays).
     * 
     * @param stepNumber The step number
     * @param arrayState Current array state
     * @param operation Operation type
     * @param description Human-readable description of the step
     */
    public VisualizationStep(int stepNumber, String[] arrayState, String operation, String description) {
        this(stepNumber, arrayState, operation);
        this.description = description;
    }

    // Getters and Setters

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public Object[] getArrayState() {
        return arrayState;
    }

    public void setArrayState(Object[] arrayState) {
        this.arrayState = arrayState;
    }

    /**
     * Sets array state from integer array.
     */
    public void setArrayState(int[] arrayState) {
        this.arrayState = new Object[arrayState.length];
        for (int i = 0; i < arrayState.length; i++) {
            this.arrayState[i] = arrayState[i];
        }
    }

    /**
     * Sets array state from string array.
     */
    public void setArrayState(String[] arrayState) {
        this.arrayState = Arrays.copyOf(arrayState, arrayState.length, Object[].class);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<Integer> getHighlightedIndices() {
        return highlightedIndices;
    }

    public void setHighlightedIndices(List<Integer> highlightedIndices) {
        this.highlightedIndices = highlightedIndices;
    }

    /**
     * Adds an index to be highlighted in the visualization.
     * 
     * @param index The array index to highlight
     */
    public void addHighlightedIndex(int index) {
        this.highlightedIndices.add(index);
    }

    /**
     * Adds an index with a specific color.
     * 
     * @param index The array index
     * @param color The color code (e.g., "RED", "GREEN", "YELLOW")
     */
    public void addHighlightedIndex(int index, String color) {
        this.highlightedIndices.add(index);
        this.colors.add(color);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Integer getActiveLeft() {
        return activeLeft;
    }

    public void setActiveLeft(Integer activeLeft) {
        this.activeLeft = activeLeft;
    }

    public Integer getActiveRight() {
        return activeRight;
    }

    public void setActiveRight(Integer activeRight) {
        this.activeRight = activeRight;
    }

    /**
     * Sets the active region boundaries for divide-and-conquer algorithms.
     * 
     * @param left Left boundary index (inclusive)
     * @param right Right boundary index (inclusive)
     */
    public void setActiveRegion(int left, int right) {
        this.activeLeft = left;
        this.activeRight = right;
    }

    /**
     * Creates a copy of this visualization step.
     * 
     * @return A new VisualizationStep with copied data
     */
    public VisualizationStep copy() {
        VisualizationStep copy = new VisualizationStep();
        copy.setStepNumber(this.stepNumber);
        copy.setArrayState(Arrays.copyOf(this.arrayState, this.arrayState.length));
        copy.setOperation(this.operation);
        copy.setDescription(this.description);
        copy.setHighlightedIndices(new ArrayList<>(this.highlightedIndices));
        copy.setColors(new ArrayList<>(this.colors));
        copy.setActiveLeft(this.activeLeft);
        copy.setActiveRight(this.activeRight);
        return copy;
    }

    @Override
    public String toString() {
        return "VisualizationStep{" +
                "stepNumber=" + stepNumber +
                ", operation='" + operation + '\'' +
                ", highlightedIndices=" + highlightedIndices +
                ", description='" + description + '\'' +
                ", arrayState=" + Arrays.toString(arrayState) +
                '}';
    }
}

