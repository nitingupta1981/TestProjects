package com.algorithmcomparison.model;

import java.util.*;

/**
 * Represents a graph data structure converted from an array dataset.
 * 
 * This model is used for graph-based search algorithms (DFS, BFS).
 * It supports multiple graph representations including adjacency lists.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class GraphDataset {
    
    private String id;
    private String sourceDatasetId;
    private GraphType graphType;
    private Map<Integer, GraphNode> nodes;
    private Map<Integer, List<Integer>> adjacencyList;
    private int nodeCount;

    /**
     * Enum representing different types of graph structures.
     */
    public enum GraphType {
        BINARY_SEARCH_TREE,  // BST structure
        COMPLETE_BINARY_TREE, // Complete binary tree
        LINKED_LIST           // Singly linked list
    }

    /**
     * Inner class representing a graph node.
     */
    public static class GraphNode {
        private int value;
        private Integer leftChild;  // Node ID of left child (for trees)
        private Integer rightChild; // Node ID of right child (for trees)
        private Integer next;       // Node ID of next node (for linked list)
        private int nodeId;

        public GraphNode(int nodeId, int value) {
            this.nodeId = nodeId;
            this.value = value;
        }

        // Getters and Setters
        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Integer getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Integer leftChild) {
            this.leftChild = leftChild;
        }

        public Integer getRightChild() {
            return rightChild;
        }

        public void setRightChild(Integer rightChild) {
            this.rightChild = rightChild;
        }

        public Integer getNext() {
            return next;
        }

        public void setNext(Integer next) {
            this.next = next;
        }

        public int getNodeId() {
            return nodeId;
        }

        public void setNodeId(int nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public String toString() {
            return "Node(" + nodeId + ", value=" + value + ")";
        }
    }

    /**
     * Default constructor.
     */
    public GraphDataset() {
        this.id = UUID.randomUUID().toString();
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Constructor with source dataset reference.
     * 
     * @param sourceDatasetId ID of the source dataset
     * @param graphType Type of graph structure
     */
    public GraphDataset(String sourceDatasetId, GraphType graphType) {
        this();
        this.sourceDatasetId = sourceDatasetId;
        this.graphType = graphType;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceDatasetId() {
        return sourceDatasetId;
    }

    public void setSourceDatasetId(String sourceDatasetId) {
        this.sourceDatasetId = sourceDatasetId;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }

    public Map<Integer, GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Integer, GraphNode> nodes) {
        this.nodes = nodes;
        this.nodeCount = nodes.size();
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Map<Integer, List<Integer>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    /**
     * Adds a node to the graph.
     * 
     * @param node The node to add
     */
    public void addNode(GraphNode node) {
        nodes.put(node.getNodeId(), node);
        adjacencyList.putIfAbsent(node.getNodeId(), new ArrayList<>());
        this.nodeCount = nodes.size();
    }

    /**
     * Adds an edge between two nodes.
     * 
     * @param fromNodeId Source node ID
     * @param toNodeId Target node ID
     */
    public void addEdge(int fromNodeId, int toNodeId) {
        adjacencyList.computeIfAbsent(fromNodeId, k -> new ArrayList<>()).add(toNodeId);
    }

    /**
     * Gets a node by its ID.
     * 
     * @param nodeId The node ID
     * @return The graph node, or null if not found
     */
    public GraphNode getNode(int nodeId) {
        return nodes.get(nodeId);
    }

    @Override
    public String toString() {
        return "GraphDataset{" +
                "id='" + id + '\'' +
                ", sourceDatasetId='" + sourceDatasetId + '\'' +
                ", graphType=" + graphType +
                ", nodeCount=" + nodeCount +
                '}';
    }
}

