package itinerary.userinterface;

import itinerary.main.Logic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

	private static final String WINDOW_TITLE = "ITnerary";
	private static final String WINDOW_FILE_NAME = "ApplicationWindow.fxml";
	
	private Logic logic;

	public MainApplication(Logic logic) {
		super();
		this.logic = logic;
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
		controller.setUpController(logic, stage);
	}

}
