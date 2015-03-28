package itinerary.userinterface;

import itinerary.storage.Storage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FileNameRequestDialog extends Application {
	
	Stage stage;
	NameRequestListener listener;
	Label label;
	TextField fileNameTextField;
	
	public FileNameRequestDialog(NameRequestListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void start(Stage dialogStage) throws Exception {
		this.stage = dialogStage;
		dialogStage.setTitle("Enter file name");
		
		label = new Label("Please enter your preferred file name or path below");
		
		fileNameTextField = new TextField();
		fileNameTextField.setPromptText("Eg. example.txt or C:\\folder\\example.txt");
		fileNameTextField.setOnAction(enterPressed);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(label, fileNameTextField);
		
		Scene scene = new Scene(vbox);
		dialogStage.setScene(scene);
		dialogStage.show();
	}
	
	private EventHandler<ActionEvent> enterPressed = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			String name = null;
			do {
				name = fileNameTextField.getText();
			} while (!Storage.isValidFileName(name));
			stage.close();
			listener.onFileNameEntered(name);
		}
	};
	
	public interface NameRequestListener {
		void onFileNameEntered (String filename);
	}
}
