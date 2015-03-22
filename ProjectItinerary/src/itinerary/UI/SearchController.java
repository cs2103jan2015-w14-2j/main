package itinerary.UI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SearchController implements Initializable {

	@FXML
	Button button;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void closeWindow (ActionEvent event) {
		SearchStage searchStage = (SearchStage) button.getScene().getWindow();
		// Get values from all of the entries
		// Create object to pass to caller
		// Pass object to caller
		searchStage.invokeCaller();
		searchStage.close();
	}
	
}
