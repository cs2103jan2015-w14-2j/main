package itinerary.userinterface;

import itinerary.main.Task;
import itinerary.search.SearchTask;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//@author A0121437N
public class SearchStage extends Stage {
	
	private static final String WINDOW_TITLE = "Search";
	private static final String WINDOW_FILE_NAME = "SearchWindow.fxml";
	
	private static SearchStage onlyInstance = null;
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
		this.initStyle(StageStyle.UTILITY);
		
		controller = (SearchController)loader.getController();
	}
	
	public static SearchStage getInstance (SearchResultCallback callback, List<Task> tasks) throws IOException {
		if (onlyInstance == null) {
			onlyInstance = new SearchStage(callback);
			onlyInstance.updateTasks(tasks);
		}
		return onlyInstance;
	}
	
	public static void closeIfShowing () {
		if (isSearchShowing()) {
			onlyInstance.close();
		}
	}
	
	private static boolean isSearchShowing () {
		if (onlyInstance == null) {
			return false;
		}
		return onlyInstance.isShowing();
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
