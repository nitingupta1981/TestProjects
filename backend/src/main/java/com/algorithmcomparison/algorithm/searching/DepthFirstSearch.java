package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.model.GraphDataset;
import com.algorithmcomparison.model.GraphDataset.GraphNode;
import com.algorithmcomparison.util.GraphConverter;
import com.algorithmcomparison.util.MetricsCollector;

import java.util.HashSet;
import java.util.Set;

/**
 * Depth First Search (DFS) implementation.
 * 
 * DFS is a graph traversal algorithm that explores as deep as possible along
 * each branch before backtracking. It uses recursion to track the traversal path.
 * 
 * This implementation:
 * 1. Converts the input array to a Binary Search Tree
 * 2. Performs recursive DFS
 * 3. Searches for the target value in the graph
 * 
 * Algorithm Steps:
 * 1. Start at root node
 * 2. If node is null or visited, return
 * 3. Mark node as visited
 * 4. Check if node value equals target
 * 5. Recursively visit left child, then right child
 * 6. Return result when target found or all nodes explored
 * 
 * Time Complexity: O(n) - visits each node once
 * Space Complexity: O(h) where h is the height of the tree (recursion stack)
 * 
 * Best Use Cases:
 * - Finding path existence
 * - Exploring all possibilities (exhaustive search)
 * - Topological sorting
 * - Cycle detection
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class DepthFirstSearch implements SearchingAlgorithm {

    // Set to track visited nodes (shared across recursive calls)
    private Set<Integer> visited;
    private GraphDataset graph;
    private int target;
    private MetricsCollector metrics;

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
        if (array == null || array.length == 0) {
            return -1;
        }

        // Convert array to Binary Search Tree for graph-based search
        this.graph = GraphConverter.convertToBinarySearchTree("dfs_temp", array);
        this.target = target;
        this.metrics = metrics;
        this.visited = new HashSet<>();

        // Perform recursive DFS starting from root (node 0)
        return dfsRecursive(0);
    }

    /**
     * Performs recursive DFS.
     * 
     * Recursive approach provides:
     * - Natural implementation of depth-first exploration
     * - Implicit use of call stack for backtracking
     * - Cleaner, more readable code
     * - Better demonstrates the algorithm's nature
     * 
     * @param nodeId The current node ID to explore
     * @return Index of node containing target, or -1 if not found
     */
    private int dfsRecursive(Integer nodeId) {
        // Base case 1: Invalid node ID
        if (nodeId == null) {
            return -1;
        }

        // Base case 2: Already visited this node
        if (visited.contains(nodeId)) {
            return -1;
        }

        // Mark node as visited
        visited.add(nodeId);
        metrics.recordArrayAccess(1); // Count as a node visit

        // Get the actual node
        GraphNode currentNode = graph.getNode(nodeId);
        
        if (currentNode == null) {
            return -1;
        }

        // Compare current node's value with target
        if (metrics.isEqual(currentNode.getValue(), target)) {
            // Target found! Return the node ID
            return nodeId;
        }

        // Recursively explore left child
        if (currentNode.getLeftChild() != null) {
            int result = dfsRecursive(currentNode.getLeftChild());
            if (result != -1) {
                return result; // Found in left subtree
            }
        }

        // Recursively explore right child
        if (currentNode.getRightChild() != null) {
            int result = dfsRecursive(currentNode.getRightChild());
            if (result != -1) {
                return result; // Found in right subtree
            }
        }

        // For linked list graphs, explore next node
        if (currentNode.getNext() != null) {
            int result = dfsRecursive(currentNode.getNext());
            if (result != -1) {
                return result; // Found in next node
            }
        }

        // Target not found in this branch
        return -1;
    }

    @Override
    public int search(String[] array, String target, MetricsCollector metrics) {
        throw new UnsupportedOperationException(
            "Depth First Search is a graph-based algorithm and only supports INTEGER datasets for graph representation. " +
            "Please use Linear Search, Binary Search, or Trie Search for STRING data.");
    }

    @Override
    public String getName() {
        return "Depth First Search";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(h)"; // h = height of tree
    }

    @Override
    public boolean requiresSortedArray() {
        // DFS works on any array structure
        return false;
    }

    @Override
    public boolean isGraphBased() {
        // DFS is a graph-based algorithm
        return true;
    }
}

