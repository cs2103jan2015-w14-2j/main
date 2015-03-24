package itinerary.userinterface;

import itinerary.main.Logic;
import itinerary.main.Task;
import itinerary.main.UserInterfaceContent;
import itinerary.userinterface.SearchStage.SearchResultCallback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

// Things To Do
// TODO New advanced search window when clicked advanced search

//@author A0121437N
public class UiController implements Initializable, SearchResultCallback {
	
	private static final String ERROR_OPEN_SEARCH = "Error! Unable to open Advanced Search window";
	@FXML
	private TextField commandTextField;
	
	@FXML
	private TextArea consoleTextArea;
	
	@FXML
	private ListView<TaskHBox> listView;
	
	@FXML
	private Hyperlink advSearch;
	
	@FXML
	private TextField basicSearchTextField;
	private SuggestionBox suggestionBox;
	
	private Stage uiStage;
	
	private Logic logic = new Logic("test");	
	private ObservableList<TaskHBox> list = FXCollections.observableArrayList();
	
	private SuggestionImplementation autoComplete = new SuggestionImplementation() {
		@Override
		public boolean textChangedHideCondition(String oldValue, String newValue) {
			return newValue.length() == 0;
		}
		
		@Override
		public void onEnterAction() {
			executeBasicSearch(null);
		}
		
		@Override
		public boolean focusShowCondition() {
			return basicSearchTextField.getText().length() > 0;
		}
	};
	
	private EventHandler<WindowEvent> closeHandler = new EventHandler<WindowEvent> () {
		@Override
		public void handle(WindowEvent event) {
			logic.close();
		}
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		consoleTextArea.setFocusTraversable(false);
		UserInterfaceContent launch = logic.initialLaunch();
		
		suggestionBox = new SuggestionBox(basicSearchTextField, autoComplete);
		updateContent(launch);
	}

	public void setupStageAndListeners(Stage stage) {
		stage.setOnCloseRequest(closeHandler);
	}
	
	public void commandEntered (ActionEvent event) {
		// adding things to the console
		String command = commandTextField.getText();
		UserInterfaceContent result = logic.executeUserInput(command);
		updateContent(result);
		commandTextField.setText("");
	}

	private void updateContent(UserInterfaceContent content) {
		appendConsoleMessage(content.getConsoleMessage());
		updateTaskList(content.getDisplayableTasks());
		updateDescriptions(content.getAllTasks());
	}
	
	private void updateDescriptions(List<Task> tasks) {
		List<String> suggestions = new ArrayList<String>();
		for (Task task : tasks) {
			suggestions.add(task.getText());
		}
		suggestionBox.updateSource(suggestions);
	}

	public void openAdvancedSearch (ActionEvent event) {
		try {
			SearchStage searchStage = SearchStage.getInstance(this);
			searchStage.show();
		} catch (IOException e) {
			appendConsoleMessage(ERROR_OPEN_SEARCH);
		}
	}
	
	@Override
	public void executeAdvancedSearch() {
		// TODO Update Search Results
		appendConsoleMessage("Update search results");
	}
	
	public void executeBasicSearch (ActionEvent event) {
		UserInterfaceContent result = logic.executeBasicSearch(basicSearchTextField.getText());
		updateContent(result);
	}

	private void appendConsoleMessage(String consoleMessage) {
		boolean isConsoleEmpty = consoleTextArea.getText().equals("");
		if (!isConsoleEmpty) {
			consoleTextArea.appendText(System.lineSeparator());
		}
		consoleTextArea.appendText(consoleMessage);
	}
	
	private void updateTaskList (List<Task> tasks) {
		list = convertList(tasks);
		listView.setItems(list);
	}
	
	private static ObservableList<TaskHBox> convertList (List<Task> tasks) {
		ObservableList<TaskHBox> list = FXCollections.observableArrayList();
		for (Task task : tasks) {
			list.add(new TaskHBox(task));
		}
		return list;
	}
}
