package itinerary.userinterface;

import itinerary.main.Logic;
import itinerary.main.Task;
import itinerary.main.Logic.HelpListener;
import itinerary.search.SearchTask;
import itinerary.userinterface.FileNameRequestDialog.NameRequestListener;
import itinerary.userinterface.SearchStage.SearchResultCallback;
import itinerary.userinterface.SuggestionBox.OnEnterPressedListener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//@author A0121437N
public class MainController implements Initializable, SearchResultCallback, HelpListener {
	
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
	
	private OnEnterPressedListener searchBoxEnter = new OnEnterPressedListener() {
		@Override
		public void onEnterPressed() {
			executeBasicSearch();
		}
	};
	
	private EventHandler<WindowEvent> closeHandler = new EventHandler<WindowEvent> () {
		@Override
		public void handle(WindowEvent event) {
			SearchStage.closeIfShowing();
			FileNameRequestDialog.closeIfShowing();
			HelpStage.closeIfShowing();
			logic.exitOperation();
		}
	};
	
	private EventHandler<KeyEvent> upDownHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			KeyCode code = event.getCode();
			if (code == KeyCode.UP || code == KeyCode.DOWN) {
				String input = null;
				switch (code) {
					case UP :
						input = logic.getPreviousInput();
						break;
					case DOWN :
						input = logic.getNextInput();
						break;
					default :
						break;
				}
				if (input != null) {
					commandTextField.setText(input);
				} else {
					commandTextField.setText("");
				}
			}
		}
	};

	NameRequestListener nameRequestListener = new NameRequestListener() {
		@Override
		public void onFileNameEntered(String name) {
			logic.saveStorageFileName(name);
			logic.setupLogicVariables(name);
			mainStage.setTitle(MainStage.formatTitle(name));
			setLaunchContent();
		}
	};
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Do nothing
	}
	
	public void setUpController (Logic logic, Stage stage) {
		this.logic = logic;
		this.logic.addHelpListener(this);
		setupStageAndListeners(stage);
		this.mainStage = stage;
		setLaunchContent();
	}

	private void setLaunchContent() {
		consoleTextArea.setFocusTraversable(false);
		UserInterfaceContent launch = logic.initialLaunch();
		
		suggestionBox = new SuggestionBox(basicSearchTextField, searchBoxEnter);
		updateContent(launch);
	}

	private void setupStageAndListeners(Stage stage) {
		stage.setOnCloseRequest(closeHandler);
		commandTextField.addEventHandler(KeyEvent.KEY_PRESSED, upDownHandler);
	}
	
	public void commandEntered () {
		// adding things to the console
		String command = commandTextField.getText();
		if (command != null && !command.equals("")) {
			UserInterfaceContent result = logic.executeUserInput(command);
			updateContent(result);
			commandTextField.setText("");
		}
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
	
	@Override
	public void onHelpEntered() {
		HelpStage.getInstance().show();
	}
}
