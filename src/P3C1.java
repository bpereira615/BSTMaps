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
 * Project 3, Part C1 - Fun with Maps
 *
 *******************************************************************/


import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/** Analyzes and sorts word frequencies given a list of words.
 */
public final class P3C1 {

    /** The constant 10.
     */
    static final int TEN = 10;

    /** Dummy constructor for utility class.
     */
    private P3C1() {
    }

    /** Given a list of words in System.in, runs frequency analysis
     * method on the words.
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

        //wordToFreq of key-words, and value-frequency
        HashMap<String, Integer> wordToFreq = new HashMap<>();

        //create map
        for (String w : words) {
            if (wordToFreq.containsKey(w)) {
                //key is present (word has been seen), increment count
                wordToFreq.put(w, wordToFreq.get(w) + 1);
            } else {
                //word has not been see, initial put
                wordToFreq.put(w, 1);
            }
        }

        //wordToFreq of key-frequency, and value - word
        HashMap<Integer, ArrayList<String>> freqToWord = new HashMap<>();

        //frequency (reverse index)
        Integer f;
        for (String w : wordToFreq.keySet()) {
            f = wordToFreq.get(w);
            if (freqToWord.containsKey(f)) {
                freqToWord.get(f).add(w);
            } else {
                ArrayList<String> wordList = new ArrayList<>();
                wordList.add(w);
                freqToWord.put(f, wordList);
            }
        }

        freqAnalysis(wordToFreq, freqToWord);

    }

    /** Takes two HashMaps: word to frequency and frequency to words (its
     * reverse), printing total number of words, most frequent words,
     * all words that occur at most three times, and all words that
     * occur within the top 10% of the most frequent words.
     * @param wordToFreq HashMap of (word, frequency) pairs
     * @param freqToWord HashMap of (frequency, words[]) pairs
     */
    public static void freqAnalysis(HashMap<String, Integer> wordToFreq, 
            HashMap<Integer, ArrayList<String>> freqToWord) {

        //---------- total number of words in the document ----------
        int numWords = wordToFreq.size();
        System.out.println("Total number of words: " + numWords);
        System.out.println();


        //---------- most frequent word(s) ----------

        //linear search, only searching once so not worth sorting
        Integer max = new Integer(0);
        for (Integer i : freqToWord.keySet()) {
            if (i.compareTo(max) > 0) {
                max = i;
            }
        }

        ArrayList<String> freqWords = freqToWord.get(max);
        System.out.println("Most frequent word(s): " + freqWords);
        System.out.println();


        //---------- all words that occur at most three times ----------

        //using set to not have duplicate words
        HashSet<String> wordSet = new HashSet<>();
        for (Integer i : freqToWord.keySet()) {
            if (i.compareTo(2 + 1) <= 0) {
                wordSet.addAll(freqToWord.get(i));
            }
        }

        System.out.println("Words occurring at most 3 times: " + wordSet);
        System.out.println();


        //----- words that occur in top 10% of most frequent words -----

        ArrayList<String> top10FreqWords = new ArrayList<String>();

        ArrayList<Integer> sortedKeys =
                new ArrayList<Integer>(freqToWord.keySet());
        Collections.sort(sortedKeys);
        
        int size = wordToFreq.size() / TEN; //integer division
        int index = sortedKeys.size() - 1; //start from end of array
        while (top10FreqWords.size() <= size) {
            top10FreqWords.addAll(freqToWord.get(sortedKeys.get(index)));
            index--; //decrement every time to get lower frequency words
        }

        System.out.println("Top 10% of most frequent words: " + top10FreqWords);
        System.out.println();

    }

}