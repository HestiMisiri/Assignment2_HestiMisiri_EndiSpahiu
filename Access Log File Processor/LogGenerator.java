import java.security.SecureRandom;
import java.time.LocalDateTime;

public class LogGenerator {
	
	private static final SecureRandom rand = new SecureRandom();
	// Need to research more on how Http Routes are done and how their routes are calculated.
	private static final String[] httpMethods = { "GET", "HEAD", "OPTIONS", "TRACE", "PUT", "DELETE", "POST", "PATCH", "CONNECT" };
	private static final String[] urls = { "/flights", "/login", "/search", "/home", "/book" };
	
	public static AccessLog generateAccessLog() {
		
		// Maybe add an option for null values?
		
		return new AccessLog( generateIp(), generateRequestDate(), generateHttpMethod(), generateUrl(), generateResponseTime() );
		
	}
	
	private static String generateIp() {
		
		StringBuilder ip = new StringBuilder();
		
		// Use an IntStream from the SecureRandom class and use a ForEach lambda to append them to the StringBuilder
		for(int i = 0; i < 4; i++) {
			
			ip.append(rand.nextInt(256));
			if(i < 3)
				ip.append('.');
			
		}
			
		
		return ip.toString();
		
	}
	
	private static String generateRequestDate() {
		
		// Need to figure out how to randomise dates between certain periods.
		
		String requestDate = LocalDateTime.now().toString();
		
		return requestDate;
		
	}
	
	private static String generateHttpMethod() {
		
		String httpMethod = httpMethods[rand.nextInt(httpMethods.length)];
		
		return httpMethod;
		
	}
	
	private static String generateUrl() {
		
		// I could take an argument here to stop certain URLs from generating based on the HttpMethod
		
		String url = urls[rand.nextInt(urls.length)];
		
		return url;
		
	}
	
	private static long generateResponseTime() {
		
		// Could make this better.
		
		long responseTime = rand.nextInt(2000);
		
		return responseTime;
		
	}
	
}