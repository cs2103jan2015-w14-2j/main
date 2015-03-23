package itinerary.test;

import java.util.Calendar;
import java.util.List;

import itinerary.main.*;

//@author A0121409R

/**
 * Demonstrates the likely behavior of FileStorage. Can also be considered as an
 * "unofficial" test of FileStorage.
 */
public class FileStorageDemo {

    // Starting state:

    static Task task1 = new Task(-1, "ExampleText1", "ExampleCategory1", true,
                                 true);
    static Task task2 = new ScheduleTask(-1, "ExampleText2", "ExampleCategory2",
                                         true, true, Calendar.getInstance(),
                                         Calendar.getInstance());
    static Task task3 = new DeadlineTask(-1, "ExampleText3", "ExampleCategory3",
                                         true, true, Calendar.getInstance());

    public static void main(String args[]) throws StorageException{

        FileStorage fileStorage = new FileStorage("mytestfile.txt");

        // Clear any old content (for this test only)
        fileStorage.clearAll();

        // Create empty list (since file is empty)
        List<Task> listTask = fileStorage.getAllTasks();

        // Add Tasks to file
        System.out.println("Add all tasks to fileStorage: ");
        addAllTasksToStorage(fileStorage);
        listTask = fileStorage.getAllTasks();
        
        printList(listTask);
        System.out.println();
        
        // Delete Task 1
        System.out.println("Delete Line 1 in fileStorage: ");
        fileStorage.deleteTask(listTask.get(0));
        List<Task> listTask2 = fileStorage.getAllTasks();

        printList(listTask2);
        System.out.println();
        
        // Delete Task 2
        System.out.println("Delete Line 2 in fileStorage: ");
        fileStorage.deleteTask(listTask.get(1));
        List<Task> listTask3 = fileStorage.getAllTasks();
        
        printList(listTask3);
        System.out.println();
        
        // Clear all tasks
        System.out.println("Clear all in fileStorage: ");
        fileStorage.clearAll();
        List<Task> listTask4 = fileStorage.getAllTasks();
        
        printList(listTask4);
        System.out.println();
        
        // Refill all tasks
        System.out.println("Refill all in fileStorage: ");
        fileStorage.refillAll(listTask);
        List<Task> listTask5 = fileStorage.getAllTasks();
        
        printList(listTask5);
        System.out.println();
        
        // Edit task1
        System.out.println("Edit 1 in fileStorage: ");
        Task editDetails = new Task(1, "edited task 1", null, false, false);
        fileStorage.editTask(editDetails);
        List<Task> listTask6 = fileStorage.getAllTasks();
        
        printList(listTask6);
        System.out.println();
        
        // Delete out of range SHOULD RESULT IN ERROR
        System.out.println("Attempt to delete OOR in fileStorage: ");
        Task deleteDetails = new Task(0, null, null, false, false);
        try {
        	fileStorage.deleteTask(deleteDetails);
        	System.out.println("Deleted " + deleteDetails.getTaskId());
        } catch (StorageException e) {
        	System.out.println("ERROR! " + e.getMessage());
        }
        
        // Edit out of range SHOULD RESULT IN ERROR
        System.out.println("Attempt to edit OOR in fileStorage: ");
        Task editOORDetails = new Task(100, null, null, false, false);
        try {
        	fileStorage.editTask(editOORDetails);
        	System.out.println("Edited " + editOORDetails.getTaskId());
        } catch (StorageException e) {
        	System.out.println("ERROR! " + e.getMessage());
        }
    }

	private static void addAllTasksToStorage(FileStorage fileStorage)
			throws StorageException {
		fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);
	}

    public static void printList(List<Task> listTask) {
        for (Task item : listTask) {
            System.out.println(JsonStringTagger.convertTasktoTaggedJsonString(item));
        }
    }
}
