package itinerary.test;

import static org.junit.Assert.*;
import itinerary.main.JsonStringTagger;
import itinerary.main.Task;
import itinerary.main.ScheduleTask;
import itinerary.main.DeadlineTask;
import java.util.Calendar;

import org.junit.Test;
import com.google.gson.*;

//@author A0121409
public class JsonStringTaggerTest {
    
    static Gson gson = new Gson();
    
    static Calendar calendar = Calendar.getInstance();
    
    static String delimiter = JsonStringTagger.STRING_DELIMITER;
    
    static Task task1 = new Task(1, "T", "ExampleCategory", true, true);
    static Task task2 = new ScheduleTask(2, "S", "ExampleCategory", true, true, calendar, calendar);
    static Task task3 = new DeadlineTask(3, "D", "ExampleCategory", true, true, calendar);
    
    static String taggedTask1 = JsonStringTagger.convertTasktoTaggedJsonString(task1);
    static String taggedTask2 = JsonStringTagger.convertTasktoTaggedJsonString(task2);
    static String taggedTask3 = JsonStringTagger.convertTasktoTaggedJsonString(task3);
    
    static String task1String = gson.toJson(task1);
    static String task2String = gson.toJson(task2);
    static String task3String = gson.toJson(task3);
    
    static String task1Name = task1.getClass().getSimpleName();
    static String task2Name = task2.getClass().getSimpleName();
    static String task3Name = task3.getClass().getSimpleName();
    
    @Test
    public void testCheckStringArray() {
        assertFalse("Test for handling null String arrays: ", JsonStringTagger.checkStringArray(null));
    }

    @Test
    public void testConvertTasktoTaggedJsonString() {
        assertEquals("Task to Tag: ", "1" + delimiter + task1Name + delimiter + task1String, taggedTask1);
        assertEquals("ScheduleTask to Tag: ", "2" + delimiter + task2Name + delimiter + task2String, taggedTask2);
        assertEquals("DeadlineTask to Tag: ", "3" + delimiter + task3Name + delimiter + task3String, taggedTask3);
    }

    @Test
    public void testConvertTaggedJsonStringtoTask() {
        assertEquals(task1, JsonStringTagger.convertTaggedJsonStringtoTask(taggedTask1));
        assertEquals(task2, JsonStringTagger.convertTaggedJsonStringtoTask(taggedTask2));
        assertTrue(task3.equals(JsonStringTagger.convertTaggedJsonStringtoTask(taggedTask3)));
    }

}
