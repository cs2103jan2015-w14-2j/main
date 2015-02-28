package itinerary.main;

//@author A0121437N
public class HistoryNode {
	private HistoryNode back = null;
	private HistoryNode forward = null;
	private State currentState = null;
	
	private HistoryNode (HistoryNode back, State state) {
		this.back = back;
		this.currentState = state;
	}
	
	public static HistoryNode createNewNode (HistoryNode back, State state) {
		HistoryNode newNode = new HistoryNode(back, state);
		if (back != null) {
			back.setForward(newNode);
		}
		return newNode;
	}
	
	private void setForward (HistoryNode forward) {
		this.forward = forward;
	}

	public HistoryNode getBack() {
		return back;
	}

	public HistoryNode getForward() {
		return forward;
	}

	public State getCurrentState() {
		return currentState;
	}
}
