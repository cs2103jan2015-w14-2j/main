package itinerary.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// @author A0121409R
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
public class ProtoFileStorage implements Storage {

    // Variables
    private static int numOfInstances = 0;

    private File currFile;
    private List<Task> listTask;
    private int personalID;

    // Constructors

    public ProtoFileStorage() {

        this("default.txt");
    }

    public ProtoFileStorage(String fileName) {

        numOfInstances++;

        this.currFile = new File(fileName);
        this.listTask = JsonIOHandler.readJsonFileListTask(currFile);
        this.personalID = numOfInstances;

        this.updateLineNum();
    }

    public ProtoFileStorage(ProtoFileStorage fileStorage) {

        numOfInstances++;

        String fullOldFileName = fileStorage.getCurrFileName();
        String oldFileName =
                             fullOldFileName.substring(0,
                                                       fullOldFileName.length() - 4);
        String oldFileType =
                             fullOldFileName.substring(fullOldFileName.length() - 4);

        this.currFile = new File(oldFileName + numOfInstances + oldFileType);

        this.listTask = fileStorage.duplicateCurrentListTask(true);
        JsonIOHandler.writeJSONList(currFile, listTask);
        this.personalID = numOfInstances;

        this.updateLineNum();
    }
    
    //Getters

    public String getCurrFileName() {

        return currFile.toString();
    }

    public int getCurrNumOfRunningInstances() {

        return numOfInstances;
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
    public State addTask(Command command) {

        Task toAdd = command.getTask();
        int taskId = toAdd.getLineNumber();

        if (taskId == -1) {
            taskId = listTask.size() + 1;
            toAdd.setLineNumber(taskId);
        }

        listTask.add(taskId - 1, command.getTask());
        updateLineNum();

        JsonIOHandler.writeJSONList(currFile, listTask);
        return new State(command, new Command(toAdd, CommandType.DELETE, null),
                         getAllTasks(), true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#editLine(itinerary.main.Command)
     */
    public State editTask(Command command) {

        Task originalTask =
                            listTask.remove(command.getTask().getLineNumber() - 1);
        listTask.add(command.getTask());
        updateLineNum();

        JsonIOHandler.writeJSONList(currFile, listTask);
        return new State(command, new Command(originalTask, CommandType.EDIT,
                                              null), getAllTasks(), true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#deleteLine(itinerary.main.Command)
     */
    public State deleteTask(Command command) {

        boolean isValid = isValidDeleteOp(command);
        Task originalTask = null;

        if (isValid) {

            originalTask =
                           listTask.remove(command.getTask().getLineNumber() - 1);

            updateLineNum();

            JsonIOHandler.writeJSONList(currFile, listTask);
        }

        return new State(command, new Command(originalTask, CommandType.ADD,
                                              null), getAllTasks(), isValid);
    }

    private boolean isValidDeleteOp(Command command) {

        if (command == null) {

            System.out.println("Command given was null.");
            return false;
        }

        if (command.getTask() == null) {

            System.out.println("Task given was null.");
            return false;
        }

        if (command.getTask().getLineNumber() - 1 >= listTask.size()) {

            System.out.println("Overexceeded Line Number.");
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#displayAll(itinerary.main.Command)
     */
    public List<Task> getAllTasks() {

        return this.listTask;
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#clearAll(itinerary.main.Command)
     */
    public State clearAll() {

        List<Task> originalState = duplicateCurrentListTask(true);
        listTask.clear();

        JsonIOHandler.writeJSONList(currFile, listTask);
        Command clearCommand = new Command(null, CommandType.CLEAR, null);
        return new State(clearCommand, clearCommand, originalState, true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public State refillAll(List<Task> tasks) {

        listTask.addAll(tasks);
        updateLineNum();

        JsonIOHandler.writeJSONList(currFile, listTask);
        return new State(null, null, duplicateCurrentListTask(true), true);
    }
}
