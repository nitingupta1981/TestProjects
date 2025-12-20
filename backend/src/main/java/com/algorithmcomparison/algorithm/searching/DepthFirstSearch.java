package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.model.GraphDataset;
import com.algorithmcomparison.model.GraphDataset.GraphNode;
import com.algorithmcomparison.util.GraphConverter;
import com.algorithmcomparison.util.MetricsCollector;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Depth First Search (DFS) implementation.
 * 
 * DFS is a graph traversal algorithm that explores as deep as possible along
 * each branch before backtracking. It uses a stack (either explicitly or via
 * recursion) to track the traversal path.
 * 
 * This implementation:
 * 1. Converts the input array to a Binary Search Tree
 * 2. Performs iterative DFS using an explicit stack
 * 3. Searches for the target value in the graph
 * 
 * Algorithm Steps:
 * 1. Push root node onto stack
 * 2. While stack is not empty:
 *    a. Pop node from stack
 *    b. If not visited, mark as visited
 *    c. Check if node value equals target
 *    d. Push unvisited children onto stack (right then left for proper order)
 * 3. Return result when target found or stack is empty
 * 
 * Time Complexity: O(n) - visits each node once
 * Space Complexity: O(h) where h is the height of the tree (stack depth)
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

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
        if (array == null || array.length == 0) {
            return -1;
        }

        // Convert array to Binary Search Tree for graph-based search
        GraphDataset graph = GraphConverter.convertToBinarySearchTree("dfs_temp", array);

        // Perform DFS starting from root (node 0)
        return dfsIterative(graph, target, metrics);
    }

    /**
     * Performs iterative DFS using an explicit stack.
     * 
     * Iterative approach is preferred over recursive to:
     * - Avoid stack overflow on deep trees
     * - Have explicit control over stack operations
     * - Make traversal easier to visualize and debug
     * 
     * @param graph The graph to search
     * @param target The target value
     * @param metrics Metrics collector
     * @return Index of node containing target, or -1 if not found
     */
    private int dfsIterative(GraphDataset graph, int target, MetricsCollector metrics) {
        if (graph.getNodeCount() == 0) {
            return -1;
        }

        // Stack for DFS traversal - stores node IDs
        Stack<Integer> stack = new Stack<>();
        
        // Set to track visited nodes (prevents infinite loops)
        Set<Integer> visited = new HashSet<>();

        // Start DFS from root node (node ID 0)
        stack.push(0);

        // Continue until stack is empty (all reachable nodes explored)
        while (!stack.isEmpty()) {
            // Pop next node to explore from top of stack
            Integer currentNodeId = stack.pop();

            // Skip if already visited
            if (visited.contains(currentNodeId)) {
                continue;
            }

            // Mark node as visited
            visited.add(currentNodeId);
            metrics.recordArrayAccess(1); // Count as a node visit

            // Get the actual node
            GraphNode currentNode = graph.getNode(currentNodeId);
            
            if (currentNode == null) {
                continue;
            }

            // Compare current node's value with target
            if (metrics.isEqual(currentNode.getValue(), target)) {
                // Target found! Return the node ID
                return currentNodeId;
            }

            // Push children onto stack for further exploration
            // Push RIGHT child first, then LEFT child
            // This ensures LEFT child is processed first (LIFO property of stack)
            
            if (currentNode.getRightChild() != null && 
                !visited.contains(currentNode.getRightChild())) {
                stack.push(currentNode.getRightChild());
            }

            if (currentNode.getLeftChild() != null && 
                !visited.contains(currentNode.getLeftChild())) {
                stack.push(currentNode.getLeftChild());
            }

            // For linked list graphs, push next node
            if (currentNode.getNext() != null && 
                !visited.contains(currentNode.getNext())) {
                stack.push(currentNode.getNext());
            }
        }

        // Target not found in graph
        return -1;
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

