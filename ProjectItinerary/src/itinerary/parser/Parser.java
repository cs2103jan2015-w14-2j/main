package itinerary.parser;

import itinerary.main.DeadlineTask;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author A0114823M
public class Parser {

	private static final String ERROR_SCHEDULE_MISSING_DATE = "Error! Schedule task must have both from and to";
	private static final String ERROR_BOTH_DEADLINE_SCHEDULE = "Error! Invalid input format, cannot be both deadline and schedule";
	private static final String ERROR_DUPLICATE_KEYWORDS = "Error! Duplicate keywords detected";
	private static final String ERROR_NO_DESCRIPTION_CATEGORY = "Error! Please enter description for category";
	private static final String ERROR_NO_DESCRIPTION_BY =  "Error! Please enter date after \"by\"";
	private static final String ERROR_NO_DESCRIPTION_FROM =  "Error! Please enter date after \"from\"";
	private static final String ERROR_NO_DESCRIPTION_TO =  "Error! Please enter date after \"to\"";
	private static final String ERROR_NO_TASK_ID = "Error! Unable to identify target task";
	private static final String ERROR_INVALID_TASK_ID = "Error! Invalid target task id";
	private static final String ERROR_NO_CONTENT_FOR_EDIT = "Error! Please enter contents for edit";
	private static final String ERROR_NO_DESCRIPTION_FOR_ADD = "Error! Please enter description for the task to be added";
	private static final String ERROR_NO_DESCRIPTION_FOR_SEARCH = "Error! Please enter content for search";
	private static final String LOGGER_CHECK_ARGUMENT_VALIDITY = "Checking argument validity";
	private static final String LOGGER_CHECKED_ARGUMENT_VALIDITY = "Finish checking argument validity";
	private static final String LOGGER_CHECK_TASK_ID = "Checking task ID validity";
	private static final String DELETE_CATEGORY = "del";
	private static final String ESCAPE_STRING = "+";
	private static final String COMMAND_ADD_PLUS = "+";
	private static final String COMMAND_HELP_QUESTIONMARK = "?";
	private static final Character ESCAPE_CHARACTER = '+';

	private static final String KEYWORD_SCHEDULE_TO = "to";
	private static final String KEYWORD_SCHEDULE_FROM = "from";
	private static final String KEYWORD_DEADLINE = "by";
	private static final String KEYWORD_PRIORITY = "pri";
	private static final String KEYWORD_CATEGORY = "cat";
	private static final String KEYWORD_COMPLETE = "com";

	private static final String[] KEYWORDS = {KEYWORD_PRIORITY,  KEYWORD_CATEGORY,
		KEYWORD_DEADLINE, KEYWORD_SCHEDULE_FROM, KEYWORD_SCHEDULE_TO, KEYWORD_COMPLETE};

	private static Logger logger = Logger.getGlobal();

	/**
	 * Called by logic when language processing is needed
	 *
	 * @param  input   The sentence input by user
	 * @throws ParserException  If the user input format is invalid
	 * @return             Command object obtained from the input
	 */
	public static Command parseCommand(String input) throws ParserException {
		assert input != null;
		String firstWord = extractFirstWord(input);
		String argument = removeFirstWord(input);
		checkArgumentValidity(argument);
		CommandType commandType = determineCommandType(firstWord);
		Task task = createTask(commandType, argument);
		commandType = convertUnmarkToMark(commandType);
		Command command = new Command(task, commandType);
		return command;
	}

	/**
	 * Create a CommandType object according to the first word of user input
	 *
	 * @param  command   The first word of user input
	 * @exception ParserException  If the commandType in invalid
	 * @return             		 A CommandType object obtained from command
	 */
	private static CommandType determineCommandType(String command){
		command = command.toLowerCase();
		return CommandType.identifyCommandType(command);
	}

	/**
	 * Create a task object according to the CommandType and details of input
	 *
	 * @param  type   		The first word of user input
	 * @param  argument  The remaining words of user input
	 * @exception ParserException  If the details of the task cannot be recognized
	 * @return             		 A task object obtained from argument
	 */
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
		if(type.equals(CommandType.MARK)){
			return createTaskToMark(argument);
		}
		if(type.equals(CommandType.UNMARK)){
			return createTaskToUnmark(argument);
		}	
		if(type.equals(CommandType.HELP)){
			return new Task (-1,null,null,null,null);
		}
		return null;
	}

	private static boolean hasDuplicateKeywords(String[] inputWords){
		for (String keyword: KEYWORDS) {
			int count = countKeywordOccurrences(inputWords, keyword);
			if (count > 1) {
				return true;
			}
		}
		return false;
	}

	private static String replaceKeywordInContent(String argument){
		if (argument.length() == 0){
			return argument;
		}

		String resultString = "";
		String[] words = convertStringToArray(argument);

		for(int i = 0; i < words.length; i++){
			String stringAfterFirstChar = words[i].substring(1);
			String letterString = removeNonLetterChar(stringAfterFirstChar);
			if(words[i].charAt(0) == ESCAPE_CHARACTER && identifyKeyword(letterString) > -1){
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

	/**
	 * Interpreting user input and divide the input into different parts 
	 * according to the keywords
	 * Also determines the type of task
	 *
	 * @param  argument   The input without the first word
	 * @exception ParserException  If unable to extract the contents
	 * @return                     A task with some attributes determined
	 */
	private static Task extractContent(String argument) throws ParserException {
		String[] argumentWords = convertStringToArray(argument);

		String description = extractDescription(argument);
		String category = extractCategory(argument);
		boolean isPriority = hasPriority(argument);
		boolean isComplete = false;

		if(category != null){
			if(category.equals(ESCAPE_STRING + DELETE_CATEGORY)){
				category = category.substring(1, category.length());
			}
		}

		if (isDeadline(argumentWords)) {
			Calendar deadline = extractDeadline(argument);
			return new DeadlineTask(-1, description, category, isPriority, isComplete, deadline);
		} else if (isSchedule(argumentWords)) {
			Calendar fromDate = extractFromDate(argument);
			Calendar toDate = extractToDate(argument);

			if (toDate.compareTo(fromDate) < 0) {
				Calendar tempDate = toDate;
				toDate = fromDate;
				fromDate = tempDate;
			}
			return new ScheduleTask(-1, description, category, isPriority, isComplete, fromDate, toDate);
		} else {
			return new Task(-1, description, category, isPriority, isComplete);
		}
	}

	private static void checkArgumentValidity(String argument) throws ParserException {
		String[] words = convertStringToArray(argument);
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

	/**
	 * Interpreting dateString from the user input
	 * Creates an instance of ParserDate class and parse date and time
	 *
	 * @param  argument   The input after keyword "from" and "to" or "by"
	 * @exception ParserException  If unable to parse the date and time
	 * @return                     A Calendar object which represents the date and time
	 */
	private static Calendar parseDateFromText(String dateString) throws ParserException {
		ParserDate parserDate = new ParserDate();
		Calendar calendar = parserDate.getDate(dateString);
		return calendar;
	}

	private static boolean hasPriority(String arg) {
		String[] words = convertStringToArray(arg);
		return containsKeyword(words, KEYWORDS[0]);
	}

	private static boolean hasComplete(String arg) {
		String[] words = convertStringToArray(arg);
		return containsKeyword(words, KEYWORDS[5]);
	}

	private static String extractAfterKeyword(String arg, String keyword, String error) throws ParserException{
		String[] words = convertStringToArray(arg);
		String[] textsAroundKeyword = {};
		String textAfterKeyword = "";
		int keywordIndex = 0;

		if (!containsKeyword(words, keyword)) {
			return null;
		}

		for(int i = 0; i < words.length; i++){
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
		words = convertStringToArray(textAfterKeyword);
		String textNeeded = removeExtraWords(words, textAfterKeyword);

		if(keyword.equals(KEYWORDS[1]) && textNeeded.equals(DELETE_CATEGORY)){
			return textNeeded;
		}

		if(keyword.equals(KEYWORDS[1]) && textNeeded.equals(ESCAPE_STRING + DELETE_CATEGORY)){
			return textNeeded;
		}
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
		for (String word: words) {
			if (word.equals(keyword)) {
				count++;
			}
		}
		return count;
	}

	private static String extractDescription(String arg) {
		String[] words = convertStringToArray(arg);
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
		String[] arguments = convertStringToArray(argument);
		int id = identifyTargetId(arguments);
		return new Task (id, null, null, null, null);
	}

	private static Task createTaskToEdit(String input) throws ParserException {
		assert input != null;

		int taskId = identifyTargetId(convertStringToArray(input));
		String textAfterIndex= removeFirstWord(input);

		if(textAfterIndex.length() == 0){
			throw new ParserException(ERROR_NO_CONTENT_FOR_EDIT);
		}

		Task task = extractContent(textAfterIndex);
		task.setTaskId(taskId);
		if (task.getText().equals("")) {
			task.setText(null);
		}

		String category = extractCategory(textAfterIndex);
		if(category != null){
			if(category.equals(ESCAPE_CHARACTER + DELETE_CATEGORY)){
				category = category.substring(1, category.length());
			}else if(category.equals(DELETE_CATEGORY)){
				category = "";
			}
		}

		task.setComplete(null);
		task.setCategory(category);
		return task;
	}

	private static Task createTaskToSearch(String input) throws ParserException{
		if(input.equals("") || input == null){
			throw new ParserException(ERROR_NO_DESCRIPTION_FOR_SEARCH);
		}

		Task task = extractContent(input);
		boolean isCompleted = hasComplete(input);

		if(isCompleted){
			task.setComplete(true);
		}
		if (task.getText() != null && task.getText().equals("")) {
			task.setText(null);
		}
		if (task.getCategory() != null &&  task.getCategory().equals("")) {
			task.setCategory(null);
		}
		return task;
	}

	private static Task createTaskToMark(String argument) throws ParserException {
		assert argument != null;	
		String[] arguments = convertStringToArray(argument);
		int id = identifyTargetId(arguments);
		boolean isComplete = true;
		return new Task (id, null, null, null, isComplete);
	}

	private static Task createTaskToUnmark(String argument) throws ParserException {
		assert argument != null;
		String[] arguments = convertStringToArray(argument);		
		int id = identifyTargetId(arguments);
		boolean isComplete = false;
		return new Task (id, null, null, null, isComplete);
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

	private static String[] convertStringToArray(String input){
		return input.trim().split("\\s+");
	}

	private static String extractFirstWord (String input) {
		assert input != null;
		return convertStringToArray(input)[0];
	}

	private static String removeFirstWord (String input) {
		assert input != null;
		String firstWord = extractFirstWord(input);
		if(firstWord.equals(COMMAND_ADD_PLUS)){
			firstWord = "\\" + COMMAND_ADD_PLUS;
		}
		if(firstWord.equals(COMMAND_HELP_QUESTIONMARK)){
			firstWord = "\\" + COMMAND_HELP_QUESTIONMARK;
		}

		return input.replaceFirst(firstWord, "").trim();
	}

	private static CommandType convertUnmarkToMark(CommandType commandType){
		if(commandType.equals(CommandType.UNMARK)){
			commandType = CommandType.MARK;
		}
		return commandType;
	}
}
