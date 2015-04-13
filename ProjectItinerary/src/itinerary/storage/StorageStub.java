package itinerary.storage;

import itinerary.main.Task;

import java.util.ArrayList;
import java.util.List;

//@author A0121409R
public class StorageStub extends Storage {
    private static final String ERROR_INVALID_ID = "Invalid Task ID!";
    private List<Task> tasks;

    //@author A0121437N
    public StorageStub() {
        this.tasks = new ArrayList<Task>();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    @Override
	public void addTask(Task task) throws StorageException {
    	Task taskClone = task.clone();
    	tasks.add(taskClone);
    	updateIds();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    @Override
	public void editTask(Task task) throws StorageException {
    	int taskIndex = task.getTaskId() - 1;
    	if (taskIndex < 0 || taskIndex >= tasks.size()) {
    		throw new StorageException(ERROR_INVALID_ID);
    	}
    	Task originalTask = tasks.remove(taskIndex);
    	Task editedTask = super.updateTaskDetails(originalTask, task);
    	tasks.add(taskIndex, editedTask);
    	updateIds();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    @Override
	public void deleteTask(Task task) throws StorageException {
    	int taskId = task.getTaskId();
    	if (taskId < 1 || taskId > tasks.size()) {
    		throw new StorageException(ERROR_INVALID_ID);
    	}
    	tasks.remove(taskId - 1);
    	updateIds();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    @Override
	public List<Task> getAllTasks() {
    	ArrayList<Task> tempTasks = new ArrayList<Task>();
    	for (Task task : tasks) {
    		tempTasks.add(task.clone());
    	}
        return tempTasks;
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    @Override
	public void clearAll() throws StorageException {
    	tasks.clear();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    @Override
	public void refillAll(List<Task> tasks) throws StorageException {
    	this.tasks = tasks;
    	updateIds();
    }
    
    private void updateIds () {
    	for (int i = 0; i < tasks.size(); i++) {
    		tasks.get(i).setTaskId(i + 1);
    	}
    }
    
    @Override
	public void close() {
        //Do nothing
    }

}
