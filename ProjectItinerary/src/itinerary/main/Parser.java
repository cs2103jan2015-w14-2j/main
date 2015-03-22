package itinerary.main;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.joestelmach.natty.DateGroup;

//@author A0114823M
public class Parser {

	private static final String ERROR_DATE_FORMAT = "Error! Date format error";
	private static final String ERROR_SCHEDULE_MISSING_DATE = "Error! Schedule task must have both from and to";
	private static final String ERROR_BOTH_DEADLINE_SCHEDULE = "Error! Invalid input format, cannot be both deadline and schedule";
	private static final String ERROR_DUPLICATE_KEYWORDS = "Error! Duplicate keywords detected";
	private static final String ERROR_NO_DESCRIPTION_CATEGORY = "Error! Please enter description for category";
	private static final String ERROR_NO_DESCRIPTION_BY =  "Error! Please enter date after \"by\"";
	private static final String ERROR_NO_DESCRIPTION_FROM =  "Error! Please enter date after \"from\"";
	private static final String ERROR_NO_DESCRIPTION_TO =  "Error! Please enter date after \"to\"";
	private static final String ERROR_NO_TASK_ID = "Error! Unable to identify target task";
	private static final String ERROR_INVALID_TASK_ID = "Error! Invalid target task id";
	private static final String ERROR_NO_CONTENT_FOR_EDIT = "Error! Please enter contenets for edit";
	private static final String ERROR_NO_DESCRIPTION_FOR_ADD = "Error! Please enter description for the task to be added";
	private static final String LOGGER_CHECK_ARGUMENT_VALIDITY = "Checking argument validity";
	private static final String LOGGER_CHECKED_ARGUMENT_VALIDITY = "Finish checking argument validity";
	private static final String LOGGER_CHECK_TASK_ID = "Checking task ID validity";

	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_UNDO = "undo";

	private static final String KEYWORD_SCHEDULE_TO = "to";
	private static final String KEYWORD_SCHEDULE_FROM = "from";
	private static final String KEYWORD_DEADLINE = "by";
	private static final String KEYWORD_PRIORITY = "pri";
	private static final String KEYWORD_CATEGORY = "cat";

	private static final String[] KEYWORDS = {KEYWORD_PRIORITY,  KEYWORD_CATEGORY,
		KEYWORD_DEADLINE, KEYWORD_SCHEDULE_FROM, KEYWORD_SCHEDULE_TO};

	private static Logger logger = Logger.getLogger("Parser");
	private static String errorMessage;

	static{
		try {
			logger.addHandler(new FileHandler(Constants.LOG_FILE));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	//returns a command object and it is called by logic
	public static Command parseCommand(String input) throws ParserException {
		assert input != null;
		String firstWord = extractFirstWord(input);
		String argument = removeFirstWord(input);
		checkArgumentValidity(argument);
		CommandType commandType = determineCommandType(firstWord);
		Task task = createTask(commandType, argument);
		Command command = new Command(task, commandType);
		return command;
	}
	
	public static String getErrorMessage (){
		return errorMessage;
	}

	private static Task createTask(CommandType type, String argument) throws ParserException {
		if(type.equals(CommandType.ADD)){
			return createTaskToAdd(argument);
		}
		if(type.equals(CommandType.DELETE)){
			return createTaskToDelete(argument);
		}
		if(type.equals(CommandType.EDIT)){
			return createTaskToEdit(argument);
		}
		if(type.equals(CommandType.SEARCH)){
			return createTaskToSearch(argument);
		}
		return null;
	}

	private static boolean hasDuplicateKeywords(String[] inputWords){
		for (String keyword : KEYWORDS) {
			int count = countKeywordOccurrences(inputWords, keyword);
			if (count > 1) {
				return true;
			}
		}
		return false;
	}

	public static String replaceKeywordInContent(String argument){
		if (argument.length() == 0){
			return argument;
		}
		
		String resultString = "";
		String[] words = stringToArray(argument);
		for(int i=0; i < words.length; i++){
			String stringAfterFirstChar = words[i].substring(1);
			String letterString = removeNonLetterChar(stringAfterFirstChar);
			if(words[i].charAt(0) == '+' && identifyKeyword(letterString) > -1){
				words[i] = stringAfterFirstChar;
			}
			resultString = resultString + words[i] + " ";
		}
		
		resultString = resultString.substring(0, resultString.length()-1);
		return resultString;
	}

	private static String removeNonLetterChar(String word){
		for(int i = word.length()-1; i >= 0; i--){
			if(!Character.isLetter(word.charAt(i))){
				word = word.substring(0,i);
			}else{
				return word;
			}
		}
		return word;
	}

	private static Task extractContent(String argument) throws ParserException {
		String[] argumentWords = stringToArray(argument);

		String description = extractDescription(argument);
		String category = extractCategory(argument);
		boolean priority = extractPriority(argument);

		if (isDeadline(argumentWords)) {
			Calendar deadline = extractDeadline(argument);
			return new DeadlineTask(-1, description, category, priority, false, deadline);
		} else if (isSchedule(argumentWords)) {
			Calendar fromDate = extractFromDate(argument);
			Calendar toDate = extractToDate(argument);

			if (toDate.compareTo(fromDate) < 0) {
				Calendar tempDate = toDate;
				toDate = fromDate;
				fromDate = tempDate;
			}
			return new ScheduleTask(-1, description, category, priority, false, fromDate, toDate);
		} else {
			return new Task(-1, description, category, priority, false);
		}
	}

	private static void checkArgumentValidity(String argument) throws ParserException {
		String[] words = stringToArray(argument);
		logger.log(Level.INFO, LOGGER_CHECK_ARGUMENT_VALIDITY);
		
		if (hasDuplicateKeywords(words)) {
			logger.log(Level.WARNING, ERROR_DUPLICATE_KEYWORDS);
			throw new ParserException(ERROR_DUPLICATE_KEYWORDS);
		}

		boolean hasDeadline = containsKeyword(words, KEYWORD_DEADLINE);
		boolean hasFrom = containsKeyword(words, KEYWORD_SCHEDULE_FROM);
		boolean hasTo = containsKeyword(words, KEYWORD_SCHEDULE_TO);

		if (hasDeadline && (hasFrom || hasTo)) {
			logger.log(Level.WARNING, ERROR_BOTH_DEADLINE_SCHEDULE);
			throw new ParserException(ERROR_BOTH_DEADLINE_SCHEDULE);
		} else if (hasFrom ^ hasTo) {
			logger.log(Level.WARNING, ERROR_SCHEDULE_MISSING_DATE);
			throw new ParserException(ERROR_SCHEDULE_MISSING_DATE);
		}
		
		logger.log(Level.INFO, LOGGER_CHECKED_ARGUMENT_VALIDITY);
	}

	private static boolean isDeadline(String[] words){
		return containsKeyword(words, KEYWORD_DEADLINE);
	}

	private static boolean isSchedule(String[] words){
		return containsKeyword(words, KEYWORD_SCHEDULE_FROM)
				&& containsKeyword(words, KEYWORD_SCHEDULE_TO);
	}

	private static Calendar extractToDate(String arg) throws ParserException {
		String toDateString =  extractAfterKeyword(arg, KEYWORDS[4], ERROR_NO_DESCRIPTION_TO);
		return parseDateFromText(toDateString);
	}

	private static Calendar extractFromDate(String arg) throws ParserException {
		String fromDateString =  extractAfterKeyword(arg, KEYWORDS[3], ERROR_NO_DESCRIPTION_FROM);
		return parseDateFromText(fromDateString);
	}
	
	private static Calendar extractDeadline(String arg) throws ParserException{
		String deadlineString =  extractAfterKeyword(arg, KEYWORDS[2], ERROR_NO_DESCRIPTION_BY);
		return parseDateFromText(deadlineString);
	}

	private static Calendar parseDateFromText(String dateString) throws ParserException {
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

	private static boolean extractPriority(String arg) {
		String[] words = stringToArray(arg);
		return containsKeyword(words, KEYWORDS[0]);
	}
	
	private static String extractAfterKeyword(String arg, String keyword, String error) throws ParserException{
		String[] words = stringToArray(arg);
		String[] textsAroundKeyword = {};
		String textAfterKeyword = "";
		int keywordIndex = 0;
		
		if (!containsKeyword(words, keyword)) {
			return null;
		}

		for(int i=0; i < words.length; i++){
			if(words[i].equals(keyword)){
				keywordIndex = i;
			}
		}

		if(keywordIndex == words.length-1){
			throw new ParserException(error);
		}

		String nextWord = words[keywordIndex + 1];
		if(identifyKeyword(nextWord) != -1){
			throw new ParserException(error);
		}

		if( !words[0].equals(keyword)){
			textsAroundKeyword = arg.split(" " + keyword + " ");	
			textAfterKeyword = textsAroundKeyword[1].trim();
		}
		if(words[0].equals(keyword)){
			textsAroundKeyword = arg.split(keyword + " ");	
			textAfterKeyword = textsAroundKeyword[1].trim();		
		}
		words = stringToArray(textAfterKeyword);
		String textNeeded = removeExtraWords(words, textAfterKeyword);
		return replaceKeywordInContent(textNeeded).trim();
	}
	
	private static String extractCategory(String arg) throws ParserException {
		return extractAfterKeyword(arg, KEYWORDS[1], ERROR_NO_DESCRIPTION_CATEGORY);
	}
	
	private static String removeExtraWords(String[] words, String text) {
		int nextType = findNextKeywordType(words);
		
		if (nextType != -1) {
			int index = text.indexOf(" " + KEYWORDS[nextType] + " ");
			if (index < 0) {
				index = text.indexOf(" " + KEYWORDS[nextType]);
			}
			if (index < 0) {
				index = text.indexOf(KEYWORDS[nextType] + " ");
			}
			if (index < 0) {
				index = text.indexOf(KEYWORDS[nextType]);
			}
			text = text.substring(0, index);
		}
		
		return text.trim();
	}

	private static boolean containsKeyword(String[] words, String keyword) {
		return countKeywordOccurrences(words, keyword) > 0;
	}

	private static int countKeywordOccurrences(String[] words, String keyword) {
		int count = 0;
		for (String word : words) {
			if (word.equals(keyword)) {
				count++;
			}
		}
		return count;
	}

	private static String extractDescription(String arg) {
		String[] words = stringToArray(arg);
		String description = removeExtraWords(words, arg);
		return  replaceKeywordInContent(description).trim();
	}

	private static int findNextKeywordType(String[] words) {
		for (int i = 0; i < words.length; i++) {
			int identity = identifyKeyword(words[i]);
			if (identity != -1) {
				return identity;
			}
		}
		return -1;
	}

	private static int identifyKeyword (String word) {
		for (int i = 0; i < KEYWORDS.length; i++) {
			if (word.equals(KEYWORDS[i])) {
				return i;
			}
		}
		return -1;
	}

	private static boolean hasDescriptionForAdd(String input){
		if(input.equals("") || input == null){
			return false;
		}
		String firstWord = extractFirstWord(input);
		for(String keyword: KEYWORDS){
			if(firstWord.equals(keyword)){
				return false;
			}
		}
		return true;
	}

	private static Task createTaskToAdd(String input) throws ParserException {
		assert input != null;
		if(!hasDescriptionForAdd(input)){
			throw new ParserException(ERROR_NO_DESCRIPTION_FOR_ADD);
		}
		return extractContent(input);
	}

	private static Task createTaskToDelete(String argument) throws ParserException {
		assert argument != null;
		String[] arguments = stringToArray(argument);
		int id = identifyTargetId(arguments);
		return new Task (id, null, null, false, false);
	}

	private static Task createTaskToEdit(String input) throws ParserException {
		assert input != null;
		int taskId = identifyTargetId(stringToArray(input));
		String textAfterIndex= removeFirstWord(input);

		if(textAfterIndex.length() == 0){
			throw new ParserException(ERROR_NO_CONTENT_FOR_EDIT);
		}

		Task task = extractContent(textAfterIndex);
		task.setTaskId(taskId);
		if (task.getText().equals("")) {
			task.setText(null);
		}
		return task;
	}

	private static int identifyTargetId(String[] arguments) throws ParserException {
		logger.log(Level.INFO, LOGGER_CHECK_TASK_ID);
		if (arguments.length == 0) {
			logger.log(Level.WARNING, ERROR_NO_TASK_ID);
			throw new ParserException(ERROR_NO_TASK_ID);
		}
		try {
			return Integer.parseInt(arguments[0]);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, ERROR_INVALID_TASK_ID);
			throw new ParserException(ERROR_INVALID_TASK_ID);
		}
	}

	private static Task createTaskToSearch(String input){
		return new Task(1, input, "", false, false);
	}

	private static String[] stringToArray(String input){
		return input.trim().split("\\s+");
	}

	private static String extractFirstWord (String input) {
		assert input != null;
		return stringToArray(input)[0];
	}

	private static String removeFirstWord (String input) {
		assert input != null;
		String firstWord = extractFirstWord(input);
		return input.replaceFirst(firstWord, "").trim();
	}

	private static CommandType determineCommandType(String command){
		if(command.equalsIgnoreCase(COMMAND_ADD)){
			return CommandType.ADD;
		}
		else if(command.equalsIgnoreCase(COMMAND_DELETE)){
			return CommandType.DELETE;
		}
		else if(command.equalsIgnoreCase(COMMAND_DISPLAY)){
			return CommandType.DISPLAY;
		}
		else if(command.equalsIgnoreCase(COMMAND_SEARCH)){
			return CommandType.SEARCH;
		}
		else if(command.equalsIgnoreCase(COMMAND_CLEAR)){
			return CommandType.CLEAR;
		}
		else if(command.equalsIgnoreCase(COMMAND_EDIT)){
			return CommandType.EDIT;
		}		
		else if(command.equalsIgnoreCase(COMMAND_UNDO)){
			return CommandType.UNDO;
		}		
		else if(command.equalsIgnoreCase(COMMAND_REDO)){
			return CommandType.REDO;
		}		
		else{
			return CommandType.UNABLE_TO_DETERMINE;
		}
	}
}
