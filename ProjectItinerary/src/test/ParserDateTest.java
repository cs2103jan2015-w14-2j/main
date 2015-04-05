package test;

import static org.junit.Assert.*;
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
		String resultDate = parserDate.changeDateFormat("2015-1-28");
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
	
	@Test 
	public void testTimeWithDot () throws ParserException {
		String dateString = "9.30pm";
		assertNotNull(parserDate.getDate(dateString));
	}
	
	@Test (expected = ParserException.class)
	public void testInvalidDayOfMonth () throws ParserException {
		parserDate.getDate("2015/4/31");
	}	
	
	@Test
	public void testValidDayOfMonth () throws ParserException {
		String dateString = "2012/3/31";
		assertNotNull(parserDate.getDate(dateString));
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidDayOfFeb () throws ParserException {
		parserDate.getDate("2015/2/29");
	}	

	@Test
	public void testValidDayOfFeb () throws ParserException {
		String dateString = "2012/2/29";
		assertNotNull(parserDate.getDate(dateString));
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidAm () throws ParserException {
		parserDate.getDate("2015/1/2 13am");
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidP () throws ParserException {
		parserDate.getDate("2015/1/2 23p");
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidTime () throws ParserException {
		parserDate.getDate("2015/1/2 930");
	}	
	
	@Test
	public void testValidTime () throws ParserException {
		String dateString = "2012/1/2 0930";
		assertNotNull(parserDate.getDate(dateString));
	}	
	
	@Test
	public void testAToOne () throws ParserException {
		assertNotNull(parserDate.getDate("a month"));
	}	
	
	@Test
	public void testTomorrow () throws ParserException {
		assertNotNull(parserDate.getDate("tomorrow"));
	}	
	
	@Test
	public void testFridayNoon () throws ParserException {
		assertNotNull(parserDate.getDate("Friday noon"));
	}	
}

