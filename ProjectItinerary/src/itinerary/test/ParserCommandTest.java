package itinerary.test;

import static org.junit.Assert.*;
import itinerary.main.*;

import org.junit.Test;

public class ParserCommandTest {

	ParserCommand parserCommand = new ParserCommand();
	@Test
	public void testGetType() {
		assertEquals(CommandType.ADD, parserCommand.getType("add"));
		assertEquals(CommandType.ADD, parserCommand.getType("ADD"));
		assertEquals(CommandType.DELETE, parserCommand.getType("delete"));
		assertEquals(CommandType.CLEAR, parserCommand.getType("Clear"));
		assertEquals(CommandType.SEARCH, parserCommand.getType("SEARCH"));
		assertEquals(CommandType.EDIT, parserCommand.getType("EdIt"));
		assertEquals(CommandType.DISPLAY, parserCommand.getType("displAy"));
		assertEquals(CommandType.REDO, parserCommand.getType("REDO"));
		assertEquals(CommandType.UNDO, parserCommand.getType("undo"));
	}
}
