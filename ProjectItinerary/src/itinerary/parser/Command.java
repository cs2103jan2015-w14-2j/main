package itinerary.parser;

import itinerary.main.Task;

//@author A0121437N
public class Command {
	private Task task;
	private CommandType type;
	
	//@author generated
	public Command(Task task, CommandType type) {
		super();
		this.task = task;
		this.type = type;
	}

	public Task getTask() {
		return task;
	}

	public CommandType getType() {
		return type;
	}
}
