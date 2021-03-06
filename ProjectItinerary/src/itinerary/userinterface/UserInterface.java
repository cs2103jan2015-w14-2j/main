package itinerary.userinterface;

import itinerary.main.Logic;
import itinerary.userinterface.FileNameRequestDialog.NameRequestListener;
import javafx.application.Application;
import javafx.stage.Stage;

//@author A0121437N
public class UserInterface extends Application {
	private static final double STAGE_MIN_WIDTH = 504.0;
	private static final double STAGE_MIN_HEIGHT = 629.0;
	
	Logic logic = new Logic();
	Stage stage;
	String fileName;

	public static void main (String[] args) {
		launch();
	}
	
	NameRequestListener listener = new NameRequestListener() {
		@Override
		public void onFileNameEntered(String name) {
			logic.saveStorageFileName(name);
			logic.setupLogicVariables(name);
			fileName = name;
			try {
				openMainApplication();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		this.stage.setMinHeight(STAGE_MIN_HEIGHT);
		this.stage.setMinWidth(STAGE_MIN_WIDTH);
		if (!logic.isFileConfigured()) {
			FileNameRequestDialog.getInstance(listener, null).show();
		} else {
			openMainApplication();
		}
	}

	private void openMainApplication() {
		new MainStage(logic).show();
	}
}
