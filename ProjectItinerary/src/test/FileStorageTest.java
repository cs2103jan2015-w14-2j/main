package test;

import static org.junit.Assert.*;
import itinerary.main.*;
import itinerary.storage.FileStorage;
import itinerary.storage.StorageException;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.Test;

import com.google.gson.Gson;

//@author A0121409R
public class FileStorageTest {

    static final String NEWLINE_SEPARATOR = System.lineSeparator();
    
    static Gson gson = new Gson();

    static Calendar calendar = Calendar.getInstance();

    static File testFile = new File("testFile.txt");
    static FileStorage fileStorage = null;

    static Task task1;
    static Task task2;
    static Task task3;
    static Task task4;

    static Task sortedTask1;
    static Task sortedTask2;
    static Task sortedTask3;

    static Task invalidTask1;
    static Task invalidTask2;

    static String task1String;
    static String task2String;
    static String task3String;
    static String task4String;

    static String sortedTask1String;
    static String sortedTask2String;
    static String sortedTask3String;

    static List<Task> taskList;
    static List<Task> sortedList;

    @Rule
    public ExpectedFailure rule = new ExpectedFailure();

    @Before
    public void setUp() {

        task1 = new Task(1, "T", "ExampleCategory", true, true);
        task2 =
                new ScheduleTask(2, "S", "ExampleCategory", true, true,
                                 calendar, calendar);
        task3 =
                new DeadlineTask(3, "D", "ExampleCategory", true, true,
                                 calendar);
        task4 = new Task(1, "T", "NewCategory", true, true);

        invalidTask1 = null;
        invalidTask2 = new Task(-1000, "T", "ExampleCategory", true, true);

        task1String = gson.toJson(task1);
        task2String = gson.toJson(task2);
        task3String = gson.toJson(task3);
        task4String = gson.toJson(task4);

        taskList = new ArrayList<Task>() {

            private static final long serialVersionUID = 1L;

            {
                add(task1);
                add(task2);
                add(task3);
            }
        };

        sortedTask1 = task1.clone();
        sortedTask1.setTaskId(3);
        sortedTask2 = task2.clone();
        sortedTask3 = task3.clone();
        sortedTask3.setTaskId(1);

        sortedTask1String = gson.toJson(sortedTask1);
        sortedTask2String = gson.toJson(sortedTask2);
        sortedTask3String = gson.toJson(sortedTask3);

        sortedList = new ArrayList<Task>() {

            private static final long serialVersionUID = 1L;

            {
                add(sortedTask3);
                add(sortedTask2);
                add(sortedTask1);
            }
        };

        testFile.delete();
        fileStorage = new FileStorage(testFile);
    }
    
    /**
     * Tests if FileStorage is able to correctly assert that given Tasks
     * should not be null. They should not be null so as to prevent 
     * data integrity issues. If the Task given is null, an Assertion 
     * Failure should be activated.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @AssertionFailure
    @Test
    public void testAddTaskNull() throws StorageException {

        fileStorage.addTask(invalidTask1);
    }

    /**
     * Tests if FileStorage is able to correctly add Tasks into a List.
     * Accounts for Auto-Sorting behavior of the class.
     * If this fails, it's likely that the sorting order has been changed.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testAddTask() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        assertEquals(sortedTask3String + NEWLINE_SEPARATOR + sortedTask2String
                             + NEWLINE_SEPARATOR + sortedTask1String
                             + NEWLINE_SEPARATOR,
                     fileStorage.currentListTaskString(false));
    }

    /**
     * Tests if FileStorage is able to assert that given Tasks should not be
     * null. They should not be null so as to prevent data integrity issues.
     * If the Task given is null, an Assertion Failure should be activated.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @AssertionFailure
    @Test
    public void testEditTaskNull() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(invalidTask1);
    }

    /**
     * Tests if FileStorage is able to handle an incorrectly formatted given Task 
     * for editing. If this fails, it's likely that the error-handling mechanism 
     * within editTask() has been changed.
     * 
     * @throws StorageException To indicate an incorrectly formatted Task was given.
     */
    @Test(expected = StorageException.class)
    public void testEditTaskInvalid() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(invalidTask2);

    }

    /**
     * Tests if FileStorage is able to correctly replace the Task in the List it has 
     * that represents the state of the system. If this fails, it's likely that the 
     * error-handling mechanism within editTask() has been changed.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testEditTask() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(task4);

        assertNotEquals(task4String + "\n",
                        fileStorage.currentListTaskString(false));
    }

    /**
     * Tests if FileStorage is able to assert that a given Task should not be 
     * null. They should not be null so as to prevent data integrity issues.
     * If the Task given is null, an Assertion Failure should be activated.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @AssertionFailure
    @Test
    public void testDeleteTaskNull() throws StorageException {

        fileStorage.deleteTask(invalidTask1);
    }

    /**
     * Tests if FileStorage is able to handle an incorrectly formatted Task for 
     * deletion. This particular tests for the deletion of a non-existent Task 
     * within the List. If this fails, it's likely that the error-handling mechanism 
     * within deleteTask() was changed.
     * 
     * @throws StorageException To indicate that a invalid Task was given.
     */
    @Test(expected = StorageException.class)
    public void testDeleteTaskInvalid() throws StorageException {

        fileStorage.deleteTask(invalidTask2);
    }

    /**
     * Tests if FileStorage can correctly delete the given Task in the List
     * that it has representing the current state of the system. If this fails, 
     * it's likely that the behavior of deleteTask() has been changed.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testDeleteTask() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(task4);
        fileStorage.deleteTask(task4);

        assertEquals("", fileStorage.currentListTaskString(false));
    }

    /**
     * Tests if FileStorage is able to correctly return a copy of the List it has
     * representing the state of the system. If this fails, it's likely due to a 
     * change in the clone() or the equals() methods in the Task objects.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testGetAllTasks() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        List<Task> currentListTask = fileStorage.getAllTasks();

        for (Task item : currentListTask) {

            assertTrue(sortedList.contains(item));
        }
    }

    /**
     * Tests if FileStorage can correctly clear out the system state. This shoudln't fail,
     * unless the behavior of clearAll() was changed.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testClearAll() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        fileStorage.clearAll();

        assertEquals("", fileStorage.currentListTaskString(false));
    }

    /**
     * Tests if FileStorage can correctly assert that the List given to refill the state of the 
     * system with should not be null.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @AssertionFailure
    @Test
    public void testRefillAllNull() throws StorageException {

        fileStorage.refillAll(null);
    }

    /**
     * Tests if FileStorage can correctly restore the state of the system with a given List.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testRefillAll() throws StorageException {

        fileStorage.refillAll(taskList);

        assertEquals(sortedTask3String + NEWLINE_SEPARATOR + sortedTask2String
                             + NEWLINE_SEPARATOR + sortedTask1String
                             + NEWLINE_SEPARATOR,
                     fileStorage.currentListTaskString(false));
    }

    /**
     * Tests if FileStorage can correctly return a deep copy of the current state of the 
     * system. If this fails, it's likely due to a 
     * change in the clone() or the equals() methods in the Task objects.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testDuplicateCurrentListTask() throws StorageException {

        List<Task> listTask = new ArrayList<Task>();

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        listTask = fileStorage.duplicateCurrentListTask(true);

        for (Task item : listTask) {

            assertTrue(sortedList.contains(item));
        }

    }

    /**
     * Tests if FileStorage can return a List containing all currently active category
     * tags for all the Tasks in the system state.
     * 
     * @throws StorageException For any abnormal behavior that arises.
     * Check for Exceptions thrown in JsonIOHandler or JsonStringTagger.
     */
    @Test
    public void testGetAllCategories() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        fileStorage.editTask(task4);

        assertEquals(2, fileStorage.getAllCategories().size());

    }

    @After
    public void close() {
        fileStorage.close();
    }

}
