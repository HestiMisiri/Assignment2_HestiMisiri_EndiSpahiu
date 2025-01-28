public class Task {
	
	private final String date; // Might be a datetime object
	private final String taskID;
	private final String startTime; // Might be a datetime object
	private final int duration;
	private final String description;
	
	public Task(String date, String taskID, String startTime, 
					int duration, String description) {
		
		this.date = date;
		this.taskID = taskID;
		this.startTime = startTime;
		this.duration = duration;
		this.description = description;
		
	}
	
	// May require a comparison method here and to implement the Comparable interface
	
	// Might return a datetime object
	public String getDate() {
		
		return this.date;
		
	}
	
	public String getTaskID() {
		
		return this.taskID
		
	}
	
	// Might return a datetime object
	public String getStartTime() {
		
		return this.startTime;
		
	}
	
	// Might return a datetime object
	// Requires some datetime calculations
	public String getEndTime() {
		
		return new String();
		
	}
	
	public int getDuration() {
		
		return this.duration;
		
	}
	
	public String getDescription() {
		
		return this.description;
		
	}
	
}