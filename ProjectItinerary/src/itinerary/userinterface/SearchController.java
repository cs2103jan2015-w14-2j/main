package itinerary.userinterface;

import itinerary.main.Task;
import itinerary.search.SearchTask;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SearchController implements Initializable {

	@FXML
	Button searchButton;
	@FXML
	CheckBox descriptionCheckBox, categoriesCheckBox, priorityCheckBox, dateCheckBox;
	@FXML
	TextField descText, catText;
	SuggestionBox descSb, catSb;
	@FXML
	DatePicker fromDatePicker, toDatePicker;
	@FXML
	Button resetButton;
	
	ChangeListener<Boolean> onDescCheckChange = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (!newValue) {
				descText.setText("");
			}
			descText.setDisable(!newValue);
		}
	};
	
	ChangeListener<Boolean> onCatCheckChange = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (!newValue) {
				catText.setText("");
			}
			catText.setDisable(!newValue);
		}
	};
	
	ChangeListener<Boolean> onDateCheckChange = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (!newValue) {
				fromDatePicker.setValue(null);
				toDatePicker.setValue(null);
			}
			fromDatePicker.setDisable(!newValue);
			toDatePicker.setDisable(!newValue);
		}
	};
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		descSb = new SuggestionBox(descText, createSuggestionImplementation(descText));
		catSb = new SuggestionBox(catText, createSuggestionImplementation(catText));
		
		descriptionCheckBox.selectedProperty().addListener(onDescCheckChange);
		categoriesCheckBox.selectedProperty().addListener(onCatCheckChange);
		dateCheckBox.selectedProperty().addListener(onDateCheckChange);
	}

	public void executeSearch () {
		SearchStage searchStage = (SearchStage) searchButton.getScene().getWindow();		
		// Get values from all of the entries
		String description = descText.getText();
		String category = catText.getText();
		
		Boolean searchPriority = priorityCheckBox.isSelected() ? true : null;
		
		LocalDate fromLd = fromDatePicker.getValue();
		LocalDate toLd = toDatePicker.getValue();
		
		Calendar from = null, to = null;
		if (fromLd != null) {
			from = Calendar.getInstance();
			from.set(fromLd.getYear(), fromLd.getMonthValue() - 1,
					fromLd.getDayOfMonth(), 0, 0);
		}
		if (toLd != null) {
			to = Calendar.getInstance();
			to.set(toLd.getYear(), toLd.getMonthValue() - 1,
					toLd.getDayOfMonth(), 23, 59);
		}
		
		SearchTask searchTask = new SearchTask(0, description, category, searchPriority, false, from, to);
		
		searchStage.invokeCaller(searchTask);
		searchStage.close();
	}

	public void updateDetails(List<Task> tasks) {
		ObservableList<String> taskDescriptions = FXCollections.observableArrayList();
		ObservableList<String> taskCategories = FXCollections.observableArrayList();
		
		for (Task task : tasks) {
			taskDescriptions.add(task.getText());
			taskCategories.add(task.getCategory());
		}
		
		descSb.updateSource(taskDescriptions);
		catSb.updateSource(taskCategories);
	}
	
	public void resetFields() {
		descriptionCheckBox.setSelected(true);
		categoriesCheckBox.setSelected(true);
		priorityCheckBox.setSelected(false);
		dateCheckBox.setSelected(true);
		
		descText.setText(null);
		catText.setText(null);
		fromDatePicker.setValue(null);
		toDatePicker.setValue(null);
	}
	
	private static SuggestionImplementation createSuggestionImplementation (TextField textField) {
		return new SuggestionImplementation() {
			@Override
			public boolean textChangedHideCondition(String oldValue, String newValue) {
				return newValue.length() == 0;
			}
			
			@Override
			public void onEnterAction() {
				// do nothing on enter
			}
			
			@Override
			public boolean focusShowCondition() {
				if (textField.getText() == null) {
					return false;
				}
				return textField.getText().length() > 0;
			}
		};
	}
}
