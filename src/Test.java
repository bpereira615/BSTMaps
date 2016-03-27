public class Test {
	public static void main(String[] args) {
		
		BSTMap<Integer, String> map = new BSTMap<Integer, String>();
		map.put(1, "test");
		map.put(2, "1");
		map.put(44, "test");
		map.put(5, "testd");
		map.put(222, "test");
		map.put(3, "testd");
		map.put(45, "test");
		map.put(67, "testd");
		map.put(889, "test");
		map.put(908, "testd");
		map.put(323, "test");
		map.put(111, "testd");
		//map.put(2, "doiwdf");
		//int result = "diwfd".compareTo(null);
		System.out.println(map.hasKey(1));
		System.out.println(map.hasKey(2));
		System.out.println(map.get(1));
		System.out.println(map.get(2));
		System.out.println(map.hasValue("testd"));
		map.test();
	}
}