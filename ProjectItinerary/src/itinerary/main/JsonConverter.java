package itinerary.main;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

//@author A0121810Y
public class JsonConverter {

    public static <T extends Task> List<String> convertTaskList(List<T> taskList) {
        List<String> json = new ArrayList<String>();
        Gson gson = new Gson();
        for (T task : taskList) {
            json.add(gson.toJson(task));
        }
        return json;
    }

    public static <T extends Task> List<T> convertJsonList(List<String> jsonList,
                                                           String classType) {
        List<T> taskList = new ArrayList<T>();
        Gson gson = new Gson();
        for (String json : jsonList) {
            if (classType.equals("task")) {
                taskList.add((T) gson.fromJson(json, Task.class));
            }
            if (classType.equals("scheduletask")) {
                taskList.add((T) gson.fromJson(json, ScheduleTask.class));
            }
            if (classType.equals("deadlinetask")) {
                taskList.add((T) gson.fromJson(json, DeadlineTask.class));
            }

        }
        return taskList;
    }

    //@author A0121409R

    /**
     * Given a tagged JSON String object made from the stringFormatter() method
     * in JsonIOHandler.java it will convert it to the corresponding Task
     * object.
     * 
     * @param taggedJSONString
     *            A String object that has an additional tag in front of the
     *            JSON String stating what type of Task the JSON String part
     *            represents.
     * @return The corresponding Task object that formattedJSONString
     *         represents. If taggedJSONString is in the wrong format, method
     *         will return null.
     */
    public static Task convertTaggedJsonString(String taggedJSONString) {

        Gson gson = new Gson();

        String stringArray[] = taggedJSONString.split(JsonIOHandler.STRING_DELIMITER);

        if (JsonIOHandler.checkStringArray(stringArray)) {

            if (stringArray[1].equals(ScheduleTask.class.getSimpleName())) {

                return gson.fromJson(stringArray[2], ScheduleTask.class);

            } else if (stringArray[1].equals(DeadlineTask.class.getSimpleName())) {

                return gson.fromJson(stringArray[2], DeadlineTask.class);

            } else {
                
                System.out.println(stringArray[1]);
                // Default Task Object
                return gson.fromJson(stringArray[2], Task.class);

            }
        }

        return null;
    }

}
