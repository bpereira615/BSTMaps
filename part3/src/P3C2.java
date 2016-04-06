import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.util.Scanner;

public class P3C2 {
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