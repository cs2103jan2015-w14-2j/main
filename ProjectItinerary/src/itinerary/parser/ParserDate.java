package itinerary.parser;

import com.joestelmach.natty.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@author A0114823M
public class ParserDate {
 
 private static final String ERROR_DATE_FORMAT = "Error! Date format error";
 
 public ParserDate(){ 
 }
 
 public Calendar parseDateFromText(String dateString) throws ParserException {
  dateString = changeDateFormat(dateString);
  dateString = switchDateMonth(dateString);
  return parseDateByNatty(dateString);
 }
 
 public String changeDateFormat(String dateString){ 
  return dateString.replaceAll("\\.", "/");
 }
 
 public String switchDateMonth(String dateString){
	 String switchedString = "";
	 String[] textsAroundSlash = dateString.split("/");
	 String temp = textsAroundSlash[0];
	 textsAroundSlash[0] = textsAroundSlash[1];
	 textsAroundSlash[1] = temp;
	 
	 for(String text: textsAroundSlash){
		 switchedString = switchedString + text + "/";
	 }
	 switchedString = switchedString.substring(0, switchedString.length()-1);
	 return switchedString;
 }
 
 public Calendar parseDateByNatty(String dateString) throws ParserException{
  com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
  List<DateGroup> dateGroups = dateParser.parse(dateString);
  if (dateGroups.isEmpty()) {
   throw new ParserException(ERROR_DATE_FORMAT);
  }
  List<Date> dates = dateGroups.get(0).getDates();
  Date date = dates.get(0);
  Calendar calendar = Calendar.getInstance();
  calendar.setTime(date);
  return calendar;
 }
 
 public Calendar convertToCalendar (Date date){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
     return calendar;
 } 
}
