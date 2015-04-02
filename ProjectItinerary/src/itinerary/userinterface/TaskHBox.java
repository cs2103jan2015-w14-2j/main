package itinerary.userinterface;

import itinerary.main.DeadlineTask;
import itinerary.main.ScheduleTask;
import itinerary.main.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

//@author A0121437N
public class TaskHBox extends HBox {
	private static final String PACKAGE_PATH = "itinerary/userinterface/";
	private static final String COMPLETE_IMAGE_PATH = PACKAGE_PATH + "complete.png";
	private static final String STAR_IMAGE_PATH = PACKAGE_PATH + "star.png";
	private static final String DATE_SEPARTATOR = " - ";
	private static final String DAY_DATE_SEPARATOR = ", ";
	private static final String DOT_AFTER_ID = ". ";
	
	private static final double MIN_SPACER_WIDTH = 10.0;
	private static final double MIN_IMAGE_PANE_WIDTH = 25.0;
	private static final double PADDING_AMOUNT = 8.0;
	
	private static final String STYLE_BOLD = "-fx-font-weight: bold";
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy";
	private static final String TIME_FORMAT = "hh:mm aaa";
	
	private static final Color COLOR_OVERDUE = Color.RED;
	private static final Image starImage = new Image(STAR_IMAGE_PATH);
	private static final Image completeImage = new Image(COMPLETE_IMAGE_PATH);
	
	VBox textContainer = new VBox();
	HBox upperLayer = new HBox();
	HBox lowerLayer = new HBox();
	
	// Upper layer
	Label taskIdLabel = new Label();
	Label taskDesLabel = new Label();
	Label taskDate = new Label();
	
	Pane spacerPane = new Pane();
	Label taskCatLabel = new Label();
	Pane catImageSpacer = new Pane();
	Pane priorityPane = new Pane();
	ImageView starImageView = new ImageView();
	Pane completePane = new Pane();
	ImageView completeImageView = new ImageView();
	
	Task task;
	
	public TaskHBox (Task task) {
		super();
		this.task = task;
		extractNormalTaskDetails(task);
		extractSpecialTaskDetails(task);

		HBox.setHgrow(spacerPane, Priority.ALWAYS);
		spacerPane.setMinWidth(MIN_SPACER_WIDTH);
		catImageSpacer.setMinWidth(MIN_SPACER_WIDTH);
		
		priorityPane.getChildren().add(starImageView);
		priorityPane.setMinWidth(MIN_IMAGE_PANE_WIDTH);
		
		completePane.getChildren().add(completeImageView);
		completePane.setMinWidth(MIN_IMAGE_PANE_WIDTH);
		
		getChildren().addAll(textContainer, spacerPane, taskCatLabel, catImageSpacer, priorityPane, completePane);
		
		// Set padding around HBox for visual appeal
		setPadding(new Insets(PADDING_AMOUNT));
	}

	private void extractSpecialTaskDetails(Task task) {
		if (task instanceof ScheduleTask || task instanceof DeadlineTask) {
			taskDate.setText(formatDates(task));
			if (task instanceof DeadlineTask) {
				DeadlineTask deadlineTask = (DeadlineTask) task;
				setOverdueColor(deadlineTask);
			}
			lowerLayer.getChildren().addAll(taskDate);
			textContainer.getChildren().add(lowerLayer);
		}
	}

	private void setOverdueColor(DeadlineTask deadlineTask) {
		Calendar deadline = deadlineTask.getDeadline();
		Calendar now = Calendar.getInstance();
		if (deadline.compareTo(now) < 0) {
			taskDate.setTextFill(COLOR_OVERDUE);
		}
	}

	private void extractNormalTaskDetails(Task task) {
		taskIdLabel.setText(task.getTaskId() + DOT_AFTER_ID);
		taskIdLabel.setStyle(STYLE_BOLD);
		
		taskDesLabel.setText(task.getText());
		taskDesLabel.setStyle(STYLE_BOLD);
		
		taskCatLabel.setText(task.getCategory());
		
		if (task.isPriority()) {
			starImageView.setImage(starImage);
		}
		
		if (task.isComplete()) {
			completeImageView.setImage(completeImage);
		}
		
		upperLayer.getChildren().addAll(taskIdLabel, taskDesLabel);
		textContainer.getChildren().add(upperLayer);
	}

	private String formatDates(Task task) {
		String result = "";
		if (task instanceof ScheduleTask) {
			ScheduleTask scheduleTask = (ScheduleTask) task;
			Calendar from = scheduleTask.getFromDate();
			Calendar to = scheduleTask.getToDate();
			
			result += formatCalendarDate(from) + DAY_DATE_SEPARATOR;
			result += formatCalendarTime(from) + DATE_SEPARTATOR;
			
			// Don't need to repeat the date again if it is on the same day
			if (!isSameDay(from, to)) {
				result += formatCalendarDate(to) + DAY_DATE_SEPARATOR;
			}
			result += formatCalendarTime(to);
			return result;
		} else {
			DeadlineTask deadlineTask = (DeadlineTask) task;
			
			result += formatCalendarDate(deadlineTask.getDeadline());
			result += DAY_DATE_SEPARATOR;
			result += formatCalendarTime(deadlineTask.getDeadline());
			return result;
		}
	}
	
	private static String formatCalendarDate (Calendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(date.getTime());
	}
	
	private static String formatCalendarTime (Calendar time) {
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
		return timeFormat.format(time.getTime());
	}
	
	private static boolean isSameDay (Calendar firstDay, Calendar secondDay) {
		boolean sameYear = firstDay.get(Calendar.YEAR) == secondDay.get(Calendar.YEAR);
		boolean sameDate = firstDay.get(Calendar.DAY_OF_YEAR) == secondDay.get(Calendar.DAY_OF_YEAR);
		return sameYear && sameDate;
	}
}