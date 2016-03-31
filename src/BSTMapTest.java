/** TESTS for 600.226 Spring 2016 Project 2 - BSTMap implementation
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

import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.Set;
import java.util.Collection;

public class BSTMapTest {

    static BSTMap<Integer, String> e4;  // empty map, max load .4
    static BSTMap<Integer, String> e7;  // empty map, max load .7
    static BSTMap<Integer, String> all;  // all in map
    
    // note - Integer hashCode() returns the int value
    static Integer[] iray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    static HashSet<Integer> ikeys;
    static String[] sray = {"zro", "one", "two", "tre", "for", "fyv", "six", "svn", "ate", "nyn", "ten"};
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
        e4 = new BSTMap<Integer, String>();   // load 2 of 5 is max
        e7 = new BSTMap<Integer, String>();   // load 3 of 5 is max

        // this is full set, assuming put works correctly
        all = new BSTMap<Integer, String>();
        for (int i=0; i < iray.length; i++) {
            all.put(iray[i], sray[i]);
            //DEBUG:            System.out.println("T->all size cap " + all.size() + " " + all.getCapacity());
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
        testEmptyMetrics();
        assertNull(e4.get(1));
        assertNull(e4.remove(1));
        assertFalse(e4.hasKey(1));
        assertFalse(e4.hasValue("one"));
        assertTrue(e4.keys().isEmpty());
        assertTrue(e4.values().isEmpty());
        assertTrue(e4.entries().isEmpty());
    }



    public void testEmptyMetrics() {  
        BSTMap<Integer,String> em = new BSTMap<Integer,String>();
        assertTrue(em.isEmpty());
        assertEquals(0, em.size());
        // may need fudge factor for floats because of potential precision errors
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
            assertEquals(pairs, e7.entries());
        }
        // now do duplicate values
        int key;
        for (int i=0; i < iray.length; i++) {
            key = iray[i] + 20;
            assertNull(e7.put(key,sray[i]));  // new key
            keys.add(key);  // no dups
            vals.add(sray[i]);  // yes dups
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(key,sray[i]));
            assertEquals(keys, e7.keys());
            assertTrue(sameCollection(e7.values(),vals));
            assertEquals(pairs, e7.entries());
        }
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], e7.get(iray[i]));
            assertEquals(sray[i], e7.get(iray[i]+20));
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
    public void testAfterAddAll() {
        for (AbstractMap.SimpleEntry<Integer,String> p : entries) {
            e4.put(p.getKey(), p.getValue());
        }
        assertFalse(e4.isEmpty());
        assertTrue(e4.size() == entries.size());
        assertEquals(ikeys, e4.keys());
        assertTrue(sameCollection(svals, e4.values()));
        assertEquals(entries, e4.entries());
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



}
