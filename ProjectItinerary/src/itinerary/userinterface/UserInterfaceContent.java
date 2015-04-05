package itinerary.userinterface;

import itinerary.main.Task;

import java.util.List;

//@author A0121437N
public class UserInterfaceContent {
	private String consoleMessage;
	private List<Task> displayableTasks;
	private List<Task> allTasks;
	
	public UserInterfaceContent(String consoleMessage, List<Task> allTasks) {
		this.consoleMessage = consoleMessage;
		this.displayableTasks = allTasks;
		this.allTasks = allTasks;
	}
	
	public UserInterfaceContent(String consoleMessage, List<Task> displayableTasks, List<Task> allTasks) {
		this.consoleMessage = consoleMessage;
		this.displayableTasks = displayableTasks;
		this.allTasks = allTasks;
	}

	public String getConsoleMessage() {
		return consoleMessage;
	}

	public List<Task> getDisplayableTasks() {
		return displayableTasks;
	}
	
	public List<Task> getAllTasks () {
		return allTasks;
	}
}
