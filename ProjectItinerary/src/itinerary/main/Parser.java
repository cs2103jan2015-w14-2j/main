package itinerary.main;

import java.util.Calendar;

import itinerary.main.ParserCommand;
import itinerary.main.Task;
import itinerary.main.CommandType;

//@author A0114823M
public class Parser {

	private static final String INVALID_INDEX = "Invalid index ";
	private static final String INVALID_INPUT_FORMAT = "Invalid input format ";
	private static final String INVALID_DATE_TIME = "Invalid date";
	private static final String ERROR_MESSAGE = "Your command is not executed due to: %1$s.";
	private static final String[] KEYWORD = {"by", "ti", "ca", "pri", "des"};
	
	private static String showMessage = null;
	
	public Parser(){
	}

	//returns a command object and it is called by logic
	public Command  getCommand(String input){
		Task task = createTask(input);
		CommandType commandType = createCommandType(input);
		String message = createMessage();
		Command command = new Command(task, commandType, message);
		return command; 
	}

	//The message will be null if the command and input format is valid,
	// It will return an error message if the input is not valid
	 public String createMessage(){
		 return showMessage;
	 }
	 
	 public String[] stringToArray(String input){
		  String[] inputWords =  input.split(" +");
		  return inputWords;
	  }
			
		public CommandType createCommandType(String input){
			String[] inputWords = stringToArray(input);
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
			return defaultTask();
		}			
	}

	public String extractContent(String input){
		String[] inputWords = stringToArray(input);
		for(int i=0; i < inputWords.length; i++){
			for(int j=0; j < KEYWORD.length; j++){
				if(inputWords[i].equals(KEYWORD[j])){
					inputWords[i] = "";
					for(int k=i+1; k < inputWords.length; k++){
						inputWords[k] = "";
					}
				}
			}
		}
		String content = "";
		for(int i=0; i < inputWords.length; i++){
			if(! inputWords[i].equals("")){
				content = content + inputWords[i] + " ";
			}
		}
		return content;
	}
	
	
// check if the line number of a task is valid.
// It will return a task with the valid line number to logic if the line number is valid.
//Otherwise, it will return a task with line number of -1 to indicate invalidation
	public Task targetTask(String[] words){
		String index = words[1];
		try { 
			Integer.parseInt(index); 
		} catch(NumberFormatException e) { 
			showMessage = String.format(ERROR_MESSAGE, INVALID_INDEX);
			 return new Task (-1, null, null, false, false);
		}
		
		try{
			int number = Integer.parseInt(index);
			if(number <= 0){
				throw new Exception(INVALID_INDEX);
			}
		}catch(Exception e){
			showMessage = String.format(ERROR_MESSAGE, INVALID_INDEX);
			return new Task (-1, null, null, false, false);
	    }
		return new Task (Integer.parseInt(index), null, null, false, false);
	}

// This is the default format of a task.
// This format can be used when the return type of task is not needed to be known.
// e.g when deleting a task, only the line number is necessary to be known.
	public Task defaultTask(){
		return new Task(-1,null,null,false,false);
	}
	
	public Task setTaskAttributes(String input){
		return new Task(-1, null, null, false, false);
	}
	
	public Task addTask(String input){
		String description = input.substring(4, input.length());
		//String content = extractContent(description);
		return new Task(-1, description, null, false, false);
	}

	public Task deleteTask(String input){
		String[] words = stringToArray(input);
		Task task = targetTask(words);
		return task;
	}

	public Task displayTasks(String input){
		return defaultTask();
	}

	public Task clearTasks(String input){
		return defaultTask();
	}

	public Task searchTask(String input){
		String description = input.substring(7, input.length());
		return new Task(1, description, null, false, false);
	}

	public Task editTask(String input){
		String[] words = stringToArray(input);
		String newDescription = input.substring(5, input.length());
		Task task = targetTask(words);
		task.setText(newDescription);
		task.setCategory("newCategory");
		task.setPriority(true);
		task.setComplete(true);
		return task;
	}

	public Task undoOperation(String input){
		return defaultTask();
	}

	public Task redoOperation(String input){
		return defaultTask();
	}
	
	public String getText(){
		return null;
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
