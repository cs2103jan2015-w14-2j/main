package itinerary.main;

//@author A0121437N
public class History {
	private HistoryNode currentPoint;
	
	public History() {
		this.currentPoint = null;
	}
	
	public void addNewState (State state) {
		currentPoint = HistoryNode.createNewNode(currentPoint, state);
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
}
