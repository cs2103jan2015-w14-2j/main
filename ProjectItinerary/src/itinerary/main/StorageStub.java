package itinerary.main;

import java.util.ArrayList;

//@author A0121409R

public class StorageStub implements Storage {

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#addLine(itinerary.main.Task)
     */
    public State addLine(Task task) {
        return new State(new Command(task, CommandType.ADD),
                         new Command(task, CommandType.DELETE), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#editLine(itinerary.main.Task)
     */
    public State editLine(Task task) {
        return new State(new Command(task, CommandType.EDIT),
                         new Command(task, CommandType.EDIT), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Task)
     */
    public State deleteLine(Task task) {
        return new State(new Command(task, CommandType.DELETE),
                         new Command(task, CommandType.ADD), null, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#searchFor(itinerary.main.Task)
     */
    public State searchFor(Task task) {
        return new State(new Command(task, CommandType.SEARCH), null,
                         new ArrayList<Task>(), true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#displayAll(itinerary.main.Task)
     */
    public State displayAll(Task task) {
        return new State(new Command(task, CommandType.DISPLAY), null,
                         new ArrayList<Task>(), true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#clearAll(itinerary.main.Task)
     */
    public State clearAll(Task task) {
        return new State(new Command(task, CommandType.CLEAR),
                         new Command(task, CommandType.REFILL), null, true);
    }

}
