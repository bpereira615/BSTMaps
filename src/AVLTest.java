
public class AVLTest {

    public static void main(String[] args) {
        AVLMap<Integer, String> myBeautifulMap = new AVLMap<Integer, String>();
        myBeautifulMap.put(1, "k");
        myBeautifulMap.put(2, "k");
        myBeautifulMap.put(5, "k");
        myBeautifulMap.put(4, "k");
        myBeautifulMap.put(3, "k");
        
        System.out.println("root: " + myBeautifulMap.root.key);
        System.out.println("root left: " + myBeautifulMap.root.left.key);
        System.out.println("root right: " + myBeautifulMap.root.right.key);
        System.out.println("root right left: " + myBeautifulMap.root.right.left.key);
        System.out.println("root right right: " + myBeautifulMap.root.right.right.key);
        
    }

}
