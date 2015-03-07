package itinerary.test;

import itinerary.main.*;
import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Test;

public class ParserTest {
	
	Parser parser = new Parser();

	@Test
	public void testEditTask() {
		Task originalTaskOne = new Task(1, "go home", "", false, false);	
		Task expectedTaskOne = new Task(1, "go home changed", "newCategory", true, true);
		String input = "edit 1 go home changed";
		 originalTaskOne = parser.editTask(input);
		 //System.out.println(originalTaskOne.getTaskId());
		// System.out.println(originalTaskOne.getText());
		// System.out.println(originalTaskOne.getCategory());
		 assertEquals(null,parser.getMessage());
		assertTrue(expectedTaskOne.equals(originalTaskOne));
	}
	
	@Test
	public void testAddTask() {
		Task expectedTaskOne = new Task(-1, "eat lunch", "", false, false);
		String input = "add eat lunch";
		Task createdTaskOne = parser.addTask(input);
		assertEquals(null,parser.getMessage());
		assertTrue(expectedTaskOne.equals(createdTaskOne));
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
	
	@Test
	public void testExtractContent(){
		String inputOne = "add go to sleep pri";
		Task expectedTaskOne = new Task(-1, "go to sleep", "", true, false);
		Task taskOne = parser.extractContent(inputOne,1);
		assertTrue(expectedTaskOne.equals(taskOne));
		
	}
	
	
}
