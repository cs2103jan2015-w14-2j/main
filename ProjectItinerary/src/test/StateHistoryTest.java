package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import itinerary.history.HistoryBoundException;
import itinerary.history.HistoryBoundException.BoundType;
import itinerary.history.StateHistory;
import itinerary.main.Task;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//@author A0121437N
public class StateHistoryTest {

	StateHistory stateHistory;
	
	private static final List<Task> TEST_LIST_1 = new ArrayList<Task>();
	private static final List<Task> TEST_LIST_2 = new ArrayList<Task>();
	private static final Task TEST_TASK_1 = new Task(1, "TASK 1", null, false, false);
		
	@BeforeClass
	public static void setupLists () {
		TEST_LIST_2.add(TEST_TASK_1);
	}

	@Before
	public void setupHistory () {
		stateHistory = new StateHistory(TEST_LIST_1);
	}
	
	/**
	 * This is a boundary case for when there is nothing to undo
	 * A HistoryBoundException is expected to be thrown with
	 * BoundType LOWER_BOUND
	 */
	@Test
	public void testUndoNothing () {
		try {
			stateHistory.getPrevious();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.LOWER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown");
	}
	
	/**
	 * This is a boundary case for when there is nothing to redo
	 * A HistoryBoundException is expected to be thrown with
	 * BoundType UPPER_BOUND
	 */
	@Test
	public void testRedoNothing () {
		try {
			stateHistory.getNext();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.UPPER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown");
	}
	
	/**
	 * This is a test case testing for when there is nothing to redo
	 * even after the next state is added
	 * A HistoryBoundException is expected to be thrown when the first
	 * getNext is called, with BoundType UPPER_BOUND
	 */
	@Test
	public void testRedoNothingAfterAdd () {
		stateHistory.add(TEST_LIST_2);
		try {
			stateHistory.getNext();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.UPPER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown or BoundType not UPPER_BOUND");
	}
	
	/**
	 * This is a test case testing for when there is nothing to undo
	 * after an undo method is called after the next state is added
	 * A HistoryBoundException is expected to be thrown when the second
	 * getPrevious is called, with BoundType LOWER_BOUND
	 */
	@Test
	public void testUndoNothingAfterAdd () throws HistoryBoundException {
		stateHistory.add(TEST_LIST_2);
		// no exception thrown
		stateHistory.getPrevious();
		// exception thrown
		try {
			stateHistory.getPrevious();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.LOWER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown or BoundType not LOWER_BOUND");
	}
	
	/**
	 * This is a test case for the normal scenario, where after a
	 * new state is added, it is possible to undo once and redo once
	 * The getPrevious is expected to return TEST_LIST_1
	 * The getNext is expected to return TEST_LIST_2
	 * @throws HistoryBoundException
	 */
	@Test
	public void testAddSecondState () throws HistoryBoundException {
		stateHistory.add(TEST_LIST_2);
		assertEquals(TEST_LIST_1, stateHistory.getPrevious());
		assertEquals(TEST_LIST_2, stateHistory.getNext());
	}
}
