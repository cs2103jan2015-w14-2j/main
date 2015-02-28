package itinerary.main;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
//@author A0121810Y
public class JsonConverter {
	
	public static <T extends Task> List<String> convertTaskList(List<T> taskList){
		List<String> json = new ArrayList<String>();
		Gson gson = new Gson();
		for ( T task : taskList ){
			json.add(gson.toJson(task));
		}
		return json;
	}
	public static <T extends Task> List<T> convertJsonList(List<String> jsonList ,String classType){
		List<T> taskList = new ArrayList<T>();
		Gson gson = new Gson();
		for (String json : jsonList ){
			if(classType.equals("task")){
				taskList.add((T)gson.fromJson(json,Task.class));
			}
			if(classType.equals("scheduletask")){
				taskList.add((T)gson.fromJson(json,ScheduleTask.class));
			}
			if(classType.equals("deadlinetask")){
				taskList.add((T)gson.fromJson(json,DeadlineTask.class));
			}

		}
		return taskList;
	}
	
	

}
