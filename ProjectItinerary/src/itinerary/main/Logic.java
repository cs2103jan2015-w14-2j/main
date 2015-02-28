package itinerary.main;

import java.util.List;

//@author A0121437N
public class Logic {
	private Storage storage;
	private Search search;
	private History history;
	
	public Logic (String fileName) {
		this.storage = new StorageStub(fileName);
		this.search = new Search();
		this.history = new History();
	}
	
	public List<Task> executeUserInput (String userInput) {
		// Parse command
		
		// Determine actions to be taken and take them
		
		// Get all tasks from storage and return to UI
		return storage.getAllTasks();
	}
}
