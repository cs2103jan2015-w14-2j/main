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
		 System.out.println(originalTaskOne.getLineNumber());
		 System.out.println(originalTaskOne.getText());
		 System.out.println(originalTaskOne.getCategory());
		assertTrue(expectedTaskOne.equals(originalTaskOne));
	}
}
