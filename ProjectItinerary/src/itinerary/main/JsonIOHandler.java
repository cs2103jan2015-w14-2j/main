package itinerary.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

// @author A0121409R
public class JsonIOHandler {

    /**
     * Given a Task object, this converts it into a String object.
     * 
     * @param task
     *            The Task object to be converted into a String object.
     * @return The String version of the Task Object.
     */
    public static String stringFormatter(Task task) {

        Gson gson = new Gson();
        String s = gson.toJson(task);

        return s;
    }

    /**
     * Writes/Appends a given Task object into the given File object in a JSON
     * format.
     * 
     * @param currFile
     *            The File object to write to.
     * @param task
     *            The Task object to write into currFile.
     * @param willOverwrite
     *            Configures if the function will overwrite and delete any
     *            content in currFile when writing the task.
     */
    public static void writeJSON(File currFile, Task task, boolean willOverwrite) {

        String taskString = stringFormatter(task);

        try {

            FileOutputStream writer =
                                      new FileOutputStream(currFile,
                                                           !willOverwrite);
            writer.write(taskString.getBytes());
            writer.write(System.getProperty("line.separator").getBytes());
            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     * Given a list of tasks, this function will write all Task objects in the
     * given List<Task> object to the given File Object. The behavior of the
     * function is to always overwrite whatever contents there is in the file.
     * 
     * @param currFile
     *            The File object to write to.
     * @param taskList
     *            The List object containing all the Task objects that will be
     *            written onto currFile.
     */
    public static void writeJSONList(File currFile, List<Task> taskList) {

        boolean firstLineOverwrite = true;

        if (taskList.isEmpty()) {

            return;

        } else {

            for (int i = 0; i < taskList.size(); i++) {

                writeJSON(currFile, taskList.get(i), firstLineOverwrite);
                firstLineOverwrite = false;
            }

        }

    }

    /**
     * Reads all the lines in a given File object and returns a String object
     * containing all the JSONs, each separated by a newline character. This
     * String object can then be printed.
     * 
     * @param currFile
     *            The File object to read from.
     * @return The String object which contains all file contents.
     */
    public static String readJSON(File currFile) {

        StringBuilder sb = new StringBuilder();
        String line = "";

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {

                sb.append(line);
                sb.append("\n");
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 
     * Reads all the lines in a given File object and returns a List<String>
     * object containing all the JSONs in the given file.
     * 
     * @param currFile
     * @return A List<String> object containing all separated JSON strings in
     *         the given file.
     */
    public static List<String> readJSONList(File currFile) {

        StringBuilder sb = new StringBuilder();
        String line = "";
        List<String> jsonList = new ArrayList<String>();

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {

                sb.append(line);
                jsonList.add(sb.toString());
                sb.setLength(0);  // To clear buffer
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return jsonList;
    }
}
