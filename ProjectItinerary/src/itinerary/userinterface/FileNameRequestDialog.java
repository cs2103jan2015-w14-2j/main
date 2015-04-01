package itinerary.userinterface;

import itinerary.storage.Storage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FileNameRequestDialog extends Stage {
	
	private static final String LABEL_TEXT = "Preferred file name or path below";
	private static final String TITLE = "Enter file name";
	private static FileNameRequestDialog onlyInstance = null;
	
	NameRequestListener listener;
	Label textLabel;
	TextField fileNameTextField;
	
	public FileNameRequestDialog(String currFileName) {		
		Insets insets = new Insets(5.0);
		this.setTitle(TITLE);
		
		textLabel = new Label(LABEL_TEXT);
		String currentFontFamily = textLabel.getFont().getFamily();
		textLabel.setFont(new Font(currentFontFamily, 15));
		
		fileNameTextField = new TextField();
		fileNameTextField.setOnAction(enterPressed);
		// Set the textfield to the current file name
		
		VBox vbox = new VBox();
		vbox.setSpacing(5.0);
		vbox.setPadding(insets);
		vbox.getChildren().addAll(textLabel, fileNameTextField);
		
		Scene scene = new Scene(vbox);
		this.setScene(scene);
		this.setResizable(false);
		this.initStyle(StageStyle.UTILITY);
	}
	
	public static FileNameRequestDialog getInstance (NameRequestListener listener, String currFileName) {
		if (onlyInstance == null) {
			onlyInstance = new FileNameRequestDialog(currFileName);
		}
		onlyInstance.listener = listener;
		if (currFileName != null) {
			onlyInstance.fileNameTextField.setText(currFileName);
			onlyInstance.fileNameTextField.selectAll();
		}
		return onlyInstance;
	}
	
	public static boolean isDialogShowing () {
		if (onlyInstance == null) {
			return false;
		}
		return ((Stage)onlyInstance).isShowing();
	}
	
	public static void closeDialog () {
		onlyInstance.close();
	}
	
	private EventHandler<ActionEvent> enterPressed = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			String name = null;
			do {
				name = fileNameTextField.getText();
			} while (!Storage.isValidFileName(name));
			FileNameRequestDialog.this.close();
			listener.onFileNameEntered(name);
		}
	};
	
	public interface NameRequestListener {
		void onFileNameEntered (String filename);
	}
}
