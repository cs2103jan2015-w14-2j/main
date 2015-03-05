package itinerary.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// @author A0121409R
public class StorageStub implements Storage {
    private String fileName;

    public StorageStub(String fileName) {
        this.fileName = fileName;
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public State addTask(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.DELETE, null), null, true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public State editTask(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.EDIT, null), null, true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public State deleteTask(Command command) {
        return new State(command, new Command(command.getTask(),
                                              CommandType.ADD, null), null, true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new DeadlineTask(1, "Finish homework", "cat6", true, false,
                Calendar.getInstance()));
        tasks.add(new ScheduleTask(2, "Team meeting", "cat5", true, false,
                        Calendar.getInstance(),
                        Calendar.getInstance()));
        tasks.add(new Task(3, "Feed the dog", "cat1", false, false));
        tasks.add(new Task(4, "Have dinner", "cat2", false, true));
        
        return tasks;
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public State clearAll() {
        Command clearCommand = new Command(null, CommandType.CLEAR, null);
        List<Task> allTasks = getAllTasks();
        return new State(clearCommand, clearCommand, allTasks, true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public State refillAll(List<Task> tasks) {
        return new State(null, null, null, true);
    }

}
