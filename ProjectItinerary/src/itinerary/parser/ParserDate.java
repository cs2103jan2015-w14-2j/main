package itinerary.parser;

import com.joestelmach.natty.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@author A0114823M
public class ParserDate {

	private static final String ERROR_DATE_FORMAT = "Error! Date format error";
	private static final String CHARACTER_DOT = ".";
	private static final String CHARACTER_DASH = "-";
	private static final String CHARACTER_UNDERSCORE = "_";
	private static final String CHARACTER_SLASH = "/";

	public ParserDate(){ 
	}

	public Calendar getDate(String dateString) throws ParserException {
		dateString = changeDateFormat(dateString);
		dateString = switchDateMonth(dateString);
		return parseDateByNatty(dateString);
	}

	public String changeDateFormat(String dateString){ 
		String changedFormat = dateString;
		if(dateString.indexOf(CHARACTER_DOT) != -1){
			changedFormat = dateString.replaceAll("\\" + CHARACTER_DOT, CHARACTER_SLASH);
		}
		if(dateString.indexOf(CHARACTER_DASH) != -1){
			changedFormat = dateString.replaceAll( CHARACTER_DASH, CHARACTER_SLASH);
		}
		if(dateString.indexOf(CHARACTER_UNDERSCORE) != -1){
			changedFormat = dateString.replaceAll( CHARACTER_DASH, CHARACTER_SLASH);
		}
		return changedFormat;
	}

	public String switchDateMonth(String dateString){

		if(dateString.indexOf(CHARACTER_SLASH) != -1){
			String switchedString = "";
			String[] textsAroundSlash = {};
			textsAroundSlash = dateString.split(CHARACTER_SLASH);

			try{
				Integer.parseInt(textsAroundSlash[0]);
			}catch(NumberFormatException e){
				return dateString;
			}

			int firstNumber = Integer.parseInt(textsAroundSlash[0]);
			if(firstNumber < 32){
				String temp = textsAroundSlash[0];
				textsAroundSlash[0] = textsAroundSlash[1];
				textsAroundSlash[1] = temp;
				for(String text: textsAroundSlash){
					switchedString = switchedString + text + CHARACTER_SLASH;
				}
				switchedString = switchedString.substring(0, switchedString.length()-1);
				return switchedString;
			}
		}
		return dateString;
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
