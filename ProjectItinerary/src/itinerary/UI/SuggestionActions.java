package itinerary.UI;

public interface SuggestionActions {
	boolean focusShowCondition ();
	
	boolean textChangedHideCondition (String oldValue, String newValue);
	
	void onEnterAction ();
}
