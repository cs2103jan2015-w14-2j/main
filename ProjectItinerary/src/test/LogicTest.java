package test;

import static org.junit.Assert.assertEquals;
import itinerary.history.History;
import itinerary.main.Logic;
import itinerary.main.Task;
import itinerary.parser.Command;
import itinerary.parser.CommandType;
import itinerary.storage.Storage;
import itinerary.storage.StorageStub;
import itinerary.userinterface.UserInterfaceContent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogicTest {

	private static final Task TASK_1 = new Task(1, "line 1", "cat 1", true, false);
	private static final Task TASK_2 = new Task(2, "line 2", "cat 2", false, false);
	private static final Task TASK_3 = new Task(3, "line 3", null, false, false);
	
	private static final Command ADD_1 = new Command(TASK_1, CommandType.ADD);
	private static final Command ADD_2 = new Command(TASK_2, CommandType.ADD);
	private static final Command ADD_3 = new Command(TASK_3, CommandType.ADD);
	
	static Logic logic;
	
	@BeforeClass
	public static void setUpTest() {
		Storage storage = new StorageStub();
		History history = new History(storage.getAllTasks());
		logic = new Logic("", storage, history);
	}
	
	@Before
	public void setUpLogic() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method executeAdd = getPrivateMethod(Logic.class, "executeAdd", new Class[]{Command.class});
		executeAdd.invoke(logic, new Object[]{ADD_1});
		executeAdd.invoke(logic, new Object[]{ADD_2});
		executeAdd.invoke(logic, new Object[]{ADD_3});
	}
	
	@After
	public void clearLogic() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method executeClear = getPrivateMethod(Logic.class, "executeClear", new Class[]{});
		executeClear.invoke(logic, new Object[]{});
	}
	
	@Test
	public void testInit () {
		UserInterfaceContent content = logic.initialLaunch();
		assertEquals(3, content.getAllTasks().size());
	}
	
	@Test
	public void testBasicSearch() {
		UserInterfaceContent content = logic.executeBasicSearch("1");
		assertEquals(1, content.getDisplayableTasks().size());
		assertEquals("line 1", content.getDisplayableTasks().get(0).getText());
	}

	/* This is a boundary case for when the search query is null */
	@Test (expected = NullPointerException.class)
	public void testBasicSearchNull() {
		logic.executeBasicSearch(null);
	}
	
	@Test
	public void testAdd() {
		UserInterfaceContent result = logic.executeUserInput("add this");
		assertEquals(4, result.getAllTasks().size());
	}

	/* This is a boundary case for when there is nothing after the add command */
	@Test
	public void testAddEmpty() {
		UserInterfaceContent result = logic.executeUserInput("add");
		assertEquals(3, result.getAllTasks().size());
	}
	
	@Test
	public void testClear() {
		UserInterfaceContent content = logic.executeUserInput("clear");
		assertEquals(0, content.getAllTasks().size());
	}
	
	@Test
	public void testDeleteValid () {
		UserInterfaceContent content = logic.executeUserInput("delete 2");
		assertEquals(2, content.getAllTasks().size());
		assertEquals("line 1", content.getAllTasks().get(0).getText());
		assertEquals("line 3", content.getAllTasks().get(1).getText());
	}
	
	/* This is a boundary case for when the taskId is negative */
	@Test
	public void testDeleteInvalidNeg () {
		UserInterfaceContent content = logic.executeUserInput("delete -1");
		assertEquals(3, content.getAllTasks().size());
	}
	
	/* This is a boundary case for when the taskId is positive but invalid */
	@Test
	public void testDeleteInvalidPos () {
		UserInterfaceContent content = logic.executeUserInput("delete 4");
		assertEquals(3, content.getAllTasks().size());
	}
	
	@Test
	public void testDisplay () {
		UserInterfaceContent content = logic.executeUserInput("display");
		assertEquals(3, content.getAllTasks().size());
		assertEquals("line 1", content.getAllTasks().get(0).getText());
		assertEquals("line 2", content.getAllTasks().get(1).getText());
		assertEquals("line 3", content.getAllTasks().get(2).getText());
	}
	
	@Test
	public void testEditValid () {
		UserInterfaceContent content = logic.executeUserInput("edit 2 edited line 2");
		assertEquals(3, content.getAllTasks().size());
		assertEquals("edited line 2", content.getAllTasks().get(1).getText());
	}
	
	/* This is a boundary case for when the taskId is negative */
	@Test
	public void testEditInvalidNeg () {
		UserInterfaceContent content = logic.executeUserInput("edit -1 edited line 2");
		assertEquals(3, content.getAllTasks().size());
		assertEquals("line 1", content.getAllTasks().get(0).getText());
		assertEquals("line 2", content.getAllTasks().get(1).getText());
		assertEquals("line 3", content.getAllTasks().get(2).getText());
	}
	
	/* This is a boundary case for when the taskId is positive but invalid */
	@Test
	public void testEditInvalidPos () {
		UserInterfaceContent content = logic.executeUserInput("edit 4 edited line 2");
		assertEquals(3, content.getAllTasks().size());
		assertEquals("line 1", content.getAllTasks().get(0).getText());
		assertEquals("line 2", content.getAllTasks().get(1).getText());
		assertEquals("line 3", content.getAllTasks().get(2).getText());
	}
	
	/* This is a boundary case for when there is nothing after the search command */
	@Test
	public void testSearchEmpty () {
		UserInterfaceContent content = logic.executeUserInput("search");
		assertEquals(3, content.getDisplayableTasks().size());
	}
	
	@Test
	public void testSearchValid () {
		UserInterfaceContent content = logic.executeUserInput("search line 1");
		assertEquals(1, content.getDisplayableTasks().size());
	}
	
	/* This is a boundary case for when there is nothing that can be found */
	@Test
	public void testSearchInvalid () {
		UserInterfaceContent content = logic.executeUserInput("search nothing valid");
		assertEquals(0, content.getDisplayableTasks().size());
	}
	
	/* This is a boundary case for when there is something input
	 * by the user which is invalid */
	@Test
	public void testInvalidFilled () {
		UserInterfaceContent content = logic.executeUserInput("invalid");
		assertEquals("invalid command: \"invalid\"", content.getConsoleMessage());
	}
	
	/* This is a boundary case for when there is nothing input by the user */
	@Test
	public void testInvalidEmpty () {
		UserInterfaceContent content = logic.executeUserInput("");
		assertEquals("invalid command: \"\"", content.getConsoleMessage());
	}
	
	@Test
	public void testUndo () {
		UserInterfaceContent content = logic.executeUserInput("undo");
		assertEquals(2, content.getAllTasks().size());
	}
	
	@Test
	public void testRedo () {
		testUndo();
		UserInterfaceContent content = logic.executeUserInput("redo");
		assertEquals(3, content.getAllTasks().size());
	}
	
	/* This is a boundary case for when there is nothing to redo */
	@Test
	public void testRedoNothing () {
		UserInterfaceContent content = logic.executeUserInput("redo");
		assertEquals("nothing to redo", content.getConsoleMessage());
	}
	
	/* This is a boundary case for when there is nothing to undo */
	@Test
	public void testUndoNothing () {
		Storage storage = new StorageStub();
		History history = new History(storage.getAllTasks());
		Logic logic = new Logic("", storage, history);
		UserInterfaceContent content = logic.executeUserInput("undo");
		assertEquals("nothing to undo", content.getConsoleMessage());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Method getPrivateMethod (Class privateObjectClass, String methodName, Class[] argClasses) {
		Method privateMethod;
		try {
			privateMethod = privateObjectClass.getDeclaredMethod(methodName, argClasses);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
		privateMethod.setAccessible(true);
		return privateMethod;
	}
}
