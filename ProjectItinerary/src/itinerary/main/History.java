package itinerary.main;

//@author A0121437N
public class History {
	private HistoryNode currentPoint;
	
	public History() {
		this.currentPoint = new HistoryNode(null, null);
	}
	
	public void addNewState (State state) {
		currentPoint = new HistoryNode(currentPoint, state);
	}
	
	public State goBack () {
		if (currentPoint.getBack() != null) {
			currentPoint = currentPoint.getBack();
			return currentPoint.getCurrentState();
		}
		return null;
	}
	
	public State goForward () {
		if (currentPoint.getForward() != null) {
			currentPoint = currentPoint.getForward();
			return currentPoint.getCurrentState();
		}
		return null;
	}
	
	public State getCurrentState () {
		if (currentPoint == null) {
			return null;
		}
		return currentPoint.getCurrentState();
	}
	
	private class HistoryNode {
		private HistoryNode back = null;
		private HistoryNode forward = null;
		private State currentState = null;
		
		private HistoryNode (HistoryNode back, State state) {
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

		protected State getCurrentState() {
			return currentState;
		}
	}
}
