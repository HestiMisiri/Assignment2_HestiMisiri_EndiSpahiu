// Reference for using Map as KeyPair: https://stackoverflow.com/questions/14677993/how-to-create-a-hashmap-with-two-keys-key-pair-value

public class Calendar {
	
	private SortedMap<String, TreeMap<String, Task>> calendar;
	
	public Calendar() {
		
		calendar = new Map<>();
		
	}
	
	public ArrayList<Task> getTasks(String day) {
		
		ArrayList<Task> tasks = new ArrayList<>();
		
		calendar.get(day)
			.entrySet()
			.forEach();
		
		return new List<Task>();
		
		// Exception handling for unexisting day
		
	}
	
	public void addTask(String task) {
		
		String[] tokens = task.split(",");
		Task newTask = new Task(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
		tokens = null;
		
		// This will require a nested if-else tree to check for whether the date
		// already exists in the Map
		
		if (calendar.containsKey(newTask.getDate())) {
			
			tasks = calendar.get(newTask.getDate());
			
			if(tasks.containsKey(newTask.getID())) {
				
				throw new IllegalArgumentException("There already is another task with this ID.");
				
			} else {
				
				tasks.put(newTask.getID(), newTask);
				
			}
			
		} else {
			
			calendar.put(newTask.getDate(), 
							new TreeMap<String, Task>()
								.put(newTask.getID(), newTask));
			
		}
		
		// Add exception handling in the case of conflicting tasks.
		
	}
	
	public void deleteTask(String day, String taskID) {
		
		calendar.get(day).remove(taskID);
		
		if (calendar.get(day).isEmpty())
			calendar.remove(day);
		
		// Exception handling for unexisting day or id
		
	}
	
	public void export(String fileName) {
		
		
		
	}
	
}