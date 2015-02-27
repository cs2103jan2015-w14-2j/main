package itinerary.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
     * Appends a given Task object into the given File object in a JSON format.
     * 
     * @param willOverwrite
     *            Configures if the function will overwrite and delete any old
     *            files.
     * @param currFile
     *            The File to write to.
     * @param task
     *            The Task to write into currFile.
     */
    public static void writeJSON(File currFile, Task task, boolean willOverwrite) {

        String taskString = stringFormatter(task);

        try {

            FileOutputStream writer = new FileOutputStream(currFile, !willOverwrite);
            writer.write(taskString.getBytes());
            writer.write(System.getProperty("line.separator").getBytes());
            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     * Reads all the lines in a given File object and returns a String object
     * containing all the JSONs, each separated by a newline operator.
     * 
     * @param currFile
     *            The File to read from.
     * @return The String which contains all file contents.
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
}
