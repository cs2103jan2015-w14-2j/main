package itinerary.test;
import itinerary.main.*;
import static org.junit.Assert.*;
import itinerary.main.ScheduleTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class SearchTest {
	public Calendar fromDate;
	public Calendar toDate;
	public ScheduleTask task;
	public ScheduleTask task2;
	public Gson gson;
	public List<Task> jsonList;
	public String classType;
	@Before
	public void init(){
		
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		task = new ScheduleTask(1, "testtest gjhjy", "testcat",
				 true,
				true,fromDate, toDate);
		task2 = new ScheduleTask(2, "test i want to eat pie", "testcat",
				 true,
				true,fromDate, toDate);
		gson = new Gson();
		jsonList = new ArrayList<Task>();
		jsonList.add(task);
		jsonList.add(task2);
		jsonList.add(new ScheduleTask(3, "fest", "testcat",
				 true,
				true,fromDate, toDate));
		jsonList.add(new ScheduleTask(4, "hello everybody", "testcat",
				 true,
				true,fromDate, toDate));

	}

	@Test
	public void testQuery() {
		List<Task> testList = new ArrayList<Task>();
		Search search = new Search(jsonList);
		try {
	        testList = search.query("wan","text");
        } catch (IOException e) {
	        e.printStackTrace();
        } catch (ParseException e) {
	        e.printStackTrace();
        }
		assertEquals("test query",gson.toJson(task),gson.toJson(testList.get(0)));
	}
	
}
