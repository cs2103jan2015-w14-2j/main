package itinerary.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	//returns a command object and it is called by logic
	public Command getCommand(String input) {
		Task task = createTask(input);
		CommandType commandType = createCommandType(input);
		Command command = new Command(task, commandType);
		return command; 
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

	public Task createTask(String input) {
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

	public boolean hasDuplicatedKeywords(String[] inputWords){
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
				return true;
			}
			if(keyWordCounter[1] > 1){
				showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_CA);
				return true;
			}
			if(keyWordCounter[2] > 1){
				showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_TIME);
				return true;
			}
		}
		return false;
	}

	//KEYWORD = {"pri",  "ca", "by", "ti"}
	public Task extractContent(String input, int startIndex) throws ParseException{	
		Task floatingTask = defaultTask();
		DeadlineTask deadlineTask = new DeadlineTask(-1, "","",false,false,null);
		ScheduleTask scheduleTask = new ScheduleTask(-1,"","",false,false,null,null);	
		int taskType = 0;         // 0: FloatingTask, 1: DeadlineTask, 2: ScheduleTask

		String[] inputWords = stringToArray(input);
		if(hasDuplicatedKeywords(inputWords)){
			return (Task) defaultTask();
		}
		else{
			String content = "";
			String category = "";
			String time = "";
			int endOfContent = 0;

			for(int i=startIndex;   (i < inputWords.length-1)&&(!inputWords[i+1].equals(KEYWORD[0])) 
					&& (!inputWords[i+1].equals(KEYWORD[1])) && (!inputWords[i+1].equals(KEYWORD[2]))  
					&& (!inputWords[i+1].equals(KEYWORD[3])); i++){
				content = content + inputWords[i] + " ";
				endOfContent = i;

			}
			endOfContent++;

			content = content + inputWords[endOfContent];		

			switch(taskType){
			case 0: floatingTask.setText(content);
			case 1:  deadlineTask.setText(content);
			default: scheduleTask.setText(content);
			}

			int i = endOfContent;
			i++;
			while( i < inputWords.length){

				int endOfTime = i;
				if(inputWords[i].equals(KEYWORD[2]) || inputWords[i].equals(KEYWORD[3])){
					for(int j=i+1;  (j < inputWords.length-1) && !inputWords[j+1].equals(KEYWORD[0]) 
							&& !inputWords[j+1].equals(KEYWORD[1]); j++){
						time = time + inputWords[j] + " ";
						endOfTime = j;
					}
					endOfTime++;
					time = time + inputWords[endOfTime];
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					DateFormat tf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
					if(inputWords[i].equals(KEYWORD[2])){		
						Calendar cal  = Calendar.getInstance();	
						if(time.indexOf(":") == -1){
							cal.setTime(df.parse(time));
						}
						else{
							cal.setTime(tf.parse(time));
						}
						cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
						deadlineTask.setDeadline(cal);
						taskType = 1;
					}
					else{
						System.out.println(time);
						String startTime = time.substring(0, time.indexOf("to") - 1);
						String endTime = time.substring(time.indexOf("to") + 3);
						Calendar startCal  = Calendar.getInstance();
						if(startTime.indexOf(":") == -1){
							startCal.setTime(df.parse(startTime));
						}
						else{
							startCal.setTime(tf.parse(startTime));
						}
						startCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH) + 1);
						
						
						Calendar endCal  = Calendar.getInstance();
						if(endTime.indexOf(":") == -1){
							startCal.setTime(df.parse(endTime));
						}
						else{
							startCal.setTime(tf.parse(endTime));
						}
						endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 1);
						scheduleTask.setFromDate(startCal);
						scheduleTask.setToDate(endCal);
						taskType = 2;
					}
					i = endOfTime + 1;
				}

				else if(inputWords[i].equals(KEYWORD[0])){
					i++;
					switch(taskType){
					case 0:floatingTask.setPriority(true);
					case 1: deadlineTask.setPriority(true);
					default: scheduleTask.setPriority(true);
					}
				}

				else if(inputWords[i].equals(KEYWORD[1])){
					int endOfCategory = i;
					for(int j=i+1; (j < inputWords.length-1) && (!inputWords[j+1].equals(KEYWORD[0])) &&
							(!inputWords[j+1].equals(KEYWORD[2])) && (!inputWords[j+1].equals(KEYWORD[3])) ; j++){
						category = category + inputWords[j] + " ";
						endOfCategory = j;
					}
					endOfCategory++;
					i = endOfCategory + 1;
					category = category + inputWords[endOfCategory];
					switch(taskType){
					case 0:floatingTask.setCategory(category);
					case 1: deadlineTask.setCategory(category);
					default: scheduleTask.setCategory(category);
					}
				}
			}
		}
		switch(taskType){
		case 0: return floatingTask;
		case 1:  return deadlineTask;
		default: return scheduleTask;
		}
	}


	// check if the line number of a task is valid.
	// It will return a task with the valid line number to logic if the line number is valid.
	//Otherwise, it will return a task with line number of -1 to indicate invalidation
	public Task targetTask(String[] words) throws Exception{
		try{
			if(words.length == 1){
				throw new Exception(NOTHING_AFTER_COMMANDTYPE);
			}
		}catch(Exception e){
			showMessage = String.format(ERROR_MESSAGE, NOTHING_AFTER_COMMANDTYPE);
			throw new Exception(showMessage);
		}

		String index = words[1];
		try { 
			Integer.parseInt(index); 
		} catch(NumberFormatException e) { 
			showMessage = String.format(ERROR_MESSAGE, INVALID_INDEX);
			throw new Exception(showMessage);
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
		return new Task (Integer.parseInt(index), "", "", false, false);
	}

	// This is the default format of a task.
	// This format can be used when the return type of task is not needed to be known.
	// e.g when deleting a task, only the line number is necessary to be known.
	public Task defaultTask(){
		return new Task(-1,"","",false,false);
	}

	public Task setTaskAttributes(String input){
		return new Task(-1, "", "", false, false);
	}

	public Task addTask(String input) {
		return extractContent(input,1);
	}

	public Task deleteTask(String input) {
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
		return new Task(1, description, "", false, false);
	}

	public Task editTask(String input) {
		String[] words = stringToArray(input);

		if(words.length == 2){
			showMessage = String.format(ERROR_MESSAGE, NO_DESCRIPTION_FOR_EDIT);
			//throw new Exception(showMessage);
		}
		else{
			int lineNumber = targetTask(words).getTaskId();
			Task task = extractContent(input, 2);
			task.setTaskId(lineNumber);
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
