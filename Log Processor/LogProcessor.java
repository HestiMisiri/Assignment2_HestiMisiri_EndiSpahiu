import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.PriorityQueue;

public class LogProcessor {
	
	// Initialise a HashMap to reference IPCountPair objects which hold the count
	// for each logged ip. We set an initial size to the map, which is then fixed
	// by another method in this class.
	private static Map<String, IPCountPair> ipCounts 
		= new HashMap<>(65538, (float) 1);
	// Used to order the client IPs with most requests. Also acts as a buffer
	// for ipCounts when its cleared later on.
	private static TreeSet<IPCountPair> topClientIPs = new TreeSet<>();
	
	// Stores RouteStats based on their Route as the key for the Map.
	// Has an initial size to lower how often the map resizes.
	private static Map<String, RouteStats> routes = new HashMap<>(64);
	
	// Priority queue which stores the RequestLog with longest response time.
	private static PriorityQueue<RequestLog> longestResponse 
		= new PriorityQueue<>(Comparator
								.comparingLong(RequestLog::getResponseTime));
	
	public static void main(String[] args) {
		
		long time = System.currentTimeMillis();
		// For some reason, the forEach() method in Streams requires this to be
		// a final variable. 
		final int[] totalRequests = {0};
		
		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
			
			String line = null;
			while( (line = reader.readLine()) != null ) {
				
				totalRequests[0]++;
				String[] tokens = line.split(" ");
				
				String ip = tokens[0];
				String requestDate = tokens[1];
				String httpMethod = tokens[2];
				String url = tokens[3];
				long responseTime = Long.parseLong(tokens[4]);
				
				updateIpCounts(ip);
				updateShortestRoutes(httpMethod, url, responseTime);
				updateLongestResponse( new RequestLog(ip, 
													requestDate, 
													httpMethod, 
													url, 
													responseTime) );
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			System.exit(1);
			
		}
		
		time = System.currentTimeMillis() - time;
		System.out.printf("Elapsed: %s%n", time);
		
		System.out.printf("Total number of requests: %s%n", totalRequests[0]);

        System.out.println("\nTop 10 client IPs by request count:");
        topClientIPs.descendingSet()
					.stream()
					.limit(10)
					.forEach(e -> System.out.printf("%s (%.2f%%)\n",
							e, (double) e.getCount() / totalRequests[0]));
						
		System.out.println("\nTop 10 fastest routes by average response time:");
        routes.values()
				.stream()
				.limit(10)
                .forEach(route -> System.out.println(route));
						
		System.out.println("\nTop 10 slowest requests:");
        longestResponse.stream()
						.sorted(longestResponse.comparator().reversed())
						.forEach(request -> System.out.println(request));
		
	}
	
	// Updates the IP count for every unique ClientIP.
	private static void updateIpCounts(String ip) {
		
		// Checks if this IP has been processed before.
		if(ipCounts.containsKey(ip)) {
			
			ipCounts.get(ip).incrementCount();
			
		} else {
			
			ipCounts.put(ip, new IPCountPair(ip));
			
		}
		
		IPCountPair temp = ipCounts.get(ip);
		if (topClientIPs.size() < 10 
			|| topClientIPs.first().compareTo(temp) > 0) {
			
			topClientIPs.add(temp);
			
		}
		
		// Clears the map to save up on memory and not have the program crash.
		// The max size chosen seems to have the best mix between speed
		// and accuaracy.
		// After clearing, stores all IPs found inside topClientIPs.
		if (ipCounts.size() > 65537) {
			
            ipCounts.clear();
			for(IPCountPair pair : topClientIPs) {
				
				ipCounts.put(pair.getIP(), pair);
				
			}
			
        }
		
		// The number used is the buffer size.
		if(topClientIPs.size() > 256) {
			
			topClientIPs.pollFirst();
			
		}
		
	}
	
	// Updates route stats.
	private static void updateShortestRoutes(String httpMethod, 
											String url, 
											long responseTime) {
		
		String route = httpMethod + " " + url;

        if(routes.containsKey(route)) {
			
			routes.get(route).addRequest(responseTime);
			
		} else {
			
			routes.put(route, new RouteStats(route, responseTime));
			
		}
		
	}
	
	// Updates longestResponse when a new request with a longer response time
	// is found.
	private static void updateLongestResponse(RequestLog log) {
		
		if ( longestResponse.peek() == null
			|| longestResponse.comparator()
								.compare(longestResponse.peek(), log) < 0 ) {
			
			longestResponse.offer(log);
			
		}

		if (longestResponse.size() > 10) {
			
			longestResponse.poll();
			
		}
		
    }
	
	// Classes used to store information for processing logs.
	
	private static class IPCountPair implements Comparable<IPCountPair> {
	
		private final String ip;
		private int count = 1;
		
		public IPCountPair(String ip) { 
		
			this.ip = ip; 
		
		}
		
		public void incrementCount() { count++; }
		
		@Override
		public int compareTo(IPCountPair other) {
			
			if (this == other)
				return 0;
			
			if( (this.count - other.getCount()) == 0 ) {
				
				// Returns a constant value since there are cases where
				// two different IPs have the same count.
				return -1;
				
			} else {
				
				return count - other.getCount();
				
			}
			
		}
		
		@Override
		public String toString() {
			
			return String.format("Client IP: %s, Request Count: %s",
								ip,
								count);
			
		}
		
		public String getIP() { return this.ip; }
		
		public int getCount() { return this.count; }
		
	}
	
	private static class RouteStats implements Comparable<RouteStats> {
		
		private final String route;
		private int count = 1;
		private long totalResponseTime;
		
		public RouteStats(String route, long responseTime) {
			
			this.route = route;
			this.totalResponseTime = responseTime;
			
		}
		
		public void addRequest(long responseTime) {
			
			count++;
			totalResponseTime += responseTime;
			
		}
		
		@Override
		public int compareTo(RouteStats other) {
			
			if(this == other || this.route.equals(other.getRoute()))
				return 0;
			
			if ( (this.getAverageResponseTime() 
				- other.getAverageResponseTime()) == 0 ) {
				
				// Returns a constant value since there are cases where
				// two different routes have the same avg. response time.
				return -1;
				
			} else {
				
				return (int) ( this.getAverageResponseTime() 
								- other.getAverageResponseTime() );
				
			}
			
		}
		
		@Override
		public String toString() {
			
			return String.format("Route: %s, Average Response Time: %s",
					this.getRoute(),
					this.getAverageResponseTime() );
			
		}
		
		public long getAverageResponseTime() {
			
			return totalResponseTime / count;
			
		}
		
		public String getRoute() { return this.route; }
		
		public int getCount() { return this.count; }
		
		public long getTotalResponseTime() { return this.totalResponseTime; }
		
		public void setCount(int count) { this.count = count; }
		
		public void setTotalResponseTime(long totalResponseTime) {
			
			this.totalResponseTime = totalResponseTime;
			
		}
		
	}
	
}