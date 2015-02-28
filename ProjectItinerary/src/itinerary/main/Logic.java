package itinerary.main;

//@author A0121437N
public class Logic {
	private static final String MESSAGE_DELETE_FAIL = "failed to delete";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted task %1$s";
	private static final String MESSAGE_CLEAR_FAIL = "failed to clear all tasks";
	private static final String MESSAGE_CLEAR_SUCCESS = "cleared all tasks";
	private static final String MESSAGE_ADD_SUCCESS = "added task %1$d";
	private static final String MESSAGE_ADD_FAIL = "failed to add task";
	private static final String MESSAGE_EDIT_SUCCESS = "edited task %1$d";
	private static final String MESSAGE_EDIT_FAIL = "failed to edit task";
	private static final String MESSAGE_DISPLAY_ALL = "displaying all tasks";
	private static final String MESSAGE_REDO_ERROR = "redo error";
	private static final String MESSAGE_REDO_NOTHING = "nothing to redo";
	private static final String MESSAGE_REDO_SUCCESS = "redo successful";
	private static final String MESSAGE_UNDO_ERROR = "undo error";
	private static final String MESSAGE_UNDO_NOTHING = "nothing to undo";
	private static final String MESSAGE_UNDO_SUCCESS = "undo successful";
	

	private static final String MESSAGE_INVALID_COMMAND = "invalid command: \"%1$s\"";
	
	private Storage storage;
	private Search search;
	private History history;
	
	public Logic (String fileName) {
		this.storage = new StorageStub(fileName);
		this.search = new Search();
		this.history = new History();
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
	
	private UserInterfaceContent executeAdd(Command command) {
		State newState = storage.addTask(command);
		if (newState.isSuccessful()) {
			history.addNewState(newState);
			String text = newState.getDoCommand().getTask().getText();
			String consoleMessage = formatAddSuccess(text);
			return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_ADD_FAIL, storage.getAllTasks());
	}

	private UserInterfaceContent executeClear(Command command) {
		State newState = storage.clearAll();
		if (newState.isSuccessful()) {
			history.addNewState(newState);
			return new UserInterfaceContent(MESSAGE_CLEAR_SUCCESS, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_CLEAR_FAIL, storage.getAllTasks());
	}

	private UserInterfaceContent executeDelete(Command command) {
		State newState = storage.deleteTask(command);
		if (newState.isSuccessful()) {
			history.addNewState(newState);
			int deleteLineNumber = newState.getDoCommand().getTask().getLineNumber();
			String consoleMessage = formatDeleteSuccess(deleteLineNumber);
			return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_DELETE_FAIL, storage.getAllTasks());
	}

	private UserInterfaceContent executeDisplay() {
		return new UserInterfaceContent(MESSAGE_DISPLAY_ALL, storage.getAllTasks());
	}
	
	private UserInterfaceContent executeEdit(Command command) {
		State newState = storage.editTask(command);
		if (newState.isSuccessful()) {
			history.addNewState(newState);
			int editLineNumber = newState.getDoCommand().getTask().getLineNumber();
			String consoleMessage = formatEditSuccess(editLineNumber);
			return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_EDIT_FAIL, storage.getAllTasks());
	}

	private UserInterfaceContent executeRedo() {
		State newState;
		if (history.getCurrentState() == null || (newState = history.goForward()) == null) {
			return new UserInterfaceContent(MESSAGE_REDO_NOTHING, storage.getAllTasks());
		}
		
		State redoState;
		Command doCommand = newState.getDoCommand();
		
		if (doCommand.getType() == CommandType.ADD) {
			redoState = storage.addTask(doCommand);
		} else if (doCommand.getType() == CommandType.CLEAR) {
			redoState = storage.clearAll();
		} else if (doCommand.getType() == CommandType.DELETE) {
			redoState = storage.deleteTask(doCommand);
		} else if (doCommand.getType() == CommandType.EDIT) {
			redoState = storage.editTask(doCommand);
		} else {
			redoState = new State(null, null, null, false);
		}
		
		if (redoState.isSuccessful()) {
			return new UserInterfaceContent(MESSAGE_REDO_SUCCESS, storage.getAllTasks());
		}
		
		history.goBack();
		return new UserInterfaceContent(MESSAGE_REDO_ERROR, storage.getAllTasks());
	}

	private UserInterfaceContent executeSearch(Command command) {
		// TODO Execute search
		return null;
	}

	private UserInterfaceContent executeUndo() {
		State newState;
		if (history.getCurrentState() == null || (newState = history.goForward()) == null) {
			return new UserInterfaceContent(MESSAGE_UNDO_NOTHING, storage.getAllTasks());
		}
		
		State undoState;
		Command undoCommand = newState.getUndoCommand();
		
		if (undoCommand.getType() == CommandType.ADD) {
			undoState = storage.addTask(undoCommand);
		} else if (undoCommand.getType() == CommandType.CLEAR) {
			undoState = storage.refillAll(newState.getTasks());
		} else if (undoCommand.getType() == CommandType.DELETE) {
			undoState = storage.deleteTask(undoCommand);
		} else if (undoCommand.getType() == CommandType.EDIT) {
			undoState = storage.editTask(undoCommand);
		} else {
			undoState = new State(null, null, null, false);
		}
		
		if (undoState.isSuccessful()) {
			return new UserInterfaceContent(MESSAGE_UNDO_SUCCESS, storage.getAllTasks());
		}
		
		history.goForward();
		return new UserInterfaceContent(MESSAGE_UNDO_ERROR, storage.getAllTasks());
	}

	public UserInterfaceContent executeUserInput (String userInput) {
		// TODO Replace temporary command constructor with Parser when completed
		Task task = new Task(1, "Test", null, false, false);
		Command userCommand = new Command(task, CommandType.ADD);
		// Determine actions to be taken and take them
		// Return List of Tasks and console message to UI
		return determineActions(userCommand, userInput);
	}

	private String formatAddSuccess(String text) {
		return String.format(MESSAGE_ADD_SUCCESS, text);
	}

	private String formatDeleteSuccess(int deleteLineNumber) {
		return String.format(MESSAGE_DELETE_SUCCESS, deleteLineNumber);		
	}

	private String formatEditSuccess(int editLineNumber) {
		return String.format(MESSAGE_EDIT_SUCCESS, editLineNumber);
	}

	private String formatInvalidCommand(String userInput) {
		return String.format(MESSAGE_INVALID_COMMAND, userInput);
	}

	private UserInterfaceContent unknownCommand(String userInput) {
		String consoleMessage = formatInvalidCommand(userInput);
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}
}
