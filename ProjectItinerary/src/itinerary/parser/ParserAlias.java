package itinerary.parser;

import java.util.ArrayList;
import java.util.TreeMap;

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

	private TreeMap<CommandType, Integer> commandTree = new TreeMap<CommandType, Integer>();
	private ArrayList<ArrayList<String>> commandList = new  ArrayList<ArrayList<String>>();

	private int addIndex, deleteIndex, editIndex, displayIndex, clearIndex, searchIndex, markIndex, redoIndex, undoIndex;

	public void createCommandTree(){
		int i = 0;
		commandTree.put(CommandType.ADD, i++);
		commandTree.put(CommandType.CLEAR, i++);
		commandTree.put(CommandType.DELETE, i++);
		commandTree.put(CommandType.DISPLAY, i++);
		commandTree.put(CommandType.EDIT, i++);
		commandTree.put(CommandType.MARK, i++);
		commandTree.put(CommandType.REDO, i++);
		commandTree.put(CommandType.SEARCH, i++);
		commandTree.put(CommandType.UNDO, i);			
	}	

	public void createIndex(){
		addIndex = commandTree.get(CommandType.ADD);
		deleteIndex = commandTree.get(CommandType.DELETE);
		displayIndex = commandTree.get(CommandType.DISPLAY);
		editIndex = commandTree.get(CommandType.EDIT);
		clearIndex = commandTree.get(CommandType.CLEAR);
		searchIndex = commandTree.get(CommandType.SEARCH);
		markIndex = commandTree.get(CommandType.MARK);
		redoIndex = commandTree.get(CommandType.REDO);
		undoIndex = commandTree.get(CommandType.UNDO);
	}

	public void initializeCommandList(){
		for(int i=0; i < 9; i++){
			commandList.add(new ArrayList<String>());
		}
	}

	public void createCommandList (){
		createCommandTree();
		createIndex();
		initializeCommandList();

		commandList.get(addIndex).add(COMMAND_ADD);
		commandList.get(addIndex).add(COMMAND_ADD_PLUS);
		commandList.get(deleteIndex).add(COMMAND_DELETE );
		commandList.get(deleteIndex).add(COMMAND_DELETE_MINUS);
		commandList.get(deleteIndex).add(COMMAND_DELETE_REMOVE);
		commandList.get(deleteIndex).add(COMMAND_DELETE_CANCEL);
		commandList.get(displayIndex).add(COMMAND_DISPLAY);
		commandList.get(displayIndex).add(COMMAND_DISPLAY_SHOW);
		commandList.get(displayIndex).add(COMMAND_DISPLAY_LIST);
		commandList.get(clearIndex).add(COMMAND_CLEAR);
		commandList.get(searchIndex).add(COMMAND_SEARCH);
		commandList.get(searchIndex).add(COMMAND_SEARCH_FIND);
		commandList.get(editIndex).add(COMMAND_EDIT);
		commandList.get(editIndex).add(COMMAND_EDIT_CHANGE);
		commandList.get(editIndex).add(COMMAND_EDIT_UPDATE);
		commandList.get(markIndex).add(COMMAND_MARK);
		commandList.get(markIndex).add(COMMAND_MARK_COMPLETE);
		commandList.get(markIndex).add(COMMAND_MARK_FINISH);
		commandList.get(markIndex).add(COMMAND_MARK_DONE);
		commandList.get(markIndex).add(COMMAND_MARK_TICK);
		commandList.get(redoIndex).add(COMMAND_REDO);
		commandList.get(undoIndex).add(COMMAND_UNDO);		
	}

	public boolean isThisType(String command, CommandType type, int index){
		for(String alias: commandList.get(index)){
			if(command.equalsIgnoreCase(alias)){
				return true;
			}
		}
		return false;
	}
	
	public CommandType determineType(String command){
		
		if( isThisType(command, CommandType.ADD, addIndex) ){
			return CommandType.ADD;
		}
		
		if( isThisType(command, CommandType.DELETE, deleteIndex) ){
			return CommandType.DELETE;
		}
		
		if( isThisType(command, CommandType.EDIT, editIndex) ){
			return CommandType.EDIT;
		}
		
		if( isThisType(command, CommandType.DISPLAY, displayIndex) ){
			return CommandType.DISPLAY;
		}
		
		if( isThisType(command, CommandType.CLEAR, clearIndex) ){
			return CommandType.CLEAR;
		}
		
		if( isThisType(command, CommandType.SEARCH, searchIndex) ){
			return CommandType.SEARCH;
		}
		
		if( isThisType(command, CommandType.MARK, markIndex) ){
			return CommandType.MARK;
		}
		
		if( isThisType(command, CommandType.REDO, redoIndex) ){
			return CommandType.REDO;
		}
		
		if( isThisType(command, CommandType.UNDO, undoIndex) ){
			return CommandType.UNDO;
		}
		
		if( isThisType(command, CommandType.ADD, addIndex) ){
			return CommandType.ADD;
		}
		
		if( isThisType(command, CommandType.ADD, addIndex) ){
			return CommandType.ADD;
		}
		
		return CommandType.UNABLE_TO_DETERMINE;
	}
	
	public ArrayList<ArrayList<String>> getCommandList(){
		createCommandList();
		return commandList;
	}
	
	public CommandType getType(String command){
		createCommandList();
		return determineType(command);
	}
}

