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
public class JsonWriter {

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
     * Writes a given Task object into the given File object in a JSON format.
     * 
     * @param currFile
     *            The File to write to.
     * @param task
     *            The Task to write into currFile.
     */
    public static void writeJSON(File currFile, Task task) {

        String taskString = stringFormatter(task);

        try {

            FileOutputStream writer = new FileOutputStream(currFile, false);
            writer.write(taskString.getBytes());
            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     * Reads all the lines in a given File object and returns a String object
     * containing all the JSONs.
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
