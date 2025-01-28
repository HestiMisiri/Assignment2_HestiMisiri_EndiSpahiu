public final class AccessLog {
	
	private final String ip;
	private final String requestDate;	// I should probably turn this to a DateTime datatype to simplify comparisons
	private final String httpMethod;
	private final String url;
	private final long responseTime;
	
	public AccessLog(String ip, String requestDate, String httpMethod, String url, long responseTime) {
		
		this.ip = ip;
		this.requestDate = requestDate;
		this.httpMethod = httpMethod;
		this.url = url;
		this.responseTime = responseTime;
		
	}
	
	public String getIp() {
		
		return this.ip;
		
	}
	
	public String getRequestDate() {
		
		return this.requestDate;
		
	}
	
	public String getHttpMethod() {
		
		return this.httpMethod;
		
	}
	
	public String getUrl() {
		
		return this.url;
		
	}
	
	public long getResponseTime() {
		
		return this.responseTime;
		
	}
	
	@Override
	public String toString() {
		
		return this.ip + " " + 
			this.requestDate + " " + 
			this.httpMethod + " " + 
			this.url + " " + 
			this.responseTime;
		
	}
	
	// I need to add some hashing stuff in here maybe.
	
}