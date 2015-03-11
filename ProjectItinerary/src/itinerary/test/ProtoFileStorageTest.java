package itinerary.test;

import static org.junit.Assert.*;
import itinerary.main.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.Test;

import com.google.gson.Gson;

//@author A0121409R
public class ProtoFileStorageTest {
    
    static Gson gson = new Gson();

    static Calendar calendar = Calendar.getInstance();
    
    static File testFile = new File("testFile");
    static ProtoFileStorage fileStorage = null;

    static Task task1 = new Task(1, "T", "ExampleCategory", true, true);
    static Task task2 = new ScheduleTask(2, "S", "ExampleCategory", true, true,
                                         calendar, calendar);
    static Task task3 = new DeadlineTask(3, "D", "ExampleCategory", true, true,
                                         calendar);
    static Task task4 = new Task(1, "T", "NewCategory", true, true);
    
    static Task invalidTask1 = null;
    static Task invalidTask2 = new Task(-1000, "T", "ExampleCategory", true, true);

    static String task1String = gson.toJson(task1);
    static String task2String = gson.toJson(task2);
    static String task3String = gson.toJson(task3);
    static String task4String = gson.toJson(task4);
    
    static List<Task> taskList = new ArrayList<Task>() {

        private static final long serialVersionUID = 1L;

        {
            add(task1);
            add(task2);
            add(task3);
        }
    };
    
    @Rule public ExpectedFailure rule = new ExpectedFailure();

    @Before
    public void setUp() {
        
        testFile.delete();
        fileStorage = new ProtoFileStorage(testFile);
    }
    
    @AssertionFailure
    @Test
    public void testAddTaskNull() throws StorageException {
        
        fileStorage.addTask(invalidTask1);
    }
    
    @Test
    public void testAddTask() {
        
        try {
            fileStorage.addTask(task1);
            fileStorage.addTask(task2);
            fileStorage.addTask(task3);
            
            assertEquals(task1String + "\n" + task2String + "\n" + task3String
                         + "\n", fileStorage.currentListTaskString(false));

        } catch (StorageException e) {
            
            System.out.println(e.getMessage());
        
        } catch (Exception e) {
            
            fail("testAddTask() has failed.");
        }
    }

    @AssertionFailure
    @Test
    public void testEditTaskNull() throws StorageException {
        
        fileStorage.addTask(task1);
        fileStorage.editTask(invalidTask1);
    }
    
    @Test (expected = StorageException.class)
    public void testEditTaskInvalid() throws StorageException {
        
        fileStorage.addTask(task1);
        fileStorage.editTask(invalidTask2);
            
    }
    
    @Test
    public void testEditTask() {
        
        try {
            fileStorage.addTask(task1);
            fileStorage.editTask(task4);
            
            assertEquals(task4String + "\n", fileStorage.currentListTaskString(false));
        } catch (StorageException e) {
            
            System.out.println(e.getMessage());
        } catch (Exception e) {
            
            fail("testEditTask() has failed.");
        }
    }
    
    @AssertionFailure
    @Test
    public void testDeleteTaskNull() throws StorageException {
        
        fileStorage.deleteTask(invalidTask1);
    }
    
    @Test (expected = StorageException.class)
    public void testDeleteTaskInvalid() throws StorageException {
        
        fileStorage.deleteTask(invalidTask2);
    }
    
    @Test
    public void testDeleteTask() {
        
        try {
            fileStorage.addTask(task1);
            fileStorage.editTask(task4);
            fileStorage.deleteTask(task4);
            
            assertEquals("", fileStorage.currentListTaskString(false));
        } catch (StorageException e) {
            
            System.out.println(e.getMessage());
        } catch (Exception e) {
            
            fail("testDeleteTask() has failed.");
        }
    }

    @Test
    public void testGetAllTasks() throws StorageException {
        
        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);
        
        List<Task> currentListTask = fileStorage.getAllTasks();
        
        for (Task item : currentListTask) {
            
            assertTrue(taskList.contains(item));
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
        
        assertEquals(task1String + "\n" + task2String + "\n" + task3String
                     + "\n", fileStorage.currentListTaskString(false));
    }

    @Test
    public void testDuplicateCurrentListTask() throws StorageException {
        
        List<Task> listTask = new ArrayList<Task>();
        
        fileStorage.addTask(task1);
        fileStorage.addTask(task2);
        fileStorage.addTask(task3);
        
        listTask = fileStorage.duplicateCurrentListTask(true);
        
        for (Task item : listTask) {
            
            assertTrue(taskList.contains(item));
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

}
