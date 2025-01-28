import java.io.IOException;
import java.nio.file.DirectoryStream; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class LogFileGenerator {
	
	public static void main(String[] args) {
		
		var input = new Scanner(System.in);
		
		System.out.println("How many files do you wish to generate?");
		System.out.print("Amount: ");
		int fileAmount = input.nextInt();
		
		System.out.println();
		
		int lineAmounts[] = new int[fileAmount];
		for(int i = 0; i < fileAmount; i++) {
			
			System.out.printf("How many lines should file %d have?%n", i + 1);
			System.out.print("Amount: ");
			lineAmounts[i] = input.nextInt();
			
		}
		
		System.out.println();
		
		System.out.println("Where should the files be saved?");
		System.out.print("Directory Path: ");
		String directoryPath = input.next();
		
		for(int i = 0; i < fileAmount; i++) {
			
			Path filePath = Paths.get(directoryPath, String.format("AccessLog%d.txt", i + 1));
			
			try(var out = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE)) {
				
				for(int j = 0; j < lineAmounts[i]; j++) {
					
					out.write(LogGenerator.generateAccessLog().toString());
					out.newLine();
					
				}
					
			} catch (IOException e){
				
				e.printStackTrace();
				System.exit(1); // terminate the program
				
			}
			
		}
		
	}
	
}