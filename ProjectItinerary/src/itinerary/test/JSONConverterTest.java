package itinerary.test;
import itinerary.main.*;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
//@author A0121810Y
public class JSONConverterTest {
	public ArrayList<Task> convertTestList;
	public ArrayList<String> convertJsonList;
	public Calendar fromDate;
	public Calendar toDate;
	public Task task;
	public Gson gson;
	@Before
	public void init(){
		
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		task = new Task(1, "testtest", "testcat",
				fromDate, toDate, true,
				true);
		gson = new Gson();
	}
	@Test
	public void testConvertTaskList() {
		convertTestList = new ArrayList<Task>();
		convertTestList.add(task);
		List<String> jsonList = new ArrayList<String>();
		jsonList = JSONConverter.convertTaskList(convertTestList);
		assertEquals("test ConvertTaskList", gson.toJson(task),jsonList.get(0));
	}

	@Test
	public void testConvertJsonList() {
		convertJsonList = new ArrayList<String>();
		convertJsonList.add(gson.toJson(task));
		List<Task> taskList = new ArrayList<Task>();
		taskList = JSONConverter.convertJsonList(convertJsonList);
		assertEquals("test ConvertJsonList", task.getClass(),taskList.get(0).getClass());
	}

}
