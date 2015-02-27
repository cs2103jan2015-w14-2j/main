package itinerary.main;

//@author A0121409R

public interface Storage {
	
	/**
	 * Call this to add a line to the file.
	 * 
	 * @param task - A Task object.
	 * @return A State object.
	 */
	State addLine(Task task);
	
	/**
	 * Call this to edit a line in the file.
	 * 
	 * @param task - A Task object.
	 * @return A State object.
	 */
	State editLine(Task task);
	
	/**
	 * Call this to delete a line in the file.
	 * 
	 * @param task - A Task object.
	 * @return A State object.
	 */
	State deleteLine(Task task);
	
	/**
	 * Call this to display all current content in the file.
	 * 
	 * @param task - A Task object.
	 * @return A State object.
	 */
	State displayAll(Task task);
	
	
	/**
	 * Call this to delete everything from the file.
	 * 
	 * @param task - A Task object.
	 * @return A State object.
	 */
	State clearAll(Task task);

}
