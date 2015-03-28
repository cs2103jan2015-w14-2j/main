package itinerary.main;

import itinerary.history.History;
import itinerary.history.HistoryException;
import itinerary.parser.Parser;
import itinerary.parser.ParserException;
import itinerary.search.Search;
import itinerary.search.SearchException;
import itinerary.storage.ConfigStorage;
import itinerary.storage.FileStorage;
import itinerary.storage.Storage;
import itinerary.storage.StorageException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author A0121437N
public class Logic {
	private static final String MESSAGE_WELCOME = "Welcome! %1$s is ready for use";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted task %1$s";
	private static final String MESSAGE_CLEAR_SUCCESS = "cleared all tasks";
	private static final String MESSAGE_ADD_SUCCESS = "added \"%1$s\"";
	private static final String MESSAGE_EDIT_SUCCESS = "edited task %1$d";
	private static final String MESSAGE_DISPLAY_ALL = "displaying all tasks";
	private static final String MESSAGE_REDO_ERROR = "redo error";
	private static final String MESSAGE_REDO_SUCCESS = "redo successful";
	private static final String MESSAGE_UNDO_ERROR = "undo error";
	private static final String MESSAGE_UNDO_SUCCESS = "undo successful";
	private static final String MESSAGE_INVALID_COMMAND = "invalid command: \"%1$s\"";
	private static final String MESSAGE_SEARCH_ERROR = "search error";
	private static final String MESSAGE_SEARCH_SUCCESS = "search success. %1$d results found";
	
	private static final Logger logger = Logger.getGlobal();
	static {
		try {
			logger.addHandler(new FileHandler(Constants.LOG_FILE, Constants.LOG_FILE_SIZE_LIMIT, 1, true));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String fileName;
	private Storage storage;
	private History history;
	private ConfigStorage config;
	
	/**
	 * Must call setUpLogicVariables to initialize variables in logic if empty constructor is called
	 */
	public Logic () {
		config = new ConfigStorage();
	}
	
	/**
	 * Constructor used for testing, setUpLogicVariables not required
	 * 
	 * @param filename the filename that will be printed after initial launch
	 * @param storage the storage object which will be referenced
	 * @param history the history object which will be referenced
	 */
	public Logic (String filename, Storage storage, History history) {
		this.fileName = filename;
		this.storage = storage;
		this.history = history;
	}

	public UserInterfaceContent executeUserInput (String userInput) {
		logger.log(Level.INFO, "executing user input: " + userInput);
		Command userCommand;
		try {
			userCommand = Parser.parseCommand(userInput);
		} catch (ParserException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		return determineActions(userCommand, userInput);
	}
	
	// returns true if config file with storage file name is found, false otherwise
	// if true, isConfigured will initialize all required objects
	public boolean isConfigured () {
		String fileName = null;
		try {
			fileName = config.getStorageFileName();
		} catch (IOException e) {
			// should not result in errors, for safety
			e.printStackTrace();
		}
		if (fileName == null) {
			return false;
		} else {
			setUpLogicVariables(fileName);
			return true;
		}
	}
	
	public void saveStorageFileName (String fileName) {
		try {
			config.setStorageFileName(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setUpLogicVariables (String fileName) {
		this.fileName = fileName;
		storage = new FileStorage(this.fileName);
		history = new History(storage.getAllTasks());
	}
	
	public UserInterfaceContent initialLaunch () {
		UserInterfaceContent displayContent = executeDisplay();
		String welcomeMessage = formatWelcomeMessage();
		return new UserInterfaceContent(welcomeMessage, displayContent.getDisplayableTasks());
	}
	
	public void exitOperation () {
		storage.close();
	}
	
	private UserInterfaceContent determineActions (Command command, String userInput) {
		if (command.getType() == CommandType.ADD) {
			return executeAdd(command);
		} else if (command.getType() == CommandType.CLEAR) {
			return executeClear();
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
			logger.log(Level.WARNING, "Unsuccessful add", e);
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		updateHistory();
		String consoleMessage = formatAddSuccess(command.getTask());
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}

	private UserInterfaceContent executeClear() {
		try {
			storage.clearAll();
		} catch (StorageException e) {
			logger.log(Level.WARNING, "Unsuccessful clear", e);
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		updateHistory();
		return new UserInterfaceContent(MESSAGE_CLEAR_SUCCESS, storage.getAllTasks());
	}

	private UserInterfaceContent executeDelete(Command command) {
		try {
			storage.deleteTask(command.getTask());
		} catch (StorageException e) {
			logger.log(Level.WARNING, "Unsuccessful delete", e);
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
			logger.log(Level.WARNING, "Unsuccessful edit", e);
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
		List<Task> nextState;
		try {
			nextState = history.redo();
		} catch (HistoryException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		try {
			storage.clearAll();
			storage.refillAll(nextState);
		} catch (StorageException storageException) {
			try {
				history.undo();
			} catch (HistoryException historyException) {
				logger.log(Level.WARNING, "Error in trying to return to original state", historyException);
			}
			return new UserInterfaceContent(MESSAGE_REDO_ERROR, storage.getAllTasks());
		}
		return new UserInterfaceContent(MESSAGE_REDO_SUCCESS, storage.getAllTasks());
	}

	private UserInterfaceContent executeSearch(Command command) {
		return executeBasicSearch(command.getTask().getText());
	}
	
	public UserInterfaceContent executeBasicSearch (String query) {
		List<Task> searchList= new ArrayList<Task>();
		List<Task> allTasks = storage.getAllTasks();
		try {
        	Search search = new Search(allTasks);
            searchList = search.query(query ,"text");
        } catch (SearchException e) {
			logger.log(Level.WARNING, "Unsuccessful search", e);
			return new UserInterfaceContent(MESSAGE_SEARCH_ERROR, allTasks);
        }
		return new UserInterfaceContent(formatSearchSuccess(searchList), searchList, allTasks);
	}

	private UserInterfaceContent executeUndo() {
		List<Task> previousState;
		try {
			previousState = history.undo();
		} catch (HistoryException e) {
			return new UserInterfaceContent(e.getMessage(), storage.getAllTasks());
		}
		try {
			storage.clearAll();
			storage.refillAll(previousState);
		} catch (StorageException storageException) {
			try {
				history.redo();
			} catch (HistoryException historyException) {
				logger.log(Level.WARNING, "Error in trying to return to original state", historyException);
			}
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
	
	private String formatSearchSuccess (List<Task> tasks) {
		return String.format(MESSAGE_SEARCH_SUCCESS, tasks.size());
	}

	private String formatInvalidCommand(String userInput) {
		return String.format(MESSAGE_INVALID_COMMAND, userInput);
	}

	private String formatWelcomeMessage() {
		return String.format(MESSAGE_WELCOME, fileName);
	}
	
	private UserInterfaceContent unknownCommand(String userInput) {
		String consoleMessage = formatInvalidCommand(userInput);
		return new UserInterfaceContent(consoleMessage, storage.getAllTasks());
	}
}
