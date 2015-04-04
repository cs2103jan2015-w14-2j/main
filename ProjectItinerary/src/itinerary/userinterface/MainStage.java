package itinerary.userinterface;

import java.io.IOException;

import itinerary.main.Logic;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Stage {

	private static final String WINDOW_TITLE = "ITnerary - %1$s";
	private static final String WINDOW_FILE_NAME = "ApplicationWindow.fxml";
	
	public MainStage(Logic logic) {
		super();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WINDOW_FILE_NAME));
		Parent root = null;
		try {
			root = (Parent)loader.load();
		} catch (IOException e) {
			// Should not result in any exceptions, if it does, check window file name
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		this.setScene(scene);
		String windowTitle = formatTitle(logic.getCurrentFileName());
		this.setTitle(windowTitle);
		
		MainController controller = (MainController)loader.getController();
		controller.setUpController(logic, this);
	}
	
	public static String formatTitle (String name) {
		return String.format(WINDOW_TITLE, name);
	}
}
