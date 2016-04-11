import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.ConcurrentModificationException;

/** Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class BSTMap<K extends Comparable<? super K>, V>
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {

    /** Inner node class.  Do not make this static because you want
        the K to be the same K as in the BSTMap header.
    */
    protected class BNode {

        /** The key of the entry (null if sentinel node). */
        protected K key;
        /** The value of the entry (null if sentinel node). */
        protected V value;
        /** The left child of this node. */
        protected BNode left;
        /** The right child of this node. */
        protected BNode right;

        /** Create a new node with a particular key and value.
         *  @param k the key for the new node
         *  @param v the value for the new node
         */
        BNode(K k, V v) {
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
    /** The state of the map. */
    private int state;

    /** Create an empty tree with a sentinel root node.
     */
    public BSTMap() {
        this.state = -1;
        this.clear();
    }

    @Override()
    public int size() {
        return this.size;
    }

    @Override()
    public void clear() {
        this.root = new BNode(null, null);
        this.size = 0;
        this.state++;
    }

    @Override()
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /** Find a key in a particular subtree.
     *  @param key the key to find
     *  @param curr the root of the tree to search
     *  @return the node containing the key, or a sentinel leaf if not found
     */
    private BNode findKey(K key, BNode curr) {
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
    public boolean hasKey(K key) {
        return this.hasKey(key, this.root);
    }

    /** See if a key is in an entry in a subtree.
     *  @param key the key to search for
     *  @param curr the root of the subtree being searched
     *  @return true if found, false otherwise
     */
    public boolean hasKey(K key, BNode curr) {
        return !this.findKey(key, curr).isLeaf();
    }

    @Override()
    public boolean hasValue(V value) {
        return this.values().contains(value);  // slow, but whatever
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
        return this.findKey(key, curr).value;
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
        if (curr.isLeaf()) {
            curr.key = key;
            curr.value = val;
            curr.left = new BNode(null, null);
            curr.right = new BNode(null, null);
            this.state++;
            this.size++;
            return null;
        }
        int comparison = key.compareTo(curr.key);
        if (comparison < 0) {
            return this.put(key, val, curr.left);
        } else if (comparison > 0) {
            return this.put(key, val, curr.right);
        } else {
            V old = curr.value;
            curr.value = val;
            return old;
        }
    }

    @Override()
    public V remove(K key) {
        BNode toDelete = this.findKey(key, this.root);
        if (toDelete.isLeaf()) {  // key not found
            return null;
        } else {
            V val = toDelete.value;
            this.size--;
            this.state++;
            this.root = this.remove(key, this.root);
            return val;
        }
    }

    /** Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the root of the new subtree
     */
    public BNode remove(K key, BNode curr) {        
        if (curr.isLeaf()) {
            return curr;
        }
        int comparison = key.compareTo(curr.key);
        if (comparison < 0) {
            curr.left = this.remove(key, curr.left);
        } else if (comparison > 0) {
            curr.right = this.remove(key, curr.right);
        } else {  // remove curr node
            if (curr.left.isLeaf()) { // replace with right
                curr = curr.right;
            } else if (curr.right.isLeaf()) { // replace with left
                curr = curr.left;
            } else {
                // replace entry with min in right subtree
                BNode min = curr.right;
                while (!min.left.isLeaf()) {
                    min = min.left;
                }
                curr.key = min.key;
                curr.value = min.value;
                // remove actual node with min recursively
                curr.right = this.remove(min.key, curr.right);
            }
        }
        return curr;  // this is essential!
    }
    
    
    @Override()
        public Set<Map.Entry<K, V>> entries() {
        return new HashSet<Map.Entry<K, V>>(this.inOrder(this.root));
    }

    @Override()
        public Set<K> keys() {
        Set<K> toReturn = new HashSet<>();
        for (Map.Entry<K, V> entry : this.inOrder()) {
            toReturn.add(entry.getKey());
        }
        return toReturn;
    }

    @Override()
        public Collection<V> values() {
        Collection<V> toReturn = new LinkedList<>();
        for (Map.Entry<K, V> entry : this.inOrder()) {
            toReturn.add(entry.getValue());
        }
        return toReturn;
    }

    /* -----   BSTMap-specific functions   ----- */

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the min key
     */
    public K firstKey(BNode curr) {
        if (curr == null || curr.isLeaf()) {
            return null;
        }
        if (curr.left.isLeaf()) {
            return curr.key;
        } else {
            return this.firstKey(curr.left);
        }
    }

    /** Get the largest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the max key
     */
    public K lastKey(BNode curr) {
        if (curr == null || curr.isLeaf()) {
            return null;
        }
        if (curr.right.isLeaf()) {
            return curr.key;
        } else {
            return this.lastKey(curr.right);
        }
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
        Collection<Map.Entry<K, V>> toReturn = new LinkedList<>();
        if (curr.isLeaf()) {
            return toReturn;
        }
        toReturn.addAll(this.inOrder(curr.left));
        toReturn.add(new AbstractMap.SimpleEntry(curr.key, curr.value));
        toReturn.addAll(this.inOrder(curr.right));
        return toReturn;
    }

    /** Returns a copy of the portion of this map whose keys are in a range.
     *  @param fromKey the starting key of the range, inclusive if found
     *  @param toKey the ending key of the range, inclusive if found
     *  @return the resulting submap
     */
    public BSTMap<K, V> subMap(K fromKey, K toKey) {
        BSTMap<K, V> toReturn = new BSTMap<>();
        for (Map.Entry<K, V> entry : this.inOrder()) {
            if (entry.getKey().compareTo(fromKey) >= 0
                && entry.getKey().compareTo(toKey) <= 0) {
                toReturn.put(entry.getKey(), entry.getValue());
            }
        }
        return toReturn;
    }


    /* ---------- from Iterable ---------- */

    private class BSTMapIterator implements Iterator<Map.Entry<K, V>> {
        private Iterator<Map.Entry<K, V>> iter;
        private int state;
        
        public BSTMapIterator() {
            this.iter = BSTMap.this.inOrder().iterator();
            this.state = BSTMap.this.state;
        }
        
        public Map.Entry<K, V> next() {
            if (BSTMap.this.state != this.state) {
                throw new ConcurrentModificationException();
            }
            return this.iter.next();
        }
        
        public boolean hasNext() {
            if (BSTMap.this.state != this.state) {
                throw new ConcurrentModificationException();
            }
            return this.iter.hasNext();
        }
        
        public void remove() {
            return;
        }
    }
    
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.inOrder().iterator();
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
    
    public static <PK extends Comparable<? super PK>, PV> void
        print(Iterable<Map.Entry<PK, PV>> iter) {
        for (Map.Entry<PK, PV> n : iter) {
            System.out.print(n.getKey() + ": " + n.getValue() + "; ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        BSTMap<Integer, String> bst = new BSTMap<>();
        assert bst.size() == 0;
        assert null == bst.put(5, "AAA");
        assert null == bst.put(3, "BBB");
        assert null == bst.put(7, "CCC");
        assert null == bst.put(6, "DDD");
        assert null == bst.put(1, "EEE");
        assert bst.size() == 5;
        print(bst.inOrder());
        assert bst.put(5, "FFF").equals("AAA");
        assert bst.size() == 5;
        print(bst);
        print(bst.subMap(2, 6));
        print(bst.subMap(6, 6));
        print(bst.subMap(70, 6));
        assert bst.lastKey(bst.root) == 7;
        assert bst.firstKey(bst.root) == 1;
        print(bst.entries());
        assert bst.hasValue("BBB");
        assert bst.hasKey(7);
        bst.clear();
        assert bst.size() == 0;
        assert null == bst.put(10, "MMM");
        assert null == bst.put(7, "BBB");
        assert null == bst.put(5, "AAA");
        assert null == bst.put(100, "CCC");
        assert null == bst.put(8, "DDD");
        assert null == bst.put(12, "QQQ");
        assert null == bst.put(9, "EEE");
        assert null == bst.put(11, "RRR");
        assert null == bst.put(13, "TTT");

        /*  tree looks like this:
                  10
               /          \
              7           100
             / \         /
            5   8       12
                 \      / \
                  9    11  13
         */
        print(bst);
        System.out.println("removing");
        assert bst.remove(7).equals("BBB");
        print(bst);
        assert bst.remove(13).equals("TTT");
        print(bst);
        assert bst.remove(8).equals("DDD");
        print(bst);
        assert bst.remove(10).equals("MMM");
        print(bst);
        assert bst.get(10) == null;
        assert bst.get(11).equals("RRR");
        assert bst.remove(100).equals("CCC");
        print(bst);
        assert bst.remove(12).equals("QQQ");
        print(bst);
        assert bst.remove(5).equals("AAA");
        print(bst);
        assert bst.remove(9).equals("EEE");
        print(bst);
        assert bst.remove(11).equals("RRR");
        print(bst);
    }
}
