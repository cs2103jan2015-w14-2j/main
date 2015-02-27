package itinerary.test;

import itinerary.main.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 
 * An example of how to use JsonIOHandler. Can also be considered an
 * "unofficial" test of JsonIOHandler.
 * 
 * @author A0121409R
 *
 */
public class JsonIOHandlerDemo {

    private static File testFile = new File("mytestfile.txt");

    public static void printList(List<String> stringList) {

        if (stringList == null) {

            return;
        }

        for (int i = 0; i < stringList.size(); i++) {

            System.out.println(stringList.get(i));
        }
    }

    public static void main(String[] args) {

        Task task1 = new Task(1, "ExampleText", "ExampleCategory", true, true);
        Task task2 =
                     new ScheduleTask(2, "ExampleText", "ExampleCategory",
                                      true, true, Calendar.getInstance(),
                                      Calendar.getInstance());
        Task task3 =
                     new DeadlineTask(3, "ExampleText", "ExampleCategory",
                                      true, true, Calendar.getInstance());

        List<Task> taskList = new ArrayList<Task>() {
            
            private static final long serialVersionUID = 1L;

            {
                add(task1);
                add(task2);
                add(task3);
            }
        };

        // System.out.println(JsonIOHandler.stringFormatter(task1));
        // System.out.println(JsonIOHandler.stringFormatter(task2));

        JsonIOHandler.writeJSONList(testFile, taskList);

        String test = JsonIOHandler.readJSON(testFile);
        System.out.println(test);

        List<String> stringList = JsonIOHandler.readJSONList(testFile);
        printList(stringList);

    }

}
