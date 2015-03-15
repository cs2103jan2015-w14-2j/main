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
	
	@Test (expected = ParserException.class)
	public void testParseCatDescriptionMissing () throws ParserException {
		Parser.parseCommand("add this cat");
	}
	
	@Test (expected = ParserException.class)
	public void testParseNoTaskID () throws ParserException {
		Parser.parseCommand("delete");
	}
	
	@Test (expected = ParserException.class)
	public void testParseInvalidTaskID () throws ParserException {
		Parser.parseCommand("delete 2.5");
	}
	
	@Test (expected = ParserException.class)
	public void testParseNoContentEdit () throws ParserException {
		Parser.parseCommand("edit 1");
	}
	
	@Test (expected = ParserException.class)
	public void testParseNoContentAddWithKeyword () throws ParserException {
		Parser.parseCommand("add by Sunday");
	}
	
	@Test (expected = ParserException.class)
	public void testParseNoContentAddWithoutKeyword () throws ParserException {
		Parser.parseCommand("add");
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
	
   // @Test
	public void testParseAddReplcaeKeyword () throws ParserException {
		Command command = Parser.parseCommand("add buy +cat, go home pri cat entertainment +from animal");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertEquals(task.getText(), "buy cat, go home");
		assertEquals(task.getCategory(), "entertainment from animal");
		assertTrue(task.isPriority());
	}
	
	@Test
	public void testParseEditDeadline () throws ParserException {
		Command command = Parser.parseCommand("edit 1 this pri cat food by tomorrow");
		assertEquals(command.getType(), CommandType.EDIT);
		Task task = command.getTask();
		assertTrue(task instanceof DeadlineTask);
		DeadlineTask scheduleTask = (DeadlineTask) task;
		assertEquals(scheduleTask.getTaskId(), 1);
		assertEquals(scheduleTask.getText(), "this");
		assertEquals(scheduleTask.getCategory(), "food");
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getDeadline());
	}
	
	@Test
	public void testReplaceKeywordInContent () throws ParserException {
		String expectedString = "Take a look at this cat, I bought it from a pet shop by the road";
		String inputString = "Take a look at this +cat, I bought it +from a pet shop +by the road";
		String resultString = Parser. replaceKeywordInContent(inputString);
		assertEquals(expectedString, resultString);
	}
}
