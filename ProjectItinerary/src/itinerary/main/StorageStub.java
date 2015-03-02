package itinerary.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//@author A0121409R
public class StorageStub implements Storage {
	private String fileName;
	
	public StorageStub (String fileName) {
		this.fileName = fileName;
	}

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
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task(1, "task 1", "cat1", false, false));
        tasks.add(new Task(2, "task 2", "cat2", false, true));
        tasks.add(new Task(3, "task 3", "cat3", true, false));
        tasks.add(new Task(4, "task 4", "cat4", true, true));
        tasks.add(new ScheduleTask(5, "stask 1", "cat5", true, true,
        		Calendar.getInstance(), Calendar.getInstance()));
        tasks.add(new DeadlineTask(6, "dtask 1", "cat6", true, true,
        		Calendar.getInstance()));
        return tasks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public State clearAll() {
    	Command clearCommand = new Command(null, CommandType.CLEAR);
    	List<Task> allTasks = getAllTasks();
        return new State(clearCommand, clearCommand, allTasks, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public State refillAll(List<Task> tasks) {
        return new State(null, null, null, true);
    }

}
