package itinerary.main;

//@author A0121437N
public class Command {
	private Task task;
	private CommandType type;
	private String message;
	
	//@author generated
	public Command(Task task, CommandType type, String message) {
		super();
		this.task = task;
		this.type = type;
		this.message = message;
	}

	public Task getTask() {
		return task;
	}

	public CommandType getType() {
		return type;
	}
	
	public String getMessage(){
		return message;
	}
}
