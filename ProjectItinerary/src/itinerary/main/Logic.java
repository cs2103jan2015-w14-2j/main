package itinerary.main;

import itinerary.history.History;
import itinerary.history.HistoryException;
import itinerary.parser.Parser;
import itinerary.parser.ParserException;
import itinerary.search.Search;
import itinerary.search.SearchException;
import itinerary.search.SearchTask;
import itinerary.storage.ConfigStorage;
import itinerary.storage.FileStorage;
import itinerary.storage.Storage;
import itinerary.storage.StorageException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author A0121437N
public class Logic {
	private static final String MESSAGE_WELCOME = "Welcome to ITnerary! %1$s is ready for use.";
	private static final String MESSAGE_DELETE_SUCCESS = "Deleted task %1$s.";
	private static final String MESSAGE_CLEAR_SUCCESS = "Cleared all tasks.";
	private static final String MESSAGE_ADD_SUCCESS = "Added \"%1$s\".";
	private static final String MESSAGE_EDIT_SUCCESS = "Edited task %1$d.";
	private static final String MESSAGE_DISPLAY_ALL = "Displaying all tasks.";
	private static final String MESSAGE_REDO_ERROR = "Redo error.";
	private static final String MESSAGE_REDO_SUCCESS = "Redo successful.";
	private static final String MESSAGE_UNDO_ERROR = "Undo error.";
	private static final String MESSAGE_UNDO_SUCCESS = "Undo successful.";
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command: \"%1$s\"";
	private static final String MESSAGE_SEARCH_ERROR = "Search error.";
	private static final String MESSAGE_SEARCH_SUCCESS = "Search success. %1$d result(s) found.";
	
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
	 * Must call setUpLogicVariables to initialize variables
	 * in logic if this constructor is called
	 */
	public Logic () {
		config = new ConfigStorage();
	}
	
	/**
	 * Constructor that can be used in instances where a user 
	 * always has to specify their preferred filename
	 * 
	 * @param filename
	 */
	public Logic (String filename) {
		this();
		setupLogicVariables(filename);
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
	public boolean isFileConfigured () {
		String fileName = null;
		try {
			fileName = config.getStorageFileName();
		} catch (IOException e) {
			// should not result in errors, for safety
			e.printStackTrace();
		}
		if (fileName == null) {
			return false;
		}
		File file = new File(fileName);
		if (!file.isDirectory() && file.exists()) {
			setupLogicVariables(fileName);
			return true;
		} else {
			return false;
		}
	}
	
	public void saveStorageFileName (String fileName) {
		try {
			config.setStorageFileName(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// returns null if the current filename is null
	public String getCurrentFileName () {
		try {
			return config.getStorageFileName();
		} catch (IOException e) {
			return null;
		}
	}
	
	public void setupLogicVariables (String fileName) {
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
		CommandType type = command.getType();
		if (type == CommandType.ADD) {
			return executeAdd(command);
		} else if (type == CommandType.CLEAR) {
			return executeClear();
		} else if (type == CommandType.DELETE) {
			return executeDelete(command);
		} else if (type == CommandType.DISPLAY) {
			return executeDisplay();
		} else if (type == CommandType.EDIT || type == CommandType.MARK) {
			return executeEdit(command);
		} else if (type == CommandType.SEARCH) {
			return executeSearch(command);
		} else if (type == CommandType.REDO) {
			return executeRedo();
		} else if (type == CommandType.UNDO) {
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
		Task task = command.getTask();
		String text = task.getText();
		String category = task.getCategory();
		Boolean priority = task.isPriority();
		priority = priority ? true : null;
		Boolean complete = task.isComplete();
		Calendar from = null;
		Calendar to = null;
		if (task instanceof ScheduleTask) {
			ScheduleTask sTask = (ScheduleTask) task;
			from = sTask.getFromDate();
			to = sTask.getToDate();
		}
		SearchTask searchTask = new SearchTask(0, text, category, priority, complete, from, to);
		return executeAdvancedSearch(searchTask);
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
	
	public UserInterfaceContent executeAdvancedSearch (SearchTask task) {
		List<Task> searchList= new ArrayList<Task>();
		List<Task> allTasks = storage.getAllTasks();
		try {
        	Search search = new Search(allTasks);
            searchList = search.query(task);
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
