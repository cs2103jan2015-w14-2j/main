package test;

import itinerary.main.*;
import itinerary.parser.Parser;
import itinerary.parser.ParserException;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	@Test (expected = ParserException.class)
	public void testTwoPriKeyword () throws ParserException {
		Parser.parseCommand("add this pri pri");
	}
	
	@Test (expected = ParserException.class)
	public void testTwoByKeyword () throws ParserException {
		Parser.parseCommand("add this by by");
	}
	
	@Test (expected = ParserException.class)
	public void testTwoCatKeyword () throws ParserException {
		Parser.parseCommand("add this cat cat");
	}
	
	@Test (expected = ParserException.class)
	public void testBothScheduleDeadline () throws ParserException {
		Parser.parseCommand("add this by tomorrow from now to tomorrow");
	}
	
	@Test (expected = ParserException.class)
	public void testScheduleMissingOne () throws ParserException {
		Parser.parseCommand("add this from tomorrow");
	}
	
	@Test (expected = ParserException.class)
	public void testAddByMissing () throws ParserException {
		Parser.parseCommand("add this by");
	}
	
	@Test (expected = ParserException.class)
	public void testAddFromToMissing () throws ParserException {
		Parser.parseCommand("add this from to");
	}
	
	@Test (expected = ParserException.class)
	public void testEditFromMissing () throws ParserException {
		Parser.parseCommand("edit 1 by");
	}
	
	@Test (expected = ParserException.class)
	public void testParseTaskIDMissing () throws ParserException {
		Parser.parseCommand("delete");
	}
	
	@Test (expected = ParserException.class)
	public void testInvalidTaskID () throws ParserException {
		Parser.parseCommand("delete job");
	}
	
	@Test (expected = ParserException.class)
	public void testEditMissing () throws ParserException {
		Parser.parseCommand("edit");
	}
	
	@Test (expected = ParserException.class)
	public void testEditContentMissing () throws ParserException {
		Parser.parseCommand("edit 1");
	}
	
	@Test (expected = ParserException.class)
	public void testEditCatMissing () throws ParserException {
		Parser.parseCommand("edit 1 cat");
	}
	
	@Test (expected = ParserException.class)
	public void testEditCatMissingWithKeyword () throws ParserException {
		Parser.parseCommand("edit 1 cat pri");
	}
	
	@Test (expected = ParserException.class)
	public void testAddContentMissing () throws ParserException {
		Parser.parseCommand("add");
	}
	
	@Test (expected = ParserException.class)
	public void testAddContentMissingWithKeyword() throws ParserException {
		Parser.parseCommand("add by Sunday");
	}
	
	@Test (expected = ParserException.class)
	public void testAddCatMissing () throws ParserException {
		Parser.parseCommand("add this cat");
	}
	
	@Test
	public void testAdd () throws ParserException {
		Command command = Parser.parseCommand("add swim");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertEquals(task.getText(), "swim");
	}
	
	@Test
	public void testAddNormal () throws ParserException {
		Command command = Parser.parseCommand("add swim pri cat sports");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertEquals(task.getText(), "swim");
		assertEquals(task.getCategory(), "sports");
		assertTrue(task.isPriority());
	}
	
	@Test
	public void testAddSchedule () throws ParserException {
		Command command = Parser.parseCommand("add do homework pri cat study from 2015/3/4 8pm to 2015/5/6 9pm");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertTrue(task instanceof ScheduleTask);
		ScheduleTask scheduleTask = (ScheduleTask) task;
		assertEquals(scheduleTask.getText(), "do homework");
		assertEquals(scheduleTask.getCategory(), "study");
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getFromDate());
		assertNotNull(scheduleTask.getToDate());
	}
	
	@Test
	public void testAddDeadline () throws ParserException {
		Command command = Parser.parseCommand("add swim pri cat sports by tomorrow");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertTrue(task instanceof DeadlineTask);
		DeadlineTask scheduleTask = (DeadlineTask) task;
		assertEquals(scheduleTask.getText(), "swim");
		assertEquals(scheduleTask.getCategory(), "sports");
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
	
    @Test
	public void testAddReplcaeKeyword () throws ParserException {
		Command command = Parser.parseCommand("add buy +cat, go home pri cat entertainment +from animal");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertEquals(task.getText(), "buy cat, go home");
		assertEquals(task.getCategory(), "entertainment from animal");
		assertTrue(task.isPriority());
	}
    
    @Test
	public void testEdit () throws ParserException {
		Command command = Parser.parseCommand("edit 1 +cat catches mouse");
		assertEquals(command.getType(), CommandType.EDIT);
		Task task = command.getTask();
		assertEquals(task.getTaskId(), 1);
		assertEquals(task.getText(), "cat catches mouse");
	}
	
	@Test
	public void testEditDeadline () throws ParserException {
		Command command = Parser.parseCommand("edit 1 eat pri cat gain weight by tomorrow");
		assertEquals(command.getType(), CommandType.EDIT);
		Task task = command.getTask();
		assertTrue(task instanceof DeadlineTask);
		DeadlineTask scheduleTask = (DeadlineTask) task;
		assertEquals(scheduleTask.getTaskId(), 1);
		assertEquals(scheduleTask.getText(), "eat");
		assertEquals(scheduleTask.getCategory(), "gain weight");
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getDeadline());
	}
}