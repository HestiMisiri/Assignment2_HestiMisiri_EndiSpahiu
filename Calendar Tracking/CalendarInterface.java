import java.util.Scanner;

public class CalendarInterface {
	
	public static void main(String[] args) {
		
		Menu menu = new Menu();
		Calendar calendar = new Calendar(args[0]);
		Scanner input = new Scanner(System.in);
		
		// Loops the program until the decides to quit.
		while(true) {
			
			System.out.println("Enter one of the prompts below:");
			// Prints all menu option values that are written.
			for (Menu.MenuOptions option : Menu.MenuOptions.values()) {
				
				System.out.println(option.toString());
				
			}
			
			System.out.print("Select menu option: ");
			// Reads the inputed line, and saves only the first two tokens.
			String[] userInput = input.nextLine().split(" ", 2);
			String selected = userInput[0];
			String argument;
			
			// The loop goes for as long as the method in Menu class returns false.
			while(!menu.setSelected(selected)) {
				
				System.out.printf("%s%n%s", 
					"Unrecognised menu option.", 
					"Try again: " );
				selected = input.nextLine().split(" ", 1)[0];
				
			}
			
			// If there was no parameter given, at the first prompt, asks the
			// user to enter it again. Otherwise, it saves the token given.
			if(userInput.length > 1)
				argument = userInput[1];
			else {
				
				System.out.printf("Give argument for parameter %s: ",
									menu.getSelected().getParameter());
				argument = input.nextLine().split(" ", 1)[0];
				
			}
				
			// The loop goes for as long as the action doesn't complete successfully.
			while(!menu.getSelected().action(calendar, argument)) {
				
				System.out.println(menu.getSelected().getWarningMessage());
				System.out.printf("Give argument for parameter %s: %n",
									menu.getSelected().getParameter());
				argument = input.nextLine().split(" ", 1)[0];
				
			}
			
			System.out.println();
			
		}
		
	}
	
}