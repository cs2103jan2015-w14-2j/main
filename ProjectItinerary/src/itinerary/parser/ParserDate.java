package itinerary.parser;

import com.joestelmach.natty.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@author A0114823M
public class ParserDate {

	private static final String ERROR_DATE_FORMAT = "Error! Date format error";
	private static final String ERROR_INVALID_DATE = "Error! The date does not exist";
	private static final String ERROR_INVALID_TIME = "Error! The time does not exist";
	private static final String AFTER_TEN_YEAR = "after 10 years";
	private static final String STRING_A = "a";
	private static final String STRING_ONE = "one";
	private static final String TIME_AM = "am";
	private static final String TIME_A = "a";
	private static final String TIME_PM = "pm";
	private static final String TIME_P = "p";
	private static final String FUTURE = "future";
	private static final String STRING_DOT = ".";
	private static final String STRING_DASH = "-";
	private static final String STRING_UNDERSCORE = "_";
	private static final String STRING_SLASH = "/";

	private int[] numOfDaysEachMonth = {0,31,28,31,30,31,30,31,31,30,31,30,31};

	public ParserDate(){ 
	}

	public Calendar getDate(String dateString) throws ParserException {
		dateString = changeDateFormat(dateString);

		if(!isValidDate(dateString)){
			throw new ParserException(ERROR_DATE_FORMAT);
		}
		if(!isValidDayOfMonth(dateString)){
			throw new ParserException(ERROR_INVALID_DATE);
		}
		if(!isValidAmPm(dateString)){
			throw new ParserException(ERROR_INVALID_TIME);
		}
		if(!isValidTime(dateString)){
			throw new ParserException(ERROR_INVALID_TIME);
		}

		dateString = switchDateMonth(dateString);
		dateString = convertAToOne(dateString);
		System.out.println(dateString);
		return parseDateByNatty(dateString);
	}

	public int countAppearance(String dateString, String string){
		int counter = 0;
		for(int i=0; i < dateString.length(); i++){
			if(dateString.charAt(i) == string.charAt(0)){
				counter++;
			}
		}
		return counter;
	}

	public String changeDateFormat(String dateString){ 
		String changedFormat = dateString;

		if(countAppearance(dateString, STRING_DASH) > 0){
			changedFormat = dateString.replaceAll( STRING_DASH, STRING_SLASH);
			return changedFormat;
		}
		if(countAppearance(dateString, STRING_UNDERSCORE) > 0){
			changedFormat = dateString.replaceAll( STRING_UNDERSCORE, STRING_SLASH);
			return changedFormat;
		}
		if(countAppearance(dateString, STRING_DOT) > 1){
			changedFormat = dateString.replaceAll( STRING_DOT, STRING_SLASH);
			return changedFormat;
		}
		return dateString;
	}

	public String switchDateMonth(String dateString){
		String[] dateWords = stringToArray(dateString);

		String date = "";
		for(int i=0; i < dateWords.length; i++){
			if(dateWords[i].indexOf(STRING_SLASH) != -1){
				date = dateWords[i];
				break;
			}
		}

		if(date.indexOf(STRING_SLASH) != -1){
			String switchedDate = "";
			String switchedString = "";
			String[] textsAroundSlash = {};
			textsAroundSlash = date.split(STRING_SLASH);

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
					switchedDate = switchedDate + text + STRING_SLASH;
				}
				switchedDate = switchedDate.substring(0, switchedDate.length()-1);
				switchedString = dateString.replaceAll(date, switchedDate);
				return switchedString;
			}
		}
		return dateString;
	}

	public String futureToTenYear(String dateString){
		if(dateString.equalsIgnoreCase(FUTURE)){
			dateString = AFTER_TEN_YEAR;
		}
		return dateString;
	}

	public Calendar parseDateByNatty(String dateString) throws ParserException{
		dateString = futureToTenYear(dateString);
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

	public boolean isValidDate (String dateString) throws ParserException{				
		if(countAppearance(dateString, STRING_SLASH) == 2 ){
			return isValidArguments(dateString);
		}
		return true;
	}

	public boolean isValidArguments (String dateString) throws ParserException{
		int valueExceedDay = 0;
		int valueWithinMonth = 0;		
		String[] dateWords = stringToArray(dateString);

		for(int i=0; i < dateWords.length; i++){
			if(dateWords[i].indexOf(STRING_SLASH) != -1){
				dateString = dateWords[i];
			}
		}
		String[] textAroundCharacter = dateString.split(STRING_SLASH);
		if(textAroundCharacter.length > 3){
			return false;
		}

		for(String text: textAroundCharacter){			
			try{
				Integer.parseInt(text);
			}catch(NumberFormatException e){
			}

			if(Integer.parseInt(text) <= 0){
				return false;
			}
			if(Integer.parseInt(text) > 31){
				valueExceedDay++;
			}
			if(Integer.parseInt(text) <= 12){
				valueWithinMonth++;
			}			
		}	
		return isValidValues(valueExceedDay, valueWithinMonth);
	}

	public boolean isValidValues (int valueExceedDay, int valueWithinMonth){
		if(valueExceedDay >= 2 || valueExceedDay == 0){
			return false;
		}
		if( valueWithinMonth < 1){
			return false;
		}
		return true;
	}

	public static boolean isLeapYear(int year){
		if (year % 400 == 0){ 
			return true;
		}
		else if (year % 4 == 0 && year % 100 != 0){ 
			return true;
		}
		return false;
	}

	public boolean isValidDayOfMonth(String dateString) throws ParserException{
		String[] dateWords = stringToArray(dateString);

		for(int i=0; i < dateWords.length; i++){
			if(dateWords[i].indexOf(STRING_SLASH) != -1){
				dateString = dateWords[i];
			}
		}

		if(countAppearance(dateString, STRING_SLASH) == 2){
			String[] textAroundCharacter = dateString.split(STRING_SLASH);
			int[] dayMonth = {0,0};
			for(String text: textAroundCharacter){
				try{
					Integer.parseInt(text);		
					int number = Integer.parseInt(text);		
					number = processYear(number);
					if(number < 32 && number > 12){
						dayMonth[1] = number;
					}
					if(number <= 12){
						dayMonth[0] = number;
					}		
				}catch(NumberFormatException e){
				}
			}

			if(dayMonth[1] != 0){
				int month = dayMonth[0];
				if(dayMonth[1] > numOfDaysEachMonth[month]){
					return false;
				}
			}
		}
		return true;
	}

	public int  processYear(int year) throws ParserException{
		if(year > 32 && year < 100){
			year = year + 2000;
		}

		if(year > 100 && year < 1900){
			throw new ParserException (ERROR_DATE_FORMAT );
		}

		if(year > 1900){
			if(isLeapYear(year)){
				numOfDaysEachMonth[2] = 29;
			}
		}
		return year;
	}

	public boolean isValidAmPm(String dateString){
		String[]dateWords = stringToArray(dateString);
		for(int i=0; i<dateWords.length; i++){
			int amIndex = dateWords[i].indexOf(TIME_AM);
			amIndex = dateWords[i].indexOf(TIME_A);
			int pmIndex = dateWords[i].indexOf(TIME_PM);
			pmIndex = dateWords[i].indexOf(TIME_P);

			String text = "";
			if(amIndex != -1 || pmIndex != -1){
				try{
					if(amIndex != -1){
						text = dateWords[i].substring(0, amIndex);
					}
					if(pmIndex != -1){
						text = dateWords[i].substring(0, pmIndex);
					}
					int time = Integer.parseInt(text);		
					if(time < 0 || time > 12){
						return false;
					}
				}catch(NumberFormatException e){
				}
			}
		}
		return true;
	}

	public boolean isValidTime(String dateString){
		String[]dateWords = stringToArray(dateString);
		for(String word: dateWords){			
			try{
				int time = Integer.parseInt(word);	
				if(word.length() != 2 && word.length() != 4 && word.length() != 6 ){
					return false;
				}
				if( time > 2400 ){
					return false;
				}
			}catch(NumberFormatException e){
			}
		}
		return true;
	}

	public String convertAToOne(String dateString){
		String[]dateWords = stringToArray(dateString);
		String newDateString = "";
		for(int i=0; i<dateWords.length; i++){
			if(dateWords[i].equalsIgnoreCase(STRING_A)){
				dateWords[i] = STRING_ONE;
			}
			newDateString = newDateString + dateWords[i] + " ";
		}
		return newDateString.trim();
	}

	private static String[] stringToArray(String input){
		return input.trim().split("\\s+");
	}
}
