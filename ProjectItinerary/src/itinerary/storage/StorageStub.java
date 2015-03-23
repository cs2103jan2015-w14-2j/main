package itinerary.storage;

import itinerary.main.DeadlineTask;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//@author A0121409R
public class StorageStub extends Storage {
    private static final String ERROR_INVALID_ID = "Invalid Task ID!";
    private List<Task> tasks;

    //@author A0121437N
    public StorageStub() {
        this.tasks = new ArrayList<Task>();
        this.tasks.add(new DeadlineTask(1, "Finish homework", "cat6", true, false,
                Calendar.getInstance()));
        this.tasks.add(new ScheduleTask(2, "Team meeting", "cat5", true, false,
                        Calendar.getInstance(),
                        Calendar.getInstance()));
        this.tasks.add(new Task(3, "Feed the dog", "cat1", false, false));
        this.tasks.add(new Task(4, "Have dinner", "cat2", false, true));
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public void addTask(Task task) throws StorageException {
    	tasks.add(task);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public void editTask(Task task) throws StorageException {
    	int taskIndex = task.getTaskId() - 1;
    	if (taskIndex < 0 || taskIndex >= tasks.size()) {
    		throw new StorageException(ERROR_INVALID_ID);
    	}
    	Task originalTask = tasks.remove(taskIndex);
    	Task editedTask = super.updateTaskDetails(originalTask, task);
    	tasks.add(taskIndex, editedTask);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public void deleteTask(Task task) throws StorageException {
    	int taskId = task.getTaskId();
    	if (taskId < 1 || taskId > tasks.size()) {
    		throw new StorageException(ERROR_INVALID_ID);
    	}
    	tasks.remove(taskId - 1);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public void clearAll() throws StorageException {
    	tasks.clear();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public void refillAll(List<Task> tasks) throws StorageException {
    	this.tasks = tasks;
    }
    
    public void close() {
        //Do nothing
    }

}
