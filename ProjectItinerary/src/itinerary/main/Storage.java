package itinerary.main;

import java.util.List;

//@author A0121409R
public abstract class Storage {

    /**
     * Call this to add a task to the file. Asserts that task cannot be null.
     * 
     * @param task
     *            the task to be added
     * @throws StorageException
     *             thrown if any error occurs trying to add task
     */
    abstract void addTask(Task task) throws StorageException;

    /**
     * Call this to edit a task in the file. Asserts that task cannot be null.
     * 
     * @param task
     *            the taskId to be edited with the details to be modified
     * @throws StorageException
     *             thrown if any error occurs trying to edit task
     */
    abstract void editTask(Task task) throws StorageException;

    /**
     * Call this to delete a line in the file. Asserts that task cannot be null.
     * 
     * @param task
     *            the taskId to be deleted
     * @throws StorageException
     *             thrown if any error occurs trying to delete task
     */
    abstract void deleteTask(Task task) throws StorageException;

    /**
     * Call this to get all current content in the file.
     * 
     * @return A List<Task> object containing all tasks.
     */
    abstract List<Task> getAllTasks();

    /**
     * Call this to delete everything from the file.
     * 
     * @throws StorageException
     *             thrown if any error occurs trying to clear all tasks
     */
    abstract void clearAll() throws StorageException;

    /**
     * Call this to undo replace all the tasks in file with the tasks in tasks.
     * Asserts that tasks cannot be null.
     * 
     * @param tasks
     *            A List<Task> object containing all the tasks to be refilled
     * @throws StorageException
     *             thrown if any error occurs trying to refill all tasks
     */
    abstract void refillAll(List<Task> tasks) throws StorageException;

    /**
     * Called by editTask to overwrite originalTask's non-null variables in
     * details
     * 
     * @param originalTask
     *            the task object to be edited
     * @param details
     *            the details to be added or replaced
     * @return a new Task object with updated details
     */
    //@author A0121437N
    static Task updateTaskDetails (Task originalTask, Task details) {
    	Task template = originalTask.clone();
    	template.updateDetails(details);
    	return template;
    }
}
