package itinerary.main;

import java.util.ArrayList;

//@author A0121409R

public class StorageStub implements Storage {

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public State addLine(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.DELETE), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public State editLine(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.EDIT), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public State deleteLine(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.ADD), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#searchFor(itinerary.main.Command)
     */
    public State searchFor(Command command) {
        return new State(command, null, new ArrayList<Task>(), true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public State displayAll(Command command) {
        return new State(command, null, new ArrayList<Task>(), true);
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
