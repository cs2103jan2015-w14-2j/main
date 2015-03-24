package itinerary.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//@author A0121437N
public class UserInterface extends Application {

	private static final String WINDOW_TITLE = "ITnerary";
	private static final String WINDOW_FILE_NAME = "ApplicationWindow.fxml";

	public static void main (String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WINDOW_FILE_NAME));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		stage.show();
		
		UiController controller = (UiController)loader.getController();
		controller.setupStageAndListeners(stage);
	}
	
}
