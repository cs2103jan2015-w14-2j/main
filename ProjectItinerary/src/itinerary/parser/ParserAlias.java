package itinerary.parser;

import itinerary.main.CommandType;

//@author A0114823M
public class ParserAlias {

	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_ADD_PLUS = "+";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_DELETE_MINUS = "-";
	private static final String COMMAND_DELETE_REMOVE = "remove";
	private static final String COMMAND_DELETE_CANCEL = "cancel";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_DISPLAY_SHOW = "show";
	private static final String COMMAND_DISPLAY_LIST = "list";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_SEARCH_FIND = "find";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_EDIT_CHANGE = "change";
	private static final String COMMAND_EDIT_UPDATE = "update";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_MARK_COMPLETE = "complete";
	private static final String COMMAND_MARK_FINISH = "finish";
	private static final String COMMAND_MARK_DONE = "done";
	private static final String COMMAND_MARK_TICK = "tick";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_UNDO = "undo";

	public CommandType getType(String command){
		
		if(command.equalsIgnoreCase(COMMAND_ADD)){
			return CommandType.ADD;
		}
		else if(command.equalsIgnoreCase(COMMAND_ADD_PLUS)){
			return CommandType.ADD;
		}
		else if(command.equalsIgnoreCase(COMMAND_DELETE)){
			return CommandType.DELETE;
		}
		else if(command.equalsIgnoreCase(COMMAND_DELETE_REMOVE)){
			return CommandType.DELETE;
		}
		else if(command.equalsIgnoreCase(COMMAND_DELETE_MINUS)){
			return CommandType.DELETE;
		}
		else if(command.equalsIgnoreCase(COMMAND_DELETE_CANCEL)){
			return CommandType.DELETE;
		}
		else if(command.equalsIgnoreCase(COMMAND_DISPLAY)){
			return CommandType.DISPLAY;
		}
		else if(command.equalsIgnoreCase(COMMAND_DISPLAY_LIST)){
			return CommandType.DISPLAY;
		}
		else if(command.equalsIgnoreCase(COMMAND_DISPLAY_SHOW)){
			return CommandType.DISPLAY;
		}
		else if(command.equalsIgnoreCase(COMMAND_SEARCH)){
			return CommandType.SEARCH;
		}
		else if(command.equalsIgnoreCase(COMMAND_SEARCH_FIND)){
			return CommandType.SEARCH;
		}
		else if(command.equalsIgnoreCase(COMMAND_CLEAR)){
			return CommandType.CLEAR;
		}
		else if(command.equalsIgnoreCase(COMMAND_EDIT)){
			return CommandType.EDIT;
		}		
		else if(command.equalsIgnoreCase(COMMAND_EDIT_CHANGE)){
			return CommandType.EDIT;
		}		
		else if(command.equalsIgnoreCase(COMMAND_EDIT_UPDATE)){
			return CommandType.EDIT;
		}		
		else if(command.equalsIgnoreCase(COMMAND_MARK)){
			return CommandType.MARK;
		}		
		else if(command.equalsIgnoreCase(COMMAND_MARK_COMPLETE)){
			return CommandType.MARK;
		}		
		else if(command.equalsIgnoreCase(COMMAND_MARK_FINISH)){
			return CommandType.MARK;
		}		
		else if(command.equalsIgnoreCase(COMMAND_MARK_DONE)){
			return CommandType.MARK;
		}	
		else if(command.equalsIgnoreCase(COMMAND_MARK_TICK)){
			return CommandType.MARK;
		}	
		else if(command.equalsIgnoreCase(COMMAND_UNDO)){
			return CommandType.UNDO;
		}		
		else if(command.equalsIgnoreCase(COMMAND_REDO)){
			return CommandType.REDO;
		}		
		else{
			return CommandType.UNABLE_TO_DETERMINE;
		}
	}
}

