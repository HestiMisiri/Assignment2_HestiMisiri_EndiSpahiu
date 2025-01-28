import java.util.Scanner;

public class CalendarInterface {
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		
		System.out.printf("%s%n%s%n%s%n%s%n%s%n%s%n", 
			"Enter one of the prompts below:",
			"quit -> Ends the application.",
			"display <day> -> Displays all tasks in given day.",
			"add <task> -> Adds a new task to the calendar.",
			"delete <day>,<taskID> -> Deletes task from calendar.",
			"export <fileName> -> Exports the calendar as a file.");
			
		String choice = input.next();
		
		System.out.println(choice);
		
	}
	
}