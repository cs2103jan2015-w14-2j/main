package itinerary.main;

import java.io.File;
import java.util.List;

//@author A0121409R
public class FileStorage implements Storage {

    // Variables

    private File currFile;

    // Constructors

    public FileStorage() {

        currFile = new File("default.txt");
    }

    public FileStorage(String fileName) {

        currFile = new File(fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public State addTask(Command command) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public State editTask(Command command) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public State deleteTask(Command command) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#searchFor(itinerary.main.Command)
     */
    public State searchFor(Command command) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public List<Task> displayAll(Command command) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public State clearAll(Command command) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public State refillAll(Command command) {
        return null;
    }
}
