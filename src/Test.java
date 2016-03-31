import java.util.*;

public class Test {
	public static void main(String[] args) {
		
		BSTMap<Integer, String> map = new BSTMap<Integer, String>();
		map.put(7, "seven");
		map.put(5, "five");
		map.put(55, "wjfwo");
		map.put(16, "sixteen");
		map.put(15, "fifteen");
		map.put(17, "seventeen");
		//map.put(77, "test");
		//map.test();
		//System.out.println("removed: " + map.remove(7));
		//map.put(null, "dowif");
		//map.test();
		//System.out.println("removed: " + map.remove(5));

		/*

		BSTMap<Integer, String> submap = map.subMap(4, 33);

		System.out.println("all: " + map.keys());
		System.out.println("keys: " + submap.keys());

		*/


		
		Iterator<Map.Entry<Integer, String>> it = map.iterator();


		//System.out.println(it.next().getKey());

/*
	
		String s = "";
		int i = 0;


		while (it.hasNext()) { 
			System.out.println(i);


			s += it.next().getKey() + " ";

			if (i == 1 || i == 3) {
				it.remove();
			}
			i++;

		}

*/
/*
		System.out.println(map.keys());
		it = map.iterator();
		it.next();
		it.remove();


		System.out.println(map.keys());
		*/

		//it.remove();

		//		System.out.println(map.keys());


		
		it = map.iterator();
		System.out.println(map.remove(7));
		it.next();
	
	

		//System.out.println("keys: " + s);
		


		/*
		map.put(2, "test");

		map.put(13, "1");

		map.put(4, "test");
		map.put(5, "testd");
		map.put(15, "test");
		map.put(12, "dd");
		*/

		/*map.put(10, "testd");
		map.put(17, "testd");
		map.put(8, "test");
		map.put(14, "testd");
		map.put(11, "test");
		*/
		//map.put(2, "doiwdf");
		//int result = "diwfd".compareTo(null);

		/*
		System.out.println(map.hasKey(1));
		System.out.println(map.hasKey(2));
		System.out.println(map.get(1));
		System.out.println(map.get(2));
		System.out.println(map.hasValue("testd"));
		*/
		//map.test();
	}
}