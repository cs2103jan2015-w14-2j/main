package itinerary.main;

import java.util.Calendar;

import itinerary.main.ParserCommand;
import itinerary.main.Task;
import itinerary.main.CommandType;

//@author A0114823M
public class Parser {

	public Parser(String input){
	}

	//returns a command object and it is called by logic
	public Command  getCommand(String input){
		Task task = createTask(input);
		CommandType commandType = createCommandType(input);
		Command command = new Command(task, commandType);
		return command; 
	}

	 public String[] convertInputStringToArray(String input){
		  String[] inputWords =  input.split(" +");
		  return inputWords;
	  }
			
		public CommandType createCommandType(String input){
			String[] inputWords = convertInputStringToArray(input);
			String firstWord = inputWords[0]; 
			ParserCommand cmd =  new ParserCommand();   
			return cmd.getType(firstWord);
		}
 
	public Task createTask(String input){
		if(createCommandType(input).equals(CommandType.ADD)){
			return addTask(input);
		}
		if(createCommandType(input).equals(CommandType.DELETE)){
			return deleteTask(input);
		}
		if(createCommandType(input).equals(CommandType.EDIT)){
			return editTask(input);
		}
		if(createCommandType(input).equals(CommandType.SEARCH)){
			return searchTask(input);
		}
		if(createCommandType(input).equals(CommandType.DISPLAY)){
			return displayTasks(input);
		}
		if(createCommandType(input).equals(CommandType.CLEAR)){
			return clearTasks(input);
		}
		if(createCommandType(input).equals(CommandType.REDO)){
			return redoOperation(input);
		}
		if(createCommandType(input).equals(CommandType.UNDO)){
			return undoOperation(input);
		}
		else{
			return new Task(1, null, null, false, false);
		}			
	}

	//////

	public Task addTask(String input){
		return new Task(1, null, null, false, false);
	}

	public Task deleteTask(String input){
		return new Task(1, null, null, false, false);
	}

	public Task displayTasks(String input){
		return new Task(1, null, null, false, false);
	}

	public Task clearTasks(String input){
		return new Task(1, null, null, false, false);
	}

	public Task searchTask(String input){
		return new Task(1, null, null, false, false);
	}

	public Task editTask(String input){
		return new Task(1, null, null, false, false);
	}

	public Task undoOperation(String input){
		return new Task(1, null, null, false, false);
	}

	public Task redoOperation(String input){
		return new Task(1, null, null, false, false);
	}


	public String getText(){
		return null;
	}

	public int getLineNumber(String[] arr){
		String firstElement = arr[0];
		Integer lineNumber = Integer.parseInt(firstElement);
		return lineNumber;
	}

	public String getCategory(){
		return null;
	}

	public Calendar getFromDate() {
		return null;
	}

	public Calendar getToDate() {
		return null;
	}

	public boolean isPriority() {
		return false;
	}

	public boolean isComplete() {
		return false;
	}
}
