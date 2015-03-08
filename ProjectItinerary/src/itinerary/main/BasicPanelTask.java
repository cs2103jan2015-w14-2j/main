package itinerary.main;

import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

public class BasicPanelTask extends JPanel {

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
		
		lblTaskId.setText(Integer.toString(this.task.getTaskId()) + ".");
		lblTaskDescription.setText(this.task.getText());
	}

}
