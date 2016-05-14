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
 * Project 3, Part C2 - Fun with Maps
 *
 *******************************************************************/


import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Scanner;

/** Class to analyze insertion times of HashMap, BSTMap, and TreeMap
 * at different increments of data to insert.
 */
public final class P3C2 {
    
    
    /** Constant 100.
     */
    static final int ONE_HUNDRED = 100;
    
    /** Constant 1000.
     */
    static final int ONE_THOUSAND = 1000;
    
    /** Constant 100,000.
     */
    static final int ONE_HUNDRED_THOUSAND = 100000;
    
    
    /** Dummy constructor for utility class.
     */
    private P3C2() {
    }
    
    /** Takes in list of words from console and analyzes their insertion
     * into a HashMap, BSTMap and TreeMap at the 100, 1000, and 100000
     * word levels.
     * @param args command line arguments, unused
     */
    public static void main(String[] args) {

        Scanner cnsl = new Scanner(System.in);
        Scanner inline;
        String line;

        //array of words
        ArrayList<String> words = new ArrayList<>();

        //insert words into array before start
        while (cnsl.hasNextLine()) {
            line = cnsl.nextLine();
            inline = new Scanner(line);
            while (inline.hasNext()) {
                words.add(inline.next());
            }
        }

        cnsl.close();
        
        if (words.size() >= ONE_HUNDRED) {
            System.out.println("First 100 Words - Insertion Times");
            analyzeList(words.subList(0, ONE_HUNDRED));
            System.out.println();
        } else {
            System.out.println("Insufficient words for analysis.");
        }
        
        if (words.size() >= ONE_THOUSAND) {
            System.out.println("First 1000 Words - Insertion Times");
            analyzeList(words.subList(0, ONE_THOUSAND));
            System.out.println();
        } 
        
        if (words.size() >= ONE_HUNDRED_THOUSAND) {
            System.out.println("First 100,000 Words - Insertion Times");
            analyzeList(words.subList(0, ONE_HUNDRED_THOUSAND));
            System.out.println();
        }
        
    }
    
    /** Takes a list of strings and inserts its contents into a
     * HashMap, BSTMap, and TreeMap then prints insertion times.
     * @param words list of words to analyze insertion times
     */
    public static void analyzeList(List<String> words) {
        //various map implementations
        HashMap<String, Integer> hashmap = new HashMap<>();
        BSTMap<String, Integer> bstmap = new BSTMap<>();
        TreeMap<String, Integer> avlmap = new TreeMap<>();

        
        //--------------- Hashmap  ---------------
        long lStartTime = System.currentTimeMillis();

        //create map
        for (String w : words) {
            if (hashmap.containsKey(w)) {
                //key is present (word has been seen), increment count
                hashmap.put(w, hashmap.get(w) + 1);
            } else {
                //word has not been see, initial put
                hashmap.put(w, 1);
            }
        }

        long lEndTime = System.currentTimeMillis();
        long difference = lEndTime - lStartTime;
        System.out.println("HashMap - elapsed milliseconds: " + difference);


        //--------------- BSTMap ---------------
        lStartTime = System.currentTimeMillis();

        //create map
        for (String w : words) {
            if (bstmap.hasKey(w)) {
                bstmap.put(w, bstmap.get(w) + 1);
            } else {
                bstmap.put(w, 1);
            }
        }


        lEndTime = System.currentTimeMillis();
        difference = lEndTime - lStartTime;
        System.out.println("BSTMap - elapsed milliseconds: " + difference);


        //--------------- AVLMap ---------------
        lStartTime = System.currentTimeMillis();

        //create map
        for (String w : words) {
            if (avlmap.containsKey(w)) {
                avlmap.put(w, avlmap.get(w) + 1);
            } else {
                avlmap.put(w, 1);
            }
        }

        lEndTime = System.currentTimeMillis();
        difference = lEndTime - lStartTime;
        System.out.println("AVLMap - elapsed milliseconds: " + difference);
    }
    
}