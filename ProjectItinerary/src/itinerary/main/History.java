package itinerary.main;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author A0121437N
public class History {
	private HistoryNode currentPoint;
	
	private static final String MESSAGE_UNDO_NOTHING = "nothing to undo";
	private static final String MESSAGE_REDO_NOTHING = "nothing to redo";
	
	private static final Logger logger = Logger.getLogger(History.class.getName());
	static {
		try {
			logger.addHandler(new FileHandler(Constants.LOG_FILE));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public History(List<Task> initialState) {
		assert initialState != null;
		
		this.currentPoint = new HistoryNode(null, initialState);
		logger.log(Level.INFO, "Creating History object");
	}
	
	public void addNewState (List<Task> tasks) {
		assert tasks != null;
		
		currentPoint = new HistoryNode(currentPoint, tasks);
		logger.log(Level.INFO, "Adding new state to History");
	}
	
	public List<Task> undo () throws HistoryException {
		if (currentPoint.getBack() != null) {
			currentPoint = currentPoint.getBack();
			logger.log(Level.INFO, "Going back one state in History");
			return currentPoint.getCurrentState();
		}
		logger.log(Level.INFO, "No back state in History");
		throw new HistoryException (MESSAGE_UNDO_NOTHING);
	}
	
	public List<Task> redo () throws HistoryException {
		if (currentPoint.getForward() != null) {
			currentPoint = currentPoint.getForward();
			logger.log(Level.INFO, "Going forward one state in History");
			return currentPoint.getCurrentState();
		}
		logger.log(Level.INFO, "No forward state in History");
		throw new HistoryException(MESSAGE_REDO_NOTHING);
	}
	
	public List<Task> getCurrentState () {
		if (currentPoint == null) {
			return null;
		}
		return currentPoint.getCurrentState();
	}
	
	private class HistoryNode {
		private HistoryNode back = null;
		private HistoryNode forward = null;
		private List<Task> currentState;
		
		private HistoryNode (HistoryNode back, List<Task> state) {
			assert state != null;
			
			if (back != null) {
				back.forward = this;
			}
			this.back = back;
			this.currentState = state;
		}

		protected HistoryNode getBack() {
			return back;
		}

		protected HistoryNode getForward() {
			return forward;
		}

		protected List<Task> getCurrentState() {
			return currentState;
		}
	}
}
