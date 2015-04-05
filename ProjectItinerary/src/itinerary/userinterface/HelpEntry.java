package itinerary.userinterface;

import javafx.beans.property.SimpleStringProperty;

public class HelpEntry {
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
