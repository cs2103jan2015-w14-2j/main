package itinerary.main;

//@author A0121437N
public class ParserException extends Exception {
	private static final long serialVersionUID = 2L;
	private static final String DEFAULT_ERROR_MESSAGE = "An error occured when trying to parse user input";

	public ParserException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public ParserException(String message) {
		super(message);
	}
}
