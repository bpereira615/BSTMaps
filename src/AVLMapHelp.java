import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.lang.Math;

/** Balanced Binary Search Tree Map implementation with inner Node class.
 * @author BaDoi Phan bphan1 badoi@jhu.edu
 * @author Lydia Carroll lcarro12 lcarro12@jhu.edu
 * March 29th, 2015
 * @param <K> the base type of the keys in the entries
 * @param <V> the base type of the values
 */
public class AVLMapHelp<K extends Comparable<? super K>, V> 
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {
    
    /** Inner node class.  
     * Do not make this static because you want 
     * the K to be the same K as in the AVLMap header.
     */
    private class BNode implements Map.Entry<K, V> {

        /** The left child of this node. */
        private BNode left;
        /** The right child of this node. */
        private BNode right;
        /** The left child of this node. */
        private V value;
        /** The right child of this node. */
        private K key;
        /** The right child of this node. */
        private int height;

        /** Create a new node with a particular key and value.
         *  @param k the key for the new node
         *  @param v the value for the new node
         */
        BNode(K k, V v) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
            this.height = -1;
        }
        
        /** changes the height of the node.
         */
        private void setHeight() {
            this.height = Math.max(this.right.height, this.left.height) + 1;
        }

        /*
        Returns height of given node
         */
        private int getHeight() {
            return this.height;
        }
        
        /** computes the balance factor of the node with another node.
         * @return the balance factor
         */
        private int bf() {
            if (this.isLeaf()) {
                return 0;
            }
            return this.left.height - this.right.height;
        }
        
        /**
         * Set Key method.
         * @param k the new key
         */
        private void setKey(K k) {
            this.key = k;
        }
        
        /** Check whether this node is a leaf sentinel, based on key.
         *  @return true if leaf, false otherwise
         */
        public boolean isLeaf() {
            return this.getKey() == null;  
            // this is a sentinel-based implementation
        }
        
        /**
         * toString method for BNode.
         * @return the string
         */
        public String toString() {
            return this.getKey().toString() + "=" + this.getValue().toString();
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v) {
            this.value = v;
            return this.value;
        }
    }

    /** the large factor. */
    private static final int BF = 1;
    /** The root of this tree. */
    private BNode root;
    /** The number of entries in this map (== non-sentinel nodes). */
    private int size;

    /** Create an empty tree with a sentinel root node.
     */
    public AVLMapHelp() {
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
        // empty tree is a sentinel for the root
        this.root = new BNode(null, null);
        this.size = 0; 
    }

    @Override()
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override()
    public boolean hasKey(K key) {
        return this.hasKey(key, this.root);
    }

    /**
     * Gives balance of root of tree
     * @return balance factor for root
     */
    public int rootBalance() {
        return getBalance(this.root);
    }

    public int getBalance(BNode curr) {
        return curr.bf();
    }
    
    /**
    * Determines if the current node has the key.
    * @param key the key to look for
    * @param curr the current node
    * @return the node
    */
    public boolean hasKey(K key, BNode curr) {
        if (curr.isLeaf()) {
            return false;
        }
        if (curr.getKey().compareTo(key) > 0) {
            return this.hasKey(key, curr.left);
        } else if (curr.getKey().compareTo(key) == 0) {
            return true;
        } else if (curr.getKey().compareTo(key) < 0) {
            return this.hasKey(key, curr.right);
        }
        return false;
    }

    @Override() 
    public boolean hasValue(V value) {
        // get a list of everything in order by value
        Collection<V> flatBST = this.values();
        return flatBST.contains(value);
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
        if (curr.isLeaf()) { // empty tree
            return null;
        }
        if (curr.getKey().compareTo(key) > 0) {
            return this.get(key, curr.left);
        } else if (curr.getKey().compareTo(key) == 0) {
            return curr.getValue();
        } else {
            return this.get(key, curr.right);
        }
    }

    @Override()
    public V put(K key, V val) {
        V oldVal = this.put(key, val, this.root);
        this.root.setHeight();
        if (Math.abs(this.root.bf()) > BF) {
            this.root = this.balance(this.root);
        } 
        this.root.setHeight();
        return oldVal;
    }

    /** Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return the original value associated with the key, or null if not found
     */
    private V put(K key, V val, BNode curr) {
        //new node made
        V oldVal;
        if (curr.isLeaf()) {
            curr.setKey(key);
            curr.setValue(val);
            curr.right = new BNode(null, null);
            curr.left = new BNode(null, null);
            this.size++;
            curr.setHeight();
            return null;
        }
        
        //choose to go left, right, or replace value
        if (curr.getKey().compareTo(key) > 0) {
            oldVal = this.put(key, val, curr.left);
            curr.setHeight();
            if (Math.abs(curr.left.bf()) > BF) {
                curr.left = this.balance(curr.left);
            }
        } else if (curr.getKey().compareTo(key) < 0) {
            oldVal = this.put(key, val, curr.right);
            curr.setHeight();
            if (Math.abs(curr.right.bf()) > BF) {
                curr.right = this.balance(curr.right);
            }
        } else { // found key 
            oldVal = curr.getValue();
            curr.setValue(val);
        }
        curr.setHeight();
        return oldVal;
    } 
    

    @Override()
    public V remove(K key) {
        // IDEA: rewrite to just call remove(key, node)
        this.size--;
        BNode match = this.find(key, this.root);
        if (match == null) {
            return null;
        }
        this.root = this.remove(key, this.root);
        this.root.setHeight(); // throwing an error in rebalancing test
        if (Math.abs(this.root.bf()) > BF) {
            this.root = this.balance(this.root);
        } 
        this.root.setHeight();
        return match.getValue();
    }

    /** Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the value associated with the removed key, or null if not found
     */
    private BNode remove(K key, BNode curr) {  
    // this does not decrement size right now      
        if (curr.isLeaf()) {
            return null;
        }
        if (curr.getKey().compareTo(key) > 0) {
            curr.left = this.remove(key, curr.left);
            curr.setHeight();
            if (Math.abs(curr.left.bf()) > BF) {
                curr.left = this.balance(curr.left);
            }        
            curr.setHeight();
        } else if (curr.getKey().compareTo(key) < 0) {
            curr.right = this.remove(key, curr.right);
            curr.setHeight();
            if (Math.abs(curr.right.bf()) > BF) {
                curr.right = this.balance(curr.right);
            }        
            curr.setHeight();
        } else { // found it
            if (curr.left.isLeaf()) {
                // do you want this to replace curr in tree?
                // curr is correct value to remove if you're in this case
                // not curr.right
                return curr.right; 
            } else if (curr.right.isLeaf()) {
                return curr.left;
            } else { // if node has 2 children
                BNode tmp2 = this.getmin(curr.right);
                curr.setValue(tmp2.getValue());
                curr.setKey(tmp2.getKey());
                curr.right = this.deletemin(curr.right);
            }
        }
        return curr;
    }
    
    /**
     * Delete min node function.
     * @param rt start root
     * @return node before
     */
    private BNode deletemin(BNode rt) {
        if (rt.left.isLeaf()) {
            return rt.right;
        }
        rt.left = this.deletemin(rt.left);
        rt.setHeight();
        if (Math.abs(rt.bf()) > BF) {
            rt = this.balance(rt);
        }
        rt.setHeight();
        return rt;
    }
    
    @Override()
    public Set<Map.Entry<K, V>> entries() {
        HashSet<Map.Entry<K, V>> tmpSet = 
                new HashSet<Map.Entry<K, V>>();        
        for (Map.Entry<K, V> elem : this.inOrder()) {
            tmpSet.add(elem);
        }
        return tmpSet;
    }

    @Override()
    public Set<K> keys() {
        HashSet<K> tmpSet = new HashSet<K>();
        for (Map.Entry<K, V> entry: this.inOrder()) {
            tmpSet.add(entry.getKey());
        }
        return tmpSet;
    }

    @Override()
    public Collection<V> values() {
        ArrayList<V> tmpSet = new ArrayList<V>();
        for (Map.Entry<K, V> entry: this.inOrder(this.root)) {
            tmpSet.add(entry.getValue());
        }
        return tmpSet;
    }
    
    /* -----   AVLMap-specific functions   ----- */
    /** Balance the subtree. 
     * @param curr the current node to balance
     * @return the new node
     */
    private BNode balance(BNode curr) {
        if (curr.bf() > 0) { // left heavy
            if (curr.left.bf() < 0) { // left's right is heavy
                curr.left = this.rotateLeft(curr.left);
            }
            return this.rotateRight(curr);
        } else { // right heavy
            if (curr.right.bf() > 0) { // right's left is heavy
                curr.right = this.rotateRight(curr.right);
            }
            return this.rotateLeft(curr);
        }
    }
    
    /**
     * Method to do a single left rotation.
     * @param curr the root of subtree to rebalance.
     * @return the new root
     */
    private BNode rotateLeft(BNode curr) {
        BNode rt = curr.right;
        curr.right = rt.left;
        rt.left = curr;
        curr.setHeight();
        rt.setHeight();
        return rt;
    }
    
    /**
     * Method to do a single right rotation.
     * @param curr the root of subtree to rebalance.
     * @return the new root
     */
    private BNode rotateRight(BNode curr) {
        BNode rt = curr.left;
        curr.left = rt.right;
        rt.right = curr;
        curr.setHeight();
        rt.setHeight();
        return rt;
    }
    
    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the min key
     */
    public K firstKey(BNode curr) {
        return this.getmin(this.root).getKey();
    }

    /** Get the largest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the max key
     */
    public K lastKey(BNode curr) {
        return this.getmax(this.root).getKey();
    }

    /** Find the (first) node containing this value.
     *  @param key the value for which to search
     *  @param curr the current node
     *  @return the tree whose root has the value, or null if not there
     */
    private BNode find(K key, BNode curr) {
        if (curr.isLeaf()) {
            return null;
        } else {
            int diff = curr.getKey().compareTo(key);
            if (diff > 0) {  // look left
                return this.find(key, curr.left);
            } else if (diff < 0) {  // look right
                return this.find(key, curr.right);
            } else {  // diff == 0, found
                return curr;
            }
        }
    }
    
    /** Inorder traversal that produces an iterator over key-value pairs.
     *  @return an iterable list of entries ordered by keys
     */
    public Iterable<Map.Entry<K, V>> inOrder() {
        return this.inOrder(this.root);
    }
    
    @Override
    public String toString() {
        return this.root.height + " " + this.toString(this.root);
    }
    
    /**
     * prints the submap into a string of (left map) root (right map).
     * @param curr the current node
     * @return the current node's to String
     */
    private String toString(BNode curr) {
        String s = "";
        if (!curr.left.isLeaf()) {
            s = s + this.toString(curr.left) + "/ ";
        }
        s = s + curr.toString() + " " + curr.height;
        if (!curr.right.isLeaf()) {
            s = s + " \\" + this.toString(curr.right);
        }
        return "( " + s + " )";
    }
    
    /** Inorder traversal produces an iterator over entries in a subtree.
     *  @param curr the root of the subtree to iterate over
     *  @return an iterable list of entries ordered by keys
     */
    private Collection<Map.Entry<K, V>> inOrder(BNode curr) {
        ArrayList<Map.Entry<K, V>> ordered = new ArrayList<Map.Entry<K, V>>();
        if (curr.isLeaf()) {
            return ordered;
        }
        if (!curr.left.isLeaf()) {
            ordered.addAll(this.inOrder(curr.left));
        }
        ordered.add(curr);
        if (!curr.right.isLeaf()) {
            ordered.addAll(this.inOrder(curr.right));
        }
        return ordered;
    }
    

    /** Returns a copy of the portion of this map whose keys are in a range.
     *  @param fromKey the starting key of the range, inclusive if found
     *  @param toKey the ending key of the range, inclusive if found
     *  @return the resulting submap
     */
    public AVLMap<K, V> subMap(K fromKey, K toKey) {
        ArrayList<Map.Entry<K, V>> tmpList = 
                (ArrayList<Map.Entry<K, V>>) this.inOrder();
        Iterator<Map.Entry<K, V>> iter = tmpList.iterator();
        BNode tmpNode;
        while (iter.hasNext()) {
            tmpNode = (AVLMap<K, V>.BNode) iter.next();
            if (tmpNode.getKey().compareTo(fromKey) < 0 
                    || tmpNode.getKey().compareTo(toKey) > 0) {
                iter.remove();
            }
        }
        AVLMap<K, V> sub = new AVLMap<K, V>();
        for (Map.Entry<K, V> node: tmpList) {
            sub.put(node.getKey(), node.getValue());
        }
        return sub;
    }
    
    /* ---------- BSTree Helper Methods ------*/         
    /**
     * get the maximum key of the root subtree.
     * @param rt the root to look from
     * @return the max root
     */
    private BNode getmax(BNode rt) {
        if (rt.right.isLeaf()) {
            return rt;
        }
        return this.getmax(rt.right);
    }
    
    /**
     * get the minimum key of the root subtree.
     * @param rt the root to look from
     * @return the min root
     */
    private BNode getmin(BNode rt) {
        if (rt.left.isLeaf()) {
            return rt;
        }
        return this.getmin(rt.left);
    }
    
    /* ---------- from Iterable ---------- */

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

    /* -----  insert the AVLMapIterator inner class here ----- */
    /**
     * This is the AVLMap Iterator iterating through the tree's nodes in order.
     * @author BaDoi Phan
     * @author Lydia Carroll
     *
     */
    private class AVLMapIterator implements Iterator<Map.Entry<K, V>> {
        
        /**
         * this is the iterable flattened AVLMap data structure.
         */
        private Collection<Map.Entry<K, V>> flatBST 
            = (Collection<Map.Entry<K, V>>) AVLMap.this.inOrder();
        
        /**
         * flattened BST iterator.
         */
        private Iterator<Map.Entry<K, V>> iter = this.flatBST.iterator();
        
        /**
         * This is the current iterator position.
         */
        private Map.Entry<K, V> curr;
        
        /**
         * Keeps track if removed next item.
         */
        private boolean canRemove = false;
        
        @Override
        public boolean hasNext() {
            if (this.flatBST.size() != AVLMap.this.size()) {
                throw new java.util.ConcurrentModificationException();
            }
            return this.iter.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {            
            if (this.hasNext()) {
                this.canRemove = true;
                Map.Entry<K, V> tmp = this.iter.next();
                this.curr = new BNode(tmp.getKey(), tmp.getValue());
                return this.curr;
            }
            return null;
        }
        
        @Override
        public void remove() {
            return;
            // if (this.flatBST.size() != AVLMap.this.size()) {
            //     throw new java.util.ConcurrentModificationException();
            // }
            // if (this.canRemove) {
            //     this.iter.remove();
            //     this.canRemove = false;
            //     // LMC just added this line 4/3
            //     // does this fix our iterator problem?
            //     AVLMap.this.remove(this.curr.getKey());
            // }
        }
        
    }
    
    public static void main(String[] args) {
        AVLMapHelp<Integer, String> myBeautifulMap = new AVLMapHelp<Integer, String>();
         System.out.println("Left -------------");
         myBeautifulMap.put(2, "k");
         System.out.println("inserted 2");
         myBeautifulMap.put(1, "k");
         System.out.println("inserted 1");
         myBeautifulMap.put(5, "k");
         System.out.println("inserted 5");
         System.out.println("root: " + myBeautifulMap.root.key);
         System.out.println("root left: " + myBeautifulMap.root.left.key);
         System.out.println("root right: " + myBeautifulMap.root.right.key);
         myBeautifulMap.put(4, "k");
         System.out.println("inserted 4");
         System.out.println("root: " + myBeautifulMap.root.key);
         System.out.println("root left: " + myBeautifulMap.root.left.key);
         System.out.println("root right: " + myBeautifulMap.root.right.key);
         myBeautifulMap.put(3, "k");
         System.out.println("inserted 3");
         System.out.println("root: " + myBeautifulMap.root.key);
         System.out.println("root left: " + myBeautifulMap.root.left.key);
         System.out.println("root right: " + myBeautifulMap.root.right.key);
        
        // // // // System.out.println("2: " + myBeautifulMap.get(2));
        // // // // System.out.println("1: " + myBeautifulMap.get(1));
        // // // // System.out.println("5: " + myBeautifulMap.get(5));
        // // // // System.out.println("4: " + myBeautifulMap.get(4));
        // // // // System.out.println("3: " + myBeautifulMap.get(3));
         System.out.println("root: " + myBeautifulMap.root.key);
         System.out.println("root left: " + myBeautifulMap.root.left.key);
         System.out.println("root right: " + myBeautifulMap.root.right.key);
         System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
         System.out.println("root right right: " + myBeautifulMap.root.right.right.key);
    }
    
}
