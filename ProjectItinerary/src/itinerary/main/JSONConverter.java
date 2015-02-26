package itinerary.main;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
//@author A0121810Y
public class JSONConverter {
	
	public static List<String> convertTaskList(List<Task> taskList){
		List<String> json = new ArrayList<String>();
		Gson gson = new Gson();
		for (Task task : taskList ){
			json.add(gson.toJson(task));
		}
		return json;
	}
	public static List<Task> convertJsonList(List<String> jsonList){
		List<Task> taskList = new ArrayList<Task>();
		Gson gson = new Gson();
		for (String json : jsonList ){
			taskList.add(gson.fromJson(json,Task.class));
		}
		return taskList;
	}
	
	

}
