package itinerary.main;

import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BasicPanelTask extends JPanel {

	private static final String DATE_FORMAT = "EEE, dd MMM yyyy, hh:mm aaa";
	private Task task;
	/**
	 * Create the panel.
	 */
	public BasicPanelTask(Task task) {
		setBackground(SystemColor.text);
		this.task = task;
		this.setOpaque(true);
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblTaskId = new JLabel("Task Id");
		add(lblTaskId);
		
		JLabel lblTaskDescription = new JLabel("TaskDescription");
		add(lblTaskDescription);
		
		String priorityStar = this.task.isPriority() ? "*" : "";
		lblTaskId.setText(priorityStar + Integer.toString(this.task.getTaskId()) + ".");
		lblTaskDescription.setText(this.task.getText());
		
		JLabel lblFromDate = new JLabel("");
		JLabel lblToDate = new JLabel("");
		if (this.task instanceof DeadlineTask) {
			DeadlineTask deadlineTask = (DeadlineTask) this.task;
			Calendar deadline = deadlineTask.getDeadline();
			lblFromDate.setText("Due: " + formatCalendarDate(deadline));
		} else if (this.task instanceof ScheduleTask) {
			ScheduleTask scheduleTask = (ScheduleTask) this.task;
			Calendar fromDate = scheduleTask.getFromDate();
			Calendar toDate = scheduleTask.getToDate();
			lblFromDate.setText("From: " + formatCalendarDate(fromDate));
			lblToDate.setText("To: " + formatCalendarDate(toDate));
		}

		add(lblFromDate);
		add(lblToDate);
	}
	
	private static String formatCalendarDate(Calendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(date.getTime());
	}
}
