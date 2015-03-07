package itinerary.main;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

// @author A0121810Y
public class JsonConverter {

    public static <T extends Task> List<String> convertTaskList(List<T> taskList) {
        List<String> json = new ArrayList<String>();
        Gson gson = new Gson();
        for (T task : taskList) {
            json.add(gson.toJson(task));
        }
        return json;
    }

    public static <T extends Task> List<String> getTypeList(List<T> taskList) {
        List<String> list = new ArrayList<String>();
        for (T task : taskList) {
            list.add(task.getClass().toString());
        }
        return list;
    }

    public static <T extends Task> List<T> convertJsonList(List<String> jsonList,
                                                           List<String> typeList) {
        List<T> taskList = new ArrayList<T>();
        Gson gson = new Gson();

        for (int i = 0; i < typeList.size(); i++) {
            String[] taskString = typeList.get(i).replace(".", " ").split(" ");
            String task = taskString[3];
            if (task.equals("Task")) {
                taskList.add((T) gson.fromJson(jsonList.get(i), Task.class));
            }
            if (task.equals("ScheduleTask")) {
                taskList.add((T) gson.fromJson(jsonList.get(i),
                                               ScheduleTask.class));

            }
            if (task.equals("DeadlineTask")) {
                taskList.add((T) gson.fromJson(jsonList.get(i),
                                               DeadlineTask.class));
            }

        }
        return taskList;
    }
}
