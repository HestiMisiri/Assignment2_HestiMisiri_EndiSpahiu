import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class Task implements Comparable<Task> {
	
	private final LocalDate date;
	private final String taskID;
	private final LocalTime startTime;
	private final int duration;
	private final String description;
	
	public Task(String line) {
		
		String[] tokens = line.split(",");
		if (tokens.length != 5)
			throw new IllegalArgumentException("Invalid task format entered.");
		
		// Time objects throw their own exceptions.
		this.date = parseDate(tokens[0]);
		
		if(!isValidID(tokens[1]))
			throw new IllegalArgumentException("Invalid ID entered.");
		this.taskID = tokens[1];
		
		// Time objects throw their own exceptions.
		this.startTime = parseTime(tokens[2]);
		
		this.duration = Integer.parseInt(tokens[3]);
		if (this.duration <= 0)
			throw new IllegalArgumentException("Invalid duration entered.");
		
		if (tokens[4].length() > 200)
			throw new IllegalArgumentException("Description larger than 200 characters.");
		this.description = tokens[4];
		
	}
	
	// This constructor is only used to create Task objects to be used in comparisons.
	public Task(LocalDate date, String taskID) {
		
		this.date = date;
		
		if(!isValidID(this.taskID = taskID))
			throw new IllegalArgumentException("Invalid ID entered.");
		
		this.startTime = LocalTime.now();
		this.duration = 0;
		this.description = null;
		
	}
	
	// This method checks if a Task instance has conflicting times with another Task.
	public boolean isConflictingWith(Task other) {
		
		if ( (this.getStartDateTime()
				.isBefore(other.getEndDateTime()) 
			 && this.getEndDateTime()
				.isAfter(other.getEndDateTime()))
				
			 || (this.getStartDateTime()
				.isBefore(other.getStartDateTime())
			 && this.getEndDateTime()
				.isAfter(other.getStartDateTime())) 
				
			 || (this.getStartDateTime()
				.isAfter(other.getStartDateTime()) 
			 && this.getEndDateTime()
				.isBefore(other.getEndDateTime())) ) {
					
					return true;
				
		}
		
		return false;
		
	}
	
	@Override
	public String toString() {
		
		return String.format("%s,%s,%s,%s,%s",
			date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
			taskID,
			startTime,
			duration,
			description);
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj)
			return true;
		
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		Task other = (Task) obj;
		if( date.equals(other.getDate()) 
			&& taskID.equals(other.getTaskID()) ) {
				
				return true;
				
			}
			
		return false;
		
	}
	
	// Used to order Tasks in SortedSets by chronological order, and check for 
	// equivalent instances of Task objects.
	@Override
	public int compareTo(Task other) {
		
		// Since SortedSets use the compareTo() method to check for equivalence,
		// we included the equals() method in here.
		if (this.equals(other))
			return 0;
		
		// We compare specifically by startTime since the date ordering is handled 
		// by the Map instance in the Calendar class.
		return this.startTime.compareTo(other.getStartTime());
		
	}
	
	// Method to parse Strings into LocalDate objects using a DateTimeFormatter.
	public static LocalDate parseDate(String date) { 
		
		DateTimeFormatter dateFormat
			= DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate parsedDate = LocalDate.parse(date, dateFormat);
		
		return parsedDate;
		
	}
	
	// Method to parse Strings into LocalTime objects using a DateTimeFormatter.
	public static LocalTime parseTime(String time) { 
		
		DateTimeFormatter timeFormat 
			= DateTimeFormatter.ofPattern("HH:mm");
		LocalTime parsedTime = LocalTime.parse(time, timeFormat);
		
		return parsedTime;
		
	}
	
	// Checks if a String is a valid ID format.
	public static boolean isValidID(String taskID) { 
	
		if(Pattern.matches("^T[0-9]+", taskID)) return true;
		
		return false;
		
	}
	
	// Used for finding conflicting times.
	public LocalDateTime getStartDateTime() { 
	
		return this.date.atTime(startTime); 
		
	}
	
	// Used for finding conflicting times.
	public LocalDateTime getEndDateTime() { 
	
		return this.getStartDateTime().plusMinutes(duration);
		
	}
	
	public LocalDate getDate() { return this.date; }
	
	public String getTaskID() { return this.taskID; }
	
	public LocalTime getStartTime() { return this.startTime; }
	
	public long getDuration() { return this.duration; }
	
	public String getDescription() { return this.description; }
	
}