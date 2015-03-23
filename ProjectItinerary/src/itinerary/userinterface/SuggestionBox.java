package itinerary.userinterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Window;

//@author A0121437N
public class SuggestionBox {
	private static final int SUGGESTIONS_HEIGHT = 24;
	
	private int maxSuggestionsShown = 6;
	private double width = -1.0;
	
	private List<String> suggestionSource = new ArrayList<String>();
	private ObservableList<String> suggestions = FXCollections.observableArrayList();
	
	private TextField textField;
	private Popup suggestionPopup = new Popup();
	private ListView<String> suggestionListView = new ListView<String>(suggestions);
	private SuggestionImplementation implementation;
	
	public SuggestionBox (TextField textField, SuggestionImplementation implementation) {
		this.textField = textField;
		this.implementation = implementation;
		this.textField.textProperty().addListener(textChangeListener);
		this.textField.focusedProperty().addListener(focusChangeListener);
		
		suggestionListView.setOnKeyReleased(keyReleaseHandler);
		suggestionListView.setOnMousePressed(mousePressHandler);
		
		suggestionPopup.getContent().add(suggestionListView);
	}
	
	private ChangeListener<String> textChangeListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			if (newValue.length() == 0) {
				hidePopup();
			} else {
				if (!suggestionPopup.isShowing()) {
					showPopup();
				}
				filterSuggestions(newValue);
			}
		}
	};
	
	private ChangeListener<Boolean> focusChangeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue && implementation.focusShowCondition()) {
				filterSuggestions(textField.getText());
				showPopup();
			} else {
				hidePopup();
			}
		}
	};
	
	private EventHandler<KeyEvent> keyReleaseHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			boolean isFromListView = event.getSource() == suggestionListView;
			boolean isEnterPressed = event.getCode() == KeyCode.ENTER;
			String suggestion = (String) suggestionListView.getSelectionModel().getSelectedItem();
			if (isFromListView && isEnterPressed) {
				if (suggestion != null) {
					selectSuggestion(suggestion);
				} else {
					hidePopup();
					implementation.onEnterAction();
				}
			}
		}
	};
	
	private EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			String suggestion = (String) suggestionListView.getSelectionModel().getSelectedItem();
			selectSuggestion(suggestion);
			hidePopup();
		}
	};
	
	public void setWidth (double width) {
		this.width = width;
	}
	
	public double getWidth () {
		if (width < 0) {
			return textField.getWidth();
		}
		return width;
	}
	
	private void selectSuggestion(String suggestion) {
		if (suggestion != null) {
			textField.setText(suggestion);
			textField.requestFocus();
			textField.end();
			hidePopup();
		}
	}
	
	public void updateSource (List<String> source) {
		while (!suggestionSource.isEmpty()) {
			suggestionSource.remove(0);
		}
		for (String string : source) {
			suggestionSource.add(string);
		}
		Collections.sort(suggestionSource);
	}
	
	public void filterSuggestions (String filter) {
		while (!suggestions.isEmpty()) {
			suggestions.remove(0);
		}
		String lowerFilter = filter.toLowerCase();
		for (String string : suggestionSource) {
			if (string.toLowerCase().contains(lowerFilter)) {
				suggestions.add(string);
			}
		}
		int count = suggestions.size();
		if (count == 0 && suggestionPopup.isShowing()) {
			hidePopup();
		} else if (suggestionPopup.isShowing()) {
			if (count > maxSuggestionsShown) {
				suggestionListView.setPrefHeight(maxSuggestionsShown * SUGGESTIONS_HEIGHT);
			} else {
				suggestionListView.setPrefHeight(count * SUGGESTIONS_HEIGHT);
			}
		}
	}
	
	private void showPopup () {
		suggestionListView.setPrefWidth(this.getWidth());
		Scene scene = textField.getScene();
		Window window = scene.getWindow();
		double anchorX = window.getX() + textField.localToScene(0, 0).getX() + scene.getX();
		double anchorY = window.getY() + textField.localToScene(0, 0).getY() + scene.getY()
				+ textField.getHeight();
		suggestionPopup.show(window, anchorX, anchorY);
		
		suggestionListView.getSelectionModel().clearSelection();
		suggestionListView.getFocusModel().focus(-1);
	}
	
	private void hidePopup () {
		suggestionPopup.hide();
	}
}
