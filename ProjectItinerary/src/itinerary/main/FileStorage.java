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
 * <li>2) BUG: updateLineNum() DOES NOT WORK AS INTENDED.
 * <li>3) Writes to file before returning State object.
 * <li>4) Assume Task lineNumbers start from 1, not 0.
 * <li>5) When returning the list of Tasks being held in an instance of
 * fileStorage, it will return a duplicated copy and not the direct reference.
 * <li>6) After adding or removing a certain item in listTask, the lineNumbers
 * for each of the Task objects in listTask will be auto-updated.
 * <ul>
 * <p>
 */
public class FileStorage implements Storage {

    // Variables
    private static int numOfInstances = 0;

    private File currFile;
    private List<Task> listTask;
    private int personalID;

    // Constructors

    public FileStorage() {

        this("default.txt");
    }

    public FileStorage(String fileName) {

        numOfInstances++;

        this.currFile = new File(fileName);
        this.listTask = JsonIOHandler.readJsonFileListTask(currFile);
        this.personalID = numOfInstances;

        this.updateLineNum();
    }

    public FileStorage(FileStorage fileStorage) {

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

    public String getCurrFileName() {

        return currFile.toString();
    }

    public int getCurrNumOfRunningInstances() {

        return numOfInstances;
    }

    /**
     * Deep copies a Task object.
     * 
     * @param item
     *            The Task object to be duplicated.
     * @return A duplicated Task object
     */
    public Task deepCopyTask(Task item) {

        if (item instanceof DeadlineTask) {

            DeadlineTask newItem =
                                   new DeadlineTask(item.getLineNumber(),
                                                    item.getText(),
                                                    item.getCategory(), false,
                                                    false, null);

            newItem.setComplete(item.isComplete());
            newItem.setPriority(item.isPriority());
            newItem.setDeadline(((DeadlineTask) item).getDeadline());

            return newItem;
        } else if (item instanceof ScheduleTask) {

            ScheduleTask newItem =
                                   new ScheduleTask(item.getLineNumber(),
                                                    item.getText(),
                                                    item.getCategory(), false,
                                                    false, null, null);

            newItem.setComplete(item.isComplete());
            newItem.setPriority(item.isPriority());
            newItem.setFromDate(((ScheduleTask) item).getFromDate());
            newItem.setToDate(((ScheduleTask) item).getToDate());

            return newItem;
        } else {

            Task newItem =
                           new Task(item.getLineNumber(), item.getText(),
                                    item.getCategory(), false, false);

            newItem.setComplete(item.isComplete());
            newItem.setPriority(item.isPriority());

            return newItem;

        }
    }

    /**
     * Duplicates the current state of the list of Tasks held in this instance
     * of FileStorage, not inclusive of the Task objects within it.
     * 
     * @param duplicateTask
     *            If true, will deep copy task objects as well
     * @return A duplicated list of Tasks held in this instance of FileStorage.
     */
    public List<Task> duplicateCurrentListTask(boolean duplicateTask) {

        List<Task> duplicateList = new ArrayList<Task>();

        for (Task item : this.listTask) {

            if (!duplicateTask) {
                duplicateList.add(item);
            } else {
                duplicateList.add(deepCopyTask(item));
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
     * Sorts listTasks according to lineNumber. This function doesn't work as
     * intended.
     */
    public void sortList() {

        List<Task> tempTaskList = new ArrayList<Task>();

        int currLineNum = 1;

        if (this.listTask != null) {
            while (tempTaskList.size() != this.listTask.size()) {
    
                for (Task item : this.listTask) {
    
                    if (item.getLineNumber() == currLineNum) {
    
                        tempTaskList.add(item);
                        currLineNum++;
                        break;
                    }
                }
    
            }
        }

        this.listTask = tempTaskList;
    }

    /**
     * Update the lineNumber variables in each Task object after operation.
     */
    private void updateLineNum() {

        if (this.listTask != null || this.listTask.size() != 0) {
            List<Task> tempTaskList = this.getAllTasks();
    
            int currLineNum = 1;
    
            for (Task item : tempTaskList) {
    
                item.setLineNumber(currLineNum);
                currLineNum++;
            }
    
            this.listTask = tempTaskList;
    
            sortList();
        } else {
            
            this.listTask = new ArrayList<Task>();
        }
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#addLine(itinerary.main.Command)
     */
    public State addTask(Command command) {

        listTask.add(command.getTask().getLineNumber() - 1, command.getTask());
        updateLineNum();
        JsonIOHandler.writeJSONList(currFile, listTask);
        return new State(command, new Command(command.getTask(),
                                              CommandType.DELETE),
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
        JsonIOHandler.writeJSONList(currFile, listTask);
        return new State(command, new Command(originalTask, CommandType.EDIT),
                         getAllTasks(), true);
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

        return new State(command, new Command(originalTask, CommandType.ADD),
                         getAllTasks(), isValid);
    }

    private boolean isValidDeleteOp(Command command) {

        if (command == null) {

            System.out.println("Command given was null.");
            return false;
        }

        if (command.getTask() == null) {

            System.out.println("Task given was null");
            return false;
        }

        if (command.getTask().getLineNumber() - 1 >= listTask.size()) {

            System.out.println("Overexceeded Line Number.");
            return false;
        }

        if (!JsonIOHandler.stringFormatter(command.getTask())
                          .equals(JsonIOHandler.stringFormatter(listTask.get(command.getTask()
                                                                                    .getLineNumber() - 1)))) {

            System.out.println("Task given and Task in " + this.personalID
                               + " do not match.");
            System.out.println(JsonIOHandler.stringFormatter(command.getTask()));
            System.out.println(JsonIOHandler.stringFormatter(listTask.get(command.getTask()
                                                                                 .getLineNumber() - 1)));
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

        List<Task> originalState = getAllTasks();
        listTask.clear();
        JsonIOHandler.writeJSONList(currFile, listTask);
        return new State(null, new Command(null, CommandType.UNDO),
                         originalState, true);
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#refillAll(itinerary.main.Command)
     */
    public State refillAll(List<Task> tasks) {

        listTask.addAll(tasks);
        updateLineNum();
        return new State(null, new Command(null, CommandType.CLEAR),
                         duplicateCurrentListTask(true), true);
    }
}
