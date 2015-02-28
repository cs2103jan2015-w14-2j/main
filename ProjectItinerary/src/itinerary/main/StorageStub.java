package itinerary.main;

import java.util.ArrayList;
import java.util.List;

//@author A0121409R
public class StorageStub implements Storage {

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public State addTask(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.DELETE), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public State editTask(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.EDIT), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public State deleteTask(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.ADD), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public List<Task> getAllTasks() {
        return new ArrayList<Task>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public State clearAll(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.REFILL), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public State refillAll(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.CLEAR), null, true);
    }

}
