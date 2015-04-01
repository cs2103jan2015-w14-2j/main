package itinerary.userinterface;

import itinerary.main.Logic;
import itinerary.main.Task;
import itinerary.main.UserInterfaceContent;
import itinerary.search.SearchTask;
import itinerary.userinterface.FileNameRequestDialog.NameRequestListener;
import itinerary.userinterface.SearchStage.SearchResultCallback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class MainController implements Initializable, SearchResultCallback {
	
	@FXML
	private TextField commandTextField;
	@FXML
	private TextArea consoleTextArea;
	@FXML
	private ListView<TaskHBox> listView;
	@FXML
	private Hyperlink advSearch;
	@FXML
	private Hyperlink config;
	@FXML
	private TextField basicSearchTextField;
	
	private Stage mainStage;
	private SuggestionBox suggestionBox;
	
	private Logic logic = new Logic();	
	private ObservableList<TaskHBox> list = FXCollections.observableArrayList();
	private List<Task> allTasks = new ArrayList<Task>();
	
	private SearchStage searchStage = null;
	
	private SuggestionImplementation autoComplete = new SuggestionImplementation() {
		@Override
		public boolean textChangedHideCondition(String oldValue, String newValue) {
			return newValue.length() == 0;
		}
		
		@Override
		public void onEnterAction() {
			executeBasicSearch();
		}
		
		@Override
		public boolean focusShowCondition() {
			return basicSearchTextField.getText().length() > 0;
		}
	};
	
	private EventHandler<WindowEvent> closeHandler = new EventHandler<WindowEvent> () {
		@Override
		public void handle(WindowEvent event) {
			if (searchStage != null && searchStage.isShowing()) {
				searchStage.close();
			}
			if (FileNameRequestDialog.isDialogShowing()) {
				FileNameRequestDialog.closeDialog();
			}
			logic.exitOperation();
		}
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void setUpController (Logic logic, Stage stage) {
		this.logic = logic;
		setupStageAndListeners(stage);
		this.mainStage = stage;
		setLaunchContent();
	}

	private void setLaunchContent() {
		consoleTextArea.setFocusTraversable(false);
		UserInterfaceContent launch = logic.initialLaunch();
		
		suggestionBox = new SuggestionBox(basicSearchTextField, autoComplete);
		updateContent(launch);
	}

	private void setupStageAndListeners(Stage stage) {
		stage.setOnCloseRequest(closeHandler);
	}
	
	public void commandEntered () {
		// adding things to the console
		String command = commandTextField.getText();
		UserInterfaceContent result = logic.executeUserInput(command);
		updateContent(result);
		commandTextField.setText("");
	}

	private void updateContent(UserInterfaceContent content) {
		appendConsoleMessage(content.getConsoleMessage());
		updateTaskList(content.getDisplayableTasks());
		allTasks = content.getAllTasks();
		updateDescriptions(allTasks);
		if (searchStage != null) {
			searchStage.updateTasks(allTasks);		
		}
	}
	
	private void updateDescriptions(List<Task> tasks) {
		List<String> suggestions = new ArrayList<String>();
		for (Task task : tasks) {
			suggestions.add(task.getText());
		}
		suggestionBox.updateSource(suggestions);
	}

	public void openAdvancedSearch () throws IOException {
		if (searchStage == null) {
			searchStage = SearchStage.getInstance(this, allTasks);
		}
		searchStage.show();
	}
	
	@Override
	public void executeAdvancedSearch(SearchTask searchTask) {
		// TODO Update Search Results
		UserInterfaceContent result = logic.executeAdvancedSearch(searchTask);
		updateContent(result);
	}
	
	public void executeBasicSearch () {
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
		list = convertTasksToHBoxes(tasks);
		listView.setItems(list);
	}
	
	private static ObservableList<TaskHBox> convertTasksToHBoxes (List<Task> tasks) {
		ObservableList<TaskHBox> list = FXCollections.observableArrayList();
		for (Task task : tasks) {
			list.add(new TaskHBox(task));
		}
		return list;
	}
	
	public void onConfigSourceClicked () {
		String current = logic.getCurrentFileName();
		logic.exitOperation();
		FileNameRequestDialog.getInstance(nameRequestListener, current).show();
	}
	
	NameRequestListener nameRequestListener = new NameRequestListener() {
		@Override
		public void onFileNameEntered(String name) {
			logic.saveStorageFileName(name);
			logic.setupLogicVariables(name);
			mainStage.setTitle(MainApplication.formatTitle(name));
			setLaunchContent();
		}
	};
}
