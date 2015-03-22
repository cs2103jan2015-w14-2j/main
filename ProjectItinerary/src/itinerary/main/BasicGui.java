package itinerary.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class BasicGui {

	private JFrame frmProjectItinerary;
	private JTextField textFieldCommand;
	private JPanel panelTasks;
	private JLabel lblConsoleMessage;
	private final Logic logic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BasicGui window = new BasicGui();
					window.frmProjectItinerary.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BasicGui() {
		logic = new Logic("test");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProjectItinerary = new JFrame();
		frmProjectItinerary.setTitle("Project Itinerary");
		frmProjectItinerary.setBounds(100, 100, 450, 300);
		frmProjectItinerary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProjectItinerary.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelLower = new JPanel();
		frmProjectItinerary.getContentPane().add(panelLower, BorderLayout.SOUTH);
		panelLower.setLayout(new BorderLayout(0, 0));
		
		JPanel panelConsole = new JPanel();
		panelLower.add(panelConsole, BorderLayout.NORTH);
		panelConsole.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblConsole = new JLabel("Console");
		panelConsole.add(lblConsole);
		
		lblConsoleMessage = new JLabel("");
		lblConsoleMessage.setOpaque(true);
		lblConsoleMessage.setForeground(SystemColor.activeCaptionText);
		lblConsoleMessage.setBackground(SystemColor.text);
		panelConsole.add(lblConsoleMessage);
		
		JPanel panelCommand = new JPanel();
		panelLower.add(panelCommand, BorderLayout.SOUTH);
		panelCommand.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblCommand = new JLabel("Command");
		panelCommand.add(lblCommand);
		
		textFieldCommand = new JTextField();
		panelCommand.add(textFieldCommand);
		textFieldCommand.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		frmProjectItinerary.getContentPane().add(scrollPane, BorderLayout.CENTER);

		panelTasks = new JPanel();
		panelTasks.setLayout(new BoxLayout(panelTasks, BoxLayout.Y_AXIS));
		scrollPane.setViewportView(panelTasks);
		
		textFieldCommand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userInput = textFieldCommand.getText();
				UserInterfaceContent content = logic.executeUserInput(userInput);
				updateFields(content);
			}
		});
		
		UserInterfaceContent content = logic.initialLaunch();
		updateFields(content);
	}

	private void populateTasks (List<Task> tasks) {
		panelTasks.removeAll();
		for (Task task : tasks) {
			BasicPanelTask panelTask = new BasicPanelTask(task);
			panelTasks.add(panelTask);
		}
		panelTasks.revalidate();
		panelTasks.repaint();
	}
	
	private void updateFields (UserInterfaceContent content) {
		textFieldCommand.setText("");
		lblConsoleMessage.setText(content.getConsoleMessage());
		populateTasks(content.getDisplayableTasks());
	}
}
