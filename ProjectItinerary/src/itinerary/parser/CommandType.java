package itinerary.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

//@author A0121437N
public enum CommandType {
	ADD ("Add", "add", "+"),
	CLEAR ("Clear", "clear"),
	DELETE ("Delete", "delete", "-", "del", "remove", "cancel"),
	DISPLAY ("Display", "display", "show", "list"),
	EDIT ("Edit", "edit", "change", "update"),
	SEARCH ("Search", "search", "find"),
	REDO ("Redo", "redo"),
	UNDO ("Undo", "undo"),
	MARK ("Mark", "mark", "complete", "finish", "done"),
	UNMARK ("Unmark", "unmark"),
	HELP ("Help", "help", "?"),
	UNABLE_TO_DETERMINE ("");
	
	private final String commandName;
	private final List<String> commandAliases = new ArrayList<String>();
	
	private static final List<CommandType> allTypes;
	static {
		allTypes = new ArrayList<CommandType>();
		for (CommandType type : EnumSet.allOf(CommandType.class)) {
			allTypes.add(type);
		}
	}
	
	CommandType (String commandName, String... commandAliases) {
		this.commandName = commandName;
		for (String alias : commandAliases) {
			this.commandAliases.add(alias);
		}
	}
	
	public String getCommandName () {
		return this.commandName;
	}
	
	public List<String> getCommandAliases () {
		List<String> copies = new ArrayList<String>(commandAliases.size());
		Collections.copy(copies, commandAliases);
		return copies;
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
