package itinerary.userinterface;

//@author A0121437N
public interface SuggestionImplementation {
	boolean focusShowCondition ();
	
	boolean textChangedHideCondition (String oldValue, String newValue);
	
	void onEnterAction ();
}
