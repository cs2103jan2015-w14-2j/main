package test;

import static org.junit.Assert.*;
import itinerary.history.HistoryBoundException;
import itinerary.history.HistoryBoundException.BoundType;
import itinerary.history.InputHistory;

import org.junit.Before;
import org.junit.Test;

//@author A0121437N
public class InputHistoryTest {
	
	private static final String COMMAND_1 = "Command 1";
	private static final String COMMAND_2 = "Command 1";
	
	private InputHistory InputHistory;
	
	@Before
	public void setupInputHistory () {
		InputHistory = new InputHistory();
	}

	/** 
	 * Initially, both ends should be null. This test tests if
	 * both ends of the InputHistory 'linked-list' are indeed null.
	 */
	@Test
	public void testGetInitial() throws HistoryBoundException {
		assertNull(InputHistory.getPrevious());
		assertNull(InputHistory.getNext());
	}

	/**
	 * Boundary case for when there is no next input at the start.
	 * A HistoryBoundException is expected to be thrown with the
	 * BoundType UPPER_BOUND
	 */
	@Test
	public void textNoNextInitial () {
		try {
			InputHistory.getNext();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.UPPER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown");
	}
	
	/**
	 * Boundary case for when there is no next input even after
	 * the first input is added. A HistoryBoundException is 
	 * expected to be thrown with the BoundType UPPER_BOUND
	 */
	@Test
	public void textNoNext () {
		InputHistory.add(COMMAND_1);
		try {
			InputHistory.getNext();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.UPPER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown");
	}
	
	/**
	 * Boundary case for when there is no previous input initially.
	 * A HistoryBoundException is expected to be thrown with the
	 * BoundType UPPER_BOUND
	 */
	@Test
	public void textNoPrevInitial () throws HistoryBoundException {
		assertNull(InputHistory.getPrevious());
		try {
			InputHistory.getPrevious();
		} catch (HistoryBoundException e) {
			assertEquals(BoundType.LOWER_BOUND, e.getBoundType());
			return;
		}
		fail("No exception thrown");
	}
	
	/**
	 * Normal test case for when after 2 commands are input
	 */
	@Test
	public void textNormal () throws HistoryBoundException {
		InputHistory.add(COMMAND_1);
		InputHistory.add(COMMAND_2);
		assertEquals(COMMAND_2, InputHistory.getPrevious());
		assertEquals(COMMAND_1, InputHistory.getPrevious());
		assertNull(InputHistory.getPrevious());
		assertEquals(COMMAND_1, InputHistory.getNext());
		assertEquals(COMMAND_2, InputHistory.getNext());
		assertNull(InputHistory.getNext());
	}
 }
