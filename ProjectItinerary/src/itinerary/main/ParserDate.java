package itinerary.main;

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
  return parseDateByNatty(dateString);
 }
 
 public String changeDateFormat(String dateString){ 
  return dateString.replaceAll("\\.", "/");
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
