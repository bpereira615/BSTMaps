import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

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



        //hashmap of key-words, and vaule-frequency
        HashMap<String, Integer> hashmap = new HashMap<>();

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



        //---------- total number of words in the document ----------
        int numWords = words.size();
        System.out.println("Total number of words: " + numWords);


        //---------- most frequent word(s) ----------
        //array of frequencies, index corresponding to words array
        ArrayList<Integer> freq = new ArrayList<>();
        for (String w : words) {
        	freq.add(hashmap.get(w));
        }

        int maxIndex = 0, currIndex = 0;
        Integer maxVal = new Integer(0);

        for (Integer i : freq) {
        	if (i.copmareTo(maxVal) > 0) {
        		maxVal = i;
        		maxIndex = currIndex;
        	}
        	currIndex ++;
        }

        String s = words.at(maxIndex);
      	System.out.println("Most frequent word(s): " + s);

        //---------- all words that occurr at most three times ----------


       	//---------- words that occurr in top 10% of most frequent word ----------

	}
}