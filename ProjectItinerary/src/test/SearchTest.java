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
	public ScheduleTask scheduleTask;
	public ScheduleTask basicTask;
	public ScheduleTask advancedTask;
	public DeadlineTask priorityTask;
	public ScheduleTask completeTask;
	public DeadlineTask categoryTask;
	public DeadlineTask deadlineTask;
	public List<Task> testList;
	public Gson gson;
	public List<Task> jsonList;
	public String classType;
	Calendar testToDate2;
	Calendar testFromDate2;
	/**
	 * Initialization, populates a list with several tasks to be searched for later on,
	 * each task corresponds to a specific field.
	 */
	@Before
	public void init(){
		gson = new Gson();
		jsonList = new ArrayList<Task>();
		
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		testFromDate = Calendar.getInstance();
		testToDate = Calendar.getInstance();
		testDeadlineDate = Calendar.getInstance();
		testFromDate.set(2016,3,20,0,0);
		testToDate.set(2016,3,25,23,59);
		testDeadlineDate.set(2016,3,24,23,59);
		testFromDate2 = Calendar.getInstance();
		testFromDate2.set(2016,2,28,21,59);
		testToDate2 = Calendar.getInstance();
		testToDate2.set(2016,4,24,23,59);
		advancedTask = new ScheduleTask(2, "Basic Task", "Advanced Category",
				 false,
				false,Calendar.getInstance(), Calendar.getInstance());
		deadlineTask = new DeadlineTask(0, "Test deadline", "Test Category",
				 false,
				false,testDeadlineDate);
		scheduleTask = new ScheduleTask(1, "Test ScheduleTask1", "Test Category",
				 false,
				false,testFromDate, testToDate);
		basicTask = new ScheduleTask(2, "Basic Task", "Test Category",
				 false,
				false,Calendar.getInstance(), Calendar.getInstance());
		categoryTask = new DeadlineTask(5, "Add Deadline Task", "Find This Category",
				 false,
				false,Calendar.getInstance());
		priorityTask = new DeadlineTask(6, "Add Priority Task 1", "Test Category",
				 true,
				false,Calendar.getInstance());
		completeTask =new ScheduleTask(7, "Add Completed Task 2", "Test Category",
				 false,
				true,Calendar.getInstance(), Calendar.getInstance());
		jsonList.add(new ScheduleTask(3, "Add Schedule Task 3", "Test Category",
				 false,
				false,Calendar.getInstance(), Calendar.getInstance()));
		jsonList.add(new ScheduleTask(4, "Add Schedule Task 4", "Test Category",
				 false,
				false,Calendar.getInstance(), Calendar.getInstance()));
		jsonList.add(scheduleTask);
		jsonList.add(basicTask);
		jsonList.add(deadlineTask);
		jsonList.add(categoryTask);
		jsonList.add(priorityTask);
		jsonList.add(completeTask);
		jsonList.add(advancedTask);
	
	}
	@Test
	public void testSearchMultiple() throws SearchException{
		SearchTask task = new SearchTask();
		task.setCategory("advanced category");
		task.setText("Basic Task");
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test query",gson.toJson(advancedTask),gson.toJson(testList.get(0)));

	}
	/**
	 * Tests fuzzy search, after searching, the testlist should only be of size 1
	 * and it should only contain basicTask
	 * @throws SearchException
	 */
	@Test
	public void testSearchCategory() throws SearchException{
		SearchTask task = new SearchTask();
		task.setCategory("Find This Category");
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test query",gson.toJson(categoryTask),gson.toJson(testList.get(0)));

	}
	/**
	 * Tests fuzzy search, after searching, the testlist should only be of size 1
	 * and it should only contain basicTask
	 * @throws SearchException
	 */
	@Test
	public void testFuzzySearch() throws SearchException{
		Search search = new Search(jsonList);
		testList = search.query("beaic TAFF","text");
		assertEquals("test query",gson.toJson(basicTask),gson.toJson(testList.get(0)));


	}
	/**
	 * Tests wildcard search, after searching, the testlist should only be of size 1
	 * and it should only contain basicTask
	 * @throws SearchException
	 */
	@Test
	public void testWildCardSearch() throws SearchException{
		Search search = new Search(jsonList);
		List<Task> testList = search.query("ba ta","text");
		assertEquals("test query",gson.toJson(basicTask),gson.toJson(testList.get(0)));


	}
	/**
	 * Tests search by isComplete, after searching, the testlist should only be of size 1
	 * and it should only contain completeTask
	 * @throws SearchException
	 */
	@Test
	public void testisCompleteSearch() throws SearchException{
		SearchTask task = new SearchTask();
		task.setComplete(true);
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test query",gson.toJson(completeTask),gson.toJson(testList.get(0)));

	}
	/**
	 * Tests search by priority, after searching, the testlist should only be of size 1
	 * and it should only contain priorityTask
	 * @throws SearchException
	 */
	@Test
	public void testPrioritySearch() throws SearchException{
		SearchTask task = new SearchTask();
		task.setPriority(true);
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test query",gson.toJson(priorityTask),gson.toJson(testList.get(0)));

	}
	/**
	 * Should return deadlineTask as well as scheduleTask.
	 * They must be in the same order because otherwise the conversion
	 * from json back to Task will fail.
	 * @throws SearchException
	 */
	@Test
	public void testSearchDate() throws SearchException{
		SearchTask task = new SearchTask();
		task.setToDate(testToDate2);
		task.setFromDate(testFromDate2);
		Search search = new Search(jsonList);
		List<Task> testList = search.query(task);
		assertEquals("test deadlineQuery",gson.toJson(deadlineTask),gson.toJson(testList.get(1)));
		assertEquals("test scheduleQuery",gson.toJson(scheduleTask),gson.toJson(testList.get(0)));

	}

}
