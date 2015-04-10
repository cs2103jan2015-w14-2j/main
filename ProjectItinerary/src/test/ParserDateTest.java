package test;

import static org.junit.Assert.*;
import itinerary.parser.ParserDate;
import itinerary.parser.ParserException;

import org.junit.Test;

//@author A0114823M
public class ParserDateTest {

	ParserDate parserDate = new ParserDate();

	@Test
	public void testWithoutTime() throws ParserException{
		String dateString = "5/8/2009";
		assertNotNull(parserDate.getDate(dateString));
	}
	
	@Test
	public void testFuture() throws ParserException{
		String dateString = "future";
		assertNotNull(parserDate.getDate(dateString));
	}
	
	@Test
	public void testNormal() throws ParserException{
		String dateString = "2017/3/4 3pm";
		assertNotNull(parserDate.getDate(dateString));
	}
	
	@Test 
	public void testTimeWithDot () throws ParserException {
		String dateString = "9.30pm";
		assertNotNull(parserDate.getDate(dateString));
	}
	
	@Test
	public void testValidDayOfMonth () throws ParserException {
		String dateString = "2012/3/31 2pm";
		assertNotNull(parserDate.getDate(dateString));
	}	

	@Test
	public void testValidDayOfFeb () throws ParserException {
		String dateString = "2012/2/29 2pm";
		assertNotNull(parserDate.getDate(dateString));
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
		assertNotNull(parserDate.getDate("tomorrow night"));
	}	
	
	@Test
	public void testFridayNoon () throws ParserException {
		assertNotNull(parserDate.getDate("next Friday noon"));
	}	
	
	@Test
	public void testFourDigitTime () throws ParserException {
		assertNotNull(parserDate.getDate("23:59"));
	}	
	
	@Test
	public void testOnlyDayMonth () throws ParserException {
		assertNotNull(parserDate.getDate("3/8 2pm"));
	}	
		
	@Test
	public void testChristmas() throws ParserException{
		String dateString = "Christmas";
		assertNotNull(parserDate.getDate(dateString));
	}
	
	@Test
	public void testChangeDateFormat(){
		String expectedDate = "2015/1/28 2pm";
		String resultDate = parserDate.changeDateFormat("2015-1-28 2pm");
		assertEquals(expectedDate, resultDate);
	}

	@Test
	public void testChangeDotToColon(){
		String expectedDate = "3 aug 9:30pm";
		String resultDate = parserDate.changeDotToColon("3 aug 9.30pm");
		assertEquals(expectedDate, resultDate);
	}
	
	@Test
	public void testSwitchDateMonth(){
		String expectedDate = "28/1/2015 2pm";
		String resultDate = parserDate.switchDateMonth("1/28/2015 2pm");
		assertEquals(expectedDate, resultDate);
	}
	
	@Test
	public void testSwitchDateMonthFail(){
		String expectedDate = "2018/12/5 2pm";
		String resultDate = parserDate.switchDateMonth("2018/12/5 2pm");
		assertEquals(expectedDate, resultDate);
	}
	
	@Test
	public void testWordForMonth () throws ParserException {
		assertNotNull(parserDate.getDate("3 aug 06:35"));
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidMonth () throws ParserException {
		parserDate.getDate("2018/23/23 3pm");
	}

	@Test (expected = ParserException.class)
	public void testInvalidDay () throws ParserException {
		parserDate.getDate("2018/12/32 3pm");
	}
	
	@Test (expected = ParserException.class)
	public void testInvalidYear () throws ParserException {
		parserDate.getDate("12/12/23 3pm");
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
	public void testInvalidDayOfFeb () throws ParserException {
		parserDate.getDate("2015/2/29 3pm");
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidDayOfMonthWithYear () throws ParserException {
		parserDate.getDate("2015/4/31 3pm");
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidDayOfMonth () throws ParserException {
		parserDate.getDate("31/4 3pm");
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidYearDot () throws ParserException {
		parserDate.getDate("2015.4.31 3pm");
	}	
	
	@Test (expected = ParserException.class)
	public void testInvalidTimeColon () throws ParserException {
		parserDate.getDate("2015/4/5 34:21");
	}	
}

