/********************************************************************
 * Benjamin Hoertnagl-Pereira 
 * JHED: bhoertn1 
 * 631-488-7197 
 * bhoertn1@jhu.edu
 *
 * 600.226.02 | CS226 Data Structures | Section 2
 * Project 3, Part A - Binary Search Tree
 *
 *******************************************************************/

import java.io.Console;
import java.util.Scanner;

/* Really simple program to transform a document into a list of words, one per line */

public class DocToWord {

    /* I could use some built-in filtering to get rid of special characters, 
       but chose to roll my own */

    static boolean isLowerCase(char c) {
	return (c >= 'a') & (c <='z');
    }

    static boolean isUpperCase(char c) {
	return (c >= 'A') & (c <='Z');
    }

    static boolean isSpecial(char c) {
	return c == '\'';
    }

    static char filterChar(char c) {
	if (isLowerCase(c) | isUpperCase(c) | isSpecial(c))
	    return c;
	else
	    return '\n';
    }

    /* A really simple parser to transform a list of text to a list of words 
       which are printed as they are found */

    static void lineToWords(String line) {

	char c[] = line.toCharArray();
	int i = 0;
	boolean inword = false;
	int first =0;
	int last = 0;


	while (i < c.length) {

	// When a word is detected, find the end of it and print it

	    if (filterChar(c[i]) == c[i]) {
		if (!inword) {
		    inword = true;
		    first = i;
		    last = i;
		}
		else
		    last++;
	    }

	    // If white space is found, spit out the word and skip over the white space

	    else {

		if (inword) {
		    System.out.println(line.substring(first,last+1));
		    inword = false;
		}
	    }

	    i++;
	}

	// push out the last word 

	if (inword) {
	    System.out.println(line.substring(first,last+1));
	}	    
    }
	

    public static void main(String[] args) {

	// Create a console input source

	Scanner cnsl = new Scanner(System.in);

	// Chew through the doc line by line, spitting out the words

	String line;
	while (cnsl.hasNext()) {
	    line = cnsl.nextLine();
	    lineToWords(line);
	}

    }

}
