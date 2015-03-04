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
    public static <T extends Task> List<String> getTypeList(List<T> taskList){
    	List<String> list = new ArrayList<String>();
    	for(T task : taskList){
    		list.add(task.getClass().toString());
    	}
    	return list;
    }
    public static <T extends Task> List<T> convertJsonList(List<String> jsonList,
                                                           List<String> typeList) {
        List<T> taskList = new ArrayList<T>();
        Gson gson = new Gson();

        for (int  i=0;i<typeList.size();i++) {
        	String[] taskString = typeList.get(i).replace(".", " ").split(" ");  
        	String task = taskString[3];
			if (task .equals("Task")) {
        		taskList.add((T) gson.fromJson(jsonList.get(i), Task.class));
        	}
        	if (task.equals("ScheduleTask")) {
        		taskList.add((T) gson.fromJson(jsonList.get(i), ScheduleTask.class));

        	}
        	if (task.equals("DeadlineTask")) {
        		taskList.add((T) gson.fromJson(jsonList.get(i), DeadlineTask.class));
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
