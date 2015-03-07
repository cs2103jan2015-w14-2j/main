package itinerary.test;

import static org.junit.Assert.*;
import itinerary.main.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

import org.junit.Test;

public class ParserDateTest {

	ParserDate parserDate = new ParserDate();

	@Test
	public void testConvertToCalendar() {
		@SuppressWarnings("deprecation") 
		Date date = new Date(115, 2, 5, 18, 50); // year = the year minus 1900
		//System.out.println(date.toString());
		Calendar calendar = parserDate.convertToCalendar(date);
		//System.out.println(calendar.toString());
		assertEquals(2015, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(5, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(18, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(50, calendar.get(Calendar.MINUTE));
		assertEquals(1, calendar.get(Calendar.DAY_OF_WEEK)); //Sunday is the first day of the week according to java Calendar API
	}

	boolean calendarEquals(Calendar calendarOne, Calendar calendarTwo){
		if(calendarOne.get(Calendar.YEAR) == calendarTwo.get(Calendar.YEAR) &&
				calendarOne.get(Calendar.MONTH) == calendarTwo.get(Calendar.MONTH)  &&
				calendarOne.get(Calendar.DAY_OF_MONTH) == calendarTwo.get(Calendar.DAY_OF_MONTH) &&
				calendarOne.get(Calendar.HOUR_OF_DAY) == calendarTwo.get(Calendar.HOUR_OF_DAY) &&
				calendarOne.get(Calendar.MINUTE) == calendarTwo.get(Calendar.MINUTE)){
			return true;
		}
		else{
			return false;
		}
	}

	@Test
	public void testDetermineTaskType() {

		List<Date> deadlineDate = new ArrayList<Date>();
		List<Date> scheduleDates = new ArrayList<Date>();

		@SuppressWarnings("deprecation")
		Date dateOne = new Date(115, 2, 5, 18, 50); // year = the year minus 1900
		@SuppressWarnings("deprecation")
		Date dateTwo = new Date(115, 2, 6, 19, 50);

		deadlineDate.add(dateOne);
		scheduleDates.add(dateOne);
		scheduleDates.add(dateTwo);

		Task taskOne = parserDate.determineTaskType(deadlineDate);
		Task taskTwo = parserDate.determineTaskType(scheduleDates);

		DeadlineTask expectedTaskOne = new DeadlineTask (-1, null, null, false,false, parserDate.convertToCalendar(dateOne));
		ScheduleTask expectedTaskTwo = new ScheduleTask (-1, null, null, false,false, parserDate.convertToCalendar(dateOne),
				parserDate.convertToCalendar(dateTwo));
		assertEquals(expectedTaskOne, taskOne);
		assertEquals(expectedTaskTwo, taskTwo);
	}

	public void testGetTask() {

        String inputTimeOne = "2015/3/5 1850";
        String inputTimeTwo = "2015/3/5 1850 to 2015/3/6 1950";
        @SuppressWarnings("deprecation")
		Date dateOne = new Date(115, 2, 5, 18, 50);
        Task taskOne = parserDate.getTask(inputTimeOne);
        DeadlineTask expectedTaskOne = new DeadlineTask (-1, null, null, false,false, parserDate.convertToCalendar(dateOne));
        assertEquals(expectedTaskOne, taskOne);

		@SuppressWarnings("deprecation")
		Date dateTwo = new Date(115, 2, 6, 19, 50);
		Task taskTwo = parserDate.getTask(inputTimeTwo);
		 ScheduleTask expectedTaskTwo = new ScheduleTask (-1, null, null, false,false, parserDate.convertToCalendar(dateOne),
                                                                                                    parserDate.convertToCalendar(dateTwo));
		assertEquals(expectedTaskTwo, taskTwo);
	}
}

