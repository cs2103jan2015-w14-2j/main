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
	private static final String STYLE_BOLD = "-fx-font-weight: bold";
	private static final double PADDING_AMOUNT = 8.0;
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy";
	private static final String TIME_FORMAT = "hh:mm aaa";
	private static final Color COLOR_OVERDUE = Color.RED;
	
	VBox textContainer = new VBox();
	HBox upperText = new HBox();
	HBox lowerText = new HBox();
	
	Label taskIdLabel = new Label();
	Label taskDesLabel = new Label();
	
	Label taskDate = new Label();
	
	Pane spacerPane = new Pane();
	Label taskCatLabel = new Label();
	Image starImage = new Image("itinerary/userinterface/star.png");
	ImageView starImageView = new ImageView();
	
	Task task;
	
	public TaskHBox (Task task) {
		super();
		this.task = task;
		extractNormalTaskDetails(task);
		extractSpecialTaskDetails(task);
		
		HBox.setHgrow(spacerPane, Priority.ALWAYS);
		getChildren().addAll(textContainer, spacerPane, taskCatLabel, starImageView);
		
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
			lowerText.getChildren().addAll(taskDate);
			textContainer.getChildren().add(lowerText);
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
		taskIdLabel.setText(task.getTaskId() + ". ");
		taskIdLabel.setStyle(STYLE_BOLD);
		
		taskDesLabel.setText(task.getText());
		taskDesLabel.setStyle(STYLE_BOLD);
		
		taskCatLabel.setText(task.getCategory());
		if (task.isPriority()) {
			starImageView.setImage(starImage);
		}
		
		upperText.getChildren().addAll(taskIdLabel, taskDesLabel);
		textContainer.getChildren().add(upperText);
	}

	private String formatDates(Task task) {
		String result = "";
		if (task instanceof ScheduleTask) {
			ScheduleTask scheduleTask = (ScheduleTask) task;
			Calendar from = scheduleTask.getFromDate();
			Calendar to = scheduleTask.getToDate();
			
			result += formatCalendarDate(from) + ", ";
			result += formatCalendarTime(from) + " - ";
			
			// Don't need to repeat the date again if it is on the same day
			if (!isSameDay(from, to)) {
				result += formatCalendarDate(to) + ", ";
			}
			result += formatCalendarTime(to);
			return result;
		} else {
			DeadlineTask deadlineTask = (DeadlineTask) task;
			
			result += formatCalendarDate(deadlineTask.getDeadline());
			result += ", ";
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