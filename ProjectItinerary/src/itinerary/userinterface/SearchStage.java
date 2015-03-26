package itinerary.userinterface;

import itinerary.main.Task;
import itinerary.search.SearchTask;

import java.io.IOException;
import java.util.List;

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
	private SearchController controller;
	
	private SearchStage (SearchResultCallback callback) throws IOException {
		super();
		assert callback != null;
		this.setCaller(callback);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WINDOW_FILE_NAME));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(WINDOW_TITLE);
		this.setResizable(false);
		
		controller = (SearchController)loader.getController();
	}
	
	public static SearchStage getInstance (SearchResultCallback callback, List<Task> tasks) throws IOException {
		if (search == null) {
			search = new SearchStage(callback);
			search.updateTasks(tasks);
		}
		return search;
	}
	
	public void updateTasks (List<Task> tasks) {
		controller.updateDetails(tasks);
	}
	
	public void setCaller (SearchResultCallback callback) {
		assert callback != null;
		this.callback = callback;
	}
	
	public void invokeCaller (SearchTask searchTask) {
		callback.executeAdvancedSearch(searchTask);
	}
	
	public static interface SearchResultCallback {
		void executeAdvancedSearch (SearchTask searchTask);
	}
}
