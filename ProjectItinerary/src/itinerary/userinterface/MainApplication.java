package itinerary.userinterface;

import itinerary.main.Logic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

	private static final String WINDOW_TITLE = "ITnerary - %1$s";
	private static final String WINDOW_FILE_NAME = "ApplicationWindow.fxml";
	
	private Logic logic;
	private String name;

	public MainApplication(Logic logic, String name) {
		super();
		this.logic = logic;
		if (name != null) {
			this.name = name;
		} else {
			this.name = logic.getCurrentFileName();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WINDOW_FILE_NAME));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(formatTitle(name));
		stage.show();
		
		MainController controller = (MainController)loader.getController();
		controller.setUpController(logic, stage);
	}

	public static String formatTitle (String name) {
		return String.format(WINDOW_TITLE, name);
	}
}
