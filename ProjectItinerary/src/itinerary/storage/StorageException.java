package itinerary.storage;

//@author A0121437N
public class StorageException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE = "An error occurred in storage!";
	
	public StorageException (String message) {
		super(message);
	}
	
	public StorageException () {
		super(DEFAULT_MESSAGE);
	}
}
