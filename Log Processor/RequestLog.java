import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestLog {
	
	private final static DateTimeFormatter DATE_FORMAT
			= DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss");
	
	private final String ip;
	private final String requestDate;
	private final String httpMethod;
	private final String url;
	private final long responseTime;
	
	public RequestLog(	String ip, 
						String requestDate, 
						String httpMethod, 
						String url, 
						long responseTime	) {
		
		this.ip = ip;
		this.requestDate = requestDate;
		this.httpMethod = httpMethod;
		this.url = url;
		this.responseTime = responseTime;
		
	}
	
	public RequestLog(String line) {
		
		String[] tokens = line.split(" ");
		
		this.ip = tokens[0];
		this.requestDate = tokens[1];
		this.httpMethod = tokens[2];
		this.url = tokens[3];
		this.responseTime = Long.parseLong(tokens[4]);
		
	}
	
	@Override
	public String toString() {
		
		return String.format("%s %s %s %s %s", 
			ip,
			requestDate,
			httpMethod,
			url,
			responseTime);
		
	}
	
	public String getIp() { return this.ip; }
	
	public String getRequestDate() { return this.requestDate; }
	
	public String getHttpMethod() { return this.httpMethod; }
	
	public String getUrl() { return this.url; }
	
	public long getResponseTime() { return this.responseTime; }
	
}