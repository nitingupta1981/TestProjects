package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.model.GraphDataset;
import com.algorithmcomparison.model.GraphDataset.GraphNode;
import com.algorithmcomparison.util.GraphConverter;
import com.algorithmcomparison.util.MetricsCollector;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Breadth First Search (BFS) implementation.
 * 
 * BFS is a graph traversal algorithm that explores all nodes at the current
 * depth level before moving to nodes at the next depth level. It uses a queue
 * to track nodes to visit, ensuring level-order traversal.
 * 
 * This implementation:
 * 1. Converts the input array to a Complete Binary Tree
 * 2. Performs BFS using a queue for level-order traversal
 * 3. Searches for the target value in the graph
 * 
 * Algorithm Steps:
 * 1. Enqueue root node
 * 2. While queue is not empty:
 *    a. Dequeue node from front of queue
 *    b. If not visited, mark as visited
 *    c. Check if node value equals target
 *    d. Enqueue all unvisited children
 * 3. Return result when target found or queue is empty
 * 
 * Time Complexity: O(n) - visits each node once
 * Space Complexity: O(w) where w is the maximum width of the tree
 * 
 * Best Use Cases:
 * - Finding shortest path in unweighted graphs
 * - Level-order traversal
 * - Finding nodes at a specific distance
 * - Web crawling (explore nearby pages first)
 * 
 * Key Difference from DFS:
 * - BFS uses a Queue (FIFO) → explores breadth-first (level by level)
 * - DFS uses a Stack (LIFO) → explores depth-first (deep into branches)
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class BreadthFirstSearch implements SearchingAlgorithm {

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
        if (array == null || array.length == 0) {
            return -1;
        }

        // Convert array to Complete Binary Tree for graph-based search
        // Complete binary tree is ideal for BFS as it naturally supports level-order traversal
        GraphDataset graph = GraphConverter.convertToCompleteBinaryTree("bfs_temp", array);

        // Perform BFS starting from root (node 0)
        return bfsIterative(graph, target, metrics);
    }

    /**
     * Performs iterative BFS using a queue.
     * 
     * Queue ensures FIFO (First In First Out) order, which is essential
     * for level-order traversal. All nodes at level k are visited before
     * any node at level k+1.
     * 
     * Example traversal on tree:
     *        1           <- Level 0 (visited first)
     *       / \
     *      2   3         <- Level 1 (visited second)
     *     / \ / \
     *    4  5 6  7       <- Level 2 (visited third)
     * 
     * Visit order: 1, 2, 3, 4, 5, 6, 7
     * 
     * @param graph The graph to search
     * @param target The target value
     * @param metrics Metrics collector
     * @return Index of node containing target, or -1 if not found
     */
    private int bfsIterative(GraphDataset graph, int target, MetricsCollector metrics) {
        if (graph.getNodeCount() == 0) {
            return -1;
        }

        // Queue for BFS traversal - stores node IDs
        // LinkedList provides efficient O(1) enqueue and dequeue operations
        Queue<Integer> queue = new LinkedList<>();
        
        // Set to track visited nodes (prevents re-visiting)
        Set<Integer> visited = new HashSet<>();

        // Start BFS from root node (node ID 0)
        queue.offer(0);
        visited.add(0); // Mark root as visited immediately when enqueued

        // Continue until queue is empty (all reachable nodes explored)
        while (!queue.isEmpty()) {
            // Dequeue next node from front of queue
            Integer currentNodeId = queue.poll();

            metrics.recordArrayAccess(1); // Count as a node visit

            // Get the actual node
            GraphNode currentNode = graph.getNode(currentNodeId);
            
            if (currentNode == null) {
                continue;
            }

            // Compare current node's value with target
            if (metrics.isEqual(currentNode.getValue(), target)) {
                // Target found! Return the node ID (which maps to original array index)
                return currentNodeId;
            }

            // Enqueue all unvisited children for exploration
            // Process children in left-to-right order for proper level-order traversal
            
            // Enqueue left child if it exists and hasn't been visited
            if (currentNode.getLeftChild() != null) {
                Integer leftChild = currentNode.getLeftChild();
                if (!visited.contains(leftChild)) {
                    queue.offer(leftChild);
                    visited.add(leftChild); // Mark as visited when enqueued
                }
            }

            // Enqueue right child if it exists and hasn't been visited
            if (currentNode.getRightChild() != null) {
                Integer rightChild = currentNode.getRightChild();
                if (!visited.contains(rightChild)) {
                    queue.offer(rightChild);
                    visited.add(rightChild); // Mark as visited when enqueued
                }
            }

            // For linked list graphs, enqueue next node
            if (currentNode.getNext() != null) {
                Integer nextNode = currentNode.getNext();
                if (!visited.contains(nextNode)) {
                    queue.offer(nextNode);
                    visited.add(nextNode); // Mark as visited when enqueued
                }
            }
        }

        // Target not found in graph
        return -1;
    }

    @Override
    public int search(String[] array, String target, MetricsCollector metrics) {
        throw new UnsupportedOperationException(
            "Breadth First Search is a graph-based algorithm and only supports INTEGER datasets for graph representation. " +
            "Please use Linear Search, Binary Search, or Trie Search for STRING data.");
    }

    @Override
    public String getName() {
        return "Breadth First Search";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(w)"; // w = maximum width of tree
    }

    @Override
    public boolean requiresSortedArray() {
        // BFS works on any array structure
        return false;
    }

    @Override
    public boolean isGraphBased() {
        // BFS is a graph-based algorithm
        return true;
    }
}

