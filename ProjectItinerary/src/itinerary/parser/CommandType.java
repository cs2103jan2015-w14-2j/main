package itinerary.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

//@author A0121437N
public enum CommandType {
	ADD ("Add a task", "add", "+"),
	CLEAR ("Clear all tasks", "clear"),
	DELETE ("Delete a task", "delete", "-", "del", "remove", "cancel"),
	DISPLAY ("Display all tasks", "display", "show", "list"),
	EDIT ("Edit a task", "edit", "change", "update"),
	SEARCH ("Search for tasks", "search", "find"),
	REDO ("Redo an action", "redo"),
	UNDO ("Undo an action", "undo"),
	MARK ("Mark a task as complete", "mark", "complete", "finish", "done"),
	UNMARK ("Unmark a task as complete", "unmark"),
	HELP ("Get help", "help", "?"),
	UNABLE_TO_DETERMINE ("");
	
	private final String commandTitle;
	private final List<String> commandAliases = new ArrayList<String>();
	
	private static final List<CommandType> allTypes;
	static {
		allTypes = new ArrayList<CommandType>();
		for (CommandType type : EnumSet.allOf(CommandType.class)) {
			allTypes.add(type);
		}
	}
	
	CommandType (String commandTitle, String... commandAliases) {
		this.commandTitle = commandTitle;
		for (String alias : commandAliases) {
			this.commandAliases.add(alias);
		}
	}
	
	public String getCommandTitle () {
		return this.commandTitle;
	}
	
	public List<String> getCommandAliases () {
		List<String> copies = new ArrayList<String>();
		for (String alias : commandAliases) {
			copies.add(alias);
		}
		return copies;
	}
	
	public static List<CommandType> getAllTypes () {
		Comparator<CommandType> alphabeticalComparator = new Comparator<CommandType>() {
			@Override
			public int compare(CommandType type1, CommandType type2) {
				return type1.getCommandTitle().compareTo(type2.getCommandTitle());
			}
		};
		Collections.sort(allTypes, alphabeticalComparator);
		return allTypes;
	}
	
	public static CommandType identifyCommandType (String alias) {
		for (CommandType commandType : allTypes) {
			if (commandType.commandAliases.contains(alias)) {
				return commandType;
			}
		}
		return UNABLE_TO_DETERMINE;
	}
}
