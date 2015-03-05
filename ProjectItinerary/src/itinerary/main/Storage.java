package itinerary.main;

import java.util.List;

//@author A0121409R
public interface Storage {

    /**
     * Call this to add a line to the file.
     * 
     * @param command
     *            - A Command object.
     *            - If lineNumber in command.getTask() is equal to -1, add task to end.
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
     * @return A List<Task> object containing all the relevant content.
     */
    List<Task> getAllTasks();

    /**
     * Call this to delete everything from the file.
     *
     * @return A State object.
     */
    State clearAll();

    /**
     * Call this to undo clearAll()
     * 
     * @param tasks
     *            - A List<Task> object containing all the tasks to be refilled
     * @return A State object
     */
    State refillAll(List<Task> tasks);
}
