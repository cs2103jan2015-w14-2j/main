package test;
import itinerary.main.*;
import static org.junit.Assert.*;
import itinerary.main.ScheduleTask;
import itinerary.search.Search;
import itinerary.search.SearchException;
import itinerary.search.SearchTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;








import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class SearchTest {
	public Calendar fromDate;
	public Calendar toDate;
	public Calendar testFromDate;
	public Calendar testToDate;
	public Calendar testDeadlineDate;
	public ScheduleTask task;
	public ScheduleTask task2;
	public DeadlineTask task3;
	public ScheduleTask task4;
	public Gson gson;
	public List<Task> jsonList;
	public String classType;
	
	@Before
	public void init(){
		
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		testFromDate = Calendar.getInstance();
		testToDate = Calendar.getInstance();
		testDeadlineDate = Calendar.getInstance();
		testFromDate.set(2015,3,20);
		testToDate.set(2015,3,25);
		testDeadlineDate.set(2015,3,24);
		task = new ScheduleTask(1, "testtest gjhjy", "testcat",
				 true,
				true,fromDate, toDate);
		task2 = new ScheduleTask(2, "test i want to eat pies", "testcat",
				 true,
				true,fromDate, toDate);
		gson = new Gson();
		jsonList = new ArrayList<Task>();
		jsonList.add(task);
		jsonList.add(task2);
		task3 = new DeadlineTask(6, " true deadline hello everybody", "testcat",
				 true,
				true,testDeadlineDate);
		task4 =new ScheduleTask(7, "true schedule hello everybody!", "testcat",
				 false,
				false,testFromDate, testToDate);
		jsonList.add(new ScheduleTask(3, "fest", "testcat",
				 true,
				true,fromDate, toDate));
		jsonList.add(new ScheduleTask(4, "hello everybody!", "testcat",
				 false,
				false,fromDate, toDate));
		jsonList.add(new DeadlineTask(5, "hello everybody", "testcat",
				 true,
				true,fromDate));
		jsonList.add(task3);
		jsonList.add(task4);
	
	}
	@Test
	//tries to query the list with the following search terms category:testcat text:wan pies
	public void testSearch() throws SearchException{
		SearchTask task = new SearchTask();
		String[] field = {"text","category","isPriority"};
		String[] catArray = {"testcat"};
		List<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList(field));
		List<String> catList = new ArrayList<String>(Arrays.asList(catArray));
		task.setSearchField(fields);
		task.setCategoryList(catList);
		task.setText("wan pis");
		task.setPriority(true);
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test query",gson.toJson(task2),gson.toJson(testList.get(0)));

	}
	@Test
	public void testSearchWithDeadline() throws SearchException{
		SearchTask task = new SearchTask();
		String[] field = {"deadline"};
		String[] catArray = {"testcat"};
		List<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList(field));
		List<String> catList = new ArrayList<String>(Arrays.asList(catArray));
		task.setSearchField(fields);
		task.setCategoryList(catList);
		task.setText("Hello Everybody");
		task.setPriority(true);
		task.setDeadline(testDeadlineDate);
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test deadlineQuery",gson.toJson(task3),gson.toJson(testList.get(0)));

	}
	@Test
	public void testSearchWithSchedule() throws SearchException{
		SearchTask task = new SearchTask();
		String[] field = {"Date"};
		String[] catArray = {"testcat"};
		List<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList(field));
		List<String> catList = new ArrayList<String>(Arrays.asList(catArray));
		task.setSearchField(fields);
		task.setCategoryList(catList);
		task.setText("Hello Everybody");
		task.setPriority(true);
		task.setDeadline(testDeadlineDate);
		task.setToDate(testToDate);
		task.setFromDate(testFromDate);
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test scheduleQuery",gson.toJson(task4),gson.toJson(testList.get(1)));

	}
}
