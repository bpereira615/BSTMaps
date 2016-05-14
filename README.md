***
	Benjamin Hoertnagl-Pereira
	bhoertn1
	631-488-7197 
	bhoertn1@jhu.edu

	Ryan Demo
	rdemo1
	rdemo1@jhu.edu

	600.226.02 CS226 Data Structures
	Project 3 - BST Maps
	Part 2

	Files:
	MapJHU.java - interface to function similarly to Map ADT
	
	AVLMap.java - implementation of the MapJHU.java ADT using binary
	search tree, balancing according to AVL principle
	
	AVLTest.java - test class (not JUnit) we used to debug AVLMap
	
	
	Summary:
	We found the most difficult method to write to be the map's remove function because of
	the number of edge cases; specifically, that of removing the root and taking different
	cases for each of the configuration of children on a node. Conceptually, we understood 
	the basics of the rebalancing, which cases to trigger when, and the difference between
	rebalancing after an insert and after a remove.
***

### Purpuose
The overall purpose of this project is to build and use classes for binary search trees and to stress-test them at an interesting scale.

## Part A: Basic Binary Search Tree Implementation

For the first part of the project we have completed a class BSTMap that implements our custom MapJHU.java map interface, as well the Iterable interface from the Java API. We have implemented a generic binary search tree, and used Map.Entry as a way to store key-value pairs. The comparator for Map.Entry operates on the key value. Our BSTMap simply wraps this general data structure and add Map-specific functionality.

Here are some details regarding the implementation:

* The implementation makes use of sentinels forming the leaves of the tree. That is, every leaf node is an empty node with no children, and no data.
* The implementation should make use of a nested class for binary tree nodes
* Note that the BSTMap is a map, but it also supports a few additional functions like creating subsets of the original Map that fall between a minimum and maximum key.

## Part B: Balanced Binary Search Tree Implementation

We have extended our BSTMap class above to a new class AVLMap which includes maintaining tree balance by adding the appropriate single and double rotations on the insert and remove functions. We have augmented each node to store the height of the tree of which it forms the root.

## Part C: Fun with Maps

Finally, we will have an opportunity to see the impact of different map structures. We'll use our maps to count word occurrences in a document as follows.

1. The input to your program will be a list of words separated by white space (spaces or line breaks) - punctuation will be stripped.

2. Create an instantiation of a map (as further described below, we'll experiment with Java HashMaps, your BSTMap, and your AVLMap) that uses the word (as a string) as the key and has a data field that holds a count of the number of times a word has been seen.

3. Read the words in the document. If the word is already there, increment the associated count, else add it to the tree with a count of 1.

4. Once the entire list of words is read, create a reverse index by extracting all key value pairs in your map and inserting the string into a second AVLMap. This AVLMap uses a key that is the count and the data is the words that have that count. Note that this is now a dictionary (i.e. multiple words may have the same count), so use a collection of some sort to hold the words associated with the count.

From here, we will explore two uses of our program:

1. Create an implementation (name your file with main P3C1.java) that after reading in all the words, prints the following:
* The total number of words in the document. [Clarification: total means total, not unique.]
* The most frequent word(s).
* All words that occur at most three times.
* All words that occur within the top 10% of the most frequent word. [Clarification: Print the 10% of the words that occur most frequently (ie, the top 10% of all words if the words are sorted by frequency), as well as any other words that have the same frequency as those in the top 10%. Words should be output in order of most frequent to least. (Also in other words, all the words in the lists for the counts that contain the top 10% most frequent words.)]


2. Create an implementation (name your file with main P3C2.java) that does the following:

* Creates 1) hashmap; 2) a BSTMap (unbalanced tree from part A); and 3) an AVLMap (balanced tree map from part B or the Java API TreeMap).
* Read the words into an array of strings before you start.
* Time the amount of time it takes to create the map (steps 1-3 above) for each instantiation
* Prints the time it took for each of the three implementation styles.
* Do this for a list of 100, 1000, and 100,000 words. [Clarification: read words and process the first 100, the first 1000 and the first 100,000 if there are that many.]