package itinerary.main;

import java.util.List;

//@author A0121409R
public interface Storage {

    /**
     * Call this to add a line to the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State addTask(Command command);

    /**
     * Call this to edit a line in the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State editTask(Command command);

    /**
     * Call this to delete a line in the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State deleteTask(Command command);

    /**
     * Call this to display all current content in the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    List<Task> displayAll(Command command);

    /**
     * Call this to delete everything from the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State clearAll(Command command);

    /**
     * Call this to undo clearAll()
     * 
     * @param command
     *            - A Command object.
     * @return A State object
     */
    State refillAll(Command command);

}
