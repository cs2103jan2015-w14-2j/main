package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import itinerary.parser.ParserDate;
import itinerary.parser.ParserException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
	public void testValidDayOfMonthWithYear () throws ParserException {
		String dateString = "2012/3/31 2pm";
		assertNotNull(parserDate.getDate(dateString));
	}	

	@Test
	public void testValidDayOfMonth () throws ParserException {
		String dateString = "3/31 2pm";
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
	public void testChangeDateFormat() throws NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException,
	InvocationTargetException, ClassNotFoundException, 
	InstantiationException{		
		String input = "2015-1-28 2pm";	
		String expected = "2015/1/28 2pm";
		Class<?> parserDate = Class.forName("itinerary.parser.ParserDate");
		Object obj = parserDate.newInstance();
		Class<?>[] paramTypes = new Class[1];
		paramTypes[0]=String.class;
		Method method = parserDate.getDeclaredMethod("changeDateFormat", paramTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(obj, input);	
		assertEquals(expected, result);
	}

	@Test
	public void testChangeDotToColon() throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException, NoSuchMethodException, 
	SecurityException, IllegalArgumentException, 
	InvocationTargetException{
		String input = "3 aug 9.30pm";	
		String expected =  "3 aug 9:30pm";
		Class<?> parserDate = Class.forName("itinerary.parser.ParserDate");
		Object obj = parserDate.newInstance();
		Class<?>[] paramTypes = new Class[1];
		paramTypes[0]=String.class;
		Method method = parserDate.getDeclaredMethod("changeDotToColon", paramTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(obj, input);	
		assertEquals(expected, result);
	}

	@Test
	public void testSwitchDateMonth() throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException, NoSuchMethodException, 
	SecurityException, IllegalArgumentException, 
	InvocationTargetException{
		String input = "1/28/2015 2pm";	
		String expected =  "28/1/2015 2pm";
		Class<?> parserDate = Class.forName("itinerary.parser.ParserDate");
		Object obj = parserDate.newInstance();
		Class<?>[] paramTypes = new Class[1];
		paramTypes[0]=String.class;
		Method method = parserDate.getDeclaredMethod("switchDateMonth", paramTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(obj, input);	
		assertEquals(expected, result);
	}

	@Test
	public void testSwitchDateMonthFail() throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException, NoSuchMethodException, 
	SecurityException, IllegalArgumentException, 
	InvocationTargetException{		
		String input = "2018/12/5 2pm";	
		String expected =  "2018/12/5 2pm";
		Class<?> parserDate = Class.forName("itinerary.parser.ParserDate");
		Object obj = parserDate.newInstance();
		Class<?>[] paramTypes = new Class[1];
		paramTypes[0]=String.class;
		Method method = parserDate.getDeclaredMethod("switchDateMonth", paramTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(obj, input);	
		assertEquals(expected, result);
	}

	@Test
	public void testWordForMonth () throws ParserException {
		assertNotNull(parserDate.getDate("3 aug 06:35"));
	}	

	/* This is a boundary case for when the month is >12*/
	@Test (expected = ParserException.class)
	public void testInvalidMonth () throws ParserException {
		parserDate.getDate("2018/23/23 3pm");
	}

	/* This is a boundary case for when the day is > 32*/
	@Test (expected = ParserException.class)
	public void testInvalidDay () throws ParserException {
		parserDate.getDate("2018/12/32 3pm");
	}

	/* This is a boundary case for when the year is invalid */
	@Test (expected = ParserException.class)
	public void testInvalidYear () throws ParserException {
		parserDate.getDate("12/12/23 3pm");
	}

	/* This is a boundary case for when the number before am is >12 */
	@Test (expected = ParserException.class)
	public void testInvalidAm () throws ParserException {
		parserDate.getDate("2015/1/2 13am");
	}	

	/* This is a boundary case for when the number before p is >12 */
	@Test (expected = ParserException.class)
	public void testInvalidP () throws ParserException {
		parserDate.getDate("2015/1/2 23p");
	}	

	/* This is a boundary case for when there is 29th for February for non-leap year */
	@Test (expected = ParserException.class)
	public void testInvalidDayOfFeb () throws ParserException {
		parserDate.getDate("2015/2/29 3pm");
	}	

	/* This is a boundary case for when the day is > maximum day of that month */
	@Test (expected = ParserException.class)
	public void testInvalidDayOfMonthWithYear () throws ParserException {
		parserDate.getDate("2015/4/31 3pm");
	}	

	/* This is a boundary case for when the day is > maximum day of that month */
	@Test (expected = ParserException.class)
	public void testInvalidDayOfMonth () throws ParserException {
		parserDate.getDate("31/4 3pm");
	}	

	/* This is a boundary case for when YYYY DD MM is input with dot */
	@Test (expected = ParserException.class)
	public void testInvalidYearDot () throws ParserException {
		parserDate.getDate("2015.4.31 3pm");
	}	

	/* This is a boundary case for when HH:MM is beyond 24:00 */
	@Test (expected = ParserException.class)
	public void testInvalidTimeColon () throws ParserException {
		parserDate.getDate("2015/4/5 34:21");
	}	
}

