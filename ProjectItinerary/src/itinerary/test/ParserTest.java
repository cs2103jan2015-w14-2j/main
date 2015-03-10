package itinerary.test;

import itinerary.main.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	@Test (expected = ParserException.class)
	public void testParseTwoPriKeyword () throws ParserException {
		Parser.parseCommand("add this pri pri");
	}
	
	@Test (expected = ParserException.class)
	public void testParseTwoByKeyword () throws ParserException {
		Parser.parseCommand("add this by by");
	}
	
	@Test (expected = ParserException.class)
	public void testParseTwoCatKeyword () throws ParserException {
		Parser.parseCommand("add this cat cat");
	}
	
	@Test (expected = ParserException.class)
	public void testParseBothScheduleDeadline () throws ParserException {
		Parser.parseCommand("add this by now from now to now");
	}
	
	@Test (expected = ParserException.class)
	public void testParseScheduleMissingOne () throws ParserException {
		Parser.parseCommand("add this from now");
	}
	
	@Test
	public void testParseAddNormal () throws ParserException {
		Command command = Parser.parseCommand("add this pri cat dog");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertEquals(task.getText(), "this");
		assertEquals(task.getCategory(), "dog");
		assertTrue(task.isPriority());
	}
	
	@Test
	public void testParseAddSchedule () throws ParserException {
		Command command = Parser.parseCommand("add this pri cat dog from today to tomorrow");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertTrue(task instanceof ScheduleTask);
		ScheduleTask scheduleTask = (ScheduleTask) task;
		assertEquals(scheduleTask.getText(), "this");
		assertEquals(scheduleTask.getCategory(), "dog");
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getFromDate());
		assertNotNull(scheduleTask.getToDate());
	}
	
	@Test
	public void testParseAddDeadline () throws ParserException {
		Command command = Parser.parseCommand("add this pri cat dog by tomorrow");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertTrue(task instanceof DeadlineTask);
		DeadlineTask scheduleTask = (DeadlineTask) task;
		assertEquals(scheduleTask.getText(), "this");
		assertEquals(scheduleTask.getCategory(), "dog");
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getDeadline());
	}
}
