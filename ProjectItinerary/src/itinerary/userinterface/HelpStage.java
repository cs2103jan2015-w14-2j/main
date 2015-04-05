package itinerary.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelpStage extends Stage{
	private static final String HEADER_COMMAND = "Command";
	private static final String HEADER_KEYWORD = "Keyword";
	private static final String WINDOW_TITLE = "Help";
	private static HelpStage onlyInstance;
	
	private TableView<String> table = new TableView<String>();
	TableColumn<String, String> commandColumn = new TableColumn<String, String>(HEADER_COMMAND);
	TableColumn<String, String> keywordColumn = new TableColumn<String, String>(HEADER_KEYWORD);
	
	private HelpStage () {
		Pane container = new Pane();
		setupTable();
		
		container.getChildren().add(this.table);
		Scene scene = new Scene(container);
		
		this.setScene(scene);
		this.setTitle(WINDOW_TITLE);
		this.setResizable(false);
		this.initStyle(StageStyle.UTILITY);
	}
	
	private void setupTable() {		
		table.getColumns().addAll(commandColumn, keywordColumn);
		table.setEditable(false);
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
