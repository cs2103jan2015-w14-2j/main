package itinerary.main;

import itinerary.main.CommandType;

//@author A0114823M
public class ParserCommand {
		
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_EDIT = "edit";
    
    public CommandType getType(String command){
    	return determineType(command);
    }
    
    public CommandType determineType(String command){
    	
		if(command.equalsIgnoreCase(COMMAND_ADD)){
			return CommandType.ADD;
		}
		else if(command.equalsIgnoreCase(COMMAND_DELETE)){
			return CommandType.DELETE;
		}
		else if(command.equalsIgnoreCase(COMMAND_DISPLAY)){
			return CommandType.DISPLAY;
		}
		else if(command.equalsIgnoreCase(COMMAND_SEARCH)){
			return CommandType.SEARCH;
		}
		else if(command.equalsIgnoreCase(COMMAND_CLEAR)){
			return CommandType.CLEAR;
		}
		else if(command.equalsIgnoreCase(COMMAND_EDIT)){
			return CommandType.EDIT;
		}		
		else{
			return CommandType.UNABLE_TO_DETERMINE;
		}
	}
}
