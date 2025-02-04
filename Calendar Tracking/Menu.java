import java.util.Collection;

public class Menu {
	
	// Instantiate an enum object of a value, to be used in the interface.
	private MenuOptions selected;
	
	// An enum type which stores all menu options as constants, which when
	// initialised, construct instances with details regarding their usage
	// and with a special implementation of the action() method regarding
	// their use.
	public enum MenuOptions {
		// QUIT exists only to be identified by the method below and end the app.
		QUIT("quit", null, "Ends the application."),
		DISPLAY ("display", "<date>", "Displays all tasks in given day.") {
			
			@Override
			public boolean action(Calendar calendar, String value) {
				
				try {
					
					Collection<Task> tasks = calendar.getTasks(value);
					System.out.println();
					tasks.iterator().forEachRemaining( i -> System.out.println(i) );
					
					return true;
					
				} catch (Exception e) {
					
					setWarningMessage(e.getMessage());
					return false;
					
				}
				
			}
			
		},
		ADD ("add", "<task>", "Adds a new task to the calendar.") {
			
			@Override
			public boolean action(Calendar calendar, String value) {
				
				try {
					
					calendar.addTask(value);
					return true;
					
				} catch (Exception e) {
					
					setWarningMessage(e.getMessage());
					return false;
					
				}
				
			}
			
		},
		DELETE ("delete", "<date>,<taskID>", "Deletes task from calendar.") {
			
			@Override
			public boolean action(Calendar calendar, String value) {
				
				try {
					
					String[] tokens = value.split(",");
					calendar.deleteTask(tokens[0], tokens[1]);
					return true;
					
				} catch (Exception e) {
					
					setWarningMessage(e.getMessage());
					return false;
					
				}
				
			}
			
		},
		EXPORT ("export", "<fileName>", "Exports the calendar as a file.") {
			
			@Override
			public boolean action(Calendar calendar, String value) {
				
				try {
					
					calendar.export(value);
					return true;
					
				} catch (Exception e) {
					
					setWarningMessage(e.getMessage());
					return false;
					
				}
				
			}
			
		};
		
		// Method overriden by constants based on their use case. Returns
		// boolean value to check if the action completed or not.
		public boolean action(Calendar calendar, String value) { 
		
			return true; 
			
		}
		
		private String warningMessage;
		// These instance variables are used in the interface to create
		// a listing of the menu options, usage and description.
		private final String optionName;
		private final String parameter;
		private final String description;
		
		// Constructor is initialised by enum values, with values acquired
		// by those constants.
		MenuOptions(String optionName, String parameter, String description) {
			
			this.optionName = optionName;
			this.parameter = parameter;
			this.description = description;
			
		}
		
		@Override
		public String toString() {
			
			// Special case when an MenuOption has no parameter (e.g. QUIT).
			if(parameter == null) {
				
				return String.format("%s -> %s", 
										optionName,
										description);
				
			}
		
			return String.format("%s %s -> %s", 
									optionName, 
									parameter, 
									description);
			
		}
		
		public String getWarningMessage() { return this.warningMessage; }
		
		public String getOptionName() { return this.optionName; }
		
		public String getParameter() { return this.parameter; }
		
		public String getDescription() { return this.description; }
		
		// Used by action() method to change error messages based on the error
		// thrown by the Calendar or Task class.
		public void setWarningMessage(String warningMessage) { 
		
			this.warningMessage = warningMessage; 
			
		}
		
	}
	
	// Gets the current initialised enum constant.
	public MenuOptions getSelected() {
		
		return this.selected;
		
	}
	
	// Initialises a new enum constant to be used. 
	public boolean setSelected(String selected) {
		
		selected = selected.toLowerCase();
		
		for (MenuOptions option : MenuOptions.values()) {
			
			if(selected.equals(option.getOptionName())) {
				
				// Special case for QUIT constant to end the application.
				if(option == MenuOptions.QUIT) {
					
					System.out.println("Goodbye!");
					System.exit(0);
					
				}
				
				// Sets selected based on the constant it matched.
				this.selected = option;
				return true;
				
			}
			
		}
		
		// Returns false if it cannot match any constants name.
		return false;
		
	}
	
}