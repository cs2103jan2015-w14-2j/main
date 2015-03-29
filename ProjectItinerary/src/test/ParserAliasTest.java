package test;

import itinerary.main.CommandType;
import itinerary.parser.ParserAlias;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class ParserAliasTest {

	ParserAlias parserAlias = new ParserAlias();
	
	@Test
	public void testEditType (){
        ArrayList<ArrayList<String>> commandList = parserAlias.getCommandList();
        for(String cmd: commandList.get(4)){
        	 assertEquals(CommandType.EDIT, parserAlias.determineType(cmd));
        }
	}
	
	@Test
	public void testMarkType (){
        ArrayList<ArrayList<String>> commandList = parserAlias.getCommandList();
        for(String cmd: commandList.get(8)){
        	 assertEquals(CommandType.UNDO, parserAlias.determineType(cmd));
        }
	}	
}
