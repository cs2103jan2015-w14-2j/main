package itinerary.main;

//@author A0121409R
public interface Storage {

    /**
     * Call this to add a line to the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State addLine(Command command);

    /**
     * Call this to edit a line in the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State editLine(Command command);

    /**
     * Call this to delete a line in the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State deleteLine(Command command);

    /**
     * Call this to display all content related to a search term given.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State searchFor(Command command);

    /**
     * Call this to display all current content in the file.
     * 
     * @param command
     *            - A Command object.
     * @return A State object.
     */
    State displayAll(Command command);

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
