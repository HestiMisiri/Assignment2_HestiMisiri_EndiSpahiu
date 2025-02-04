import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;

public class Calendar {
	
	// TreeMap object which orders Tasks by using their Date as the key.
	// LocalDate has its own comparison logic.
	// Tasks are stored in TreeSets to prevent Tasks with the same ID, and
	// to provide ordering for them via their startTime.
	private Map<LocalDate, TreeSet<Task>> calendar = new TreeMap<>();
	
	// Reads into the calendar all tasks stored in the text file using
	// addTask() method, while ignoring all conflicting tasks.
	public Calendar(String fileName) {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			
			String line = null;  
			while ((line = reader.readLine()) != null)  
			{  
		
				try { 
				
					addTask(line); 
					
				} catch(Exception e) { 
				
					// If there is a conflicting task, it will print out
					// the conflict and then move on to other tasks.
					System.out.println(e.getMessage()); 
					
				}
				
			} 
			
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
			
		}
		
		System.out.println();
		
	}
	
	// Gets all tasks stored on a specific date
	public Collection<Task> getTasks(String date) {
		
		// Checks if the given date is in the right format.
		LocalDate parsedDate;
		try {
			
			parsedDate = Task.parseDate(date);
			
		} catch (Exception e) {
			
			throw new IllegalArgumentException("Invalid date format entered. Use 'dd/MM/YYYY'.");
			
		}
		
		// Checks if tasks exist on the given date.
		if (calendar.containsKey(parsedDate)) {
			 
			return calendar.get(parsedDate);
			
		} else {
			
			// If a date doesn't exist as a key, then there should not be any tasks
			// logged on that date.
			throw new IllegalArgumentException("There are not tasks logged on this date.");
			
		}
		
	}
	
	// Adds new Tasks based on the given format for them.
	public void addTask(String line) {
		
		// If this fails, it will throw an exception handled by the Menu class.
		Task task = new Task(line);
		
		// Checks if this date has any logged tasks.
		if (calendar.containsKey(task.getDate())) {
			
			// We get the object reference for the collection 
			// of Tasks on the date.
			SortedSet<Task> tasks = calendar.get(task.getDate());
			
			if (tasks.contains(task)) {
				
				throw new IllegalArgumentException("There already is another task with this ID or start time on this date.");
				
			} else {
				
				// Finds if there are any time conflicts caused by the Task.
				Task conflict = findConflicts(task);
				if (conflict != null) {
					
					throw new IllegalArgumentException(String.format("%s[%s]%n%s[%s]", 
								"The task ", 
								task.toString(),
								"is conflicting with ", 
								conflict.toString()));
					
				}
				
				// Adds task to collection.
				tasks.add(task);
				
			}
			
		} else {
			// Since the date doesn't exist as a key, it will add it to the map
			// and then the new associated Task with it.
			calendar.put(task.getDate(), new TreeSet<>());
			calendar.get(task.getDate()).add(task);
			
		}
		
	}
	
	// Removes a task based on its date and ID.
	public void deleteTask(String date, String taskID) {
		
		// Checks if the given date is in the right format.
		LocalDate parsedDate;
		try {
			
			parsedDate = Task.parseDate(date);
			
		} catch (Exception e) {
			
			throw new IllegalArgumentException("Invalid date format entered.");
			
		}
		
		// Checks if this date has any logged tasks.
		if (calendar.containsKey(parsedDate)) {
			
			// Since remove() returns a boolean value, checks if the Task
			// exists or not. Also, creates a new instance Task using the
			// special constructor provided by the Task class, since remove()
			// requires an object of the same type to function.
			if (!calendar.get(parsedDate).remove(new Task(parsedDate, taskID))) {
				
				throw new IllegalArgumentException("There is no task with this ID on this date.");
				
			} 
			
		} else {
			
			throw new IllegalArgumentException("There are not tasks logged on this date.");
			
		}
		
		// Removes the date key if there are no more tasks logged on it.
		if (calendar.get(parsedDate).isEmpty())
			calendar.remove(parsedDate);
		
	}
	
	// Exports the Calendar collection to a file in chronological order.
	public void export(String fileName) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			
			for (Collection<Task> tasks : calendar.values()) {
				
				for (Task task : tasks) {
					
					writer.write(task.toString());
					writer.newLine();
					
				}
				
			}
			
		} catch (IOException e) {
			
			throw new IllegalArgumentException("Invalid file name.");
			
		}
		
	}
	
	// Special method used to find conflicts between different dates.
	// Returns the Task which its conflicting with.
	private Task findConflicts(Task task) {
		
		// Checks if there is overlap with a Task entered the previous
		// date which may span up to the this date.
		LocalDate prvDate = task.getDate().minusDays(1);	
		if(calendar.containsKey(prvDate)) {
			
			Task other = calendar.get(prvDate).last();
			
			if(task.isConflictingWith(other))
				return other;
			
		}
		
		for (Task other : calendar.get(task.getDate())) {
			
			if(task.isConflictingWith(other))
				return other;
			
		}
		
		// Checks if there is overlap with a Task entered the next
		// day which this task may span up to.
		LocalDate nxtDate = task.getDate().plusDays(1);
		if(task.getEndDateTime().toLocalDate().equals(nxtDate)
			&& calendar.containsKey(nxtDate)) {
			
			Task other = calendar.get(prvDate).first();
			
			if(task.isConflictingWith(other))
				return other;
			
		}		
		
		// Returns null if it doesn't find any conflicts.
		return null;
		
	}
	
}