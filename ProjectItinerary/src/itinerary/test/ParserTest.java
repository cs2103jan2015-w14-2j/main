package itinerary.test;

import java.text.ParseException;
import java.util.Date;

import itinerary.main.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	Parser parser = new Parser();
	ParserDate parserDate = new ParserDate();

	@Test
	public void testExtractContent() throws ParseException{

		String inputOne = "add go home";
		Task expectedTaskOne = new Task(-1, "go home", "", false, false);
		Task taskOne = parser.extractContent(inputOne);
		assertTrue(expectedTaskOne.equals(taskOne));

		String inputTwo = "add go to sleep pri";
		Task expectedTaskTwo = new Task(-1, "go to sleep", "", true, false);
		Task taskTwo = parser.extractContent(inputTwo);
		assertTrue(expectedTaskTwo.equals(taskTwo));

		String inputThree = "add go swimming ca sports";
		Task expectedTaskThree = new Task(-1, "go swimming", "sports", false, false);
		Task taskThree = parser.extractContent(inputThree);
		System.out.println(taskThree.getTaskId());
		System.out.println(taskThree.getText());
		System.out.println(taskThree.getCategory());
		System.out.println(taskThree.isPriority());
		System.out.println(taskThree.isComplete());
		assertTrue(expectedTaskThree.equals(taskThree));

		String inputFour = "add go to LA ca summer holiday plan";
		Task expectedTaskFour = new Task(-1, "go to LA", "summer holiday plan", false, false);
		Task taskFour = parser.extractContent(inputFour);
		assertTrue(expectedTaskFour.equals(taskFour));

		String inputFive = "edit 3 go to LA ca summer holiday plan";
		Task expectedTaskFive = new Task(-1, "go to LA", "summer holiday plan", false, false);
		Task taskFive = parser.extractContent(inputFive);
		assertTrue(expectedTaskFive.equals(taskFive));

		String inputSix = "add do homework by 26/5/2015 18:50";
		DeadlineTask taskSix = (DeadlineTask)parser.extractContent(inputSix);
		System.out.println(taskSix.getDeadline());
		
		String inputSeven = "add go to Malaysia ti 20/5/2015 to 21/5/2015 17:30 ";
		ScheduleTask taskSeven = (ScheduleTask) parser.extractContent(inputSeven);
		System.out.println(taskSeven.getFromDate());
		System.out.println(taskSeven.getToDate());


/*
		String inputSix = "add go to LA by 2015/3/5 1850";
		@SuppressWarnings("deprecation")
		Date dateLA = new Date(115, 2, 5, 18, 50);
        DeadlineTask expectedTaskLA = new DeadlineTask (-1, "go to LA", "", false,false, parserDate.convertToCalendar(dateLA));
		Task taskLA = parser.extractContent(inputSix,1);
		System.out.println(taskLA.toString());
		assertTrue(expectedTaskLA.equals(taskLA));
*/
	}

	@Test
	public void testEditTask() throws Exception {
		Task originalTaskOne = new Task(3, "eat Chinese food", "", false, false);	
		Task expectedTaskOne = new Task(1, "eat Indian food", "", false, false);
		String inputOne = "edit 1 eat Indian food";
		originalTaskOne = parser.createTaskToEdit(inputOne);
		System.out.println(originalTaskOne.getTaskId());
		System.out.println(originalTaskOne.getText());
		System.out.println(originalTaskOne.isPriority());
		System.out.println(originalTaskOne.getCategory());
		assertTrue(expectedTaskOne.equals(originalTaskOne));


		Task originalTaskTwo = new Task(1, "eat bread", "", false, false);	
		Task expectedTaskTwo = new Task(2, "eat cake", "", true, false);
		String inputTwo = "edit 2 eat cake pri ";
		originalTaskTwo = parser.createTaskToEdit(inputTwo);
		System.out.println(originalTaskTwo.getTaskId());
		System.out.println(originalTaskTwo.getText());
		System.out.println(originalTaskTwo.isPriority());
		System.out.println(originalTaskTwo.getCategory());
		assertEquals(true, originalTaskTwo.isPriority());
		assertTrue(expectedTaskTwo.equals(originalTaskTwo));
	}

	@Test
	public void testAddTask() throws ParseException {
		
		Task expectedTaskOne = new Task(-1, "visit", "", true, false);
		String input = "add visit pri";
		Task createdTaskOne = parser.createTaskToAdd(input);
		System.out.println(createdTaskOne.getTaskId());
		System.out.println(createdTaskOne.getText());
		System.out.println(createdTaskOne.isPriority());
		assertTrue(expectedTaskOne.equals(createdTaskOne));
		
		Task expectedTaskTwo = new Task(-1, "visit grandmom", "plan for recess week", true, false);
		String inputTwo = "add visit grandmom pri ca plan for recess week";
		Task taskTwo = parser.createTaskToAdd(inputTwo);
		System.out.println(taskTwo.getTaskId());
		System.out.println(taskTwo.getText());
		System.out.println(taskTwo.getCategory());
		assertEquals(true, taskTwo.isPriority());
		assertTrue(expectedTaskTwo.equals(taskTwo));
		
		Task expectedTaskThree = new Task(-1, "visit sister", "",false, false);
		String inputThree = "add visit sister";
		Task createdTaskThree = parser.createTaskToAdd(inputThree);
		System.out.println(createdTaskThree.getTaskId());
		System.out.println(createdTaskThree.getText());
		System.out.println(createdTaskThree.isPriority());
		assertTrue(expectedTaskThree.equals(createdTaskThree));
		
	}

	@Test
	public void testHasDuplicatedKeywords() {
		String inputOne = "add do homework pri catagory study";
		String[] inputWordsOne = parser.stringToArray(inputOne);
		assertEquals(false, parser.hasDuplicatedKeywords(inputWordsOne));

		String inputTwo = "add do homework pri catagory study pri";
		String[] inputWordsTwo = parser.stringToArray(inputTwo);
		assertEquals(true, parser.hasDuplicatedKeywords(inputWordsTwo));

		String inputThree = "add do homework pri catagory study by next Wednesday ti 6pm to 9 pm";
		String[] inputWordsThree = parser.stringToArray(inputThree);
		assertEquals(true, parser.hasDuplicatedKeywords(inputWordsThree));
	}
}
