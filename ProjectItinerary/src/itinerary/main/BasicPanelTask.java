package itinerary.main;

import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class BasicPanelTask extends JPanel {

	private Task task;
	/**
	 * Create the panel.
	 */
	public BasicPanelTask(Task task) {
		setBackground(SystemColor.text);
		this.task = task;
		this.setOpaque(true);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTaskId = new JLabel("Task Id");
		GridBagConstraints gbc_lblTaskId = new GridBagConstraints();
		gbc_lblTaskId.insets = new Insets(0, 0, 0, 5);
		gbc_lblTaskId.gridx = 0;
		gbc_lblTaskId.gridy = 0;
		add(lblTaskId, gbc_lblTaskId);
		
		JLabel lblTaskDescription = new JLabel("TaskDescription");
		GridBagConstraints gbc_lblTaskDescription = new GridBagConstraints();
		gbc_lblTaskDescription.gridx = 2;
		gbc_lblTaskDescription.gridy = 0;
		add(lblTaskDescription, gbc_lblTaskDescription);
		
		lblTaskId.setText(Integer.toString(this.task.getTaskId()) + ".");
		lblTaskDescription.setText(this.task.getText());
	}

}
