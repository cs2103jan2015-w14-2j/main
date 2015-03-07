package itinerary.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//@author A0121409R
/**
 * Class for handling IO File operations. <br>
 * <br>
 * Assumptions:
 * <p>
 * <ul>
 * <li>1) BUG: Adding in duplicated Tasks or Commands WILL NOT WORK.
 * <li>2) Writes to file before returning State object.
 * <li>3) Assume Task lineNumbers start from 1, not 0.
 * <li>4) When returning the list of Tasks being held in an instance of
 * fileStorage, it will return a duplicated copy and not the direct reference.
 * <li>5) After adding or removing a certain item in listTask, the lineNumbers
 * for each of the Task objects in listTask will be auto-updated.
 * <ul>
 * <p>
 */
public class ProtoFileStorage extends Storage {

    private File currFile;
    private List<Task> listTask;

    // Constructors

    public ProtoFileStorage() {

        this("default.txt");
    }

    public ProtoFileStorage(String fileName) {
        this.currFile = new File(fileName);
        this.listTask = JsonIOHandler.readJsonFileListTask(currFile);

        this.updateLineNum();
    }
    
    // Getters

    public String getCurrFileName() {

        return currFile.toString();
    }

    /**
     * Duplicates the current state of the list of Tasks held in this instance
     * of FileStorage. The duplicateTask variable indicates if Task objects in
     * listTask will be deep copied as well.
     * 
     * @param duplicateTask
     *            If true, will deep copy Task objects as well.
     * @return A duplicated list of Tasks held in this instance of FileStorage.
     */
    public List<Task> duplicateCurrentListTask(boolean duplicateTask) {

        List<Task> duplicateList = new ArrayList<Task>();

        for (Task item : this.listTask) {

            if (!duplicateTask) {
                duplicateList.add(item);
            } else {
                duplicateList.add(item.clone());
            }
        }

        return duplicateList;
    }

    /**
     * Returns a printable ready String of the current contents held in currFile
     * in JSON format.
     * 
     * @param toAddTags
     *            If true, it will add in the tags alongside the JSON formatted
     *            String object.
     * @return A String object containing all current contents in currFile in
     *         JSON format.
     */
    public String currentListTaskString(boolean toAddTags) {

        return JsonIOHandler.readJSON(currFile, toAddTags);
    }

    /**
     * Update the lineNumber variables in each Task object after operation.
     */
    private void updateLineNum() {

        if (this.listTask != null && this.listTask.size() != 0) {

            List<Task> tempTaskList = this.getAllTasks();

            int currLineNum = 1;

            for (Task item : tempTaskList) {

                item.setLineNumber(currLineNum);
                currLineNum++;
            }

            this.listTask = tempTaskList;

        } else {

            this.listTask = new ArrayList<Task>();
        }
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public void addTask(Task task) throws StorageException {

        Task toAdd = task.clone();
        int taskId = toAdd.getLineNumber();

        if (taskId == -1) {
            taskId = listTask.size() + 1;
            toAdd.setLineNumber(taskId);
        }

        listTask.add(taskId - 1, toAdd);
        updateLineNum();

        JsonIOHandler.writeJSONList(currFile, listTask);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public void editTask(Task task) throws StorageException {

    	int taskIndex = task.getLineNumber() - 1;
    	if (isInvalidIndex(taskIndex)) {
    		// TODO Extract error message as constant
    		throw new StorageException("Unable to edit task, invalid Task ID!");
    	}
    	
        Task originalTask = listTask.remove(taskIndex).clone();
        // TODO Edit original task
        listTask.add(task.getLineNumber(), task);
        updateLineNum();

        JsonIOHandler.writeJSONList(currFile, listTask);
    }

	private boolean isInvalidIndex(int taskIndex) {
		return taskIndex < 0 || taskIndex >= listTask.size();
	}

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public void deleteTask(Task task) throws StorageException {
    	int taskIndex = task.getLineNumber() - 1;
    	if (isInvalidIndex(taskIndex)) {
    		// TODO Extract error message as constant
    		throw new StorageException("Unable to delete task, invalid Task ID!");
    	}
    	
    	listTask.remove(taskIndex);
    	updateLineNum();
    	JsonIOHandler.writeJSONList(currFile, listTask);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public List<Task> getAllTasks() {
        return duplicateCurrentListTask(true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public void clearAll() throws StorageException {
        listTask.clear();
        JsonIOHandler.writeJSONList(currFile, listTask);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public void refillAll(List<Task> tasks) throws StorageException {
        listTask = tasks;
        updateLineNum();
        JsonIOHandler.writeJSONList(currFile, listTask);
    }
}
