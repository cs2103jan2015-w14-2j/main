package itinerary.userinterface;

import itinerary.main.Task;
import itinerary.search.SearchTask;
import itinerary.userinterface.SuggestionBox.OnEnterPressedListener;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

//@author A0121437N
public class SearchController implements Initializable {

	@FXML
	Button searchButton, clearButton;
	@FXML
	CheckBox priorityCheckBox, completedCheckBox;
	@FXML
	TextField descText, catText;
	SuggestionBox descBox, catBox;
	@FXML
	DatePicker fromDatePicker, toDatePicker;
	
	private OnEnterPressedListener enterListener = new OnEnterPressedListener() {
		@Override
		public void onEnterPressed() {
			// do nothing on enter press
		}
	};
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		descBox = new SuggestionBox(descText, enterListener);
		catBox = new SuggestionBox(catText, enterListener);
	}

	public void executeSearch () {
		SearchStage searchStage = (SearchStage) searchButton.getScene().getWindow();
		if (isAtLeastOneFilled()) {
			// Get values from all of the entries
			String description = getStringToSearch(descText.getText());
			String category = getStringToSearch(catText.getText());
			
			Boolean searchPriority = getBooleanToSearch(priorityCheckBox.isSelected());
			Boolean searchCompleted = getBooleanToSearch(completedCheckBox.isSelected());
			
			Calendar from = getCalendarToSearch(fromDatePicker.getValue());
			Calendar to = getCalendarToSearch(toDatePicker.getValue());
			
			SearchTask searchTask = new SearchTask(0, description, category,
					searchPriority, searchCompleted, from, to);
			searchStage.invokeCaller(searchTask);
		}
		searchStage.close();
	}

	private Calendar getCalendarToSearch(LocalDate date) {
		Calendar calendar = null;
		if (date != null) {
			calendar = Calendar.getInstance();
			calendar.set(date.getYear(), date.getMonthValue() - 1,
					date.getDayOfMonth(), 0, 0);
		}
		return calendar;
	}

	private Boolean getBooleanToSearch(Boolean bool) {
		return bool ? true : null;
	}

	private String getStringToSearch(String string) {
		return string != null && !string.equals("") ? string : null;
	}

	public void updateDetails(List<Task> tasks) {
		ObservableList<String> taskDescriptions = FXCollections.observableArrayList();
		ObservableList<String> taskCategories = FXCollections.observableArrayList();
		
		for (Task task : tasks) {
			taskDescriptions.add(task.getText());
			taskCategories.add(task.getCategory());
		}
		
		descBox.updateSource(taskDescriptions);
		catBox.updateSource(taskCategories);
	}
	
	public void clearFields() {
		descText.setText(null);
		catText.setText(null);
		fromDatePicker.setValue(null);
		toDatePicker.setValue(null);
		
		priorityCheckBox.setSelected(false);
		completedCheckBox.setSelected(false);
	}
	
	private boolean isAtLeastOneFilled () {
		String desc = descText.getText();
		boolean isDescFilled = desc != null && !desc.equals("");
		String cat = catText.getText();
		boolean isCatFilled = cat != null  && !cat.equals("");
		boolean isFromFilled = fromDatePicker.getValue() != null;
		boolean isToFilled = toDatePicker.getValue() != null;
		
		return isDescFilled || isCatFilled || isFromFilled || isToFilled;
	}
}
