package itinerary.main;

import com.joestelmach.natty.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@author A0114823M
public class ParserDate {
	
	public ParserDate(){	
	}
	
	public Task getTask(String input){
			com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
			List<DateGroup> groups = parser.parse(input);
			
			List<Date> dates = groups.get(0).getDates();		
			
			return determineTaskType(dates);
		}
	
	public Task determineTaskType (List<Date> dates){
		if(dates.size() == 1){
			Calendar endDate = convertToCalendar(dates.get(0));
			return new DeadlineTask(-1, "", "", false,false, endDate);
		}
		else{
		    Calendar startDate = convertToCalendar(dates.get(0));
		    Calendar endDate = convertToCalendar(dates.get(1));
			return new ScheduleTask(-1, "", "", false, false, startDate, endDate);
		}
	}
	
	public Calendar convertToCalendar (Date date){
			  Calendar calendar = Calendar.getInstance();
			  calendar.setTime(date);
			  calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			  return calendar;
	}	
}
