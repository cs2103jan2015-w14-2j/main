package itinerary.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@author A0121409R
/**
 * Class for handling IO File operations. <br>
 * <br>
 * Assumptions:
 * <p>
 * <ul>
 * <li>1) Writes to file before returning State object.
 * <li>2) Assume Task taskIds start from 1, not 0.
 * <li>3) When returning the list of Tasks being held in an instance of
 * fileStorage, it will return a duplicated copy and not the direct reference.
 * <li>4) After adding or removing a certain item in listTask, the taskIds for
 * each of the Task objects in listTask will be auto-updated.
 * <li>5) If no fileName is given, a default fileName will be used.
 * <ul>
 * <p>
 */
public class ProtoFileStorage extends Storage {
    
    private static final String ERROR_EDIT_TASK_ID =
                                                     "Unable to edit task, invalid Task ID!";
    private static final String ERROR_DELETE_TASK_ID =
                                                       "Unable to delete task, invalid Task ID!";
    private static final String ERROR_IO = "Error writing to file.";
    
    private static final String DEFAULT_FILENAME = "default.txt";
    
    private File currFile;
    private List<Task> listTask;

    // Constructors

    public ProtoFileStorage() {
        this(DEFAULT_FILENAME);
    }
    
    public ProtoFileStorage(File file) {
        this(file.toString());
    }

    public ProtoFileStorage(String fileName) {
        
        if (fileName == "") {
            
            fileName = DEFAULT_FILENAME;
        }
        
        this.currFile = new File(fileName);
        this.listTask = JsonIOHandler.readJSONFileListTask(currFile);

        this.updateTaskId();
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
     * Update the taskId variables in each Task object after operation.
     */
    private void updateTaskId() {
        if (this.listTask != null && this.listTask.size() != 0) {
            List<Task> tempTaskList = this.getAllTasks();
            int currentTaskId = 1;

            for (Task item : tempTaskList) {
                item.setTaskId(currentTaskId);
                currentTaskId++;
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
        
        assert task != null;
        
        Task toAdd = task.clone();
        int taskId = toAdd.getTaskId();

        if (taskId == -1) {
            taskId = listTask.size() + 1;
            toAdd.setTaskId(taskId);
        }

        listTask.add(taskId - 1, toAdd);
        updateTaskId();

        writeToFile();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public void editTask(Task task) throws StorageException {
        
        assert task != null;

        int taskIndex = task.getTaskId() - 1;
        if (isInvalidIndex(taskIndex)) {
            throw new StorageException(ERROR_EDIT_TASK_ID);
        }

        Task originalTask = listTask.remove(taskIndex).clone();
        Task editedTask = super.updateTaskDetails(originalTask, task);
        listTask.add(taskIndex, editedTask);
        updateTaskId();

        writeToFile();
    }

    private boolean isInvalidIndex(int taskIndex) {
        return taskIndex < 0 || taskIndex >= listTask.size();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public void deleteTask(Task task) throws StorageException {
        
        assert task != null;
        
        int taskIndex = task.getTaskId() - 1;
        if (isInvalidIndex(taskIndex)) {
            throw new StorageException(ERROR_DELETE_TASK_ID);
        }

        listTask.remove(taskIndex);
        updateTaskId();

        writeToFile();
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

        writeToFile();
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public void refillAll(List<Task> tasks) throws StorageException {
        
        assert tasks != null;
        
        listTask = tasks;
        updateTaskId();

        writeToFile();
    }

    /**
     * The method which saves the current list stored in virtual memory to
     * currFile.
     * 
     * @throws StorageException
     *             If an IOException is thrown in JsonIOHandler.
     */
    public void writeToFile() throws StorageException {
        try {
            JsonIOHandler.writeJSONList(currFile, listTask);
        } catch (IOException e) {
            throw new StorageException(ERROR_IO);
        }
    }
    
    /**
     * This returns a list of all categories currently held in listTask.
     * 
     * @return A List<String> object containing all the categories.
     */
    public List<String> getAllCategories() {
        List<String> list = new ArrayList<String>();
        List<Task> taskList = getAllTasks();
        for (Task task : taskList) {
            if (!list.contains(task.getCategory())) {
                list.add(task.getCategory());
            }
        }
        return list;
    }
}
