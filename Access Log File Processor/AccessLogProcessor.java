import java.io.*;
import java.io.IOException;
import java.nio.*;
import java.nio.file.DirectoryStream; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.stream.Collectors;

public class AccessLogProcessor {
	
	public static void main(String[] args) {
		
		var input = new Scanner(System.in);
		
		System.out.println("Where are the Access Log files located?");
		System.out.print("Directory Path: ");
		Path dir = Paths.get(input.next()); 
		
		System.out.println();
		
		try(DirectoryStream<Path> dirStream = 
				Files.newDirectoryStream(dir)) {
			
			for(Path entry : dirStream) {
				
				long start = System.currentTimeMillis();
				
				BufferedReader reader = 
					new BufferedReader(new FileReader(entry.toString()), 16384);
				
				Map<String, Long> map = reader.lines()
					.map(AccessLogProcessor::ProcessLine)
					.collect(Collectors.groupingBy(AccessLog::getIp, HashMap::new, Collectors.counting()))
					.entrySet().stream()
					.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.limit(10)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
							
				long end = System.currentTimeMillis();
				
				long elapsed = end - start;
				
				System.out.printf("Elapsed: %d", elapsed);
					
				
			}
			
		} catch(IOException e) {
			
			e.printStackTrace();
			System.exit(1); // terminate the program
			
		}
		
	}
	
	private static AccessLog ProcessLine(String line) {
		
		String[] tokens = line.split("\\s");
		
		return new AccessLog(tokens[0], tokens[1], tokens[2], tokens[3], Long.parseLong(tokens[4]));
		
	}
	
	private static String ProcessLogString(String line) {
		
		String[] tokens = line.split("\\s");
		
		return tokens[0];
		
	}
	
}