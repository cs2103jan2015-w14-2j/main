package test;

import static org.junit.Assert.*;
import itinerary.main.*;
import itinerary.parser.ParserDate;

import org.junit.Test;

//@author A0114823M
public class ParserDateTest {

	ParserDate parserDate = new ParserDate();

	@Test
	public void testChangeDateFormat(){
		String expectedDate = "2015/1/28";
		String resultDate = parserDate.changeDateFormat("2015.1.28");
		assertEquals(expectedDate, resultDate);
	}
	
	@Test
	public void testSwitchDateMonth(){
		String expectedDate = "28/1/2015";
		String resultDate = parserDate.switchDateMonth("1/28/2015");
		assertEquals(expectedDate, resultDate);
	}
}

