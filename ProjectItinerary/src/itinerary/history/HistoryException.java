package itinerary.history;

//@author A0121437N
public class HistoryException extends Exception {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "An error has occurred in History";
	
	public HistoryException (String message) {
		super(message);
	}
}
