package itinerary.main;

import com.google.gson.Gson;

// @author A0121409R
public class JsonStringTagger {

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
     * taskID of the Task object, the type of Task object and it's JSON String
     * representation.
     * 
     * @param task
     *            The Task object to be converted into a String object.
     * @return The String version of the Task Object.
     */
    public static String convertTasktoTaggedJsonString(Task task)
                                                                 throws NullPointerException {

        if (task == null) {

            throw new NullPointerException();
        }

        Gson gson = new Gson();
        String s =
                   task.getLineNumber() + JsonStringTagger.STRING_DELIMITER
                           + task.getClass().getSimpleName()
                           + JsonStringTagger.STRING_DELIMITER
                           + gson.toJson(task);

        return s;
    }

    /**
     * Given a tagged JSON String object made from the
     * convertTasktoTaggedJsonString() method it will convert it to the
     * corresponding Task object.
     * 
     * @param taggedJSONString
     *            A String object that has an additional tag in front of the
     *            JSON String stating what type of Task the JSON String part
     *            represents.
     * @return The corresponding Task object that formattedJSONString
     *         represents. If taggedJSONString is in the wrong format, method
     *         will return null.
     */
    public static Task convertTaggedJsonStringtoTask(String taggedJSONString) {

        Gson gson = new Gson();

        String stringArray[] =
                               taggedJSONString.split(JsonStringTagger.STRING_DELIMITER);

        if (checkStringArray(stringArray)) {

            if (stringArray[1].equals(ScheduleTask.class.getSimpleName())) {

                return gson.fromJson(stringArray[2], ScheduleTask.class);

            } else if (stringArray[1].equals(DeadlineTask.class.getSimpleName())) {

                return gson.fromJson(stringArray[2], DeadlineTask.class);

            } else {

                // Default Task Object
                return gson.fromJson(stringArray[2], Task.class);

            }
        }

        return null;
    }

}
