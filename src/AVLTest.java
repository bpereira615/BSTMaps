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

/** Test class for AVLMap.
 */
public final class AVLTest {
    
    /** Dummy constructor for final class.
     */
    private AVLTest() {
        
    }

    /** Main test method for AVLMap. Unnecessary to run but
     * included anyway.
     * @param args command line args, unused
     */
    public static void main(String[] args) {

        int fail = 0;

        testPut();

        AVLMap<Integer, String> myBeautifulMap
            = new AVLMap<Integer, String>();
        System.out.println("\n\nPUT- ROOT change test ---------------------");
        myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(3, "six");
        System.out.println("inserted 3");
        myBeautifulMap.put(2, "k");
        System.out.println("inserted 2");
        myBeautifulMap.put(1, "k");
        System.out.println("inserted 1");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }


        System.out.println("\n\nPUT ADVANCED test ----------------------");
        myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(6, "six");
        System.out.println("inserted 6");
        myBeautifulMap.put(4, "k");
        System.out.println("inserted 4");
        myBeautifulMap.put(8, "k");
        System.out.println("inserted 8");
        myBeautifulMap.put(2, "k");
        System.out.println("inserted 2");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.put(7, "k");
        System.out.println("inserted 7");
        myBeautifulMap.put(1, "k");
        System.out.println("inserted 1");
        myBeautifulMap.put(3, "k");
        System.out.println("inserted 3");
        myBeautifulMap.put(0, "k");
        System.out.println("inserted 0");


        System.out.println("Before Remove:");
        myBeautifulMap.printTree();


        System.out.println(myBeautifulMap.get(6));
        System.out.println(myBeautifulMap.get(4));
        System.out.println(myBeautifulMap.get(8));
        System.out.println(myBeautifulMap.get(2));
        System.out.println(myBeautifulMap.get(5));
        System.out.println(myBeautifulMap.get(7));
        System.out.println(myBeautifulMap.get(1));
        System.out.println(myBeautifulMap.get(3));
        System.out.println(myBeautifulMap.get(0));

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        System.out.println("\n\nREMOVE test 2 ----------------------");
        myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(10, "six");
        System.out.println("inserted 10");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.put(20, "k");
        System.out.println("inserted 20");
        myBeautifulMap.put(2, "k");
        System.out.println("inserted 2");
        myBeautifulMap.put(7, "six");
        System.out.println("inserted 7");
        myBeautifulMap.put(15, "k");
        System.out.println("inserted 15");
        myBeautifulMap.put(25, "k");
        System.out.println("inserted 25");
        myBeautifulMap.put(1, "k");
        System.out.println("inserted 1");
        myBeautifulMap.put(4, "k");
        System.out.println("inserted 4");
        myBeautifulMap.put(6, "k");
        System.out.println("inserted 6");
        myBeautifulMap.put(9, "k");
        System.out.println("inserted 9");
        myBeautifulMap.put(17, "k");
        System.out.println("inserted 17");
        myBeautifulMap.put(3, "k");
        System.out.println("inserted 3");
        myBeautifulMap.put(8, "k");
        System.out.println("inserted 8");

        System.out.println("Before Remove:");
        myBeautifulMap.printTree();



        myBeautifulMap.remove(10);
        System.out.println("removed 10");

        System.out.println("\nAfter Remove:");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        System.out.println("\n\nREMOVE test ----------------------");
        myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(60, "six");
        System.out.println("inserted 6");
        myBeautifulMap.put(40, "k");
        System.out.println("inserted 4");
        myBeautifulMap.put(80, "k");
        System.out.println("inserted 8");
        myBeautifulMap.put(20, "k");
        System.out.println("inserted 2");
        myBeautifulMap.put(50, "six");
        System.out.println("inserted 5");
        myBeautifulMap.put(70, "k");
        System.out.println("inserted 7");
        myBeautifulMap.put(45, "k");
        System.out.println("inserted 1");

        System.out.println("Before Remove:");
        myBeautifulMap.printTree();

        myBeautifulMap.remove(20);
        System.out.println("removed 20");

        System.out.println("After Remove:");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        System.out.println("failures: " + fail);
    }

    /** Test put function.
     */
    public static void testPut() {

        int fail = 0;

        AVLMap<Integer, String> myBeautifulMap
            = new AVLMap<Integer, String>();

        System.out.println("Left -------------");
        myBeautifulMap.put(2, "k");
        System.out.println("inserted 2");
        myBeautifulMap.put(1, "k");
        System.out.println("inserted 1");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.printTree();
        myBeautifulMap.put(4, "k");
        System.out.println("inserted 4");
        myBeautifulMap.printTree();
        myBeautifulMap.put(3, "k");
        System.out.println("inserted 3");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
            System.out.println(1);
        }

        myBeautifulMap = new AVLMap<Integer, String>();
        System.out.println("Right1 -------------");
        myBeautifulMap.put(4, "k");
        System.out.println("inserted 4");
        myBeautifulMap.put(3, "k");
        System.out.println("inserted 3");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.put(6, "k");
        System.out.println("inserted 6");
        myBeautifulMap.put(7, "k");
        System.out.println("inserted 7");
        myBeautifulMap.printTree();
        myBeautifulMap.put(8, "k");
        System.out.println("inserted 8");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        myBeautifulMap = new AVLMap<Integer, String>();
        System.out.println("Right2 -------------");
        myBeautifulMap.put(10, "k");
        System.out.println("inserted 10");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.put(20, "k");
        System.out.println("inserted 20");
        myBeautifulMap.put(3, "k");
        System.out.println("inserted 3");
        myBeautifulMap.put(7, "k");
        System.out.println("inserted 7");
        myBeautifulMap.put(12, "k");
        System.out.println("inserted 12");
        myBeautifulMap.put(25, "k");
        System.out.println("inserted 25");
        myBeautifulMap.put(23, "k");
        System.out.println("inserted 23");
        myBeautifulMap.put(28, "k");
        System.out.println("inserted 28");
        myBeautifulMap.put(30, "k");
        System.out.println("inserted 30");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        System.out.println("Right3 -------------");
        myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(10, "k");
        System.out.println("inserted 10");
        myBeautifulMap.put(15, "k");
        System.out.println("inserted 15");
        myBeautifulMap.put(12, "k");
        System.out.println("inserted 12");
        myBeautifulMap.printTree();

        myBeautifulMap.put(8, "k");
        System.out.println("inserted 8");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        System.out.println("New test -------------");
        myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(6, "k");
        System.out.println("inserted 6");
        myBeautifulMap.put(4, "k");
        System.out.println("inserted 4");
        myBeautifulMap.put(5, "k");
        System.out.println("inserted 5");
        myBeautifulMap.printTree();

        if (!myBeautifulMap.isBalanced()) {
            fail++;
        }

        System.out.println("failures: " + fail);
    }
}