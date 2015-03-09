package itinerary.main;

import java.util.ArrayList;
import java.util.List;


//@author A0121437N
public class Logic {
	private static final String MESSAGE_WELCOME = "Welcome! %1$s is ready for use";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted task %1$s";
	private static final String MESSAGE_CLEAR_SUCCESS = "cleared all tasks";
	private static final String MESSAGE_ADD_SUCCESS = "added \"%1$s\"";
	private static final String MESSAGE_EDIT_SUCCESS = "edited task %1$d";
	private static final String MESSAGE_DISPLAY_ALL = "displaying all tasks";
	private static final String MESSAGE_REDO_ERROR = "redo error";
	private static final String MESSAGE_REDO_NOTHING = "nothing to redo";
	private static final String MESSAGE_REDO_SUCCESS = "redo successful";
	private static final String MESSAGE_UNDO_ERROR = "undo error";
	private static final String MESSAGE_UNDO_NOTHING = "nothing to undo";
	private static final String MESSAGE_UNDO_SUCCESS = "undo successful";
	private static final String MESSAGE_INVALID_COMMAND = "invalid command: \"%1$s\"";
	private static final String MESSAGE_SEARCH_ERROR = "search error";
	private static final String MESSAGE_SEARCH_SUCCESS = "search success";
	
	private String fileName;
	private Parser parser;
	private Storage storage;
	private History history;
	
	public Logic (String fileName) {
		this.fileName = fileName;
		this.parser = new Parser();
		this.storage = new ProtoFileStorage(fileName);
		this.history = new History(storage.getAllTasks());
	}

	public UserInterfaceContent executeUserInput (String userInput) {
		Command userCommand = parser.getCommand(userInput);
		return determineActions(userCommand, userInput);
	}
	
	public UserInterfaceContent initialLaunch () {
		UserInterfaceContent displayContent = executeDisplay();
		String welcomeMessage = formatWelcomeMessage();
		return new UserInterfaceContent(welcomeMessage, displayContent.getTasks());
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
			return unknownCommand(userInput);
		}
	}
	
	private UserInterfaceContent executeAdd(Command command) {
		try {
			storage.addTask(command.getTask());
		} catch (StorageException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		updateHistory();
		String consoleMessage = formatAddSuccess(command.getTask());
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}

	private UserInterfaceContent executeClear(Command command) {
		try {
			storage.clearAll();
		} catch (StorageException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		updateHistory();
		return new UserInterfaceContent(MESSAGE_CLEAR_SUCCESS, storage.getAllTasks());
	}

	private UserInterfaceContent executeDelete(Command command) {
		try {
			storage.deleteTask(command.getTask());
		} catch (StorageException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		
		updateHistory();
		
		int deleteTaskId = command.getTask().getTaskId();
		String consoleMessage = formatDeleteSuccess(deleteTaskId);
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}

	private UserInterfaceContent executeDisplay() {
		return new UserInterfaceContent(MESSAGE_DISPLAY_ALL, storage.getAllTasks());
	}
	
	private UserInterfaceContent executeEdit(Command command) {
		try {
			storage.editTask(command.getTask());
		} catch (StorageException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		
		updateHistory();
		
		int editTaskId = command.getTask().getTaskId();
		String consoleMessage = formatEditSuccess(editTaskId);
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}

	private void updateHistory() {
		List<Task> newState = storage.getAllTasks();
		history.addNewState(newState);
	}

	private UserInterfaceContent executeRedo() {
		List<Task> nextState = history.goForward();
		if (nextState == null) {
			return new UserInterfaceContent(MESSAGE_REDO_NOTHING, storage.getAllTasks());
		}
		try {
			storage.clearAll();
			storage.refillAll(nextState);
		} catch (StorageException e) {
			history.goBack();
			return new UserInterfaceContent(MESSAGE_REDO_ERROR, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_REDO_SUCCESS, storage.getAllTasks());
	}

	private UserInterfaceContent executeSearch(Command command) {
		List<Task> searchList= new ArrayList<Task>();
	        try {
	        	// TODO implement date search
	        	Search search = new Search(storage.getAllTasks(), false);
	            searchList = search.query(command.getTask().getText(),"text");
            } catch (SearchException e) {
    			return new UserInterfaceContent(MESSAGE_SEARCH_ERROR, storage.getAllTasks());
            }
       
		return new UserInterfaceContent(MESSAGE_SEARCH_SUCCESS, searchList);
	}

	private UserInterfaceContent executeUndo() {
		List<Task> previousState = history.goBack();
		if (previousState == null) {
			return new UserInterfaceContent(MESSAGE_UNDO_NOTHING, storage.getAllTasks());
		}
		try {
			storage.clearAll();
			storage.refillAll(previousState);
		} catch (StorageException e) {
			history.goForward();
			return new UserInterfaceContent(MESSAGE_UNDO_ERROR, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_UNDO_SUCCESS, storage.getAllTasks());
	}

	private String formatAddSuccess(Task task) {
		return String.format(MESSAGE_ADD_SUCCESS, task.getText());
	}

	private String formatDeleteSuccess(int deleteTaskId) {
		return String.format(MESSAGE_DELETE_SUCCESS, deleteTaskId);		
	}

	private String formatEditSuccess(int editTaskId) {
		return String.format(MESSAGE_EDIT_SUCCESS, editTaskId);
	}

	private String formatInvalidCommand(String userInput) {
		return String.format(MESSAGE_INVALID_COMMAND, userInput);
	}

	private String formatWelcomeMessage() {
		return String.format(MESSAGE_WELCOME, fileName);
	}
	
	private UserInterfaceContent invalidCommand(String message) {
		return new UserInterfaceContent(message, storage.getAllTasks());
	}
	
	private UserInterfaceContent unknownCommand(String userInput) {
		String consoleMessage = formatInvalidCommand(userInput);
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}
}
