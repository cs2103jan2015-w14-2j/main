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
    
    public static final String STRING_DELIMITER = "<SPLIT>"; 

    /**
     * Given a String array object, this method checks if the array is suitable
     * for use in the methods below.
     * 
     * @param stringArray
     *            The String array to be checked.
     * @return A boolean indicating if the array can be used in the methods
     *         below.
     */
    public static boolean checkStringArray(String stringArray[]) {

        if (stringArray == null) {

            return false;
        }

        if (stringArray.length <= 2) {

            return false;
        }

        return true;
    }

    /**
     * Given a Task object, this converts it into a String object containing the
     * lineNumber of the Task object, the type of Task object and it's JSON
     * String representation.
     * 
     * @param task
     *            The Task object to be converted into a String object.
     * @return The String version of the Task Object.
     */
    public static String stringFormatter(Task task) throws NullPointerException {

        if (task == null) {

            throw new NullPointerException();
        }

        Gson gson = new Gson();
        String s = task.getLineNumber()+ STRING_DELIMITER + task.getClass().getSimpleName() + STRING_DELIMITER + gson.toJson(task);

        return s;
    }

    /**
     * Writes/Appends a given Task object into the given File object in a JSON
     * format. Inserts the lineNumber of each task before the JSON String.
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

        try {

            FileOutputStream writer =
                                      new FileOutputStream(currFile,
                                                           !willOverwrite);

            if (task != null) {
                String taskString = stringFormatter(task);
                writer.write(taskString.getBytes());
                writer.write(System.getProperty("line.separator").getBytes());
            }

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

        if (taskList == null || taskList.isEmpty()) {

            writeJSON(currFile, null, true);

        } else {

            for (int i = 0; i < taskList.size(); i++) {

                writeJSON(currFile, taskList.get(i), firstLineOverwrite);
                firstLineOverwrite = false;
            }

        }

    }

    /**
     * Reads all the lines in a given File object and returns a String object.
     * This String object can then be printed.
     * 
     * @param currFile
     *            The File object to read from.
     * @param toAddTags
     *            If true, it adds in the tags in each line to the final String object.
     * @return The String object which contains all file contents.
     */
    public static String readJSON(File currFile, boolean toAddTags) {

        StringBuilder sb = new StringBuilder();
        String line = "";

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {

                String stringArray[] = line.split(STRING_DELIMITER);

                if (toAddTags) {

                    if (checkStringArray(stringArray)) {
                        
                        sb.append(stringArray[0] + " ");
                        sb.append(stringArray[1] + " ");
                        sb.append(stringArray[2]);
                    }

                } else {

                    sb.append(line);

                }

                sb.append("\n");
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            
            writeJSON(currFile, null, true);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * Reads all the lines in a given File object and returns a List<String>
     * object containing all the JSONs in the given file.
     * 
     * @param currFile
     *            The File object to be read from.
     * @param toAddTags
     *            If true, it adds the tags in each line to the final String object.
     * @return A List<String> object containing all separated JSON strings in
     *         cuffFile.
     */
    public static List<String> readJSONFileListString(File currFile,
                                                      boolean toAddTags) {

        StringBuilder sb = new StringBuilder();
        String line = "";
        List<String> jsonList = new ArrayList<String>();

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {

                String stringArray[] = line.split(STRING_DELIMITER);

                if (toAddTags) {

                    if (checkStringArray(stringArray)) {

                        sb.append(stringArray[0] + " ");
                        sb.append(stringArray[1] + " ");
                        sb.append(stringArray[2]);
                    }

                } else {

                    sb.append(line);

                }

                jsonList.add(sb.toString());
                sb.setLength(0);  // To clear buffer
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            
            writeJSON(currFile, null, true);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return jsonList;
    }

    /**
     * Given a file, this method will return a List object containing all the
     * Task objects in the file.
     * 
     * @param currFile
     *            The File object to be read from.
     * @return A List<Task> object containing all the items in currFile.
     */
    public static List<Task> readJsonFileListTask(File currFile) {

        String line = "";
        List<Task> taskList = new ArrayList<Task>();

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {

                taskList.add(JsonConverter.convertTaggedJsonString(line));
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            
            writeJSON(currFile, null, true);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return taskList;
    }
}
