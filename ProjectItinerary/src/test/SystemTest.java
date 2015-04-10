package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import itinerary.main.DeadlineTask;
import itinerary.main.Logic;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;
import itinerary.search.SearchTask;
import itinerary.userinterface.UserInterfaceContent;

import java.io.File;
import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SystemTest {
	
	private static final String FILE_NAME = "ITnerary_system_test.txt";
	Logic logic = new Logic(FILE_NAME);
	
	@BeforeClass
	@AfterClass
	public static void deleteTestFile () {
		File file = new File (FILE_NAME);
		if (file.exists()) {
			file.delete();
		}
	}
	
	@After
	public void clear () {
		this.logic.executeUserInput("clear");
	}
	
	/**
	 * Tests adding a task normally.
	 * A Task object matching the input should be in the list of all tasks.
	 */
	@Test
	public void testAddNormal() {
		UserInterfaceContent result = this.logic.executeUserInput("add first task cat +cat");
		Task expectedTask = new Task(1, "first task", "cat", false, false);
		int size = result.getAllTasks().size();
		assertEquals(1, size);
		assertEquals(expectedTask, result.getAllTasks().get(size-1));
	}
	
	/**
	 * A boundary test case where nothing is input after the add command.
	 * The all tasks list should be empty since the command is invalid.
	 */
	@Test
	public void testAddNothing() {
		UserInterfaceContent result = this.logic.executeUserInput("add       ");
		int size = result.getAllTasks().size();
		assertEquals(0, size);
	}
	
	/**
	 * A boundary test case where no description is input after the add command.
	 * The all tasks list should be empty since the command is invalid.
	 */
	@Test
	public void testAddNothingWithPri() {
		UserInterfaceContent result = this.logic.executeUserInput("add pri");
		int size = result.getAllTasks().size();
		assertEquals(0, size);
	}
	
	/**
	 * Tests adding a task normally but with a deadline.
	 * A DeadlineTask object matching the input should be in the list of all tasks.
	 */
	@Test
	public void testAddDeadline() {
		UserInterfaceContent result = this.logic.executeUserInput("add deadline task by today pri");
		int size = result.getAllTasks().size();
		assertEquals(1, size);
		Task task = result.getAllTasks().get(size-1);
		assertTrue(task instanceof DeadlineTask);
		Calendar now = Calendar.getInstance();
		Calendar saved = ((DeadlineTask)task).getDeadline();
		assertEquals(now.get(Calendar.DAY_OF_YEAR), saved.get(Calendar.DAY_OF_YEAR));
		assertEquals(now.get(Calendar.YEAR), saved.get(Calendar.YEAR));
		assertTrue(task.isPriority());
	}
	
	/**
	 * Tests adding a task normally but with a schedule.
	 * A ScheduleTask object matching the input should be in the list of all tasks.
	 */
	@Test
	public void testAddSchedule() {
		UserInterfaceContent result = this.logic.executeUserInput("add schedule task from today to tomorrow cat sched");
		int size = result.getAllTasks().size();
		assertEquals(1, size);
		Task task = result.getAllTasks().get(size-1);
		assertTrue(task instanceof ScheduleTask);
		Calendar now = Calendar.getInstance();
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeInMillis(now.getTimeInMillis() + 1000 * 60 * 60 * 24);
		Calendar savedFrom = ((ScheduleTask)task).getFromDate();
		assertEquals(now.get(Calendar.DAY_OF_YEAR), savedFrom.get(Calendar.DAY_OF_YEAR));
		assertEquals(now.get(Calendar.YEAR), savedFrom.get(Calendar.YEAR));
		Calendar savedTo = ((ScheduleTask)task).getToDate();
		assertEquals(tomorrow.get(Calendar.DAY_OF_YEAR), savedTo.get(Calendar.DAY_OF_YEAR));
		assertEquals(tomorrow.get(Calendar.YEAR), savedTo.get(Calendar.YEAR));
		assertFalse(task.isPriority());
		assertEquals("sched", task.getCategory());
	}
	
	/**
	 * A boundary test case where the date input is invalid.
	 * The all tasks list should be empty since the command is invalid.
	 */
	@Test
	public void testAddInvalidDate () {
		UserInterfaceContent result = this.logic.executeUserInput("add first task by INVALIDDATE");
		int size = result.getAllTasks().size();
		assertEquals(0, size);
	}
	
	/**
	 * A boundary test case where the date input empty.
	 * The all tasks list should be empty since the command is invalid.
	 */
	@Test
	public void testAddEmptyDate () {
		UserInterfaceContent result = this.logic.executeUserInput("add first task from now to");
		int size = result.getAllTasks().size();
		assertEquals(0, size);
	}
	
	/**
	 * Tests deleting a task normally after a task is added.
	 * After the task is added, the all tasks list should be length 1.
	 * After the task is deleted, the all tasks list should be length 0.
	 */
	@Test
	public void testDeleteValid () {
		UserInterfaceContent first = this.logic.executeUserInput("add first task");
		assertEquals(1, first.getAllTasks().size());

		UserInterfaceContent second = this.logic.executeUserInput("delete 1");
		assertEquals(0, second.getAllTasks().size());
	}
	
	/**
	 * A boundary test case where the task id provided is negative.
	 * After the task is added, the all tasks list should be length 1.
	 * After the unsuccessful deletion, the all tasks list should still be length 1.
	 */
	@Test
	public void testDeleteInvalidNeg () {
		UserInterfaceContent first = this.logic.executeUserInput("add first task");
		assertEquals(1, first.getAllTasks().size());

		UserInterfaceContent second = this.logic.executeUserInput("delete -1");
		assertEquals(1, second.getAllTasks().size());
	}
	
	/**
	 * A boundary test case where the task id provided is greater than the number of tasks.
	 * After the task is added, the all tasks list should be length 1.
	 * After the unsuccessful deletion, the all tasks list should still be length 1.
	 */
	@Test
	public void testDeleteInvalidPos () {
		UserInterfaceContent first = this.logic.executeUserInput("add first task");
		assertEquals(1, first.getAllTasks().size());

		UserInterfaceContent second = this.logic.executeUserInput("delete 2");
		assertEquals(1, second.getAllTasks().size());
	}
	
	/**
	 * Tests undoing and redoing a command normally.
	 * After the add command, the all tasks list should be length 1
	 * After the undo command, the all tasks list should be length 0
	 * After the redo command, the all tasks list should be length 1
	 */
	@Test
	public void testUndoRedoNormal () {
		UserInterfaceContent first = this.logic.executeUserInput("add first task");
		assertEquals(1, first.getAllTasks().size());

		UserInterfaceContent second = this.logic.executeUserInput("undo");
		assertEquals(0, second.getAllTasks().size());
		
		UserInterfaceContent third = this.logic.executeUserInput("redo");
		assertEquals(1, third.getAllTasks().size());
	}
	
	/**
	 * Tests undoing and redoing when no command was given intially.
	 * The all tasks list should be always be of length 0
	 */
	@Test
	public void testUndoRedoNothing () {
		UserInterfaceContent second = this.logic.executeUserInput("undo");
		assertEquals(0, second.getAllTasks().size());
		
		UserInterfaceContent third = this.logic.executeUserInput("redo");
		assertEquals(0, third.getAllTasks().size());
	}
	
	/**
	 * Tests editing a task.
	 * After the add command, the task in the task list should match the input
	 * After the edit command, the task in the task list should have fields edited
	 * to match the input of the edit command.
	 */
	@Test
	public void testEditNormal () {
		UserInterfaceContent first = this.logic.executeUserInput("add first task cat dog");
		Task expectedTask1 = new Task(1, "first task", "dog", false, false);
		int size1 = first.getAllTasks().size();
		assertEquals(1, size1);
		assertEquals(expectedTask1, first.getAllTasks().get(size1-1));

		UserInterfaceContent second = this.logic.executeUserInput("edit 1 edited cat meow pri");
		Task expectedTask2 = new Task(1, "edited", "meow", true, false);
		int size2 = second.getAllTasks().size();
		assertEquals(1, size2);
		assertEquals(expectedTask2, second.getAllTasks().get(size2-1));
	}
	
	/**
	 * Tests the removal of categories.
	 * After the add command, the task in the task list should match the input
	 * After the edit command, the task in the list should no longer have a category
	 */
	@Test
	public void testEditRemoveCat () {
		this.logic.executeUserInput("add CS cat srudy");
		UserInterfaceContent result = this.logic.executeUserInput("edit 1 edited cat del");
		
		Task expectedTask = new Task(1, "edited", "", false, false);
		int size = result.getAllTasks().size();
		assertEquals(1, size);
		assertEquals(expectedTask, result.getAllTasks().get(size-1));
	}
	
	/**
	 * Tests if the system can end and start up regularly.
	 * After the first exitOperation is called, the details should be saved to disk
	 * After the second logic is created, its contents should match the first logic. 
	 */
	@Test
	public void testEnd () {
		Logic logic1 = new Logic(FILE_NAME);
		UserInterfaceContent first = logic1.executeUserInput("add first task cat dog");
		Task expectedTask = new Task(1, "first task", "dog", false, false);
		int size1 = first.getAllTasks().size();
		assertEquals(1, size1);
		assertEquals(expectedTask, first.getAllTasks().get(size1-1));
		logic1.exitOperation();
		
		Logic logic2 = new Logic(FILE_NAME);
		UserInterfaceContent second = logic2.executeUserInput("display");
		int size2 = second.getAllTasks().size();
		assertEquals(1, size2);
		assertEquals(expectedTask, second.getAllTasks().get(size2-1));
		logic2.executeUserInput("clear");
		logic2.exitOperation();
	}
	
	/**
	 * Tests searching for a task.
	 * After the first search command, the displayable list should length 3
	 * After the second search command, the displayable list should length 0
	 * to match the input of the edit command.
	 */
	@Test
	public void testSearchFound () {
		this.logic.executeUserInput("add first task");
		this.logic.executeUserInput("add second task");
		this.logic.executeUserInput("add third task");
		
		UserInterfaceContent result1 = this.logic.executeUserInput("search task");
		assertEquals(3, result1.getDisplayableTasks().size());
		
		UserInterfaceContent result2 = this.logic.executeUserInput("search second");
		assertEquals(1, result2.getDisplayableTasks().size());
	}
	
	/**
	 * Tests unsuccessfully searching for a task.
	 * After the search command, the displayable list should length 0
	 * to match the input of the edit command.
	 */
	@Test
	public void testSearchNotFound () {
		this.logic.executeUserInput("add first task");
		this.logic.executeUserInput("add second task");
		this.logic.executeUserInput("add third task");
		
		UserInterfaceContent result1 = this.logic.executeUserInput("search blah");
		assertEquals(0, result1.getDisplayableTasks().size());
	}
	
	@Test
	public void testInvalidBlankCommand () {
		UserInterfaceContent result = this.logic.executeUserInput("");
		assertEquals(0, result.getAllTasks().size());
	}
	
	@Test
	public void testInvalidCommand () {
		UserInterfaceContent result = this.logic.executeUserInput("invalid");
		assertEquals(0, result.getAllTasks().size());
	}
	
	@Test
	public void testInvalidDayOfMonth () {
		UserInterfaceContent result = this.logic.executeUserInput("add CS by 31/4/2015");
		assertEquals(0, result.getAllTasks().size());
	}
	
	@Test
	public void testInvalidAm () {
		UserInterfaceContent result = this.logic.executeUserInput("add CS by 13am");
		assertEquals(0, result.getAllTasks().size());
	}
	
	@Test
	public void testInvalidTime () {
		UserInterfaceContent result = this.logic.executeUserInput("add CS by 24:01");
		assertEquals(0, result.getAllTasks().size());
	}
	
	@Test
	public void testInvalidYear () {
		UserInterfaceContent result = this.logic.executeUserInput("add CS by 12/12/23 3pm");
		assertEquals(0, result.getAllTasks().size());
	}
	
	@Test
	public void testAdvancedSearchCat () {
		this.logic.executeUserInput("add first task cat one");
		this.logic.executeUserInput("add second task cat two");
		this.logic.executeUserInput("add third task cat one");
		
		SearchTask searchTask = new SearchTask();
		searchTask.setCategory("two");
		UserInterfaceContent result1 = this.logic.executeAdvancedSearch(searchTask);
		assertEquals(1, result1.getDisplayableTasks().size());
	}
	
	@Test
	public void testAdvancedSearchTextAndDate () {
		this.logic.executeUserInput("add first task by tomorrow");
		this.logic.executeUserInput("add second task");
		this.logic.executeUserInput("add third task by 3 days after today");
		
		SearchTask searchTask = new SearchTask();
		searchTask.setText("task");
		Calendar today = Calendar.getInstance();
		searchTask.setFromDate(today);
		Calendar twoDaysLater = Calendar.getInstance();
		twoDaysLater.setTimeInMillis(today.getTimeInMillis() + 1000 * 60 * 60 * 48);
		searchTask.setToDate(twoDaysLater);
		UserInterfaceContent result1 = this.logic.executeAdvancedSearch(searchTask);
		assertEquals(1, result1.getDisplayableTasks().size());
		assertEquals("first task", result1.getDisplayableTasks().get(0).getText());
	}
}
