package itinerary.userinterface;

import java.util.ArrayList;
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
	private static final int MAX_VISIBLE_SUGGESTIONS = 6;
	private static final double SCROLL_BAR_HEIGHT = 15.0;
	private static final int SUGGESTION_HEIGHT = 24;
	
	private double width = -1.0;
	private double anchorX = -1.0;
	private double anchorY = -1.0;
	
	private List<String> suggestionSource = new ArrayList<String>();
	private ObservableList<String> suggestions = FXCollections.observableArrayList();
	
	private TextField textField;
	private Popup suggestionPopup = new Popup();
	private ListView<String> suggestionListView = new ListView<String>(suggestions);
	private OnEnterPressedListener listener;
	
	public SuggestionBox (TextField textField, OnEnterPressedListener implementation) {
		this.textField = textField;
		this.listener = implementation;
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
			if (newValue == null || newValue.length() == 0) {
				hidePopup();
			} else {
				filterSuggestions(newValue);
				if (!suggestionPopup.isShowing()) {
					showPopup();
				}
			}
		}
	};
	
	private ChangeListener<Boolean> focusChangeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			// Show the popup only if it is in focus and the length of text is greater than 0
			if (newValue && textField.getText().length() > 0) {
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
			boolean isEscapePressed = event.getCode() == KeyCode.ESCAPE;
			
			// The currently selected suggestion
			String suggestion = (String) suggestionListView.getSelectionModel().getSelectedItem();
			if (isFromListView && isEnterPressed) {
				if (suggestion != null) {
					selectSuggestion(suggestion);
				} else { // Nothing is selected
					hidePopup();
					listener.onEnterPressed();
				}
			} else if (isEscapePressed && suggestionPopup.isShowing()) {
				suggestionPopup.hide();
			}
		}
	};
	
	private EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			String suggestion = (String) suggestionListView.getSelectionModel().getSelectedItem();
			selectSuggestion(suggestion);
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
	
	public double getAnchorX() {
		if (anchorX < 0) {
			Scene scene = textField.getScene();
			Window window = scene.getWindow();
			return window.getX() + textField.localToScene(0, 0).getX() + scene.getX();
		}
		return anchorX;
	}

	public void setAnchorX(double anchorX) {
		this.anchorX = anchorX;
	}

	public double getAnchorY() {
		if (anchorY < 0) {
			Scene scene = textField.getScene();
			Window window = scene.getWindow();
			return window.getY() + textField.localToScene(0, 0).getY() + scene.getY() + textField.getHeight();
		}
		return anchorY;
	}

	public void setAnchorY(double anchorY) {
		this.anchorY = anchorY;
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
		suggestionSource.clear();
		for (String string : source) {
			suggestionSource.add(string);
		}
	}
	
	private void filterSuggestions (String filter) {
		suggestions.clear();
		String lowerFilter = filter.toLowerCase();
		for (String string : suggestionSource) {
			if (string != null && string.toLowerCase().contains(lowerFilter)) {
				if (!suggestions.contains(string)) {
					suggestions.add(string);
				}
			}
		}
		
		int count = suggestions.size();
		double height;
		if (count > MAX_VISIBLE_SUGGESTIONS) {
			height = calculateSuggestionHeight(MAX_VISIBLE_SUGGESTIONS);
		} else {
			height = calculateSuggestionHeight(count);
		}
		suggestionListView.setPrefHeight(height);
		
		if (count == 0 && suggestionPopup.isShowing()) {
			hidePopup();
		}
	}

	private double calculateSuggestionHeight(int count) {
		return count * SUGGESTION_HEIGHT  + SCROLL_BAR_HEIGHT;
	}
	
	private void showPopup () {
		if (suggestions != null && suggestions.size() > 0) {
			suggestionListView.setPrefWidth(this.getWidth());
			Scene scene = textField.getScene();
			Window window = scene.getWindow();
			suggestionPopup.show(window, getAnchorX(), getAnchorY());
			suggestionListView.getSelectionModel().clearSelection();
			suggestionListView.getFocusModel().focus(-1);
		}
	}
	
	private void hidePopup () {
		suggestionPopup.hide();
	}
	
	public interface OnEnterPressedListener {
		void onEnterPressed ();
	}
}
