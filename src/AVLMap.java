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


/** Balanced Binary Search Tree Map implementation.
 * @param <K> the base type of the keys in the entries
 * @param <V> the base type of the values
 */
public class AVLMap<K extends Comparable<? super K>, V>
    extends BSTMap<K, V> {
    
    /** Inner node class. */
    public class AVLNode extends BSTMap<K, V>.BNode {
        
        /** The height of a node. */
        private int height;
        /** The left child of this node. */
        private AVLNode left;
        /** The right child of this node. */
        private AVLNode right;
        
        /** Create a new node with a particular key and value.
         *  @param key the key for the new node
         *  @param val the value for the new node
         */
        AVLNode(K key, V val) {
            super(key, val);
            this.height = -1;
        }
        
        /** Evaluate balance factor of the node by comparing
         * the height of its children, left - right.
         * @return balance factor
         */
        public int bf() {
            return this.left.height - this.right.height;
        }
    }
    
    /** Create an empty tree with a sentinel root node.
     */
    public AVLMap() {
        // empty tree is a sentinel for the root
        super.root = new AVLNode(null, null);
        super.size = 0;
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
        
        return this.put(key, val, (AVLNode) super.root);
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
                return null;
            }

            //in order search for position
            int diff = key.compareTo(curr.key);
            
            //if key is smaller than root key, search left subtree
            if (diff < 0) {
                V value = this.put(key, val, curr.left);
                this.rebalance(curr);
                return value;
            } else if (diff == 0) { //node found with key, update and return old
                V oldVal = curr.value;
                curr.key = key;
                curr.value = val;
                this.rebalance(curr);
                return oldVal;
            } else { //if key is larger than root key, search right subtree
                V value = this.put(key, val, curr.right);
                this.rebalance(curr);
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
        return this.remove(key, (AVLNode) super.root);
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
            if (curr.key == super.root.key) {

                super.root.value = this.get(firstKey);
                
                this.removeMin(curr.right);
                
                super.root.key = firstKey;

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
                return doubleLR(root);
            } else {
                //root's right subtree is not left heavy
                return singleLeft(root);
            }
        } else if (root.bf() > 1) { //root is left heavy
            if (root.left.bf() < -1) {
                //root's left subtree is right heavy
                return doubleRL(root);
            } else {
                //root's left subtree is not right heavy
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
        curr.right = root.left;
        root.left = curr;
        return root;
    }
    
    /** Perform right rotation on an AVLNode.
     * @param curr AVLNode to right rotate
     * @return right rotated AVLNode
     */
    public AVLNode singleRight(AVLNode curr) {
        AVLNode root = curr.left;
        curr.left = root.right;
        root.right = curr;
        return root;
    }

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
    
    public static void main(String[] args) {
        AVLMap<Integer, String> myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(2, "k");
        System.out.println("2: " + myBeautifulMap.get(2));
        myBeautifulMap.put(1, "k");
        myBeautifulMap.put(5, "k");
        myBeautifulMap.put(4, "k");
        myBeautifulMap.put(3, "k");
        
        System.out.println("2: " + myBeautifulMap.get(2));
        System.out.println("1: " + myBeautifulMap.get(1));
        System.out.println("5: " + myBeautifulMap.get(5));
        System.out.println("4: " + myBeautifulMap.get(4));
        System.out.println("3: " + myBeautifulMap.get(3));
        System.out.println("root: " + myBeautifulMap.root.key);
        System.out.println("root left: " + myBeautifulMap.root.left.key);
        System.out.println("root right: " + myBeautifulMap.root.right.key);
        System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
        System.out.println("root right right: " + myBeautifulMap.root.right.right.key);
        
    }
}