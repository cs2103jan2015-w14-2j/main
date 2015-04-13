package itinerary.storage;

import itinerary.main.DeadlineTask;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author A0121409R
/**
 * Class for handling the data and saving it onto file. <br>
 * <br>
 * Assumptions:
 * <p>
 * <ul>
 * <li>1) Writes to a tempFile first. Only upon exit will it overwrite the old file.
 * <li>2) Assume Task taskIds start from 1, not 0.
 * <li>3) When returning the list of Tasks, it will return a duplicated copy and
 * not the direct reference.
 * <li>4) After adding or removing a certain item in listTask, the taskIds for
 * each of the Task objects in listTask will be auto-updated.
 * <li>5) If no fileName is given, a default fileName will be used.
 * <ul>
 * </p>
 */
public class FileStorage extends Storage {
    
    private static final String ERROR_ADD_NO_TASK_DESC =
                                                         "Please ensure the Task to be added actually has a description.";
    private static final String ERROR_EDIT_TASK_ID =
                                                     "Unable to edit task, invalid Task ID!";
    private static final String ERROR_DELETE_TASK_ID =
                                                       "Unable to delete task, invalid Task ID!";
    private static final String ERROR_IO = "Error writing to file.";
    
    private static final String DEFAULT_FILENAME = "default.txt";
    
    private static final String TEMP_FILENAME = "temp.txt";
    
    private File currFile;
    private File tempFile;
    private List<Task> listTask;

    // Constructors

    public FileStorage() {
        this(DEFAULT_FILENAME);
    }
    
    public FileStorage(File file) {
        this(file.toString());
    }

    public FileStorage(String fileName) {
        
        if (fileName == "") {
            
            fileName = DEFAULT_FILENAME;
        }
        
        this.currFile = new File(fileName);
        this.listTask = JsonIOHandler.readJSONFileListTask(currFile);
        
        this.tempFile = new File(TEMP_FILENAME);

        this.updateTaskId();
    }

    // Getters

    public String getCurrFileName() {
        return currFile.toString();
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
     * Returns a printable ready String of the current contents held in tempFile
     * in JSON format.
     * 
     * @param toAddTags
     *            If true, it will add in the tags alongside the JSON formatted
     *            String object.
     * @return A String object containing all current contents in currFile in
     *         JSON format.
     */
    public String currentListTaskString(boolean toAddTags) {
        return JsonIOHandler.readJSON(tempFile, toAddTags);
    }
    
    /**
     * Sorts listTask. Super inefficient.
     */
    private void sortList() {
        this.listTask = TaskSorter.sort(this.listTask);
    }

    /**
     * Update the taskId variables in each Task object after operation.
     */
    private void updateTaskId() {
        if (this.listTask != null && this.listTask.size() != 0) {
            
            sortList();
            
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
        
        if (toAdd.hasNoText()) {
            throw new StorageException(ERROR_ADD_NO_TASK_DESC);
        }
        
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
            JsonIOHandler.writeJSONList(tempFile, listTask);
        } catch (IOException e) {
            throw new StorageException(ERROR_IO);
        }
    }

    /*
     * (non-Javadoc)
     * @see itinerary.main.Storage#close(itinerary.main.Command)
     */
    public void close() {
        if (tempFile.exists()) {
            if (!currFile.delete()) {
                Logger.getGlobal().log(Level.WARNING, "Old file not deleted.");
            }
            
            if (!tempFile.renameTo(currFile)) {
                Logger.getGlobal().log(Level.WARNING, "System unable to replace old file.");
            }
            
            File tempFile = new File(TEMP_FILENAME);
            tempFile.delete();
        }
    }

    /**
     * The sub-class which helps to sort the Tasks in ITnerary afetr every command. 
     */
    private static class TaskSorter {
    	private static final Comparator<Task> byComplete = new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
		        int returnVal = 0;
		        //Check against Completeness.
		        if (o1.isComplete() && !o2.isComplete()) { //o1 is greater.
		            returnVal = 1;
		        } else if (!o1.isComplete() && o2.isComplete()) { //o2 is greater.
		            returnVal = -1;
		        }
		        return returnVal;
			}
		};
		
		private static final Comparator<Task> byDates = new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
		        int returnVal = 0;

		        String classType1 = o1.getClass().getSimpleName();
		        String classType2 = o2.getClass().getSimpleName();

		        String deadlineTaskClassName = DeadlineTask.class.getSimpleName();
		        String scheduleTaskClassName = ScheduleTask.class.getSimpleName();

		        // o1 and o2 are of the same class type.
		        // Check against their Calendar arguments
		        if (classType1.equals(deadlineTaskClassName)
		            && classType2.equals(deadlineTaskClassName)) {
		            // o1 == DeadlineTask
		            // o2 == DeadlineTask
		            if (((DeadlineTask) o1).getDeadline()
		                                   .before(((DeadlineTask) o2).getDeadline())) {
		                // o1 has the earlier Deadline.
		            	// o1 is greater.
		                returnVal = -1;
		            } else if (((DeadlineTask) o2).getDeadline()
		                                          .before(((DeadlineTask) o1).getDeadline())) {
		                // o2 has the earlier Deadline.
		            	// o2 is greater.
		                returnVal = 1;
		            } else {
		                // o1 and o2 have the exact same Deadlines.
		                return 0;
		            }
		        } else if (classType1.equals(scheduleTaskClassName)
		                   && classType2.equals(scheduleTaskClassName)) {
		            // o1 == ScheduleTask
		            // o2 == ScheduleTask
		            if (((ScheduleTask) o1).getToDate()
		                                   .before(((ScheduleTask) o2).getToDate())) {
		                // o1 has the earlier ToDate.
		                // o1 is greater.
		                returnVal = -1;
		            } else if (((ScheduleTask) o2).getToDate()
		                                          .before(((ScheduleTask) o1).getToDate())) {
		                // o2 has the earlier ToDate.
		                // o2 is greater.
		                returnVal = 1;
		            } else {
		                // o1 and o2 have the exact same ToDates.
		                return 0;
		            }
		        } else {
		            // o1 and o2 are regular Tasks.
		            return 0;
		        }
		        return returnVal;
			}
		};
		
		private static final Comparator<Task> byDescription = new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
		        return o1.getText().compareTo(o2.getText());
			}
		};
		
		private static final Comparator<Task> byPriority = new Comparator<Task>() {
			@Override
		    public int compare(Task o1, Task o2) {
		        int returnVal = 0;
		        //Check against Priority.
		        if (o1.isPriority() == true && o2.isPriority() == false) { //o1 is greater.
		            returnVal = -1;
		        } else if (o2.isPriority() == true && o1.isPriority() == false) { //o2 is greater.
		            returnVal = 1;
		        } else {
		            returnVal = 0;
		        }		        
		        return returnVal;
		    }
		};
		
		private static final Comparator<Task> byType = new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
		        String classType1 = o1.getClass().getSimpleName();
		        String classType2 = o2.getClass().getSimpleName();

		        String taskClassName = Task.class.getSimpleName();
		        String deadlineTaskClassName = DeadlineTask.class.getSimpleName();
		        String scheduleTaskClassName = ScheduleTask.class.getSimpleName();

		        int returnVal = 0;

		        if (!classType1.equals(classType2)) { // o1 and o2 are not of the same class type.
		            if (classType1.equals(deadlineTaskClassName)
		                && !classType2.equals(deadlineTaskClassName)) {
		                // o1 == DeadlineTask
		                // o2 == ScheduleTask || Task

		                // o1 is greater.
		                returnVal = -1;
		            } else if (classType2.equals(deadlineTaskClassName)
		                       && !classType1.equals(deadlineTaskClassName)) {
		                // o1 == ScheduleTask || Task
		                // o2 == DeadlineTask

		                // o2 is greater.
		                returnVal = 1;
		            } else if (classType1.equals(scheduleTaskClassName)
		                       && classType2.equals(taskClassName)) {
		                // o1 == ScheduleTask
		                // o2 == Task

		                // o1 is greater.
		                returnVal = -1;
		            } else {
		                // o1 == Task
		                // o2 == ScheduleTask

		                // o2 is greater.
		                returnVal = 1;
		            }
		        } else {
		            returnVal = 0;
		        }

		        return returnVal;
			}
		};
		
		public static List<Task> sort(List<Task> tasks) {
			Collections.sort(tasks, byDescription);
			Collections.sort(tasks, byType);
			Collections.sort(tasks, byDates);
			Collections.sort(tasks, byPriority);
			Collections.sort(tasks, byComplete);
			
			return tasks;
		}
    }
}
