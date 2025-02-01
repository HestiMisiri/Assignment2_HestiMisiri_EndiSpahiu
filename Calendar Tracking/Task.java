import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class Task {
	
	private final LocalDate date;
	private final String taskID;
	private final LocalTime startTime;
	private final long duration;
	private final String description;
	
	public Task(String line) {
		
		String[] tokens = line.split(",");
		if (tokens.length != 5)
			throw new IllegalArgumentException("Invalid task format entered.");
		
		
		this.date = parseDate(tokens[0]);
		
		if(!isValidID(tokens[1]))
			throw new IllegalArgumentException("Invalid ID entered.");
		this.taskID = tokens[1];
		
		this.startTime = parseTime(tokens[2]);
		
		this.duration = Long.parseLong(tokens[3]);
		if (this.duration <= 0)
			throw new IllegalArgumentException("Invalid duration entered.");
		
		if (tokens[4].length() > 200)
			throw new IllegalArgumentException("Description larger than 200 characters.");
		this.description = tokens[4];
		
	}
	
	public Task(LocalDate date, String taskID) {
		
		this.date = date;
		
		if(!isValidID(this.taskID = taskID))
			throw new IllegalArgumentException("Invalid ID entered.");
		
		this.startTime = null;
		this.duration = 0;
		this.description = null;
		
	}

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
	
	public boolean equals(Task other) {
		
		if( this.date.equals(other.getDate()) 
			&& this.taskID.equals(other.getTaskID()) ) {
				
				return true;
				
			}
			
		return false;
		
	}
	
	public static LocalDate parseDate(String date) { 
		
		DateTimeFormatter dateFormat
			= DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate parsedDate = LocalDate.parse(date, dateFormat);
		
		return parsedDate;
		
	}
	
	public static LocalTime parseTime(String time) { 
		
		DateTimeFormatter timeFormat 
			= DateTimeFormatter.ofPattern("HH:mm");
		LocalTime parsedTime = LocalTime.parse(time, timeFormat);
		
		return parsedTime;
		
	}
	
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