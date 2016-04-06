import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

public class P3C1 {
	public static void main(String[] args) {
		//create scanner from file
		Scanner infile = null;
        boolean inerror = false;

        try {
            System.out.println("0 " + args[0]
                    + " should be input filename.");
            infile = new Scanner(new FileReader(args[0]));
        } catch (ArrayIndexOutOfBoundsException a) {
            System.err.println("Must give input filename at command line.");
            inerror = true;
        } catch (IOException f) {
            System.err.println("Can't open that file, try again.");
            inerror = true;
        }
        if (inerror) {
            System.err.println("Exiting...");
            System.exit(1);
        }


        //array of words
        ArrayList<String> words = new ArrayList<>();

        //insert words into array before start
        while(infile.hasNext()) {
        	words.add(infile.next());
        }



        //wordToFreq of key-words, and vaule-frequency
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
        
        //frequency
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



        //---------- total number of words in the document ----------
        int numWords = wordToFreq.keySet().size();
        System.out.println("Total number of words: " + numWords);




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

        //---------- all words that occurr at most three times ----------

        //TODO: set for no duplicates, right?
        HashSet<String> wordSet = new HashSet<>();
        for (Integer i : freqToWord.keySet()) {
            if (i.compareTo(3) <= 0) {
                wordSet.addAll(freqToWord.get(i));
            }
        }

        System.out.println("Words occurring at most 3 times: " + wordSet);

       	//---------- words that occurr in top 10% of most frequent word ----------


	}
}