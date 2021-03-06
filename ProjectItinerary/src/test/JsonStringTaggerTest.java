package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import itinerary.main.DeadlineTask;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;
import itinerary.storage.JsonStringTagger;

import java.util.Calendar;

import org.junit.Rule;
import org.junit.Test;

import com.google.gson.Gson;

//@author A0121409R
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
    
    @Rule public ExpectedFailure rule = new ExpectedFailure();
    
    /**
     * Tests for avoiding execution on null arrays.
     * Prevents system from crashing due to thrown NullPointerExceptions.
     */
    @Test
    public void testCheckStringArray() {
        assertFalse("test for handling null String arrays: ", JsonStringTagger.checkStringArray(null));
    }
    
    /**
     * Tests for the behavior of asserting for no null tasks.
     * Prevents abnormal behavior or data loss when in standard operation.
     */
    @AssertionFailure
    @Test
    public void testConvertTasktoTaggedJsonStringNull() {
        JsonStringTagger.convertTasktoTaggedJsonString(null);
    }

    /**
     * Tests the ability of the class to convert the different types of Tasks to the intended String format.
     * If this fails, this means the format of the String was changed. The format is supposed to be:
     * "TaskID""SPLIT""Type of Task""SPLIT""Gson representation of the Task object""Newline character"
     * where "Type of Task" ::=  Task | ScheduleTask | DeadlineTask
     */
    @Test
    public void testConvertTasktoTaggedJsonString() {
        assertEquals("Task to Tag: ", "1" + delimiter + task1Name + delimiter + task1String, taggedTask1);
        assertEquals("ScheduleTask to Tag: ", "2" + delimiter + task2Name + delimiter + task2String, taggedTask2);
        assertEquals("DeadlineTask to Tag: ", "3" + delimiter + task3Name + delimiter + task3String, taggedTask3);
    }
    
    /**
     * Tests the ability for the class to convert back to the different types of Tasks given their 
     * intended String forms.
     * If this fails, this means the format of the String was changed. The format is supposed to be:
     * "TaskID""SPLIT""Type of Task""SPLIT""Gson representation of the Task object""Newline character"
     * where "Type of Task" ::=  Task | ScheduleTask | DeadlineTask
     */
    @Test
    public void testConvertTaggedJsonStringtoTask() {
        assertEquals(task1, JsonStringTagger.convertTaggedJsonStringtoTask(taggedTask1));
        assertEquals(task2, JsonStringTagger.convertTaggedJsonStringtoTask(taggedTask2));
        assertEquals(task3, JsonStringTagger.convertTaggedJsonStringtoTask(taggedTask3));
    }

}
