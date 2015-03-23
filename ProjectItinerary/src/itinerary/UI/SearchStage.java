package itinerary.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//@author A0121437N
public class SearchStage extends Stage {
	
	private static final String WINDOW_TITLE = "Advanced Search";
	private static final String WINDOW_FILE_NAME = "SearchWindow.fxml";
	
	private static SearchStage search = null;
	private SearchResultCallback callback;
	
	private SearchStage (SearchResultCallback callback) throws IOException {
		super();
		assert callback != null;
		this.setCaller(callback);
		
		Parent root = FXMLLoader.load(getClass().getResource(WINDOW_FILE_NAME));
		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(WINDOW_TITLE);
	}
	
	public static SearchStage getInstance (SearchResultCallback callback) throws IOException {
		if (search == null) {
			search = new SearchStage(callback);
		}
		return search;
	}
	
	public void setCaller (SearchResultCallback callback) {
		assert callback != null;
		this.callback = callback;
	}
	
	public void invokeCaller () {
		callback.executeAdvancedSearch();
	}
	
	public static interface SearchResultCallback {
		void executeAdvancedSearch ();
	}
}
