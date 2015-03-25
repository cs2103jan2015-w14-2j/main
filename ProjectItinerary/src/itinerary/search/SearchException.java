package itinerary.search;

//@author A0121810Y
public class SearchException extends Exception {    

    private static final long serialVersionUID = 7854129702281405376L;
	private static final String DEFAULT_MESSAGE = "An error occurred in search!";
	
	public SearchException (String message) {
		super(message);
	}
	
	public SearchException () {
		super(DEFAULT_MESSAGE);
	}
}
