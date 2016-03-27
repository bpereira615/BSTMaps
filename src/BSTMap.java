/********************************************************************
 * Benjamin Hoertnagl-Pereira 
 * JHED: bhoertn1 
 * 631-488-7197 
 * bhoertn1@jhu.edu
 *
 * 600.226.02 | CS226 Data Structures | Section 2
 * Project 3, Part A - Binary Search Tree
 *
 *******************************************************************/


import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;


/** Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class BSTMap<K extends Comparable<? super K>, V>
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {

    /** Inner node class.  Do not make this static because you want
        the K to be the same K as in the BSTMap header.
    */
    private class BNode {

        /** The key of the entry (null if sentinel node). */
        private K key;
        /** The value of the entry (null if sentinel node). */
        private V value;
        /** The left child of this node. */
        private BNode left;
        /** The right child of this node. */
        private BNode right;

        /** Create a new node with a particular key and value.
         *  @param k the key for the new node
         *  @param v the value for the new node
         */
        BNode(K k, V v){
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
        }

        /** Check whether this node is a leaf sentinel, based on key.
         *  @return true if leaf, false otherwise
         */
        public boolean isLeaf() {
            return this.key == null;  // this is a sentinel-based implementation
        }
    }

    /** The root of this tree. */
    private BNode root;
    /** The number of entries in this map (== non-sentinel nodes). */
    private int size;

    /** Create an empty tree with a sentinel root node.
     */
    public BSTMap() {
        // empty tree is a sentinel for the root
        this.root = new BNode(null, null);
        this.size = 0;
    }

    @Override()
    public int size() {
        return this.size;
    }

    @Override()
    public void clear() {

        //TODO: reuse constructor?
        this.root = new BNode(null, null);
        this.size = 0; 
    }

    @Override()
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override()
    public boolean hasKey(K key) {
        if (this.size == 0) {
            return false;
        }
        
        //call helper function
        return this.hasKey(key, this.root);
    }

    /** See if a key is in an entry in a subtree.
     *  @param key the key to search for
     *  @param curr the root of the subtree being searched
     *  @return true if found, false otherwise
     */
    public boolean hasKey(K key, BNode curr) {

        //reached end, not found
        if (curr.isLeaf()) {
            return false;
        }


        //in order search for node with given key
        int diff = key.compareTo(curr.key);

        
        //if key is smaller than root key, search left subtree
        if (diff < 0) {
            return hasKey(key, curr.left);
        } else if (diff == 0){ //node found
            return true;
        } else { //if key is larger than root key, search right subtree
            return hasKey(key, curr.right);
        }

    }

    @Override()
    public boolean hasValue(V value) {
        //essentially linear search, must look through all values to determine
        return false;
    }
    
    @Override()
    public V get(K key) {
        return this.get(key, this.root);
    }

    /** Get the value associated with key from subtree with given root node.
     *  @param key the key of the entry
     *  @param curr the root of the subtree from which to get the entry
     *  @return the value associated with the key, or null if not found
     */
    public V get(K key, BNode curr) {
        //run hasKey operation to find node associated with key, then return value

        //reached end, not found
        if (curr.isLeaf()) {
            return null;
        }


        //in order search for node with given key
        int diff = key.compareTo(curr.key);

        
        //if key is smaller than root key, search left subtree
        if (diff < 0) {
            return get(key, curr.left);
        } else if (diff == 0){ //node found
            return curr.value;
        } else { //if key is larger than root key, search right subtree
            return get(key, curr.right);
        }
    }

    @Override()
    public V put(K key, V val) {

        return this.put(key, val, this.root);
    }

    /** Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return the original value associated with the key, or null if not found
     */
    private V put(K key, V val, BNode curr) {

        //increment size
        this.size++;

        if (curr.isLeaf()) {

            //in correct position, insert node
            curr.key = key;
            curr.value = val;
            curr.left = new BNode(null, null);
            curr.right = new BNode(null, null);

            //TODO: return null for new nodes?
            return null;
        }


        //in order search for position
        int diff = key.compareTo(curr.key);
        
        //if key is smaller than root key, search left subtree
        if (diff < 0) {
            return put(key, val, curr.left);
        } else if (diff == 0){ //node found with same key, updated and return old
            V oldVal = curr.value;
            curr = new BNode(key, val);
            return oldVal;
        } else { //if key is larger than root key, search right subtree
            return put(key, val,  curr.right);
        }

    }

    @Override()
    public V remove(K key) {
        return this.remove(key, this.root);
    }

    /** Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the value associated with the removed key, or null if not found
     */
    public V remove(K key, BNode curr) {
    // Fill in
        return null;
    }
    
    @Override()
    public Set<Map.Entry<K, V>> entries() {
    // Fill in
        return null;
    }

    @Override()
    public Set<K> keys() {
    //Fill in
        return null;
    }

    @Override()
    public Collection<V> values() {
    // Fill in
        return null;
    }

    /* -----   BSTMap-specific functions   ----- */

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the min key
     */
    public K firstKey(BNode curr) {
        //edge case, no nodes
        if (this.size == 0) {
            return null;
        } else if (this.size == 1) {
            return this.root.key;
        }

        BNode forerunner = curr.left;

        while (!forerunner.isLeaf()) {
            forerunner = forerunner.left;
            curr = curr.left;
        }

        return curr.key;
    }

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the max key
     */
    public K lastKey(BNode curr) {
    // Fill in
        return null;
    }

    /** Inorder traversal that produces an iterator over key-value pairs.
     *  @return an iterable list of entries ordered by keys
     */
    public Iterable<Map.Entry<K, V>> inOrder() {
        return this.inOrder(this.root);
    }
    
    /** Inorder traversal produces an iterator over entries in a subtree.
     *  @param curr the root of the subtree to iterate over
     *  @return an iterable list of entries ordered by keys
     */
    private Collection<Map.Entry<K, V>> inOrder(BNode curr) {
        LinkedList<Map.Entry<K, V>> ordered = new LinkedList<Map.Entry<K, V>>();

    // Fill in

        return ordered;
    }

    /** Returns a copy of the portion of this map whose keys are in a range.
     *  @param fromKey the starting key of the range, inclusive if found
     *  @param toKey the ending key of the range, inclusive if found
     *  @return the resulting submap
     */
    public BSTMap<K, V> subMap(K fromKey, K toKey) {
    // Fill in
        return null;
    }

    /* ---------- from Iterable ---------- */

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        // you do not have to implement this
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        // you do not have to implement this
        return null;
    }

    /* -----  insert the BSTMapIterator inner class here ----- */

    public BNode test() {
        return this.root;
    }
}
