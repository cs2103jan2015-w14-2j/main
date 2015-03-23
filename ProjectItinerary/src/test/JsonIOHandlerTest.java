package test;

import static org.junit.Assert.*;
import itinerary.main.*;
import itinerary.storage.JsonIOHandler;

import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

//@author A0121409R
public class JsonIOHandlerTest {

    static Gson gson = new Gson();

    static Calendar calendar = Calendar.getInstance();
    
    static File testFile = new File("testFile");

    static Task task1 = new Task(1, "T", "ExampleCategory", true, true);
    static Task task2 = new ScheduleTask(2, "S", "ExampleCategory", true, true,
                                         calendar, calendar);
    static Task task3 = new DeadlineTask(3, "D", "ExampleCategory", true, true,
                                         calendar);

    static String task1String = gson.toJson(task1);
    static String task2String = gson.toJson(task2);
    static String task3String = gson.toJson(task3);

    static List<Task> listError = new ArrayList<Task>() {

        private static final long serialVersionUID = 1L;

        {
            add(null);
            add(null);
            add(task1);
            add(null);
            add(task2);
            add(null);
            add(task3);
        }
    };

    static List<Task> taskList = new ArrayList<Task>() {

        private static final long serialVersionUID = 1L;

        {
            add(task1);
            add(task2);
            add(task3);
        }
    };

    @Before
    public void wipe() {
        
        testFile.delete();
    }

    @Test
    public void testWriteJSON() {
        try {
            JsonIOHandler.writeJSON(null, null, true);
            wipe();
            JsonIOHandler.writeJSON(null, task1, true);
            wipe();
            JsonIOHandler.writeJSON(testFile, null, true);
            wipe();
            JsonIOHandler.writeJSON(testFile, task1, true);
        } catch (IOException e) {
            e.printStackTrace();
            fail("testWriteJSON() has failed.");
        }
    }

    @Test
    public void testWriteJSONList() {
        try {
            JsonIOHandler.writeJSONList(null, null);
            wipe();
            JsonIOHandler.writeJSONList(null, new ArrayList<Task>());
            wipe();
            JsonIOHandler.writeJSONList(testFile, null);
            wipe();
            JsonIOHandler.writeJSONList(testFile, new ArrayList<Task>());
            wipe();
            JsonIOHandler.writeJSONList(testFile, taskList);
            wipe();
            JsonIOHandler.writeJSONList(null, listError);
            wipe();
            JsonIOHandler.writeJSONList(testFile, listError);
        } catch (IOException e) {
            e.printStackTrace();
            fail("testWriteJSONList() has failed.");
        }
    }

    @Test
    public void testReadJSON() throws Exception {
        assertEquals("", JsonIOHandler.readJSON(null, false));
        wipe();
        assertEquals("", JsonIOHandler.readJSON(testFile, false));
        wipe();
        JsonIOHandler.writeJSONList(testFile, null);
        assertEquals("", JsonIOHandler.readJSON(testFile, false));
        wipe();
        JsonIOHandler.writeJSONList(testFile, listError);
        assertEquals(task1String + "\n" + task2String + "\n" + task3String
                     + "\n", JsonIOHandler.readJSON(testFile, false));
        wipe();
        JsonIOHandler.writeJSONList(testFile, taskList);
        assertEquals(task1String + "\n" + task2String + "\n" + task3String
                     + "\n", JsonIOHandler.readJSON(testFile, false));
    }

    @Test
    public void testReadJSONFileListString() throws Exception {

        List<String> testList = null;

        testList = JsonIOHandler.readJSONFileListString(null, false);
        assertEquals(0, testList.size());
        wipe();
        testList = JsonIOHandler.readJSONFileListString(testFile, false);
        assertEquals(0, testList.size());
        wipe();
        JsonIOHandler.writeJSONList(testFile, null);
        testList = JsonIOHandler.readJSONFileListString(testFile, false);
        assertEquals(0, testList.size());
        wipe();
        JsonIOHandler.writeJSONList(testFile, listError);
        testList = JsonIOHandler.readJSONFileListString(testFile, false);
        assertEquals(3, testList.size());
        wipe();
        JsonIOHandler.writeJSONList(testFile, taskList);
        testList = JsonIOHandler.readJSONFileListString(testFile, false);
        assertEquals(3, testList.size());
    }

    @Test
    public void testReadJsonFileListTask() throws IOException {
        List<Task> testList = null;

        testList = JsonIOHandler.readJSONFileListTask(null);
        assertEquals(0, testList.size());
        wipe();
        testList = JsonIOHandler.readJSONFileListTask(testFile);
        assertEquals(0, testList.size());
        wipe();
        JsonIOHandler.writeJSONList(testFile, null);
        testList = JsonIOHandler.readJSONFileListTask(testFile);
        assertEquals(0, testList.size());
        wipe();
        JsonIOHandler.writeJSONList(testFile, listError);
        testList = JsonIOHandler.readJSONFileListTask(testFile);
        assertEquals(3, testList.size());
        wipe();
        JsonIOHandler.writeJSONList(testFile, taskList);
        testList = JsonIOHandler.readJSONFileListTask(testFile);
        assertEquals(3, testList.size());
    }

}