public class Test {
	public static void main(String[] args) {
		
		BSTMap<Integer, String> map = new BSTMap<Integer, String>();
		map.put(1, "test");
		map.put(2, "testd");
		//map.put(2, "doiwdf");
		//int result = "diwfd".compareTo(null);
		System.out.println(map.hasKey(1));
		System.out.println(map.hasKey(2));
		System.out.println(map.get(1));
		System.out.println(map.get(2));
	}
}