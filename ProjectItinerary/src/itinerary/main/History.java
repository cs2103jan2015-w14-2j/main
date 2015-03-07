package itinerary.main;

import java.util.List;

//@author A0121437N
public class History {
	private HistoryNode currentPoint;
	
	public History(List<Task> initialState) {
		assert initialState != null;
		
		this.currentPoint = new HistoryNode(null, initialState);
	}
	
	public void addNewState (List<Task> tasks) {
		currentPoint = new HistoryNode(currentPoint, tasks);
	}
	
	public List<Task> goBack () {
		if (currentPoint.getBack() != null) {
			currentPoint = currentPoint.getBack();
			return currentPoint.getCurrentState();
		}
		return null;
	}
	
	public List<Task> goForward () {
		if (currentPoint.getForward() != null) {
			currentPoint = currentPoint.getForward();
			return currentPoint.getCurrentState();
		}
		return null;
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
