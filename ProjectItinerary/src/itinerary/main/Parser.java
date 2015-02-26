package itinerary.main;

import java.util.Calendar;

import itinerary.main.ParserCommand;
import itinerary.main.Task;

//@author A0114823M
public class Parser {

	private Task task;
	private CommandType type;

	public Parser(String input){
		setCommandType(input);
		setTask(input);
	}

	public CommandType getType(){
		return this.type;
	}
	
	public Task getTask(){
		return this.task;
	}

	public void setCommandType(String input){
		this.type = getCommand(input);
	}


	
	public CommandType getCommand(String input){
		String[] inputWords =  input.split(" ");
		String firstWord = inputWords[0]; 
		ParserCommand cmd =  new ParserCommand();			
		return cmd.getType(firstWord);
	}
	
	public String[] getTaskArray(String input){
		String[] inputWords = input.split(" ");
		String[] secondWordOnwards = new String[inputWords.length-1];
		for(int i=1; i < inputWords.length; i++){
			secondWordOnwards[i-1] = inputWords[i];
		}
		return secondWordOnwards;
	}
	
	//////
	
	
	
	public void setTask(String input){
		
	}
	
	public String getText(){
		return null;
	}
	
    public int getLineNumber(){
    	return -1;
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
