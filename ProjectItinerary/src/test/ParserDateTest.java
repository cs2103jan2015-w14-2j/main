package test;

import static org.junit.Assert.*;
import itinerary.main.*;
import itinerary.parser.Parser;
import itinerary.parser.ParserDate;
import itinerary.parser.ParserException;

import org.junit.Test;

//@author A0114823M
public class ParserDateTest {

	ParserDate parserDate = new ParserDate();

	@Test
	public void testNormal() throws ParserException{
		String dateString = "3pm 2017/3/4";
		assertNotNull(parserDate.getDate(dateString));
	}
	
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

	@Test
	public void testSwitchDateMonthFail(){
		String expectedDate = "2018/12/5";
		String resultDate = parserDate.switchDateMonth("2018/12/5");
		assertEquals(expectedDate, resultDate);
	}

	@Test (expected = ParserException.class)
	public void testInvalidMonth () throws ParserException {
		parserDate.getDate("2018/23/23");
	}

	@Test (expected = ParserException.class)
	public void testInvalidDate () throws ParserException {
		parserDate.getDate("2018/12/32");
	}
	
	@Test (expected = ParserException.class)
	public void testInvalidYear () throws ParserException {
		parserDate.getDate("12/12/23");
	}
	
	@Test (expected = ParserException.class)
	public void testExtraValue () throws ParserException {
		parserDate.getDate("2015/12/1/2");
	}
}

