package itinerary.main;
import com.google.gson.*;
public class ParserSearch {
	public static SearchTask parseString(String input)
	{
		Gson gson = new Gson();
		SearchTask task = new SearchTask();
		String[] priority = {"priority"};
		System.out.println(gson.toJson(priority));
		return task;
	}
}
