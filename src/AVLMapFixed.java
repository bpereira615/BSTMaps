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
 * Project 3, Part A - Binary Search Tree
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
public class AVLMapFixed<K extends Comparable<? super K>, V>
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {

    /** Inner node class.  Do not make this static because you want
        the K to be the same K as in the AVLMapFixed header.
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
         *  @param key the key for the new node
         *  @param val the value for the new node
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
            return this.left.height - this.right.height;
        }

        /** Check whether this node is a leaf sentinel, based on key.
         *  @return true if leaf, false otherwise
         */
        public boolean isLeaf() {
            return this.key == null;  // this is a sentinel-based implementation
        }
    }

    /** The root of this tree. */
    protected AVLNode root;
    /** The number of entries in this map (== non-sentinel nodes). */
    protected int size;
    /** Keeps track of state for iterator, incremented
     * when put or remove is called. */
    protected int operations;

    /** Create an empty tree with a sentinel root node.
     */
    public AVLMapFixed() {
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
        
        this.operations++;
        
        return this.put(key, val, (AVLNode) this.root);
    }

    /** Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return the original value associated with the key, or null if not found
     */
    private V put(K key, V val, AVLNode curr) {
        
            if (curr.isLeaf()) {
                //in correct position, insert node
                curr.key = key;
                curr.value = val;
                curr.left = new AVLNode(null, null);
                curr.right = new AVLNode(null, null);
                curr.height = determineNewHeight(curr);
                return null;
            }

            //in order search for position
            int diff = key.compareTo(curr.key);
            
            //if key is smaller than root key, search left subtree
            if (diff < 0) {
                V value = this.put(key, val, curr.left);
                // curr = this.rebalance(curr);
                AVLNode newNode = this.rebalance(curr);
                curr.left = newNode.left;
                curr.right = newNode.right;
                curr.key = newNode.key;
                curr.value = newNode.value;
                curr.height = determineNewHeight(curr);
                this.root = this.rebalance(root);
                return value;
            } else if (diff == 0) { //node found with key, update and return old
                V oldVal = curr.value;
                curr.key = key;
                curr.value = val;
                return oldVal;
            } else { //if key is larger than root key, search right subtree
                V value = this.put(key, val, curr.right);
                // curr = this.rebalance(curr);
                AVLNode newNode = this.rebalance(curr);
                curr.left = newNode.left;
                curr.right = newNode.right;
                curr.key = newNode.key;
                curr.value = newNode.value;
                curr.height = determineNewHeight(curr);
                this.root = this.rebalance(root);
                return value;
            }
    }
    
    /** Remove the entry associated with a key.
     *  @param key the key for the entry being deleted
     *  @return the value associated with the key, or null if key not there
     */
    public V remove(K key) {

        //throw exception if key is null
        if (key == null) {
            throw new NullPointerException();
        }

        if (!this.hasKey(key)) {
            return null;
        }
        
        //decrement size
        this.size--;
        this.operations++;
        return this.remove(key, (AVLNode) this.root);
    }

    /** Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the value associated with the removed key, or null if not found
     */
    public V remove(K key, AVLNode curr) {

        //in order search for position
        int diff = key.compareTo(curr.key);
        
        if (curr.isLeaf() && diff != 0) {
            return null;
        }
        
        //if key is smaller than root key, search left subtree
        if (diff < 0) {
            
            return this.remove(key, curr.left);
            
        } else if (diff > 0) {
            
            //if key is larger than root key, search right subtree
            return this.remove(key, curr.right);
            
        } else if (curr.left.key != null && curr.right.key != null) {
            
            //has two non-sentinel children
                
            V val = curr.value;
            K firstKey = this.firstKey(curr.right);

            //deleting root edge case
            if (curr.key == this.root.key) {

                this.root.value = this.get(firstKey);
                
                this.removeMin(curr.right);
                
                this.root.key = firstKey;

            } else {
                
                curr.value = this.get(firstKey);
                
                this.removeMin(curr.right);
                
                curr.key = firstKey;

            }
            
            return val;
            
        } else { //has 0 or 1 non-sentinel children

            V val = curr.value;

            this.removeIncompleteSubTree(curr);

            return val;
        }

    }
    
    /** Rebalances tree by checking left and right heaviness of
     * the root and its left and right subtrees.
     * @param root the root of subtree to balance
     * @return balanced subtree root AVLNode
     */
    public AVLNode rebalance(AVLNode root) {
        
        //check if tree is right heavy
        if (root.bf() < -1) {
            if (root.right.bf() > 1) {
                //if root's right subtree is left heavy
                System.out.println("doubleLR");
                return doubleLR(root);
            } else {
                //root's right subtree is not left heavy
                System.out.println("singleLeft");
                return singleLeft(root);
            }
        } else if (root.bf() > 1) { //root is left heavy
            if (root.left.bf() < -1) {
                System.out.println("doubleRL");
                //root's left subtree is right heavy
                return doubleRL(root);
            } else {
                //root's left subtree is not right heavy
                System.out.println("singleRight");
                return singleRight(root);
            }
        }
        
        //if tree is balanced return itself
        return root;
        
    }
    
    /** Perform left rotation on an AVLNode.
     * @param curr AVLNode to left rotate
     * @return left rotated AVLNode
     */
    public AVLNode singleLeft(AVLNode curr) {
        AVLNode root = curr.right;
        AVLNode replacement = new AVLNode(curr.key, curr.value);
        replacement.left = curr.left;
        replacement.right = root.left;



        // AVLNode a = new AVLNode(root.key, root.value);
        // a.left = root.left;
        // a.right = root.right;

        // replacement.right = a;

        curr.right = root.left;
        root.left = replacement;
        curr.height = determineNewHeight(curr);
        root.height = determineNewHeight(root);
        return root;
    }
    
    /** Perform right rotation on an AVLNode.
     * @param curr AVLNode to right rotate
     * @return right rotated AVLNode
     */
    public AVLNode singleRight(AVLNode curr) {
        AVLNode root = curr.left;
        AVLNode replacement = new AVLNode(curr.key, curr.value);
        replacement.left = root.right;
        replacement.right = curr.right;

        curr.left = root.right;
        root.right = replacement;
        curr.height = determineNewHeight(curr);
        root.height = determineNewHeight(root);
        return root;
    }

    // /** Perform left rotation on an AVLNode.
    //  * @param curr AVLNode to left rotate
    //  * @return left rotated AVLNode
    //  */
    // public AVLNode singleRight(AVLNode curr) {
    //     AVLNode newRight = curr;
    //     AVLNode newRoot =   
    //     curr.height = determineNewHeight(curr);
    //     root.height = determineNewHeight(root);
    //     return root;
    // }
    
    // * Perform right rotation on an AVLNode.
    //  * @param curr AVLNode to right rotate
    //  * @return right rotated AVLNode
     
    // public AVLNode singleLeft(AVLNode curr) {
    //     AVLNode root = curr.left;
    //     curr.left = root.right;
    //     root.right = curr;
    //     curr.height = determineNewHeight(curr);
    //     root.height = determineNewHeight(root);
    //     return root;
    // }

    /** Perform double left (left-right) rotation on an AVLNode.
     * @param curr AVLNode to double left rotate
     * @return left-right rotated AVLNode
     */
    public AVLNode doubleLR(AVLNode curr) {
        curr.right = singleRight(curr.right);
        return singleLeft(curr);
    }
    
    /** Perform double right (right-left) rotation on an AVLNode.
     * @param curr AVLNode to double right rotate
     * @return right-left rotated AVLNode
     */
    public AVLNode doubleRL(AVLNode curr) {
        curr.left = singleLeft(curr.left);
        return singleRight(curr);
    }

    private int determineNewHeight(AVLNode curr) {
        int noo = Math.max(curr.left.height, curr.right.height) + 1;
        System.out.printf("%s: %d\n",curr.key,noo);
        return noo;
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
            }
        } else {
            if (curr.left.key != null) {
                curr = curr.left;
            } else if (curr.right.key != null) {
                //if left and right are both null, curr becomes null here
                curr = curr.right;
            } else {
                curr.key = null;
                curr.value = null;
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
            return null;
        }

        return this.removeMin(curr.left);
    }
    
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

    /* -----   AVLMapFixed-specific functions   ----- */

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the min key
     */
    public K firstKey(AVLNode curr) {
        if (curr.left.isLeaf()) {
            return curr.key;
        }

        return this.firstKey(curr.left);
    }

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the max key
     */
    public K lastKey(AVLNode curr) {
        if (curr.right.isLeaf()) {
            return curr.key;
        }

        return this.lastKey(curr.right);
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
    public AVLMapFixed<K, V> subMap(K fromKey, K toKey) {
        
        AVLMapFixed<K, V> sub = new AVLMapFixed<K, V>();
        
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

    /* ---------- from Iterable ---------- */

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new AVLMapFixedIterator();
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

    /* -----  insert the AVLMapFixedIterator inner class here ----- */

    /**
     * Inner AVLMapFixedIterator class for convenience.
     * Note that the generic type is implied since we are
     * within Map.Entry<K, V>.
     */
    public class AVLMapFixedIterator implements Iterator<Map.Entry<K, V>> {

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
         * Make a AVLMapFixedIterator.
         */
        public AVLMapFixedIterator() {
            this.ordered = (new ArrayList<Map.Entry<K, V>>(
                    (Collection<Map.Entry<K, V>>) AVLMapFixed.this.inOrder()));
            this.operationsAtInit = AVLMapFixed.this.operations;
            this.justRemoved = false;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.ordered.size() - 1;
        }

        @Override
        public Map.Entry<K, V> next() {
            //check if map has been changed
            if (this.operationsAtInit != AVLMapFixed.this.operations) {
                throw new ConcurrentModificationException();
            }
            this.justRemoved = false;
            this.pos++;
            return this.ordered.get(this.pos);
        }

        @Override
        public void remove() {
            
            if (!this.justRemoved) {
                if (this.operationsAtInit != AVLMapFixed.this.operations) {
                    throw new ConcurrentModificationException();
                }
                AVLMapFixed.this.remove(this.ordered.get(this.pos).getKey());
                this.ordered.remove(this.pos);
                this.operationsAtInit++; 
                this.justRemoved = true;
            } else {
                throw new IllegalStateException();
            }
            
        }
    }
    
    public static void main(String[] args) {
        AVLMapFixed<Integer, String> myBeautifulMap = new AVLMapFixed<Integer, String>();
        // System.out.println("Left -------------");
        // myBeautifulMap.put(2, "k");
        // System.out.println("inserted 2");
        // myBeautifulMap.put(1, "k");
        // System.out.println("inserted 1");
        // myBeautifulMap.put(5, "k");
        // System.out.println("inserted 5");
        // myBeautifulMap.put(4, "k");
        // System.out.println("inserted 4");
        // myBeautifulMap.put(3, "k");
        // System.out.println("inserted 3");
        
        // // // // System.out.println("2: " + myBeautifulMap.get(2));
        // // // // System.out.println("1: " + myBeautifulMap.get(1));
        // // // // System.out.println("5: " + myBeautifulMap.get(5));
        // // // // System.out.println("4: " + myBeautifulMap.get(4));
        // // // // System.out.println("3: " + myBeautifulMap.get(3));
        // System.out.println("root: " + myBeautifulMap.root.key);
        // System.out.println("root left: " + myBeautifulMap.root.left.key);
        // System.out.println("root right: " + myBeautifulMap.root.right.key);
        // System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
        // System.out.println("root right right: " + myBeautifulMap.root.right.right.key);

        // myBeautifulMap = new AVLMapFixed<Integer, String>();
        // System.out.println("Right -------------");
        // myBeautifulMap.put(4, "k");
        // System.out.println("inserted 4");
        // myBeautifulMap.put(3, "k");
        // System.out.println("inserted 3");
        // myBeautifulMap.put(5, "k");
        // System.out.println("inserted 5");
        // myBeautifulMap.put(6, "k");
        // System.out.println("inserted 6");
        // myBeautifulMap.put(7, "k");
        // System.out.println("inserted 7");
        // System.out.println("root: " + myBeautifulMap.root.key);
        // System.out.println("root left: " + myBeautifulMap.root.left.key);
        // System.out.println("root right: " + myBeautifulMap.root.right.key);
        // System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
        // System.out.println("root right right: " + myBeautifulMap.root.right.right.key);
        // myBeautifulMap.put(8, "k");
        // System.out.println("inserted 8");
        // System.out.println("root: " + myBeautifulMap.root.key);
        // System.out.println("root left: " + myBeautifulMap.root.left.key);
        // System.out.println("root right: " + myBeautifulMap.root.right.key);
        // System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
        // System.out.println("root right left left: " + myBeautifulMap.root.right.left.left.key);
        // System.out.println("root right right: " + myBeautifulMap.root.right.right.key);

        myBeautifulMap = new AVLMapFixed<Integer, String>();
        System.out.println("Right -------------");
        myBeautifulMap.put(10, "k");
        System.out.println("inserted 10");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.put(20, "k");
        System.out.println("inserted 20");
        myBeautifulMap.put(3, "k");
        System.out.println("inserted 3");
        myBeautifulMap.put(7, "k");
        System.out.println("inserted 7");
                myBeautifulMap.put(12, "k");
        System.out.println("inserted 12");
        myBeautifulMap.put(25, "k");
        System.out.println("inserted 25");
        myBeautifulMap.put(23, "k");
        System.out.println("inserted 23");
        myBeautifulMap.put(28, "k");
        System.out.println("inserted 28");
        myBeautifulMap.put(30, "k");
        System.out.println("inserted 30");
        System.out.println("root: " + myBeautifulMap.root.key);
        System.out.println("root left: " + myBeautifulMap.root.left.key);
                System.out.println("root left left: " + myBeautifulMap.root.left.left.key);
                        System.out.println("root left right: " + myBeautifulMap.root.left.right.key);
        System.out.println("root right: " + myBeautifulMap.root.right.key);
        System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
        System.out.println("root right right: " + myBeautifulMap.root.right.right.key);
                System.out.println("root right left left: " + myBeautifulMap.root.right.left.left.key);
        System.out.println("root right left right: " + myBeautifulMap.root.right.left.right.key);
        System.out.println("root right right left: " + myBeautifulMap.root.right.right.left.key);
        System.out.println("root right right right: " + myBeautifulMap.root.right.right.right.key);
        System.out.println("root right right right right: " + myBeautifulMap.root.right.right.right.right.key);
    }
}
