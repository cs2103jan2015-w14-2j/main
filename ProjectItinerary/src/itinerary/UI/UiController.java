package itinerary.UI;

import itinerary.UI.SearchStage.SearchResultCallback;
import itinerary.main.DeadlineTask;
import itinerary.main.Logic;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;
import itinerary.main.UserInterfaceContent;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

// Things To Do
// TODO Implement Auto-Complete in basic search text field
// TODO New advanced search window when clicked advanced search

//@author A0121437N
public class UiController implements Initializable, SearchResultCallback, EventHandler<Event>, ChangeListener<String> {
	
	private static final int SUGGESTIONS_HEIGHT = 24;
	private static final int SUGGESTIONS_MAX = 6;
	private static final String ERROR_OPEN_SEARCH = "Error! Unable to open Advanced Search window";
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy";
	private static final String TIME_FORMAT = "hh:mm aaa";
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
	// For auto complete
	private Popup suggestionPopup;
	private ListView<String> suggestionListView;
	
	private List<String> taskDescriptions = new ArrayList<String>();
	private ObservableList<String> suggestions = FXCollections.observableArrayList();
	
	private Logic logic = new Logic("test");	
	private ObservableList<TaskHBox> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		consoleTextArea.setFocusTraversable(false);
		UserInterfaceContent launch = logic.initialLaunch();
		updateContent(launch);
		
		basicSearchTextField.setOnKeyPressed(this);
		basicSearchTextField.textProperty().addListener(this);
		basicSearchTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue && basicSearchTextField.getText().length() > 0) {
					showPopup();
				} else {
					hidePopup();
				}
			}
		});
		
		suggestionListView = new ListView<String>(suggestions);
		suggestionListView.setOnMouseReleased(this);
		suggestionListView.setOnKeyReleased(this);
		
		suggestionPopup = new Popup();
		suggestionPopup.getContent().add(suggestionListView);
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
		while (!taskDescriptions.isEmpty()) {
			taskDescriptions.remove(0);
		}
		for (Task task : tasks) {
			taskDescriptions.add(task.getText());
		}
		// Sort alphabetically
		Collections.sort(taskDescriptions);
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
	
	public static class TaskHBox extends HBox {
		private static final double PADDING_AMOUNT = 8.0;
		
		VBox textContainer = new VBox();
		HBox upperText = new HBox();
		HBox lowerText = new HBox();
		
		Label taskIdLabel = new Label();
		Label taskDesLabel = new Label();
		
		Label taskDate = new Label();
		
		Pane spacerPane = new Pane();
		Label taskCatLabel = new Label();
		Image starImage = new Image("itinerary/UI/star.png");
		ImageView starImageView = new ImageView();
		
		Task task;
		
		public TaskHBox (Task task) {
			super();
			this.task = task;
			extractNormalTaskDetails(task);
			extractSpecialTaskDetails(task);
			
			HBox.setHgrow(spacerPane, Priority.ALWAYS);
			getChildren().addAll(textContainer, spacerPane, taskCatLabel, starImageView);
			
			// Set padding around HBox for visual appeal
			setPadding(new Insets(PADDING_AMOUNT));
		}

		private void extractSpecialTaskDetails(Task task) {
			if (task instanceof ScheduleTask || task instanceof DeadlineTask) {
				taskDate.setText(formatDates(task));
				lowerText.getChildren().addAll(taskDate);
				textContainer.getChildren().add(lowerText);
			}
		}

		private void extractNormalTaskDetails(Task task) {
			taskIdLabel.setText(task.getTaskId() + ". ");
			taskDesLabel.setText(task.getText());
			
			taskCatLabel.setText(task.getCategory());
			if (task.isPriority()) {
				starImageView.setImage(starImage);
			}
			
			upperText.getChildren().addAll(taskIdLabel, taskDesLabel);
			textContainer.getChildren().add(upperText);
		}

		private String formatDates(Task task) {
			String result = "";
			if (task instanceof ScheduleTask) {
				ScheduleTask scheduleTask = (ScheduleTask) task;
				Calendar from = scheduleTask.getFromDate();
				Calendar to = scheduleTask.getToDate();
				
				result += formatCalendarDate(from) + ", ";
				result += formatCalendarTime(from) + " - ";
				
				// Don't need to repeat the date again if it is on the same day
				if (!isSameDay(from, to)) {
					result += formatCalendarDate(to) + ", ";
				}
				result += formatCalendarTime(to);
				return result;
			} else {
				DeadlineTask deadlineTask = (DeadlineTask) task;
				result += formatCalendarDate(deadlineTask.getDeadline());
				result += ", ";
				result += formatCalendarTime(deadlineTask.getDeadline());
				return result;
			}
		}
		
		private static String formatCalendarDate (Calendar date) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			return dateFormat.format(date.getTime());
		}
		
		private static String formatCalendarTime (Calendar time) {
			SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
			return timeFormat.format(time.getTime());
		}
		
		private static boolean isSameDay (Calendar firstDay, Calendar secondDay) {
			boolean sameYear = firstDay.get(Calendar.YEAR) == secondDay.get(Calendar.YEAR);
			boolean sameDate = firstDay.get(Calendar.DAY_OF_YEAR) == secondDay.get(Calendar.DAY_OF_YEAR);
			return sameYear && sameDate;
		}
	}

	// For the auto-complete search box
	@Override
	public void handle(Event event) {
		if (event.getEventType() == KeyEvent.KEY_RELEASED) { // listview
			if (event.getSource() == suggestionListView) {
				KeyEvent keyEvent = (KeyEvent) event;
				boolean isFromListView = keyEvent.getSource() == suggestionListView;
				boolean isEnterPressed = keyEvent.getCode() == KeyCode.ENTER;
				String suggestion = (String) suggestionListView.getSelectionModel().getSelectedItem();
				if (isFromListView && isEnterPressed) {
					if (suggestion != null) {
						selectSuggestion(suggestion);
					} else {
						hidePopup();
						executeBasicSearch(null);
					}
					
				}
			}
		} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) { //listview
			hidePopup();
		}
	}

	private void selectSuggestion(String suggestion) {
		if (suggestion != null) {
			basicSearchTextField.setText(suggestion);
			basicSearchTextField.requestFocus();
			basicSearchTextField.end();
			hidePopup();
		}
	}

	@Override
	public void changed(ObservableValue<? extends String> observable,
			String oldValue, String newValue) {
		if (newValue.length() == 0) {
			hidePopup();
		} else {
			if (!suggestionPopup.isShowing()) {
				showPopup();
			}
			updateSuggestions(newValue);
		}
	}

	private void showPopup () {
		suggestionListView.setPrefWidth(basicSearchTextField.getWidth());
		Scene scene = basicSearchTextField.getScene();
		Window window = scene.getWindow();
		double anchorX = window.getX() + basicSearchTextField.localToScene(0, 0).getX() + scene.getX();
		double anchorY = window.getY() + basicSearchTextField.localToScene(0, 0).getY() + scene.getY()
				+ basicSearchTextField.getHeight();
		suggestionPopup.show(window, anchorX, anchorY);
		
		suggestionListView.getSelectionModel().clearSelection();
		suggestionListView.getFocusModel().focus(-1);
	}
	
	private void hidePopup () {
		suggestionPopup.hide();
	}
	
	private void updateSuggestions (String start) {
		while (!suggestions.isEmpty()) {
			suggestions.remove(0);
		}
		for (String description : taskDescriptions) {
			if (description.toLowerCase().contains(start.toLowerCase())) {
				suggestions.add(description);
			}
		}
		int count = suggestions.size();
		if (count == 0 && suggestionPopup.isShowing()) {
			hidePopup();
		} else if (suggestionPopup.isShowing()) {
			if (count > SUGGESTIONS_MAX) {
				suggestionListView.setPrefHeight(SUGGESTIONS_MAX * SUGGESTIONS_HEIGHT);
			} else {
				suggestionListView.setPrefHeight(count * SUGGESTIONS_HEIGHT);
			}
		}
	}
}
