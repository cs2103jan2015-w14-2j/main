package itinerary.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import itinerary.main.History;
import itinerary.main.Task;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HistoryTest {

	History history;
	
	private static final List<Task> TEST_LIST_1 = new ArrayList<Task>();
	private static final List<Task> TEST_LIST_2 = new ArrayList<Task>();
	private static final List<Task> TEST_LIST_3 = new ArrayList<Task>();
	private static final Task TEST_TASK_1 = new Task(1, "TASK 1", null, false, false);
	private static final Task TEST_TASK_2 = new Task(2, "TASK 2", null, false, false);
	
	// Add -ea to VM arguments in Run Configurations for HistoryTest
	// This enables assertions
	@Test(expected = AssertionError.class)
	public void testNullConstructor() {
		new History(null);
	}
	
	@BeforeClass
	public static void setupLists () {
		TEST_LIST_2.add(TEST_TASK_1);
		
		TEST_LIST_3.add(TEST_TASK_1);
		TEST_LIST_3.add(TEST_TASK_2);
	}

	@Before
	public void setupHistory () {
		history = new History(TEST_LIST_1);
	}
	
	@Test
	public void testUndoNothing () {
		assertNull(history.goBack());
	}
	
	@Test
	public void testRedoNothing () {
		assertNull(history.goForward());
	}
	
	@Test
	public void testAddSecondState () {
		history.addNewState(TEST_LIST_2);
		assertEquals(TEST_LIST_2, history.getCurrentState());
		assertNull(history.goForward());
		assertNotNull(history.goBack());
		assertNull(history.goBack());
	}
	
	@Test
	public void testAddThirdState () {
		history.addNewState(TEST_LIST_2);
		history.addNewState(TEST_LIST_3);
		assertEquals(TEST_LIST_3, history.getCurrentState());
		assertNull(history.goForward());
		assertNotNull(history.goBack());
		assertNotNull(history.goBack());
		assertNull(history.goBack());
		assertNotNull(history.goForward());
		assertNotNull(history.goForward());
		assertNull(history.goForward());
	}
}
