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
	
	private Map<LocalDate, TreeSet<Task>> calendar = new TreeMap<>();
	
	@SuppressWarnings("finally")
	public Calendar(String fileName) {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			
			String line = null;  
			while ((line = reader.readLine()) != null)  
			{  
		
			   try { 
				
					addTask(line); 
					
				} catch(Exception e) { 
				
					System.out.println(e.getMessage()); 
					
				}  finally {
					
					continue;
					
				} 
				
			} 
			
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
			
		}
		
		System.out.println();
		
	}
	
	public Collection<Task> getTasks(String date) {
		
		LocalDate parsedDate;
		try {
			
			parsedDate = Task.parseDate(date);
			
		} catch (Exception e) {
			
			throw new IllegalArgumentException("Invalid date format entered.");
			
		}
		
		if (calendar.containsKey(parsedDate)) {
			 
			return calendar.get(parsedDate);
			
		} else {
			
			throw new IllegalArgumentException("There are not tasks logged on this date.");
			
		}
		
	}
	
	public void addTask(String line) {
		
		Task task = new Task(line);
		
		if (calendar.containsKey(task.getDate())) {
			
			SortedSet<Task> tasks = calendar.get(task.getDate());
			
			if (tasks.contains(task)) {
				
				throw new IllegalArgumentException("There already is another task with this ID or start time on this date.");
				
			} else {
				
				Task conflict = findConflicts(task);
				if (conflict != null) {
					
					throw new IllegalArgumentException(String.format("%s[%s]%n%s[%s]", 
								"The task ", 
								task.toString(),
								"is conflicting with ", 
								conflict.toString()));
					
				}
				
				tasks.add(task);
				
			}
			
		} else {
			
			calendar.put(task.getDate(), new TreeSet<>());
			calendar.get(task.getDate()).add(task);
			
		}
		
	}
	
	public void deleteTask(String date, String taskID) {
		
		LocalDate parsedDate;
		try {
			
			parsedDate = Task.parseDate(date);
			
		} catch (Exception e) {
			
			throw new IllegalArgumentException("Invalid date format entered.");
			
		}
		
		if (calendar.containsKey(parsedDate)) {
			
			if (!calendar.get(parsedDate).remove(new Task(parsedDate, taskID))) {
				
				throw new IllegalArgumentException(
					"There is no task with this ID on this date."
				);
				
			} 
			
		} else {
			
			throw new IllegalArgumentException(
				"There are not tasks logged on this date."
			);
			
		}
		
		
		if (calendar.get(parsedDate).isEmpty())
			calendar.remove(parsedDate);
		
	}
	
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
	
	private Task findConflicts(Task task) {
		
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
		
		LocalDate nxtDate = task.getDate().plusDays(1);
		if(calendar.containsKey(nxtDate)) {
			
			Task other = calendar.get(prvDate).first();
			
			if(task.isConflictingWith(other))
				return other;
			
		}		
		
		return null;
		
	}
	
}