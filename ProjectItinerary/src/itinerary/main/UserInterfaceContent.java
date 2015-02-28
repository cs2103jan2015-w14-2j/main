package itinerary.main;

import java.util.List;

//@author A0121437N
public class UserInterfaceContent {
	private String consoleMessage;
	private List<Task> tasks;
	
	//@author generated
	public UserInterfaceContent(String consoleMessage, List<Task> tasks) {
		super();
		this.consoleMessage = consoleMessage;
		this.tasks = tasks;
	}

	public String getConsoleMessage() {
		return consoleMessage;
	}

	public List<Task> getTasks() {
		return tasks;
	}
}
