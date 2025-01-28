public class Test {
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		AccessLog log = LogGenerator.generateAccessLog();
		
		long end = System.currentTimeMillis();
		
		long elapsed = end - start;
		
		System.out.printf("%s Elapsed: %d", log, elapsed);
		
	}
	
}