package itinerary.storage;

import itinerary.main.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//@author A0121409R
public class JsonIOHandler {
    
    static File defaultFile = new File("default");
    
    public static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Writes/Appends a given Task object into the given File object in a JSON
     * format. Inserts the taskId of each task before the JSON String.
     * 
     * @param currFile
     *            The File object to write to.
     * @param task
     *            The Task object to write into currFile.
     * @param willOverwrite
     *            Configures if the function will overwrite and delete any
     *            content in currFile when writing the task.
     */
    public static void writeJSON(File currFile, Task task, boolean willOverwrite) throws IOException {
        
        if (currFile == null) {
            writeJSON(defaultFile, task, willOverwrite);
            return;
        }
        
        FileOutputStream writer = new FileOutputStream(currFile, !willOverwrite);

        if (task != null) {
            String taskString = JsonStringTagger.convertTasktoTaggedJsonString(task);
            writer.write(taskString.getBytes());
            writer.write(NEWLINE_SEPARATOR.getBytes());
        }
        writer.close();
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
    public static void writeJSONList(File currFile, List<Task> taskList) throws IOException {
        boolean firstLineOverwrite = true;
        if (taskList == null || taskList.isEmpty()) {
            writeJSON(currFile, null, firstLineOverwrite);
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
     *            If true, it adds in the tags in each line to the final String
     *            object.
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
                String stringArray[] = line.split(JsonStringTagger.STRING_DELIMITER);
                if (toAddTags) {
                    if (JsonStringTagger.checkStringArray(stringArray)) {
                        sb.append(stringArray[0] + " ");
                        sb.append(stringArray[1] + " ");
                        sb.append(stringArray[2]);
                    }
                } else {
                    sb.append(stringArray[2]);
                }
                sb.append(NEWLINE_SEPARATOR);
            }
            bufferedReader.close();
        } catch (NullPointerException e) {
            readJSON(defaultFile, true);
        } catch (FileNotFoundException e) {
            try {
				writeJSON(currFile, null, true);
				readJSON(currFile, true);
			} catch (IOException io) {
				io.printStackTrace();
			}
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
     *            If true, it adds the tags in each line to the final String
     *            object.
     * @return A List<String> object containing all separated JSON strings in
     *         cuffFile.
     */
    public static List<String> readJSONFileListString(File currFile, boolean toAddTags) {
        StringBuilder sb = new StringBuilder();
        String line = "";
        List<String> jsonList = new ArrayList<String>();

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {
                String stringArray[] = line.split(JsonStringTagger.STRING_DELIMITER);
                if (toAddTags) {
                    if (JsonStringTagger.checkStringArray(stringArray)) {
                        sb.append(stringArray[0] + " ");
                        sb.append(stringArray[1] + " ");
                        sb.append(stringArray[2]);
                    }
                } else {
                    sb.append(stringArray[2]);
                }

                jsonList.add(sb.toString());
                sb.setLength(0);  // To clear buffer
            }
            bufferedReader.close();
        } catch (NullPointerException e) {
            readJSON(defaultFile, true);
        } catch (FileNotFoundException e) {
            try {
                writeJSON(currFile, null, true);
                readJSON(currFile, true);
            } catch (IOException io) {
                io.printStackTrace();
            }
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
    public static List<Task> readJSONFileListTask(File currFile) {
        String line = "";
        List<Task> taskList = new ArrayList<Task>();

        try {
            FileInputStream reader = new FileInputStream(currFile);
            InputStreamReader isr = new InputStreamReader(reader);
            BufferedReader bufferedReader = new BufferedReader(isr);
            
            while ((line = bufferedReader.readLine()) != null) {
                taskList.add(JsonStringTagger.convertTaggedJsonStringtoTask(line));
            }
            bufferedReader.close();
        } catch (NullPointerException e) {
            readJSON(defaultFile, true);
        } catch (FileNotFoundException e) {
            try {
                writeJSON(currFile, null, true);
                readJSON(currFile, true);
            } catch (IOException io) {
                io.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return taskList;
    }

}
