package itinerary.main;

import java.util.Calendar;

import itinerary.main.ParserCommand;
import itinerary.main.Task;
import itinerary.main.CommandType;

//@author A0114823M
public class Parser {

	private static final String DUPLICATED_KEYWORD_PRI = "duplicated keyword \"pri\" !";
	private static final String DUPLICATED_KEYWORD_CA = "duplicated keyword \"ca\" !";
	private static final String DUPLICATED_KEYWORD_TIME = "duplicated keyword for time (\"by\" or \"ti\") !";
	private static final String INVALID_INDEX = "Invalid index ";
	private static final String NO_DESCRIPTION_FOR_EDIT = "Please key in the new eescriptions "
			+ "and/or \"ca\" for setting category and/or \" pri\" for setting importance level !";
	private static final String NOTHING_AFTER_COMMANDTYPE = "Please key in descriptions!";
	private static final String INVALID_INPUT_FORMAT = "Invalid input format ";
	private static final String INVALID_DATE_TIME = "Invalid date";
	private static final String ERROR_MESSAGE = "Your command is not executed due to: %1$s.";
	private static final String[] KEYWORD = {"pri",  "ca", "by", "ti"};

	private static String showMessage = null;
	private static String time = "";

	public Parser(){
	}

	public String getMessage(){
		return showMessage;
	}

	public String getTime(){
		return time;
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

	public Task checkDuplicatedKeyword(String[] inputWords){
		int[] keyWordCounter = {0,0,0};	

		for(int i=0; i < inputWords.length; i++){
			if(inputWords[i].equals(KEYWORD[0])){
				keyWordCounter[0]++;
			}
			if(inputWords[i].equals(KEYWORD[1])){
				keyWordCounter[1]++;
			}
			if(inputWords[i].equals(KEYWORD[2]) || inputWords[i].equals(KEYWORD[3])){
				keyWordCounter[2]++;
			}
		}

		for(int i=0; i < keyWordCounter.length; i++){
			if(keyWordCounter[0] > 1){
				showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_PRI);
				return defaultTask();
			}
			if(keyWordCounter[1] > 1){
				showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_CA);
				return defaultTask();
			}
			if(keyWordCounter[2] > 1){
				showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_TIME);
				return defaultTask();
			}
		}
		return defaultTask();
	}

	//KEYWORD = {"pri",  "ca", "by", "ti"}
	public Task extractContent(String input){		
		String[] inputWords = stringToArray(input);
		Task task = checkDuplicatedKeyword(inputWords);

		String content = "";
		String category = "";
		String time = "";
		int endOfContent = 0;
		
		for(int i=0; !inputWords[i].equals(KEYWORD[0]) && !inputWords[i].equals(KEYWORD[1]) && 
			   !inputWords[i].equals(KEYWORD[2])  && !inputWords[i].equals(KEYWORD[3]) ; i++){
			    	content = content + inputWords[i];
			    	endOfContent = i;
	   }
		task.setText(content);
		
		for(int i = endOfContent + 1; i < inputWords.length; i++){

			if(inputWords[i].equals(KEYWORD[0])){
				task.setPriority(true);
			}
			if(inputWords[i].equals(KEYWORD[1])){
				for(int j=i+1; !inputWords[j+1].equals(KEYWORD[0]) && !inputWords[j+1].equals(KEYWORD[2])
						&&!inputWords[j+1].equals(KEYWORD[3]); j++){
					category = inputWords[j];
				}
				task.setCategory(category);
			}
			if(inputWords[i].equals(KEYWORD[2]) || inputWords[i].equals(KEYWORD[3])){
				for(int j=i+1;  !inputWords[j+1].equals(KEYWORD[0]) && !inputWords[j+1].equals(KEYWORD[1]); j++){
					time = inputWords[j];
				}
                ParserDate parserDate = new ParserDate();
				Task dateTask = parserDate.getTask(time);
                if(dateTask instanceof ScheduleTask){
                	task = (ScheduleTask) task;
                	dateTask = (ScheduleTask) dateTask;
                	//task.setFromDate(dateTask.getFromDate());
                //	task.setToDate(dateTask.getToDate());
                }
                if(dateTask instanceof DeadlineTask){
                	task = (DeadlineTask) task;
                	dateTask = (DeadlineTask) dateTask;
                //	task.setFromDate(dateTask.getFromDate());
                }
			}
		}
		return task;
	}


	// check if the line number of a task is valid.
	// It will return a task with the valid line number to logic if the line number is valid.
	//Otherwise, it will return a task with line number of -1 to indicate invalidation
	public Task targetTask(String[] words){
		try{
			if(words.length == 1){
				throw new Exception(NOTHING_AFTER_COMMANDTYPE);
			}
		}catch(Exception e){
			showMessage = String.format(ERROR_MESSAGE, NOTHING_AFTER_COMMANDTYPE);
			return new Task (-1, "", "", false, false);
		}

		String index = words[1];
		try { 
			Integer.parseInt(index); 
		} catch(NumberFormatException e) { 
			showMessage = String.format(ERROR_MESSAGE, INVALID_INDEX);
			return new Task (-1, "", "", false, false);
		}

		try{
			int number = Integer.parseInt(index);
			if(number <= 0){
				throw new Exception(INVALID_INDEX);
			}
		}catch(Exception e){
			showMessage = String.format(ERROR_MESSAGE, INVALID_INDEX);
			return new Task (-1, "", "", false, false);
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

		if(words.length == 2){
			showMessage = String.format(ERROR_MESSAGE, NO_DESCRIPTION_FOR_EDIT);
			return defaultTask();
		}
		else{
			Task task = targetTask(words);
			String newDescription = "";
			newDescription = input.substring(input.indexOf(" ")+1);
			newDescription = newDescription.substring(newDescription.indexOf(" ")+1);

			task.setText(newDescription);
			task.setCategory("newCategory");
			task.setPriority(true);
			task.setComplete(true);
			return task;
		}
	}

	public Task undoOperation(String input){
		return defaultTask();
	}

	public Task redoOperation(String input){
		return defaultTask();
	}
}
