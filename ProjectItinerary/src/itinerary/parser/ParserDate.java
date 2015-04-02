package itinerary.parser;

import com.joestelmach.natty.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@author A0114823M
public class ParserDate {

	private static final String ERROR_DATE_FORMAT = "Error! Date format error";
	private static final String AFTER_TEN_YEAR = "after 10 years";
	private static final String FUTURE = "future";
	private static final String CHARACTER_DOT = ".";
	private static final String CHARACTER_DASH = "-";
	private static final String CHARACTER_UNDERSCORE = "_";
	private static final String CHARACTER_SLASH = "/";

	public ParserDate(){ 
	}

	public Calendar getDate(String dateString) throws ParserException {
		dateString = changeDateFormat(dateString);
		if(!isValidDate(dateString)){
			throw new ParserException(ERROR_DATE_FORMAT);
		}
		dateString = switchDateMonth(dateString);
		return parseDateByNatty(dateString);
	}

	public String changeDateFormat(String dateString){ 
		String changedFormat = dateString;

		if(dateString.indexOf(CHARACTER_DASH) != -1){
			changedFormat = dateString.replaceAll( CHARACTER_DASH, CHARACTER_SLASH);
			return changedFormat;
		}
		if(dateString.indexOf(CHARACTER_UNDERSCORE) != -1){
			changedFormat = dateString.replaceAll( CHARACTER_UNDERSCORE, CHARACTER_SLASH);
			return changedFormat;
		}
		return dateString;
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

	public String futureToOneYear(String dateString){
		if(dateString.equalsIgnoreCase(FUTURE)){
			dateString = AFTER_TEN_YEAR;
		}
		return dateString;
	}
	
	public Calendar parseDateByNatty(String dateString) throws ParserException{
		dateString = futureToOneYear(dateString);
		System.out.println(dateString);
		com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
		List<DateGroup> dateGroups = dateParser.parse(dateString);

		if (dateGroups.isEmpty()) {
			System.out.println("problem.....");
			throw new ParserException(ERROR_DATE_FORMAT);
		}

		List<Date> dates = dateGroups.get(0).getDates();
		Date date = dates.get(0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public boolean isValidDate (String dateString) throws ParserException{				
		if(dateString.indexOf(CHARACTER_SLASH) != -1){
			return isValidArguments(dateString);
		}
		return true;
	}

	public boolean isValidArguments (String dateString) throws ParserException{
		int valueExceedDay = 0;
		int valueWithinMonth = 0;		
		String[] dateWords = stringToArray(dateString);
		
		for(int i=0; i < dateWords.length; i++){
			if(dateWords[i].indexOf(CHARACTER_SLASH) != -1){
				dateString = dateWords[i];
			}
		}
		String[] textAroundCharacter = dateString.split(CHARACTER_SLASH);
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

	private static String[] stringToArray(String input){
		return input.trim().split("\\s+");
	}
}
