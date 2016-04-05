/** TESTS for 600.226 Spring 2016 Project 3 - AVLMap implementation
 *  Explicit tests for metrics after inserting and insert caused rehashing
 *  Explicit tests for contents and lookup after inserting, with duplicate vals
 *  Explicit test for linear probing when insert, get, contains
 *  Explicit test for clear, probe functions
 *  Explicit test that put of existing key replaces value, no key changes
 *  Explicit test remove all, remove duplicate values - contents and metrics
 *  Explicit tests of tombstones:
 *    get/insert/probing after remove with tombstones
 *    rehashing when ghosts > size
 * 
 *  Explicit tests of iterator functions
 *  Missing tests: exceptions - you should throw them as specified, we may
 *  add tests for them later.
 */

// assertEquals(pairs, e7.entries())
// assertTrue(pairs.containsAll(e7.entries()));

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;import java.util.Map;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.Set;
import java.util.Collection;
import java.util.TreeMap;

public class AVLTreeTest {

    static AVLMap<Integer, String> e4;  // empty map, max load .4
    static AVLMap<Integer, String> e7;  // empty map, max load .7
    static AVLMap<Integer, String> all;  // all in map
    
    // note - Integer hashCode() returns the int value
    static Integer[] iray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    static HashSet<Integer> ikeys;
    static String[] sray = {"zro", "one", "two", "tre", "for", "fyv", "six", "svn", "ate", "nyn", "ten"};
    static String[] sray2 = {"ten", "nyn", "ate", "svn", "six", "fyv", "for", "tre", "two", "one","zro"}; 
    static ArrayList<String> svals;
    static String[] nray = new String[sray.length];
    static ArrayList<String> nvals;
    static HashSet<AbstractMap.SimpleEntry<Integer,String>> entries;
    static AbstractMap.SimpleEntry<Integer,String> pair;


    /** This is a helper template method to see if two collections have the
     *  same contents, but not necessarily in the same order.
     *  @param c1 the first collection
     *  @param orig the other collection
     *  @return true if same values, false otherwise
     */
    public static <T> boolean sameCollection(Collection<T> c1, Collection<T> orig) {
        ArrayList<T> c2 = new ArrayList<T>(orig); // make copy
        if (c1.size() != c2.size())
            return false;
        for (T val : c1) {  // uses iterator
            if (!c2.remove(val))   // so count will be accurate
                return false;
        }
        if (c2.size() != 0)  // should be empty by now
            return false;
        return true;  // passed all tests
    }

    @BeforeClass
    public static void init() {
        ikeys = new HashSet<Integer>();
        for (Integer val: iray) {
            ikeys.add(val);
        }

        svals= new ArrayList<String>();
        for (String val: sray) {
            svals.add(val);
        }

        nvals = new ArrayList<String>(svals.size());
        int idx = 0;
        for (String val: svals) {
            nray[idx] = "N"+val;
            nvals.add(nray[idx]);
            idx++;
        }

        entries = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        for (int i=0; i < iray.length; i++) {
            pair = new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]);
            entries.add(pair);
        }
    }

    @Before        
    public void setup() {
        // these start out empty before each test, different load factors
        e4 = new AVLMap<Integer, String>();   // load 2 of 5 is max
        e7 = new AVLMap<Integer, String>();   // load 3 of 5 is max

        // this is full set, assuming put works correctly
        all = new AVLMap<Integer, String>();
        for (int i=0; i < iray.length; i++) {
            all.put(iray[i], sray[i]);
        }
    }


    @Test 
    public void testInit() {
        assertEquals(ikeys.toString(), "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
        assertEquals(svals.toString(), "[zro, one, two, tre, for, fyv, six, svn, ate, nyn, ten]");
        assertEquals(nvals.toString(), "[Nzro, None, Ntwo, Ntre, Nfor, Nfyv, Nsix, Nsvn, Nate, Nnyn, Nten]");

        // won't necessarily be in this order, so we can't test
        // assertEquals(entries.toString(), "[3=tre, 6=six, 0=zro, 1=one, 10=ten, 5=fyv, 8=ate, 9=nyn, 4=for, 7=svn, 2=two]");
    }


    @Test 
    public void testEmptyMap() {
        assertNull(e4.get(1));
        assertNull(e4.remove(1));
        assertFalse(e4.hasKey(1));
        assertFalse(e4.hasValue("one"));
        assertTrue(e4.keys().isEmpty());
        assertTrue(e4.values().isEmpty());
        assertTrue(e4.entries().isEmpty());
    }

    @Test 
    public void testClearEmpty() {
        assertTrue(e7.isEmpty());
        e7.clear();
        assertTrue(e7.isEmpty());
        assertEquals(0, e7.size());
        assertTrue(e7.keys().isEmpty());
        assertTrue(e7.values().isEmpty());
        assertTrue(e7.entries().isEmpty());
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.get(iray[i]));
            assertFalse(e7.hasKey(iray[i]));
            assertFalse(e7.hasValue(sray[i]));
        }
    }

    @Test 
    public void testClear() {
        assertTrue(e7.isEmpty());
        for (int i=0; i < iray.length; i++) {
            e7.put(iray[i],sray[i]);
        }
        int size = e7.size();
        assertTrue(size == iray.length);
        e7.clear();
        assertTrue(e7.isEmpty());
        assertEquals(0, e7.size());
        assertTrue(e7.keys().isEmpty());
        assertTrue(e7.values().isEmpty());
        assertTrue(e7.entries().isEmpty());
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.get(iray[i]));
            assertFalse(e7.hasKey(iray[i]));
            assertFalse(e7.hasValue(sray[i]));
        }
    }

    @Test 
    public void testPutGetHas() {
        assertTrue(e4.isEmpty());
        // very hard to test one without the others
        for (int i=0; i < iray.length; i++) {
            assertNull(e4.put(iray[i],sray[i]));  // no old value
            assertEquals(sray[i], e4.get(iray[i]));
            assertTrue(e4.hasKey(iray[i]));
            assertTrue(e4.hasValue(sray[i]));
        }
        // make sure all are still there
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], e4.get(iray[i]));
            assertTrue(e4.hasKey(iray[i]));
            assertTrue(e4.hasValue(sray[i]));
        }
        // assert that entire collection is contained
        assertTrue(entries.containsAll(e4.entries()));
    }

    @Test 
    public void testPutCollections() { 
        HashSet<Integer> keys = new HashSet<Integer>();
        ArrayList<String> vals = new ArrayList<String>();
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.put(iray[i],sray[i]));  // new key
            keys.add(iray[i]);
            vals.add(sray[i]);
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]));
            assertEquals(keys, e7.keys());
            assertTrue(sameCollection(e7.values(),vals));
            // for (Entry<Integer, String> p: e7.entries()) {
            //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer,String>(p.getKey(), p.getValue()))); 
            // }
            assertTrue(pairs.containsAll(e7.entries()));
        }
        // now do duplicate values
        int key;
        for (int i=0; i < iray.length; i++) {
            key = iray[i]; 
            assertNotNull(e7.put(key,sray2[i]));  // overwrite old entries
            keys.add(key);  // no dups
            vals.remove(sray[i]);
            vals.add(sray2[i]);  // yes dups
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(key,sray2[i])); 
            assertEquals(keys, e7.keys());
            System.out.println(e7.values().toString());
            System.out.println(vals.toString());
            assertTrue(sameCollection(e7.values(),vals));
            // for (Entry<Integer, String> p: e7.entries()) {
            //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
            // }
            assertTrue(pairs.containsAll(e7.entries()));
        }
    }

    @Test 
    public void testPutOverwrites() {
        int size = all.size();
        assertTrue(size == iray.length);
        Set<Integer> keys = all.keys();
        assertEquals(ikeys,keys);
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], all.put(iray[i],nray[i])); // returns old
            assertEquals(size, all.size());  // no size change
            assertEquals(nray[i], all.get(iray[i]));  // overwritten
            assertFalse(all.hasValue(sray[i]));  // overwritten
            assertTrue(all.hasValue(nray[i]));  // new value
            assertTrue(all.hasKey(iray[i]));  // key still there
        }
        // check again after all overwrites
        assertEquals(size, all.size());  // no size change
        assertEquals(keys, all.keys());  // same keys
        assertEquals(keys.toString(), all.keys().toString()); // same key order
        for (int i=0; i < iray.length; i++) {
            assertEquals(nray[i], all.get(iray[i]));  // overwritten
            assertFalse(all.hasValue(sray[i]));  // overwritten
            assertTrue(all.hasValue(nray[i]));  // new value
            assertTrue(all.hasKey(iray[i]));  // key still there
        }
    }

    @Test 
    public void testMetricsFewAdds() {  // assuming put works
        int size = 0;
        assertEquals(size, e4.size());

        e4.put(20,"twenty");  // 1st insert
        size++;
        assertEquals(size, e4.size());
        
        e4.put(30,"thirty");  // 2nd insert
        size++;
        assertEquals(size, e4.size());
       
        e4.put(40,"fourty");
        size++;  
        assertEquals(size, e4.size());
        assertEquals("fourty", e4.get(40));
    }

    @Test 
    public void testAfterAddAll() {
        for (Map.Entry<Integer,String> p : entries) {
            e4.put(p.getKey(), p.getValue());
        }
        assertFalse(e4.isEmpty());
        assertTrue(e4.size() == entries.size());
        assertEquals(ikeys, e4.keys());
        assertTrue(sameCollection(svals, e4.values()));
        // for(Entry<Integer, String> p: e4.entries()) {
        //     assertTrue(entries.contains(
        //             new AbstractMap.SimpleEntry<Integer,String>(p.getKey(),p.getValue())));
        // }
        assertTrue(entries.containsAll(e4.entries()));
    }

    @Test 
    public void testRemoveAll() {
        int size = all.size();
        Set<Integer> keys = all.keys();
        Collection<String> vals = all.values();
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], all.remove(iray[i])); // returns val
            size--;
            assertEquals(size, all.size());  // size decrease
            assertNull(all.get(iray[i]));  // key not there now
            assertFalse(all.hasValue(sray[i]));  // gone
            assertFalse(all.hasKey(iray[i]));  // gone
            keys.remove(iray[i]);
            vals.remove(sray[i]);
            assertEquals(keys, all.keys());
            assertTrue(sameCollection(vals, all.values()));
        }
        assertTrue(all.isEmpty());
        assertTrue(all.keys().isEmpty());
        assertTrue(all.values().isEmpty());
        assertTrue(all.entries().isEmpty());
        assertEquals(0, all.size());
    }

    @Test 
    public void testRemoveDuplicateValues() {
        for (int i=0; i < iray.length; i++) {
            all.put(iray[i]+20,sray[i]);  
            // duplicate VALUES, no duplicate keys.
        }
        int size = all.size();
        assertTrue(size == iray.length*2);
        Set<Integer> keys = all.keys();
        Collection<String> vals = all.values();  // has duplicates
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i],all.remove(iray[i])); // returns val
            size--;
            assertEquals(size, all.size());  // size decrease
            assertNull(all.get(iray[i]));  // key not there now
            assertFalse(all.hasKey(iray[i]));  // key gone
            assertTrue(all.hasValue(sray[i]));  // val still there
            assertTrue(all.hasKey(iray[i]+20)); // new key there
            keys.remove(iray[i]);
            vals.remove(sray[i]);  // only one copy removed
            assertEquals(keys, all.keys());
            assertTrue(sameCollection(vals, all.values()));
        }
        assertTrue(all.size() == iray.length); // half the values there
        for (int i=0; i < iray.length; i++) {
            assertEquals(all.get(iray[i]+20),sray[i]); 
        }
    }

    /* TEST SUBMAP for BSTMAP CLASS -------------------------------- **/
    @Test
    public void testSubMap() {
    	// start with e4 and add stuff
    	for (int i=0; i < iray.length; i++) {
            assertNull(e4.put(iray[i],sray[i]));  // no old value
            assertEquals(sray[i], e4.get(iray[i])); // new value added
        }
        // make a submap from 2 to 5
        AVLMap<Integer, String> submap = e4.subMap(2,5);
        // check that submap contains all entries 2 to 5
        int count = 2;
        for (int i=0; i < submap.size(); i++){
        	assertEquals(submap.get(iray[count]), sray[count]);
        	count++;
        }
    	// check that submap does NOT contain other keys
    	assertNull(submap.get(iray[0])); // shouldn't have 0
    	assertNull(submap.get(iray[6])); // shouldn't have 6
    }

    /* HERE THERE BE BALANCING CHECKS ------------------------------ **/
    @Test
    public void testEmptyMapBalanced() {
    	// make sure map is empty (sanity check)
    	assertTrue(e4.isEmpty());
    	// check balance factor of root is zero
    	assertEquals(e4.rootBalance(),0);
        // IDEA: add getHeight function? make root public?
    	//assertTrue(e4.root.left.isLeaf());
    	//assertTrue(e4.root.right.isLeaf());
    	// balance factor should be 0
    	//assertEquals(e4.root.bf(), 0);

    }

    @Test
    public void testRebalancesAfterAdds(){
    	//start with empty map e4
    	assertTrue(e4.isEmpty());
    	// add key-value pairs to e4 one at a time
        for (int i=0; i < iray.length; i++) {
            assertNull(e4.put(iray[i],sray[i]));  // no old value
            assertEquals(sray[i], e4.get(iray[i])); // new value added
            assertFalse(e4.rootBalance() > 1); // bf should be < 1
            assertFalse(e4.rootBalance() < -1); // bf should be > -1
        }
    	
    }

    @Test
    public void testRebalancesAfterRemoves() {
    	// start with empty map e4 and add stuff
    	for (int i=0; i < iray.length; i++) {
            assertNull(e4.put(iray[i],sray[i]));  // no old value
            assertEquals(sray[i], e4.get(iray[i])); // new value added
        }
        // get current size of map
        int size = e4.size();
        // now remove things and check for rebalancing
        for (int i=0; i < iray.length; i++){
        	e4.remove(iray[i]);
        	size--;
        	assertEquals(size, e4.size()); // make sure remove worked
        	assertFalse(e4.rootBalance() > 1); // bf less than 1
        	assertFalse(e4.rootBalance() < -1); // bf > -1
        }
    }

    /* WELCOME TO THE LAND OF THE ITERATOR ------------------------- **/
    @Test 
    public void testIteratorFullNoCollisions() {
        AVLMap<Integer, String> full = new AVLMap<Integer,String>();
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        int cap = 5;
        // put 5 entries in to fill
        for (int i=0; i < cap; i++) {
            full.put(i, i+"");
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(i, i+""));
        }
        // for (Map.Entry<Integer, String> p: full.entries()) {
        //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(),p.getValue())));
        // }
        assertTrue(pairs.containsAll(full.entries()));
        Iterator<Map.Entry<Integer, String>> it = full.iterator();
        int count = 0;
        while (it.hasNext()) {
            assertEquals(new AbstractMap.SimpleEntry<Integer,String>(count, count+""), it.next());
            count++;
        }
        assertEquals("iterated through all elements", cap, count);
        assertEquals(cap, full.size());  // everything still there
        // for (Entry<Integer, String> p: full.entries()) {
        //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
        // }
        assertTrue(pairs.containsAll(full.entries()));
        // do it all again
        it = full.iterator();
        count = 0;
        while (it.hasNext()) {
            assertEquals(new AbstractMap.SimpleEntry<Integer,String>(count, count+""), it.next());
            count++;
        }
        assertEquals("iterated through all elements", cap, count);
        assertEquals(cap, full.size());  // everything still there
        // for (Entry<Integer, String> p: full.entries()) {
        //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
        // }
        assertTrue(pairs.containsAll(full.entries()));
    }

    // REMOVED: AVLMap iterator can't remove
    // @Test 
    // public void testIteratorFullRemove() {
    //     AVLMap<Integer, String> full = new AVLMap<Integer,String>();
    //     HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
    //     int cap = 5;
    //     // put 5 entries in to fill
    //     for (int i=0; i < cap; i++) {
    //         full.put(i, i+"");
    //         pairs.add(new AbstractMap.SimpleEntry<Integer,String>(i, i+""));
    //     }
    //     for (Entry<Integer, String> p: full.entries()) {
    //         assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
    //     }
    //     Iterator<Map.Entry<Integer, String>> it = full.iterator();
    //     int count = cap;
    //     int i = 0;
    //     System.out.println(full.toString());
    //     while (it.hasNext()) {
    //         Map.Entry<Integer, String> tmp = it.next();
    //         assertTrue(tmp.getKey().equals(i));
    //         assertEquals(tmp.getValue(), i + "");
    //         it.remove();
    //         count--;
    //         pairs.remove(new AbstractMap.SimpleEntry<Integer,String>(i, i+""));
    //         assertEquals(count, full.size());
    //         for (Entry<Integer, String> p: full.entries()) {
    //             assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
    //         }
    //         i++;
    //     }
    //     assertEquals("iterated through all elements", 0, count);
    //     assertEquals("removed all entries with iterator", 0, full.size());
    //     assertEquals(0, full.entries().size());
    //     assertEquals(0, full.keys().size());
    //     assertEquals(0, full.values().size());
    //     count = 0;
    // }

    @Test //ok
    public void testIteratorAllEntryTypes() {
        // using load factor .7 table
        HashSet<Integer> keys = new HashSet<Integer>();
        ArrayList<String> vals = new ArrayList<String>();
        AbstractMap.SimpleEntry<Integer,String>[] entries = new AbstractMap.SimpleEntry[iray.length];
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.put(iray[i],sray[i]));  // new key
            keys.add(iray[i]);
            vals.add(sray[i]);
            entries[i] = new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]);
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]));
            assertEquals(keys, e7.keys());
            assertTrue(sameCollection(e7.values(),vals));
            // for (Entry<Integer, String> p: e7.entries()) {
            //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
            // }
            assertTrue(pairs.containsAll(e7.entries()));
        }
        Iterator<Map.Entry<Integer , String>> it = e7.iterator();

        //int cap = e7.getCapacity();
        int size = e7.size();
        //assertTrue(size < cap);  // has empty slots at end
        int count = 0;
        Map.Entry<Integer,String> e;

        // while (it.hasNext()) {
        //     e = it.next();
        //     if (count < size) {
        //         assertTrue(entries[count].equals(
        //                 new AbstractMap.SimpleEntry<Integer,String>(e.getKey(), e.getValue())));
        //     }
        //     // LMC: ignore failures past this point, iterator can't remove
        //     if (count % 3 == 0) {  // only remove 1/3 of the entries, no rehash
        //         it.remove();  
        //         pairs.remove(new AbstractMap.SimpleEntry<Integer,String>(e.getKey(), e.getValue())); 
        //         keys.remove(e.getKey());
        //         vals.remove(e.getValue());
        //     }
        //     count++;
        //}

        assertEquals(keys,e7.keys());
        assertTrue(sameCollection(vals, e7.values()));
        // for (Entry<Integer, String> p: e7.entries()) {
        //     assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer,String>(p.getKey(), p.getValue()))); 
        // }
        assertTrue(pairs.containsAll(e7.entries()));
    }

}
   