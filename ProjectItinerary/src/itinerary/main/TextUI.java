package itinerary.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

//@author A0121437N
public class TextUI {

	private static final String DATE_FORMAT = "EEE, dd MMM yyyy, hh:mm aaa";
	private static final String EXIT_COMMAND = "exit";
	private static final String HEADER_BORDER = "###################";
	private static final String HEADER_CONTENT = "#Project Itinerary#";
	private static final String HEADER = HEADER_BORDER + System.lineSeparator()
			+ HEADER_CONTENT + System.lineSeparator() + HEADER_BORDER;
	private static final String CONSOLE = "Console: %1$s";
	private static final String REQUEST_FILE = "File name: ";
	private static final String REQUEST_COMMAND = "Command: ";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String fileName = requestFileName(scanner);
		Logic logic = new Logic(fileName);
		executeUntilExit(scanner, logic);
	}

	private static void executeUntilExit(Scanner scanner, Logic logic) {
		UserInterfaceContent content = logic.initialLaunch();
		String userInput;
		do {
			clearConsole();
			printSequence(content);
			userInput = requestUserInput(scanner);
			content = logic.executeUserInput(userInput);
		} while (!userInput.equals(EXIT_COMMAND));
	}

	private static String requestUserInput(Scanner scanner) {
		System.out.print(REQUEST_COMMAND);
		return scanner.nextLine();
	}

	private static String requestFileName(Scanner scanner) {
		System.out.print(REQUEST_FILE);
		String fileName = scanner.nextLine();
		return fileName;
	}

	private static void printSequence(UserInterfaceContent content) {
		printHeader();
		printTasks(content.getTasks());
		printConsoleMessage(content.getConsoleMessage());
	}

	private static void printHeader() {
		System.out.println(HEADER);
	}
	
	private static void printTasks(List<Task> tasks) {
		for (Task task : tasks) {
			printTask(task);
		}
	}

	private static void printConsoleMessage(String consoleMessage) {
		String printedMessage = String.format(CONSOLE, consoleMessage);
		System.out.println(printedMessage);
	}

	private static void printTask(Task task) {
		String taskDetails = formatTaskDetails(task);
		if (task instanceof ScheduleTask) {
			taskDetails += formatScheduleTaskDetails((ScheduleTask) task);
		} else if (task instanceof DeadlineTask) {
			taskDetails += formatDeadlineTaskDetails((DeadlineTask) task);
		}
		System.out.println(taskDetails);
	}

	private static String formatTaskDetails(Task task) {
		String details = "";
		details += task.isPriority() ? "*" : "";
		details += task.getLineNumber() + ".\t";
		details += task.getText() + "\t";
		details += (task.isComplete() ? "Complete" : "Incomplete") + "\t";
		return details;
	}

	private static String formatScheduleTaskDetails(ScheduleTask task) {
		String details = "from: ";
		details += formatCalendarDate(task.getFromDate());
		details += "\tto: ";
		details += formatCalendarDate(task.getToDate());
		return details;
	}

	private static String formatDeadlineTaskDetails(DeadlineTask task) {
		String details = "due: ";
		details += formatCalendarDate(task.getDeadline());
		return details;
	}

	private static String formatCalendarDate(Calendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(date.getTime());
	}
	
	private static void clearConsole () {
		final String operatingSystem = System.getProperty("os.name");
		if (operatingSystem.contains("Windows")) {
			try {
				Runtime.getRuntime().exec("cls");
			} catch (IOException e) {
				System.out.println("an error occurred when trying to clear console");
			}
		} else {
			try {
				Runtime.getRuntime().exec("clear");
			} catch (IOException e) {
				System.out.println("an error occurred when trying to clear console");
			}
		}
	}
}
