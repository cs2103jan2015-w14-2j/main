package itinerary.userinterface;

import javafx.beans.property.SimpleStringProperty;

//@author A0121437N
public class HelpEntry {
	public static final String COMMAND_NAME_VARIABLE = "commandName";
	public static final String COMMAND_ALIAS_VARIABLE = "commandAlias";
	private final SimpleStringProperty commandName;
	private final SimpleStringProperty commandAlias;
	
	public HelpEntry(String commandName,
			String commandAlias) {
		super();
		this.commandName = new SimpleStringProperty(commandName);
		this.commandAlias = new SimpleStringProperty(commandAlias);
	}

	public String getCommandName() {
		return commandName.get();
	}

	public String getCommandAlias() {
		return commandAlias.get();
	}
}
