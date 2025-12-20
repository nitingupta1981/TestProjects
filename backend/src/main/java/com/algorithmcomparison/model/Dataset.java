package com.algorithmcomparison.model;

import java.util.Arrays;
import java.util.UUID;

/**
 * Represents a dataset of integers used for algorithm testing.
 * 
 * A dataset contains an array of integers and metadata about its generation.
 * Each dataset has a unique ID for tracking and reference.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class Dataset {
    
    private String id;
    private String name;
    private int[] data;
    private int size;
    private String type; // "RANDOM", "SORTED", "REVERSE_SORTED", "CUSTOM"
    private long createdTimestamp;

    /**
     * Default constructor for JSON serialization.
     */
    public Dataset() {
        this.id = UUID.randomUUID().toString();
        this.createdTimestamp = System.currentTimeMillis();
    }

    /**
     * Constructor with data and type.
     * 
     * @param data The integer array
     * @param type The type of dataset (RANDOM, SORTED, REVERSE_SORTED, CUSTOM)
     */
    public Dataset(int[] data, String type) {
        this();
        this.data = Arrays.copyOf(data, data.length);
        this.size = data.length;
        this.type = type;
        this.name = type + "_" + size;
    }

    /**
     * Constructor with all fields.
     * 
     * @param name The name of the dataset
     * @param data The integer array
     * @param type The type of dataset
     */
    public Dataset(String name, int[] data, String type) {
        this(data, type);
        this.name = name;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
        this.size = data != null ? data.length : 0;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    /**
     * Creates a copy of this dataset with a new array instance.
     * Useful for running multiple algorithms on the same data.
     * 
     * @return A new Dataset with copied data
     */
    public Dataset copy() {
        Dataset copy = new Dataset(this.name + "_copy", Arrays.copyOf(this.data, this.data.length), this.type);
        copy.setId(this.id + "_copy");
        return copy;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
}

