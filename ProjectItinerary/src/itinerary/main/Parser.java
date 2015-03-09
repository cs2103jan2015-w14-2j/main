package itinerary.main;

import java.util.Calendar;
import java.util.Date;

//@author A0114823M
public class Parser {

	private static final String DUPLICATED_KEYWORD_PRI = "duplicated keyword \"pri\" !";
	private static final String DUPLICATED_KEYWORD_CA = "duplicated keyword \"ca\" !";
	private static final String DUPLICATED_KEYWORD_TIME = "duplicated keyword for time (\"by\" or \"ti\") !";
	private static final String INVALID_INDEX = "Invalid index ";
	private static final String NO_DESCRIPTION_FOR_EDIT = "Please key in the new eescriptions "
			+ "and/or \"ca\" for setting category and/or \" pri\" for setting importance level !";
	private static final String NOTHING_AFTER_COMMANDTYPE = "Please key in descriptions!";
	private static final String INVALID_INPUT_FORMAT = "Invalid input format ";
	private static final String INVALID_DATE_TIME = "Invalid date";
	private static final String ERROR_MESSAGE = "Your command is not executed due to: %1$s.";
	private static final String[] KEYWORD = {"pri",  "ca", "by", "from", "to"};

	//returns a command object and it is called by logic
	public static Command getCommand(String input) throws ParserException {
		String firstWord = extractFirstWord(input);
		String argument = removeFirstWord(input);
		CommandType commandType = ParserCommand.determineType(firstWord);
		Task task = createTask(commandType, argument);
		Command command = new Command(task, commandType);
		return command;
	}

	public static Task createTask(CommandType type, String argument) throws ParserException {
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

	public static boolean hasDuplicatedKeywords(String[] inputWords){
		int[] keyWordCounter = {0,0,0};	

		for(int i=0; i < inputWords.length; i++){
			if(inputWords[i].equals(KEYWORD[0])){
				keyWordCounter[0]++;
			}
			if(inputWords[i].equals(KEYWORD[1])){
				keyWordCounter[1]++;
			}
			if(inputWords[i].equals(KEYWORD[2]) || inputWords[i].equals(KEYWORD[3])){
				keyWordCounter[2]++;
			}
		}

		for(int i=0; i < keyWordCounter.length; i++){
			if(keyWordCounter[0] > 1){
				//showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_PRI);
				return true;
			}
			if(keyWordCounter[1] > 1){
				//showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_CA);
				return true;
			}
			if(keyWordCounter[2] > 1){
				//showMessage = String.format(ERROR_MESSAGE, DUPLICATED_KEYWORD_TIME);
				return true;
			}
		}
		return false;
	}

	private static Task extractContent(String arg) throws ParserException {
		String[] words = stringToArray(arg);
		if (hasDuplicatedKeywords(words)) {
			throw new ParserException("Duplicate keywords detected");
		}
		String description = extractDescription(arg);
		String category = extractCategory(arg);
		boolean priority = extractPriority(arg);
		
		if (containsKeyword(words, KEYWORD[2])) {
			if (!(containsKeyword(words, KEYWORD[3]) || containsKeyword(words, KEYWORD[4]))) {
				// Deadline task
				Calendar deadline = extractDeadline(arg);
				return new DeadlineTask(-1, description, category, priority, false, deadline);
			} else {
				throw new ParserException("Invalid input format, cannot be both deadline and schedule");
			}
		} else if (containsKeyword(words, KEYWORD[3]) && containsKeyword(words, KEYWORD[4])) {
			// schedule task
			Calendar fromDate = extractFromDate(arg);
			Calendar toDate = extractToDate(arg);
			if (toDate.compareTo(fromDate) < 0) {
				throw new ParserException("Error! To date must be after from date");
			}
			return new ScheduleTask(-1, description, category, priority, false, fromDate, toDate);
		} else if (containsKeyword(words, KEYWORD[3]) || containsKeyword(words, KEYWORD[4])) {
			throw new ParserException("Invalid input format, schedule task must have both from and to");
		}
		return new Task(-1, description, category, priority, false);
	}
	
	private static Calendar extractToDate(String arg) {
		String textAfterKeyword = arg.split(" " + KEYWORD[4] + " ")[1];
		String[] words = stringToArray(textAfterKeyword);
		String toString = removeExtraWords(words, textAfterKeyword);
		return parseDateFromText(toString);
	}

	private static Calendar extractFromDate(String arg) {
		String textAfterKeyword = arg.split(" " + KEYWORD[3] + " ")[1];
		String[] words = stringToArray(textAfterKeyword);
		String fromString = removeExtraWords(words, textAfterKeyword);
		return parseDateFromText(fromString);
	}

	private static Calendar extractDeadline(String arg) {
		String textAfterKeyword = arg.split(" " + KEYWORD[2] + " ")[1];
		String[] words = stringToArray(textAfterKeyword);
		String deadlineString = removeExtraWords(words, textAfterKeyword);
		return parseDateFromText(deadlineString);
	}

	private static Calendar parseDateFromText(String dateString) {
		com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
		Date date = dateParser.parse(dateString).get(0).getDates().get(0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	private static boolean extractPriority(String arg) {
		String[] words = stringToArray(arg);
		return containsKeyword(words, KEYWORD[0]);
	}

	private static String extractCategory(String arg) {
		String[] words = stringToArray(arg);
		if (!containsKeyword(words, KEYWORD[1])) {
			return null;
		}
		String textAfterKeyword = arg.split(KEYWORD[1])[1].trim();
		words = stringToArray(textAfterKeyword);
		return removeExtraWords(words, textAfterKeyword);
	}

	private static String removeExtraWords(String[] words, String text) {
		int nextType = findNextKeywordType(words);
		if (nextType != -1) {
			int index = text.indexOf(" " + KEYWORD[nextType] + " ");
			if (index < 0) {
				index = text.indexOf(" " + KEYWORD[nextType]);
			}
			if (index < 0) {
				index = text.indexOf(KEYWORD[nextType] + " ");
			}
			text = text.substring(0, index);
		}
		return text.trim();
	}

	private static boolean containsKeyword(String[] words, String string) {
		for (String word : words) {
			if (word.equals(string)) {
				return true;
			}
		}
		return false;
	}

	private static String extractDescription(String arg) {
		String[] words = stringToArray(arg);
		return removeExtraWords(words, arg);
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
		for (int i = 0; i < KEYWORD.length; i++) {
			if (word.equals(KEYWORD[i])) {
				return i;
			}
		}
		return -1;
	}

	private static Task createTaskToAdd(String input) throws ParserException {
		return extractContent(input);
	}

	private static Task createTaskToDelete(String argument) throws ParserException {
		String[] arguments = stringToArray(argument);
		int id = identifyTargetId(arguments);
		return new Task (id, null, null, false, false);
	}

	private static Task createTaskToEdit(String input) throws ParserException {
		int taskId = identifyTargetId(stringToArray(input));
		Task task = extractContent(removeFirstWord(input));
		task.setTaskId(taskId);
		if (task.getText().equals("")) {
			task.setText(null);
		}
		return task;
	}

	private static int identifyTargetId(String[] arguments) throws ParserException {
		if (arguments.length == 0) {
			throw new ParserException("Unable to identify target task");
		}
		try {
			return Integer.parseInt(arguments[0]);
		} catch (NumberFormatException e) {
			throw new ParserException("Invalid target task id");
		}
	}

	private static Task createTaskToSearch(String input){
		return new Task(1, input, "", false, false);
	}

	private static String[] stringToArray(String input){
		return input.trim().split("\\s+");
	}
	
	private static String extractFirstWord (String input) {
		return stringToArray(input)[0];
	}

	private static String removeFirstWord (String input) {
		return input.replaceFirst(extractFirstWord(input), "").trim();
	}
}
