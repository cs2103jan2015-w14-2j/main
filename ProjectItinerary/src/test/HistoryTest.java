package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import itinerary.history.History;
import itinerary.history.HistoryException;
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
	
	/* This is a boundary case for when there is nothing to undo */
	@Test (expected = HistoryException.class)
	public void testUndoNothing () throws HistoryException {
		history.undo();
	}
	
	/* This is a boundary case for when there is nothing to redo */
	@Test (expected = HistoryException.class)
	public void testRedoNothing () throws HistoryException {
		history.redo();
	}
	
	@Test (expected = HistoryException.class)
	public void testRedoNothingAfterAdd () throws HistoryException {
		history.addNewState(TEST_LIST_2);
		history.redo();
	}
	
	@Test (expected = HistoryException.class)
	public void testUndoNothingAfterAdd () throws HistoryException {
		history.addNewState(TEST_LIST_2);
		// no exception thrown
		history.undo();
		// exception thrown
		history.undo();
	}
	
	@Test
	public void testAddSecondState () throws HistoryException {
		history.addNewState(TEST_LIST_2);
		assertEquals(TEST_LIST_2, history.getCurrentState());
		assertNotNull(history.undo());
	}
	
	@Test
	public void testAddThirdState () throws HistoryException {
		history.addNewState(TEST_LIST_2);
		history.addNewState(TEST_LIST_3);
		assertEquals(TEST_LIST_3, history.getCurrentState());
		assertNotNull(history.undo());
		assertNotNull(history.undo());
		assertNotNull(history.redo());
		assertNotNull(history.redo());
	}
}
