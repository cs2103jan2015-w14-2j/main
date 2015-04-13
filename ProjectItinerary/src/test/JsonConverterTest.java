package test;

import static org.junit.Assert.assertEquals;
import itinerary.main.JsonConverter;
import itinerary.main.ScheduleTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

//@author A0121810Y
public class JsonConverterTest {
	public ArrayList<ScheduleTask> convertTestList;
	public ArrayList<String> convertJsonList;
	public Calendar fromDate;
	public Calendar toDate;
	public ScheduleTask task;
	public Gson gson;

	@Before
	public void init() {

		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		task = new ScheduleTask(1, "testtest", "testcat", true, true, fromDate,
		        toDate);
		gson = new Gson();
	}

	@Test
	public void testConvertTaskList() {
		convertTestList = new ArrayList<ScheduleTask>();
		convertTestList.add(task);
		List<String> jsonList = new ArrayList<String>();
		jsonList = JsonConverter.convertTaskList(convertTestList);
		assertEquals("test ConvertTaskList", gson.toJson(task), jsonList.get(0));
	}

	@Test
	public void testConvertJsonList() {
		convertJsonList = new ArrayList<String>();
		convertJsonList.add(gson.toJson(task));
		ArrayList<String> convertClassList = new ArrayList<String>();
		List<ScheduleTask> taskList = new ArrayList<ScheduleTask>();
		convertClassList.add("class main.itinerary.ScheduleTask");
		taskList = JsonConverter.convertJsonList(convertJsonList,
		        convertClassList);
		assertEquals("test ConvertJsonList", task.getClass(), taskList.get(0)
		        .getClass());
	}

}
