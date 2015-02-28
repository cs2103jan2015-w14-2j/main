package itinerary.test;
import itinerary.main.*;
import static org.junit.Assert.*;
import itinerary.main.JsonConverter;
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
	public List<String> jsonList;
	@Before
	public void init(){
		
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		task = new ScheduleTask(1, "testtest", "testcat",
				 true,
				true,fromDate, toDate);
		task2 = new ScheduleTask(2, "test", "testcat",
				 true,
				true,fromDate, toDate);
		gson = new Gson();
		jsonList = new ArrayList<String>();
		jsonList.add(gson.toJson(task));
		jsonList.add(gson.toJson(task2));
	}

	@Test
	public void testQuery() {
		List<String> testList = new ArrayList<String>();
		Search search = new Search(jsonList);
		try {
	        testList = search.query("testtest","text");
        } catch (IOException e) {
	        e.printStackTrace();
        } catch (ParseException e) {
	        e.printStackTrace();
        }
		assertEquals("test query",gson.toJson(task),testList.get(0));
	}

}
