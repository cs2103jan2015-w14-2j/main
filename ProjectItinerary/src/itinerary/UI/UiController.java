package itinerary.UI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public class UiController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private TextField commandTextField;
	
	@FXML
	private ScrollPane scrollPane;
	
	public void commandEntered (ActionEvent event) {
		System.out.println(commandTextField.getText());
		commandTextField.setText("");
	}
}
