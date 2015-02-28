package itinerary.main;

//@author A0121437N
public class Logic {
	private static final String MESSAGE_UNDO_NOTHING = "nothing to undo";

	private static final String MESSAGE_INVALID_COMMAND = "invalid command: \"%1$s\"";
	
	private Storage storage;
	private Search search;
	private History history;
	
	public Logic (String fileName) {
		this.storage = new StorageStub(fileName);
		this.search = new Search();
		this.history = new History();
	}
	
	public UserInterfaceContent executeUserInput (String userInput) {
		// TODO Replace temporary command constructor with Parser when completed
		Task task = new Task(1, "Test", null, false, false);
		Command userCommand = new Command(task, CommandType.ADD);
		// Determine actions to be taken and take them
		// Return List of Tasks and console message to UI
		return determineActions(userCommand, userInput);
	}
	
	private UserInterfaceContent determineActions (Command command, String userInput) {
		if (command.getType() == CommandType.ADD) {
			return executeAdd(command);
		} else if (command.getType() == CommandType.CLEAR) {
			return executeClear(command);
		} else if (command.getType() == CommandType.DELETE) {
			return executeDelete(command);
		} else if (command.getType() == CommandType.DISPLAY) {
			return executeDisplay();
		} else if (command.getType() == CommandType.EDIT) {
			return executeEdit(command);
		} else if (command.getType() == CommandType.SEARCH) {
			return executeSearch(command);
		} else if (command.getType() == CommandType.REDO) {
			return executeRedo();
		} else if (command.getType() == CommandType.UNDO) {
			return executeUndo();
		} else {
			return unknownCommand (userInput);
		}
	}

	private UserInterfaceContent unknownCommand(String userInput) {
		String consoleMessage = String.format(MESSAGE_INVALID_COMMAND, userInput);
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}

	private UserInterfaceContent executeUndo() {
		State newState = history.goBack();
		if (newState == null) {
			return new UserInterfaceContent(MESSAGE_UNDO_NOTHING, storage.getAllTasks());
		}
		Command undoCommand = newState.getUndoCommand();
		if (undoCommand.getType() == CommandType.ADD) {
			return executeAdd(undoCommand);
		} else if (undoCommand.getType() == CommandType.CLEAR) {
			return executeClear(undoCommand);
		} else if (undoCommand.getType() == CommandType.DELETE) {
			return executeDelete(undoCommand);
		} else if (undoCommand.getType() == CommandType.EDIT) {
			return executeEdit(undoCommand);
		}
		return null;
	}

	private UserInterfaceContent executeRedo() {
		// TODO Auto-generated method stub
		return null;
	}

	private UserInterfaceContent executeSearch(Command command) {
		// TODO Auto-generated method stub
		return null;
	}

	private UserInterfaceContent executeEdit(Command command) {
		// TODO Auto-generated method stub
		return null;
	}

	private UserInterfaceContent executeDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	private UserInterfaceContent executeDelete(Command command) {
		// TODO Auto-generated method stub
		return null;
	}

	private UserInterfaceContent executeClear(Command command) {
		// TODO Auto-generated method stub
		return null;
	}

	private UserInterfaceContent executeAdd(Command command) {
		// TODO Auto-generated method stub
		return null;
	}
}
