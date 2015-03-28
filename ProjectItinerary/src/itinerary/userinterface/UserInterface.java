package itinerary.userinterface;

import itinerary.main.Logic;
import itinerary.userinterface.FileNameRequestDialog.NameRequestListener;
import javafx.application.Application;
import javafx.stage.Stage;

//@author A0121437N
public class UserInterface extends Application {
	Logic logic = new Logic();
	Stage stage;

	public static void main (String[] args) {
		launch();
	}
	
	NameRequestListener listener = new NameRequestListener() {
		@Override
		public void onFileNameEntered(String name) {
			logic.saveStorageFileName(name);
			logic.setUpLogicVariables(name);
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
		Logic logic = new Logic();
		if (!logic.isConfigured()) {
			new FileNameRequestDialog(listener).start(stage);
		} else {
			openMainApplication(this.stage);
		}
	}
	
	private void openMainApplication (Stage stage) throws Exception {
		new MainApplication(logic).start(stage);		
	}
}
