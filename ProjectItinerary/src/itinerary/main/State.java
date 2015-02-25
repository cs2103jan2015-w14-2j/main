package itinerary.main;

import java.util.List;

//@author A0121437N
public class State {
	private Command doCommand;
	private Command undoCommand;
	private List<Task> tasks;
	private boolean isSuccessful;
	
	//@author generated
	public State(Command doCommand, Command undoCommand, List<Task> tasks,
			boolean isSuccessful) {
		super();
		this.doCommand = doCommand;
		this.undoCommand = undoCommand;
		this.tasks = tasks;
		this.isSuccessful = isSuccessful;
	}

	public Command getDoCommand() {
		return doCommand;
	}

	public Command getUndoCommand() {
		return undoCommand;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}
}
