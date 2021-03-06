package itinerary.parser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;

//@author A0114823M
public class ParserDate {

	private static final String ERROR_DATE_FORMAT = "Error! Date format error";
	private static final String ERROR_INVALID_DATE = "Error! The date does not exist";
	private static final String ERROR_INVALID_TIME = "Error! The time does not exist";
	private static final String AFTER_TEN_YEAR = "after 10 years";
	private static final String FUTURE = "future";
	private static final String STRING_A = "a";
	private static final String STRING_ONE = "one";
	private static final String STRING_COLON = ":";
	private static final String STRING_DOT = ".";
	private static final String STRING_DASH = "-";
	private static final String STRING_UNDERSCORE = "_";
	private static final String STRING_SLASH = "/";

	private int[] numOfDaysEachMonth = {0,31,28,31,30,31,30,31,31,30,31,30,31};

	public ParserDate(){ 
	}

	/**
	 * Called by Parser when a DeadlineTask or a ScheduleTask is to be created
	 *
	 * @param  dateString   The input which Parser interpret as date or time
	 * @throws ParserException  If the input format is invalid or
	 * 												the date and time does not exist
	 * @return                       A calendar object which reflects the date and/or time
	 * 										from dateString
	 */
	public Calendar getDate(String dateString) throws ParserException {
		dateString = changeDateFormat(dateString);
		dateString = changeDotToColon(dateString);

		if(!isValidDate(dateString)){
			throw new ParserException(ERROR_DATE_FORMAT);
		}
		if(!isValidDayOfMonth(dateString)){
			throw new ParserException(ERROR_INVALID_DATE);
		}

		if(!isValidTime(dateString)){
			throw new ParserException(ERROR_INVALID_TIME);
		}

		dateString = switchDateMonth(dateString);
		dateString = convertAToOne(dateString);
		return parseDateByNatty(dateString);
	}

	private String changeDotToColon(String dateString){
		dateString = dateString.replaceAll("\\" + STRING_DOT, STRING_COLON);
		return dateString;
	}

	private int countAppearance(String dateString, String string){
		int counter = 0;
		for(int i = 0; i < dateString.length(); i++){
			if(dateString.charAt(i) == string.charAt(0)){
				counter++;
			}
		}
		return counter;
	}

	private String changeDateFormat(String dateString){ 
		String changedFormat = dateString;

		if(countAppearance(dateString, STRING_DASH) > 0){
			changedFormat = dateString.replaceAll( STRING_DASH, STRING_SLASH);
			return changedFormat;
		}
		if(countAppearance(dateString, STRING_UNDERSCORE) > 0){
			changedFormat = dateString.replaceAll( STRING_UNDERSCORE, STRING_SLASH);
			return changedFormat;
		}

		return dateString;
	}

	private String switchDateMonth(String dateString){
		String[] dateWords = convertStringToArray(dateString);

		String date = "";
		for(int i = 0; i < dateWords.length; i++){
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

			if(textsAroundSlash.length  > 1){
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
		}
		return dateString;
	}

	private String convertFutureToTenYear(String dateString){
		if(dateString.equalsIgnoreCase(FUTURE)){
			dateString = AFTER_TEN_YEAR;
		}
		return dateString;
	}

	/**
	 * Every dateString will be passed to this method and parsed by Natty
	 *
	 * @param  dateString   The string after checking invalid format and/or
	 * 										change format when necessary
	 * @throws ParserException  If Natty is unable to parse the dateString
	 * @return                        A calendar object which reflects the date and/or time
	 * 										from dateString
	 */
	private Calendar parseDateByNatty(String dateString) throws ParserException{
		dateString = convertFutureToTenYear(dateString);
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

	private boolean isValidDate (String dateString) throws ParserException{				
		if(countAppearance(dateString, STRING_SLASH) == 2 ){
			return isValidArguments(dateString);
		}
		return true;
	}

	private boolean isValidArguments (String dateString) throws ParserException{
		int valueExceedDay = 0;
		int valueWithinMonth = 0;		
		String[] dateWords = convertStringToArray(dateString);

		for(int i = 0; i < dateWords.length; i++){
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

	private boolean isValidValues (int valueExceedDay, int valueWithinMonth){
		if(valueExceedDay >= 2 || valueExceedDay == 0){
			return false;
		}
		if( valueWithinMonth < 1){
			return false;
		}
		return true;
	}

	private static boolean isLeapYear(int year){
		if (year % 400 == 0){ 
			return true;
		}else if (year % 4 == 0 && year % 100 != 0){ 
			return true;
		}
		return false;
	}

	private boolean isValidDayOfMonth(String dateString) throws ParserException{
		String[] dateWords = convertStringToArray(dateString);

		for(int i = 0; i < dateWords.length; i++){
			if(dateWords[i].indexOf(STRING_SLASH) != -1){
				dateString = dateWords[i];
			}
		}

		if(countAppearance(dateString, STRING_SLASH) == 2 || 
				countAppearance(dateString, STRING_SLASH) == 1	){
			String[] textAroundCharacter = dateString.split(STRING_SLASH);

			if(textAroundCharacter.length  > 1){
				try{
					Integer.parseInt(textAroundCharacter[1]);
					if(Integer.parseInt(textAroundCharacter[1]) >= 13){
						return false;
					}
				}catch(NumberFormatException e){
				}
			}

			int[] monthDay = {0,0};
			for(String text: textAroundCharacter){
				try{
					Integer.parseInt(text);		
					int number = Integer.parseInt(text);		
					number = processYear(number);
					if(number < 32 && number > 12){
						monthDay[1] = number;
					}
					if(number <= 12){
						monthDay[0] = number;
					}		
				}catch(NumberFormatException e){
				}
			}

			if(monthDay[1] != 0){
				int month = monthDay[0];
				if(monthDay[1] > numOfDaysEachMonth[month]){
					return false;
				}
			}
		}
		return true;
	}

	private int  processYear(int year) throws ParserException{
		if(year > 31 && year < 1900){
			throw new ParserException (ERROR_INVALID_DATE );
		}

		if(year > 1900){
			if(isLeapYear(year)){
				numOfDaysEachMonth[2] = 29;
			}
		}
		return year;
	}

	private boolean isValidTime(String dateString){
		String[]dateWords = convertStringToArray(dateString);
		for(String word: dateWords){	
			int colonIndex = word.indexOf(STRING_COLON);
			if(colonIndex != -1){
				word = word.replaceAll("\\" + STRING_COLON, "");
			}
			try{
				int time = Integer.parseInt(word);	
				if( time > 2400 ){
					return false;
				}
			}catch(NumberFormatException e){
			}
		}
		if(countAppearance(dateString, STRING_COLON) > 1){
			return false;
		}
		return true;
	}

	private String convertAToOne(String dateString){
		String[]dateWords = convertStringToArray(dateString);
		String newDateString = "";
		for(int i = 0; i<dateWords.length; i++){
			if(dateWords[i].equalsIgnoreCase(STRING_A)){
				dateWords[i] = STRING_ONE;
			}
			newDateString = newDateString + dateWords[i] + " ";
		}
		return newDateString.trim();
	}

	private static String[] convertStringToArray(String input){
		return input.trim().split("\\s+");
	}
}
