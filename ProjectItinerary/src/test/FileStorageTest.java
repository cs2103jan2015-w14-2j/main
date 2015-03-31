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

    @AssertionFailure
    @Test
    public void testAddTaskNull() throws StorageException {

        fileStorage.addTask(invalidTask1);
    }

    @Test
    public void testAddTask() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        assertEquals(sortedTask3String + "\n" + sortedTask2String + "\n"
                             + sortedTask1String + "\n",
                     fileStorage.currentListTaskString(false));
    }

    @AssertionFailure
    @Test
    public void testEditTaskNull() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(invalidTask1);
    }

    @Test(expected = StorageException.class)
    public void testEditTaskInvalid() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(invalidTask2);

    }

    @Test
    public void testEditTask() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(task4);

        assertNotEquals(task4String + "\n",
                        fileStorage.currentListTaskString(false));
    }

    @AssertionFailure
    @Test
    public void testDeleteTaskNull() throws StorageException {

        fileStorage.deleteTask(invalidTask1);
    }

    @Test(expected = StorageException.class)
    public void testDeleteTaskInvalid() throws StorageException {

        fileStorage.deleteTask(invalidTask2);
    }

    @Test
    public void testDeleteTask() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.editTask(task4);
        fileStorage.deleteTask(task4);

        assertEquals("", fileStorage.currentListTaskString(false));
    }

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

    @Test
    public void testClearAll() throws StorageException {

        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);

        fileStorage.clearAll();

        assertEquals("", fileStorage.currentListTaskString(false));
    }

    @AssertionFailure
    @Test
    public void testRefillAllNull() throws StorageException {

        fileStorage.refillAll(null);
    }

    @Test
    public void testRefillAll() throws StorageException {

        fileStorage.refillAll(taskList);

        assertEquals(sortedTask3String + "\n" + sortedTask2String + "\n"
                             + sortedTask1String + "\n",
                     fileStorage.currentListTaskString(false));
    }

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
