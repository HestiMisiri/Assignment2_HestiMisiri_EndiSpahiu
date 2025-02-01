import java.util.Collection;

public class Menu {
		
	private MenuOptions selected;
	
	public enum MenuOptions {
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
		
		public boolean action(Calendar calendar, String value) { 
		
			return true; 
			
		}
		
		private String warningMessage;
		private final String optionName;
		private final String parameter;
		private final String description;
		
		MenuOptions(String optionName, String parameter, String description) {
			
			this.optionName = optionName;
			this.parameter = parameter;
			this.description = description;
			
		}
		
		@Override
		public String toString() {
			
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
		
		public void setWarningMessage(String warningMessage) { 
		
			this.warningMessage = warningMessage; 
			
		}
		
	}
	
	public MenuOptions getSelected() {
		
		return this.selected;
		
	}
	
	public boolean setSelected(String selected) {
		
		selected = selected.toLowerCase();
		
		for (MenuOptions option : MenuOptions.values()) {
			
			if(selected.equals(option.getOptionName())) {
				
				if(option == MenuOptions.QUIT) {
					
					System.out.println("Goodbye!");
					System.exit(0);
					
				}
				
				this.selected = option;
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
}