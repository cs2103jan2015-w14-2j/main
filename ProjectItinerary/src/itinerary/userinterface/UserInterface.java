package itinerary.userinterface;

import itinerary.main.Logic;
import itinerary.userinterface.FileNameRequestDialog.NameRequestListener;
import javafx.application.Application;
import javafx.stage.Stage;

//@author A0121437N
public class UserInterface extends Application {
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
				openMainApplication(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		double STAGE_MIN_HEIGHT = 629.0;
		this.stage.setMinHeight(STAGE_MIN_HEIGHT);
		double STAGE_MIN_WIDTH = 504.0;
		this.stage.setMinWidth(STAGE_MIN_WIDTH);
		if (!logic.isConfigured()) {
			new FileNameRequestDialog(listener, null).show();
		} else {
			openMainApplication(this.stage);
		}
	}
	
	private void openMainApplication (Stage stage) throws Exception {
		new MainApplication(logic, fileName).start(stage);		
	}
}
