package itinerary.main;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
//@author A0121810Y
public class JsonConverter {
	
	public static List<String> convertTaskList(List<ScheduleTask> taskList){
		List<String> json = new ArrayList<String>();
		Gson gson = new Gson();
		for (Task task : taskList ){
			json.add(gson.toJson(task));
		}
		return json;
	}
	public static List<ScheduleTask> convertJsonList(List<String> jsonList){
		List<ScheduleTask> taskList = new ArrayList<ScheduleTask>();
		Gson gson = new Gson();
		for (String json : jsonList ){
			taskList.add(gson.fromJson(json,ScheduleTask.class));
		}
		return taskList;
	}
	
	

}
