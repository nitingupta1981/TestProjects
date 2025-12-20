package com.algorithmcomparison.util;

import com.algorithmcomparison.model.GraphDataset;
import com.algorithmcomparison.model.GraphDataset.GraphNode;
import com.algorithmcomparison.model.GraphDataset.GraphType;

/**
 * Utility class for converting array datasets to graph structures.
 * 
 * This converter enables graph-based search algorithms (DFS, BFS) to operate
 * on array data by transforming arrays into various graph representations.
 * 
 * Supported Conversions:
 * - Binary Search Tree (BST): Values inserted to maintain BST property
 * - Complete Binary Tree: Array indices map to tree structure
 * - Linked List: Sequential chain of nodes
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class GraphConverter {

    /**
     * Converts an array to a Binary Search Tree.
     * 
     * The BST is constructed by inserting array elements sequentially.
     * For each element:
     * - If less than current node, go to left subtree
     * - If greater than or equal, go to right subtree
     * 
     * Time Complexity: O(n * h) where h is the height of the tree
     * - Best case (balanced): O(n log n)
     * - Worst case (skewed): O(n²)
     * 
     * @param sourceDatasetId ID of the source dataset
     * @param array Array to convert
     * @return GraphDataset representing a BST
     */
    public static GraphDataset convertToBinarySearchTree(String sourceDatasetId, int[] array) {
        GraphDataset graph = new GraphDataset(sourceDatasetId, GraphType.BINARY_SEARCH_TREE);

        if (array == null || array.length == 0) {
            return graph;
        }

        // Root node is the first element
        GraphNode root = new GraphNode(0, array[0]);
        graph.addNode(root);

        // Insert remaining elements
        for (int i = 1; i < array.length; i++) {
            insertIntoBST(graph, array[i], i);
        }

        return graph;
    }

    /**
     * Inserts a value into the BST maintaining BST properties.
     * 
     * @param graph The graph dataset
     * @param value The value to insert
     * @param nodeId The ID for the new node
     */
    private static void insertIntoBST(GraphDataset graph, int value, int nodeId) {
        GraphNode newNode = new GraphNode(nodeId, value);
        GraphNode current = graph.getNode(0); // Start from root

        // Traverse tree to find insertion point
        while (true) {
            if (value < current.getValue()) {
                // Go to left subtree
                if (current.getLeftChild() == null) {
                    // Found insertion point - make new node left child
                    current.setLeftChild(nodeId);
                    graph.addNode(newNode);
                    graph.addEdge(current.getNodeId(), nodeId);
                    break;
                } else {
                    // Continue traversing left
                    current = graph.getNode(current.getLeftChild());
                }
            } else {
                // Go to right subtree (includes equal values)
                if (current.getRightChild() == null) {
                    // Found insertion point - make new node right child
                    current.setRightChild(nodeId);
                    graph.addNode(newNode);
                    graph.addEdge(current.getNodeId(), nodeId);
                    break;
                } else {
                    // Continue traversing right
                    current = graph.getNode(current.getRightChild());
                }
            }
        }
    }

    /**
     * Converts an array to a Complete Binary Tree.
     * 
     * Uses array index mapping:
     * - Node at index i has:
     *   - Left child at index 2*i + 1
     *   - Right child at index 2*i + 2
     *   - Parent at index (i-1)/2
     * 
     * This creates a complete binary tree where all levels are filled
     * except possibly the last, which is filled from left to right.
     * 
     * Time Complexity: O(n)
     * 
     * @param sourceDatasetId ID of the source dataset
     * @param array Array to convert
     * @return GraphDataset representing a complete binary tree
     */
    public static GraphDataset convertToCompleteBinaryTree(String sourceDatasetId, int[] array) {
        GraphDataset graph = new GraphDataset(sourceDatasetId, GraphType.COMPLETE_BINARY_TREE);

        if (array == null || array.length == 0) {
            return graph;
        }

        // Create all nodes
        for (int i = 0; i < array.length; i++) {
            GraphNode node = new GraphNode(i, array[i]);
            graph.addNode(node);
        }

        // Link nodes according to complete binary tree structure
        for (int i = 0; i < array.length; i++) {
            GraphNode node = graph.getNode(i);
            
            int leftChildIndex = 2 * i + 1;
            int rightChildIndex = 2 * i + 2;

            // Set left child if it exists
            if (leftChildIndex < array.length) {
                node.setLeftChild(leftChildIndex);
                graph.addEdge(i, leftChildIndex);
            }

            // Set right child if it exists
            if (rightChildIndex < array.length) {
                node.setRightChild(rightChildIndex);
                graph.addEdge(i, rightChildIndex);
            }
        }

        return graph;
    }

    /**
     * Converts an array to a Linked List.
     * 
     * Creates a singly linked list where each node points to the next.
     * The first array element becomes the head of the list.
     * 
     * Time Complexity: O(n)
     * 
     * @param sourceDatasetId ID of the source dataset
     * @param array Array to convert
     * @return GraphDataset representing a linked list
     */
    public static GraphDataset convertToLinkedList(String sourceDatasetId, int[] array) {
        GraphDataset graph = new GraphDataset(sourceDatasetId, GraphType.LINKED_LIST);

        if (array == null || array.length == 0) {
            return graph;
        }

        // Create all nodes
        for (int i = 0; i < array.length; i++) {
            GraphNode node = new GraphNode(i, array[i]);
            graph.addNode(node);

            // Link to next node if not the last element
            if (i < array.length - 1) {
                node.setNext(i + 1);
                graph.addEdge(i, i + 1);
            }
        }

        return graph;
    }

    /**
     * Converts an array to a graph using the specified graph type.
     * 
     * @param sourceDatasetId ID of the source dataset
     * @param array Array to convert
     * @param graphType Type of graph to create
     * @return GraphDataset representing the specified graph type
     */
    public static GraphDataset convert(String sourceDatasetId, int[] array, GraphType graphType) {
        switch (graphType) {
            case BINARY_SEARCH_TREE:
                return convertToBinarySearchTree(sourceDatasetId, array);
            case COMPLETE_BINARY_TREE:
                return convertToCompleteBinaryTree(sourceDatasetId, array);
            case LINKED_LIST:
                return convertToLinkedList(sourceDatasetId, array);
            default:
                throw new IllegalArgumentException("Unsupported graph type: " + graphType);
        }
    }
}

