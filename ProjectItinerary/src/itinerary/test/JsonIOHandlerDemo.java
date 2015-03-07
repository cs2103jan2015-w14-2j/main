package itinerary.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import itinerary.main.*;

//@author A0121409R

/**
 * 
 * An example of how to use JsonIOHandler. Can also be considered an
 * "unofficial" test of JsonIOHandler.
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

    public static void main(String[] args) throws IOException {

        Task task1 = new Task(1, "ExampleText1", "ExampleCategory1", true, true);
        Task task2 =
                     new ScheduleTask(2, "ExampleText2", "ExampleCategory2",
                                      true, true, Calendar.getInstance(),
                                      Calendar.getInstance());
        Task task3 =
                     new DeadlineTask(3, "ExampleText3", "ExampleCategory3",
                                      true, true, Calendar.getInstance());

        List<Task> taskList = new ArrayList<Task>() {

            private static final long serialVersionUID = 1L;

            {
                add(task1);
                add(task2);
                add(task3);
            }
        };
        
        JsonIOHandler.writeJSONList(testFile, null);     
        List<Task> testList = JsonIOHandler.readJsonFileListTask(testFile);
        System.out.println(testList.size());
        System.out.println();
        
        JsonIOHandler.writeJSONList(testFile, taskList);

        System.out.println("Without additional tags: ");
        System.out.println();
        
        //Get a printable String from the file, without the additional tags.
        String test = JsonIOHandler.readJSON(testFile, false);
        System.out.println(test);

        //Get a List of printable Strings from the file, without the additional tags.
        List<String> stringList = JsonIOHandler.readJSONFileListString(testFile, false);
        printList(stringList);
        System.out.println();

        System.out.println("With additional tags: ");
        System.out.println();
        
        //Get a printable String from the file, with the additional tags.
        test = JsonIOHandler.readJSON(testFile, true);
        System.out.println(test);

        //Get a List of printable Strings from the file, with the additional tags.
        stringList = JsonIOHandler.readJSONFileListString(testFile, true);
        printList(stringList);
        System.out.println();
        
        // Get a List of Tasks from the file.
        
        testList = JsonIOHandler.readJsonFileListTask(testFile);
        
        for (Task item : testList) {
            
            System.out.println(item.getTaskId() + " " + item.getText());
        }
        

    }

}
