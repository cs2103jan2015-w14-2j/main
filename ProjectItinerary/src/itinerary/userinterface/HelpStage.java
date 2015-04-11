package itinerary.userinterface;

import itinerary.parser.CommandType;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//@author A0121437N
public class HelpStage extends Stage{
	private static final String HEADER_COMMAND = "Command";
	private static final String HEADER_KEYWORD = "Keyword(s)";
	private static final String WINDOW_TITLE = "Help";
	private static final double WINDOW_MIN_WIDTH = 350.0;
	private static HelpStage onlyInstance;
	
	private TableView<HelpEntry> helpTable = new TableView<HelpEntry>();
	TableColumn<HelpEntry, String> commandColumn = new TableColumn<HelpEntry, String>(HEADER_COMMAND);
	TableColumn<HelpEntry, String> aliasColumn = new TableColumn<HelpEntry, String>(HEADER_KEYWORD);
	
	private HelpStage () {
		Pane container = new Pane();
		setupTable();
		
		container.getChildren().add(this.helpTable);
		Scene scene = new Scene(container);
		
		this.setScene(scene);
		this.setTitle(WINDOW_TITLE);
		this.setResizable(false);
		this.initStyle(StageStyle.UTILITY);
	}
	
	@SuppressWarnings("unchecked")
	private void setupTable() {
		List<CommandType> allTypes = CommandType.getAllTypes();
		commandColumn.setCellValueFactory(new PropertyValueFactory<HelpEntry, String>(HelpEntry.COMMAND_NAME_VARIABLE));
		aliasColumn.setCellValueFactory(new PropertyValueFactory<HelpEntry, String>(HelpEntry.COMMAND_ALIAS_VARIABLE));
		
		ObservableList<HelpEntry> entries = FXCollections.observableArrayList();
		for (CommandType type : allTypes) {
			if (type.getCommandTitle().equals("")) {
				continue;
			}
			String collated = "";
			List<String> aliases = type.getCommandAliases();
			for (int i = 0; i < aliases.size(); i++) {
				collated += aliases.get(i);
				if (i + 1 != aliases.size()) {
					collated += ", ";
				}
			}
			entries.add(new HelpEntry(type.getCommandTitle(), collated));
		}
		
		helpTable.getColumns().addAll(commandColumn, aliasColumn);
		helpTable.setItems(entries);
		helpTable.setEditable(false);
		helpTable.setMinWidth(WINDOW_MIN_WIDTH);
	}

	public static HelpStage getInstance () {
		if (onlyInstance == null) {
			onlyInstance = new HelpStage();
		}
		return onlyInstance;
	}
	
	public static void closeIfShowing () {
		if (isHelpShowing()) {
			onlyInstance.close();
		}
	}
	
	private static boolean isHelpShowing () {
		if (onlyInstance == null) {
			return false;
		}
		return onlyInstance.isShowing();
	}
}
