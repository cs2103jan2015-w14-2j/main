package itinerary.userinterface;

import itinerary.storage.Storage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FileNameRequestDialog extends Application {
	
	private static final String LABEL_TEXT = "Preferred file name or path below";
	private static final String TITLE = "Enter file name";
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
		Insets insets = new Insets(5.0);
		dialogStage.setTitle(TITLE);
		
		label = new Label(LABEL_TEXT);
		label.setFont(new Font(label.getFont().getFamily(), 15));
		
		fileNameTextField = new TextField();
		fileNameTextField.setOnAction(enterPressed);
		
		VBox vbox = new VBox();
		vbox.setSpacing(5.0);
		vbox.setPadding(insets);
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
