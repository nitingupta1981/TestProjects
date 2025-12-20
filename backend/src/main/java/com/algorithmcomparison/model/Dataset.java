package com.algorithmcomparison.model;

import java.util.Arrays;
import java.util.UUID;

/**
 * Represents a dataset used for algorithm testing.
 * 
 * A dataset contains an array of data (integers or strings) and metadata about its generation.
 * Each dataset has a unique ID for tracking and reference.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class Dataset {
    
    private String id;
    private String name;
    private int[] data;
    private String[] stringData;
    private int size;
    private String type; // "RANDOM", "SORTED", "REVERSE_SORTED", "CUSTOM"
    private String dataType; // "INTEGER" or "STRING"
    private long createdTimestamp;

    /**
     * Default constructor for JSON serialization.
     */
    public Dataset() {
        this.id = UUID.randomUUID().toString();
        this.createdTimestamp = System.currentTimeMillis();
        this.dataType = "INTEGER"; // Default to integer
    }

    /**
     * Constructor with integer data and type.
     * 
     * @param data The integer array
     * @param type The type of dataset (RANDOM, SORTED, REVERSE_SORTED, CUSTOM)
     */
    public Dataset(int[] data, String type) {
        this();
        this.data = Arrays.copyOf(data, data.length);
        this.size = data.length;
        this.type = type;
        this.dataType = "INTEGER";
        this.name = type + "_INT_" + size;
    }

    /**
     * Constructor with string data and type.
     * 
     * @param stringData The string array
     * @param type The type of dataset (RANDOM, SORTED, REVERSE_SORTED, CUSTOM)
     */
    public Dataset(String[] stringData, String type) {
        this();
        this.stringData = Arrays.copyOf(stringData, stringData.length);
        this.size = stringData.length;
        this.type = type;
        this.dataType = "STRING";
        this.name = type + "_STR_" + size;
    }

    /**
     * Constructor with all fields for integer data.
     * 
     * @param name The name of the dataset
     * @param data The integer array
     * @param type The type of dataset
     */
    public Dataset(String name, int[] data, String type) {
        this(data, type);
        this.name = name;
    }

    /**
     * Constructor with all fields for string data.
     * 
     * @param name The name of the dataset
     * @param stringData The string array
     * @param type The type of dataset
     */
    public Dataset(String name, String[] stringData, String type) {
        this(stringData, type);
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

    public String[] getStringData() {
        return stringData;
    }

    public void setStringData(String[] stringData) {
        this.stringData = stringData;
        this.size = stringData != null ? stringData.length : 0;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * Gets the data as an Object array (works for both int[] and String[]).
     * 
     * @return Object array representation of the data
     */
    public Object[] getDataAsObjectArray() {
        if ("STRING".equals(dataType) && stringData != null) {
            return stringData;
        } else if (data != null) {
            Object[] result = new Object[data.length];
            for (int i = 0; i < data.length; i++) {
                result[i] = data[i];
            }
            return result;
        }
        return new Object[0];
    }

    /**
     * Creates a copy of this dataset with a new array instance.
     * Useful for running multiple algorithms on the same data.
     * 
     * @return A new Dataset with copied data
     */
    public Dataset copy() {
        if ("STRING".equals(dataType) && stringData != null) {
            Dataset copy = new Dataset(this.name + "_copy", Arrays.copyOf(this.stringData, this.stringData.length), this.type);
            copy.setId(this.id + "_copy");
            return copy;
        } else {
            Dataset copy = new Dataset(this.name + "_copy", Arrays.copyOf(this.data, this.data.length), this.type);
            copy.setId(this.id + "_copy");
            return copy;
        }
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}

