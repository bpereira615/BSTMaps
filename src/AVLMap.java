/********************************************************************
 * Benjamin Hoertnagl-Pereira 
 * JHED: bhoertn1 
 * 631-488-7197 
 * bhoertn1@jhu.edu
 * 
 * Ryan Demo
 * JHED: rdemo1
 * rdemo1@jhu.edu
 *
 * 600.226.02 | CS226 Data Structures | Section 2
 * Project 3, Part B - Balanced Binary Search Tree
 *
 *******************************************************************/


import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

//inOrder Map.Entry collection
import java.util.AbstractMap;
import java.util.ArrayList;
//entries and values sets
import java.util.HashSet;


/** Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class AVLMap<K extends Comparable<? super K>, V>
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {

    //==========================================================================
    // AVLNode Inner Class
    //==========================================================================

    /** Inner node class.  Do not make this static because you want
        the K to be the same K as in the AVLMap header.
     */
    protected class AVLNode {

        /** The key of the entry (null if sentinel node). */
        protected K key;
        /** The value of the entry (null if sentinel node). */
        protected V value;
        /** The left child of this node. */
        protected AVLNode left;
        /** The right child of this node. */
        protected AVLNode right;

        /** The height of a node. */
        private int height;

        /** Create a new node with a particular key and value.
         *  @param k the key for the new node
         *  @param v the value for the new node
         */
        AVLNode(K k, V v) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
            this.height = -1;
        }

        /** Evaluate balance factor of the node by comparing
         * the height of its children, left - right.
         * @return balance factor
         */
        public int bf() {
            if (this.isLeaf()) {
                return 0;
            }
            return this.left.height - this.right.height;
        }

        /**
         * toString method for BNode.
         * @return the string
         */
        public String toString() {
            if (this.isLeaf()) {
                return "  X  ";
            }
            return "(" + this.key.toString() + ":"
                + this.value.toString() + ")";
        }

        /** Redetermines height of Node based on child heights.
         */
        private void determineNewHeight() {
            if (this.isLeaf()) {
                this.height = -1;
            } else {
                this.height = Math.max(this.left.height, this.right.height) + 1;
            }
            System.out.printf("%s: %d\n", this.key, this.height);
        }

        /** Check whether this node is a leaf sentinel, based on key.
         *  @return true if leaf, false otherwise
         */
        public boolean isLeaf() {
            return this.key == null;  // this is a sentinel-based implementation
        }
    }


    //==========================================================================
    // Global Map Variables
    //==========================================================================

    /** The root of this tree. */
    protected AVLNode root;
    /** The number of entries in this map (== non-sentinel nodes). */
    protected int size;
    /** Keeps track of state for iterator, incremented
     * when put or remove is called. */
    protected int operations;


    //==========================================================================
    // Map Reading Functions
    //==========================================================================

    /** Create an empty tree with a sentinel root node.
     */
    public AVLMap() {
        // empty tree is a sentinel for the root
        this.root = new AVLNode(null, null);
        this.size = 0;
    }

    @Override()
    public int size() {
        return this.size;
    }

    @Override()
    public void clear() {
        this.root = new AVLNode(null, null);
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
    public boolean hasKey(K key, AVLNode curr) {

        //reached end, not found (base case)
        if (curr.isLeaf()) {
            return false;
        }

        //in order search for node with given key
        int diff = key.compareTo(curr.key);

        //if key is smaller than root key, search left subtree
        if (diff < 0) {
            return this.hasKey(key, curr.left);
        } else if (diff == 0) { //node found
            return true;
        } else { //if key is larger than root key, search right subtree
            return this.hasKey(key, curr.right);
        }

    }

    /** Find a key in a particular subtree.
     *  @param key the key to find
     *  @param curr the root of the tree to search
     *  @return the node containing the key, or a sentinel leaf if not found
     */
    private AVLNode findKey(K key, AVLNode curr) {
        if (curr.isLeaf()) {
            return curr;
        }
        int comparison = key.compareTo(curr.key);
        if (comparison < 0) {
            return this.findKey(key, curr.left);
        } else if (comparison > 0) {
            return this.findKey(key, curr.right);
        } else {
            return curr;
        }
    }

    @Override()
    public boolean hasValue(V value) {
        //essentially linear search, must look through all values to determine
        for (Map.Entry<K, V> entry : this.inOrder(this.root)) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }

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
    public V get(K key, AVLNode curr) {
        //run hasKey operation to find node associated with key, 
        //then return value

        //reached end, not found
        if (curr.isLeaf()) {
            return null;
        }

        //in order search for node with given key
        int diff = key.compareTo(curr.key);

        //if key is smaller than root key, search left subtree
        if (diff < 0) {
            return this.get(key, curr.left);
        } else if (diff == 0) { //node found
            return curr.value;
        } else { //if key is larger than root key, search right subtree
            return this.get(key, curr.right);
        }
    }


    //==========================================================================
    // Map Writing Functions
    //==========================================================================

    @Override()
    public V put(K key, V val) {
        //throw exception if key is null
        if (key == null) {
            throw new NullPointerException();
        }

        if (!this.hasKey(key)) {
            //increment size
            this.size++;
        }

        V prevVal = this.put(key, val, this.root);
        this.root.determineNewHeight();
        if (Math.abs(this.root.bf()) > 1) {
            this.root = this.rebalance(this.root);
        }
        this.root.determineNewHeight();

        this.operations++;

        return prevVal;
    }

    /** Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return the original value associated with the key, or null if not found
     */
    private V put(K key, V val, AVLNode curr) {

        V prevVal = null;

        if (curr.isLeaf()) {
            //in correct position, insert node
            curr.key = key;
            curr.value = val;
            curr.left = new AVLNode(null, null);
            curr.right = new AVLNode(null, null);
            curr.determineNewHeight();
            return null;
        }

        //in order search for position
        int diff = key.compareTo(curr.key);

        //if key is smaller than curr key, put in left subtree
        if (diff < 0) {
            prevVal = this.put(key, val, curr.left);
            curr.determineNewHeight();
            curr.left = this.rebalance(curr.left);
        } else if (diff > 0) {
            //if key is larger than root key, search right subtree
            prevVal = this.put(key, val, curr.right);
            curr.determineNewHeight();
            curr.right = this.rebalance(curr.right);
        } else { //node found with key, update and return old
            prevVal = curr.value;
            curr.value = val;
        }
        curr.determineNewHeight();
        return prevVal;
    }

    /** Remove the entry associated with a key.
     *  @param key the key for the entry being deleted
     *  @return the value associated with the key, or null if key not there
     */
    @Override()
    public V remove(K key) {
        AVLNode toDelete = this.findKey(key, this.root);
        if (toDelete.isLeaf()) {  // key not found
            return null;
        } else {
            V val = toDelete.value;
            this.size--;
            this.operations++;
            this.root = this.remove(key, this.root);
            return val;
        }
    }

    /** Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the root of the new subtree
     */
    public AVLNode remove(K key, AVLNode curr) {        
        if (curr.isLeaf()) {
            return curr;
        }
        int comparison = key.compareTo(curr.key);
        if (comparison < 0) {
            curr.left = this.remove(key, curr.left);
            curr.left.determineNewHeight();
            curr.left = this.rebalance(curr.left);
        } else if (comparison > 0) {
            curr.right = this.remove(key, curr.right);
            curr.right.determineNewHeight();
            curr.right = this.rebalance(curr.right);
        } else {  // remove curr node
            if (curr.left.isLeaf()) { // replace with right
                curr = curr.right;
                curr.determineNewHeight();
                curr = this.rebalance(curr);
            } else if (curr.right.isLeaf()) { // replace with left
                curr.determineNewHeight();
                curr = this.rebalance(curr);
            } else {
                // replace entry with min in right subtree
                AVLNode min = curr.right;
                while (!min.left.isLeaf()) {
                    min = min.left;
                }
                curr.key = min.key;
                curr.value = min.value;
                // remove actual node with min recursively
                curr.right = this.remove(min.key, curr.right);
                curr.right.determineNewHeight();
                curr.right = this.rebalance(curr.right);
                curr.determineNewHeight();
                curr = this.rebalance(curr);
            }
        }
        return curr;  // this is essential!
    }

    /** Handles remove case where the node has 0 or 1 non-sentinel children.
     * @param curr the AVLNode to remove
     */
    public void removeIncompleteSubTree(AVLNode curr) {
        //edge case deleting root
        if (curr.key == this.root.key) {
            if (this.root.left.key != null) {
                this.root = this.root.left;
            } else if (this.root.right.key != null) {
                //if left and right are both null, curr becomes null here
                this.root = this.root.right;
            } else {

                this.root.key = null;
                this.root.value = null;

                this.root.height = -1;
            }
        } else {
            if (curr.left.key != null) {
                curr = curr.left;
            } else if (curr.right.key != null) {
                //if left and right are both null, curr becomes null here
                curr = curr.right;
            } else {
                //System.out.println(curr.key + " to be deleted...");
                curr.key = null;
                curr.value = null;
                curr.height = -1;
                //System.out.println("height: " + curr.height);
            }
        }
    }

    /** Search subtree recursively and remove its smallest value.
     * @param curr the subtree
     * @return AVLNode with the min key
     */
    public AVLNode removeMin(AVLNode curr) {
        if (curr.left.isLeaf()) {
            curr.key = null;
            curr.value = null;
            curr.height = -1;
            return null;
        }

        return this.removeMin(curr.left);
    }


    //==========================================================================
    // Node Balancing and Rotation
    //==========================================================================

    /** Rebalances tree by checking left and right heaviness of
     * the root and its left and right subtrees.
     * @param curr the root of subtree to balance
     * @return balanced subtree root AVLNode
     */
    public AVLNode rebalance(AVLNode curr) {

        if (curr.bf() < -1) { //curr is right heavy
            if (curr.right.bf() > 0) {
                //if curr's right subtree is left heavy
                //System.out.println("doubleLR on " + curr.key);
                return this.doubleLR(curr);
            } else {
                //curr's right subtree is not left heavy
                //System.out.println("singleLeft on " + curr.key);
                return this.singleLeft(curr);
            }
        } else if (curr.bf() > 1) { //curr is left heavy
            if (curr.left.bf() < 0) {
                //curr's left subtree is right heavy
                //System.out.println("doubleRL on " + curr.key);
                return this.doubleRL(curr);
            } else {
                //curr's left subtree is not right heavy
                //System.out.println("singleRight on " + curr.key);
                return this.singleRight(curr);
            }
            //} else {
            //    System.out.println("No rebalance on " + curr.key);
        }

        //if tree is balanced return itself
        return curr;
    }

    /** Perform left rotation on an AVLNode.
     * @param curr AVLNode to left rotate
     * @return left rotated AVLNode
     */
    public AVLNode singleLeft(AVLNode curr) {
        AVLNode subroot = curr.right;
        curr.right = subroot.left;
        subroot.left = curr;
        curr.determineNewHeight();
        subroot.determineNewHeight();
        return subroot;
    }

    /** Perform right rotation on an AVLNode.
     * @param curr AVLNode to right rotate
     * @return right rotated AVLNode
     */
    public AVLNode singleRight(AVLNode curr) {
        AVLNode subroot = curr.left;
        curr.left = subroot.right;
        subroot.right = curr;
        curr.determineNewHeight();
        subroot.determineNewHeight();
        return subroot;
    }

    /** Perform double left (left-right) rotation on an AVLNode.
     * @param curr AVLNode to double left rotate
     * @return left-right rotated AVLNode
     */
    public AVLNode doubleLR(AVLNode curr) {
        curr.right = this.singleRight(curr.right);
        return this.singleLeft(curr);
    }

    /** Perform double right (right-left) rotation on an AVLNode.
     * @param curr AVLNode to double right rotate
     * @return right-left rotated AVLNode
     */
    public AVLNode doubleRL(AVLNode curr) {
        curr.left = this.singleLeft(curr.left);
        return this.singleRight(curr);
    }

    /** Checks if a binary tree is balanced.
     * Source: http://www.algoqueue.com/algoqueue/default/
     * view/8912896/check-binary-tree-balanced-or-not
     * @param curr root of subtree to check balance
     * @return true if balanced, false if not
     */
    public boolean checkBinaryTreeIsBalanced(AVLNode curr) {
        return !(this.computeAndCheckHeight(curr) == -1);
    }

    /** Computes and checks the height of a node.
     * @param curr node to check height
     * @return 0 if tree is empty, -1 if not balanced, else
     * height of node
     */
    public int computeAndCheckHeight(AVLNode curr) {
        /* Base case - Tree is empty */
        if (curr.key == null) {
            return 0; 
        }
        /* Height of left subtree */
        int leftSubTreeHeight = this.computeAndCheckHeight(curr.left);
        /* Left subtree is not balanced */
        if (leftSubTreeHeight == -1) {
            return -1; 
        }

        /* Height of right subtree */
        int rightSubTreeHeight = this.computeAndCheckHeight(curr.right);
        /* Right subtree is not balanced */
        if (rightSubTreeHeight == -1) {
            return -1;
        }

        /* Difference in height */
        int heightDifference = Math.abs(leftSubTreeHeight - rightSubTreeHeight);
        /* curr node is not balanced */
        if (heightDifference > 1) {
            return -1;
        } else {
            // Height of the root (curr) node
            return Math.max(leftSubTreeHeight, rightSubTreeHeight) + 1;
        }            
    }

    /** Check if entire tree is balanced.
     * @return true if balanced, false if not
     */
    public boolean isBalanced() {
        return this.checkBinaryTreeIsBalanced(this.root);
    }


    //==========================================================================
    // Set and Iterator Functions
    //==========================================================================

    @Override()
    public Set<Map.Entry<K, V>> entries() {
        HashSet<Map.Entry<K, V>> entries = new HashSet<>();

        for (Map.Entry<K, V> entry : this.inOrder(this.root)) {
            entries.add(entry);
        }
        return entries;
    }

    @Override()
    public Set<K> keys() {
        HashSet<K> keys = new HashSet<>();

        for (Map.Entry<K, V> entry : this.inOrder(this.root)) {
            keys.add(entry.getKey());
        }
        return keys;

    }

    @Override()
    public Collection<V> values() {
        LinkedList<V> values = new LinkedList<>();

        for (Map.Entry<K, V> entry : this.inOrder(this.root)) {
            values.add(entry.getValue());
        }
        return values;
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
    private Collection<Map.Entry<K, V>> inOrder(AVLNode curr) {
        LinkedList<Map.Entry<K, V>> ordered = new LinkedList<Map.Entry<K, V>>();

        if (curr.isLeaf()) {
            return ordered;
        }

        //handle left subtree
        ordered.addAll(this.inOrder(curr.left));

        //handle current "root"
        Map.Entry<K, V> entry = 
                new AbstractMap.SimpleEntry<>(curr.key, curr.value);
        ordered.add(entry);

        //handle right subtree
        ordered.addAll(this.inOrder(curr.right));

        return ordered;
    }

    /** Returns a copy of the portion of this map whose keys are in a range.
     *  @param fromKey the starting key of the range, inclusive if found
     *  @param toKey the ending key of the range, inclusive if found
     *  @return the resulting submap
     */
    public AVLMap<K, V> subMap(K fromKey, K toKey) {

        AVLMap<K, V> sub = new AVLMap<K, V>();

        Collection<Map.Entry<K, V>> orderedMap =
                (Collection<Entry<K, V>>) this.inOrder();

        for (Map.Entry<K, V> item : orderedMap) {
            if (item.getKey().compareTo(fromKey) >= 0
                    && item.getKey().compareTo(toKey) <= 0) {
                sub.put(item.getKey(), item.getValue());
            }
        }
        return sub;
    }


    //==========================================================================
    // Override Iterable
    //==========================================================================

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new AVLMapIterator();
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


    //==========================================================================
    // AVLMapIterator Inner Class
    //==========================================================================

    /**
     * Inner AVLMapIterator class for convenience.
     * Note that the generic type is implied since we are
     * within Map.Entry<K, V>.
     */
    public class AVLMapIterator implements Iterator<Map.Entry<K, V>> {

        /** Ordered ArrayList of keys to iterate on. */
        ArrayList<Map.Entry<K, V>> ordered;
        /** Current position in the ArrayList. */
        int pos = -1;
        /** Number of operations applied to map at init,
         * used for state check when doing next or remove. */
        int operationsAtInit;
        /** True if the iterator has just removed the last item it
         * iterated over. Used to filter out calling remove more than once. */
        boolean justRemoved;


        /**
         * Make a AVLMapIterator.
         */
        public AVLMapIterator() {
            this.ordered = (new ArrayList<Map.Entry<K, V>>(
                    (Collection<Map.Entry<K, V>>) AVLMap.this.inOrder()));
            this.operationsAtInit = AVLMap.this.operations;
            this.justRemoved = false;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.ordered.size() - 1;
        }

        @Override
        public Map.Entry<K, V> next() {
            //check if map has been changed
            if (this.operationsAtInit != AVLMap.this.operations) {
                throw new ConcurrentModificationException();
            }
            this.justRemoved = false;
            this.pos++;
            return this.ordered.get(this.pos);
        }

        @Override
        public void remove() {

            if (!this.justRemoved) {
                if (this.operationsAtInit != AVLMap.this.operations) {
                    throw new ConcurrentModificationException();
                }
                AVLMap.this.remove(this.ordered.get(this.pos).getKey());
                this.ordered.remove(this.pos);
                this.operationsAtInit++; 
                this.justRemoved = true;
            } else {
                throw new IllegalStateException();
            }

        }
    }


    //==========================================================================
    // Tree Printing
    //==========================================================================

    /** Prints a tree visually in the output. Spacing isn't perfect
     * but sentinels are included.
     */
    public void printTree() {
        ArrayList<AVLNode> ra = new ArrayList<AVLNode>();
        ra.add(this.root);
        this.printLevel(ra);
        System.out.println("total height: " + this.root.height);
    }

    /** Breadth-first traversal of tree given an initial ArrayList
     * containing nodes on a particular level of the tree. toString()
     * calls this function with a new ArrayList only containing the
     * map's root. Spacing of output is not perfect.
     * @param n ArrayList of root nodes
     */
    void printLevel(ArrayList<AVLNode> n) {
        ArrayList<AVLNode> next = new ArrayList<AVLNode>();  
        for (int i = 0; i < n.get(0).height * 2 * ((1 + 1) * 2 + 1)
                + (1 + 1 + 1) * (this.root.height - n.get(0).height); i++) {
            System.out.print(" ");
        }

        for (AVLNode t: n) {
            if (n.get(0).height < this.root.height
                    && n.indexOf(t) % 2 == 0) {
                System.out.print("  ");
                for (int i = 0; i < n.get(0).height
                        + (1 + 1 + 1) * (n.get(0).height); i++) {
                    System.out.print(" ");
                }
            }

            System.out.print(t.toString() + " ");
            if (t.left != null) {
                next.add(t.left);
            }
            if (t.right != null) {
                next.add(t.right);
            }
        }
        System.out.println();
        if (next.size() != 0) {
            this.printLevel(next);
        }
    }

}