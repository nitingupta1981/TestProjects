package com.algorithmcomparison.algorithm.searching;

import com.algorithmcomparison.util.MetricsCollector;

/**
 * Trie-based Search implementation for String datasets.
 * 
 * A Trie (prefix tree) is a tree-like data structure that stores strings
 * in a way that allows for efficient prefix-based searching.
 * 
 * Algorithm Steps:
 * 1. Build a Trie from all strings in the array
 * 2. Search for the target string by traversing the Trie
 * 3. Return the first index where the string was found
 * 
 * Time Complexity: 
 * - Build: O(n * m) where n is number of strings, m is average length
 * - Search: O(m) where m is length of target string
 * Space Complexity: O(n * m) for the Trie structure
 * 
 * Best Use Cases:
 * - Multiple searches on the same dataset
 * - Prefix matching operations
 * - Autocomplete functionality
 * - Dictionary lookups
 * 
 * Note: This implementation builds the Trie for each search.
 * In practice, you'd build once and search multiple times.
 * 
 * @author Algorithm Comparison Team
 * @version 1.0
 */
public class TrieSearch implements SearchingAlgorithm {

    /**
     * Trie Node class representing each character in the tree.
     */
    private static class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;
        int firstIndex; // Stores the first index where this string appears
        
        public TrieNode() {
            // Support all printable ASCII characters (128 characters)
            children = new TrieNode[128];
            isEndOfWord = false;
            firstIndex = -1;
        }
    }

    @Override
    public int search(int[] array, int target, MetricsCollector metrics) {
        throw new UnsupportedOperationException(
            "Trie Search only supports STRING datasets. " +
            "Please use Linear Search or Binary Search for INTEGER data.");
    }

    @Override
    public int search(String[] array, String target, MetricsCollector metrics) {
        if (array == null || array.length == 0 || target == null) {
            return -1;
        }

        // Build the Trie from the array
        TrieNode root = buildTrie(array, metrics);
        
        // Search for the target in the Trie
        return searchInTrie(root, target, metrics);
    }

    /**
     * Builds a Trie from the given string array.
     * 
     * @param array The array of strings
     * @param metrics Metrics collector
     * @return The root node of the Trie
     */
    private TrieNode buildTrie(String[] array, MetricsCollector metrics) {
        TrieNode root = new TrieNode();
        
        for (int i = 0; i < array.length; i++) {
            String word = array[i];
            metrics.recordArrayAccess(1);
            
            if (word == null) continue;
            
            TrieNode current = root;
            
            // Insert each character of the word into the Trie
            for (int j = 0; j < word.length(); j++) {
                char ch = word.charAt(j);
                int index = (int) ch;
                
                // Ensure character is within our supported range
                if (index >= 128) {
                    index = 127; // Use last slot for unsupported characters
                }
                
                if (current.children[index] == null) {
                    current.children[index] = new TrieNode();
                }
                
                current = current.children[index];
            }
            
            // Mark end of word and store the first occurrence index
            current.isEndOfWord = true;
            if (current.firstIndex == -1) {
                current.firstIndex = i;
            }
        }
        
        return root;
    }

    /**
     * Searches for a target string in the Trie.
     * 
     * @param root The root node of the Trie
     * @param target The string to search for
     * @param metrics Metrics collector
     * @return Index of first occurrence, or -1 if not found
     */
    private int searchInTrie(TrieNode root, String target, MetricsCollector metrics) {
        TrieNode current = root;
        
        // Traverse the Trie following the characters of the target
        for (int i = 0; i < target.length(); i++) {
            char ch = target.charAt(i);
            int index = (int) ch;
            
            metrics.recordComparison(1); // Count each character comparison
            
            if (index >= 128) {
                index = 127;
            }
            
            if (current.children[index] == null) {
                // Character not found - target doesn't exist in Trie
                return -1;
            }
            
            current = current.children[index];
        }
        
        // Check if we've reached the end of a valid word
        if (current.isEndOfWord) {
            return current.firstIndex;
        }
        
        return -1; // Target is a prefix but not a complete word
    }

    @Override
    public String getName() {
        return "Trie Search";
    }

    @Override
    public String getTimeComplexity() {
        return "O(m)"; // m = length of search string (after Trie is built)
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n*m)"; // n = number of strings, m = average length
    }

    @Override
    public boolean requiresSortedArray() {
        // Trie search doesn't require sorted data
        return false;
    }
}

