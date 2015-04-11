package test;

import itinerary.main.*;
import itinerary.parser.Command;
import itinerary.parser.CommandType;
import itinerary.parser.Parser;
import itinerary.parser.ParserException;
import static org.junit.Assert.*;

import org.junit.Test;

//@author A0114823M
public class ParserTest {
	
	//Corner test case when there exists duplicate key word
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
	public void testEditByMissing () throws ParserException {
		Parser.parseCommand("edit 1 by");
	}
	
	@Test (expected = ParserException.class)
	public void testParseTaskIDMissing () throws ParserException {
		Parser.parseCommand("delete");
	}
	
	//Corner test case when the index of task is not valid
	@Test (expected = ParserException.class)
	public void testInvalidTaskID () throws ParserException {
		Parser.parseCommand("delete job");
	}
	
	@Test (expected = ParserException.class)
	public void testEditMissing () throws ParserException {
		Parser.parseCommand("edit");
	}
	
	//Corner test case when the things to be edited is not specified
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
	public void testSearchContentMissing () throws ParserException {
		Parser.parseCommand("search");
	}
	
	//Corner test case when the task to be added is not described
	@Test (expected = ParserException.class)
	public void testAddContentMissingWithKeyword() throws ParserException {
		Parser.parseCommand("add by Sunday");
	}
	
	//Corner test case when there is not description for category
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
		Command command = Parser.parseCommand("add do homework pri cat study from 2017/3/4 8pm to 2017/5/6 9pm");
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
	public void testSearchFull () throws ParserException {
		Command command = Parser.parseCommand("search CS pri cat exam com from 2015/3/4 to future");
		assertEquals(command.getType(), CommandType.SEARCH);
		Task task = command.getTask();
		assertTrue(task instanceof ScheduleTask);
		ScheduleTask scheduleTask = (ScheduleTask) task;
		assertEquals(scheduleTask.getText(), "CS");
		assertEquals(scheduleTask.getCategory(), "exam");
		assertTrue(scheduleTask.isComplete());
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getFromDate());
		assertNotNull(scheduleTask.getToDate());
	}
	
	@Test
	public void testSearch () throws ParserException {
		Command command = Parser.parseCommand("search pri");
		assertEquals(command.getType(), CommandType.SEARCH);
		Task task = command.getTask();
		assertNull(task.getText());
		assertNull(task.getCategory());
		assertTrue(task.isPriority());
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
		String inputString = "add Take a look at this +cat, I bought it +from a pet shop +by the road cat +cat meow";
		Command command = Parser. parseCommand(inputString);
		Task task = command.getTask();
		assertEquals(task.getText(), "Take a look at this cat, I bought it from a pet shop by the road");
		assertEquals(task.getCategory(), "cat meow");
	}
	
    @Test
	public void testAddReplcaeKeyword () throws ParserException {
		Command command = Parser.parseCommand("add buy +cat +dog, go home pri cat www.entertainment +from animal. +com");
		assertEquals(command.getType(), CommandType.ADD);
		Task task = command.getTask();
		assertEquals(task.getText(), "buy cat +dog, go home");
		assertEquals(task.getCategory(), "www.entertainment from animal. com");
		assertTrue(task.isPriority());
	}
    
    @Test
	public void testEdit () throws ParserException {
		Command command = Parser.parseCommand("edit 1 +cat catches mouse");
		assertEquals(command.getType(), CommandType.EDIT);
		Task task = command.getTask();
		assertEquals((int)task.getTaskId(), 1);
		assertEquals(task.getText(), "cat catches mouse");
	}
    
    @Test
	public void testUnmark () throws ParserException {
		Command command = Parser.parseCommand("Unmark 1");
		assertEquals(command.getType(), CommandType.MARK);
		Task task = command.getTask();
		assertEquals((int)task.getTaskId(), 1);
		assertFalse( task.isComplete());
	}
    
    @Test
	public void testMark () throws ParserException {
		Command command = Parser.parseCommand("Mark 1");
		assertEquals(command.getType(), CommandType.MARK);
		Task task = command.getTask();
		assertEquals((int)task.getTaskId(), 1);
		assertTrue( task.isComplete());
	}
	
	@Test
	public void testEditDeadline () throws ParserException {
		Command command = Parser.parseCommand("edit 1 eat pri cat gain weight by tomorrow");
		assertEquals(command.getType(), CommandType.EDIT);
		Task task = command.getTask();
		assertTrue(task instanceof DeadlineTask);
		DeadlineTask scheduleTask = (DeadlineTask) task;
		assertEquals((int)scheduleTask.getTaskId(), 1);
		assertEquals(scheduleTask.getText(), "eat");
		assertEquals(scheduleTask.getCategory(), "gain weight");
		assertTrue(scheduleTask.isPriority());
		assertNotNull(scheduleTask.getDeadline());
	}
}
