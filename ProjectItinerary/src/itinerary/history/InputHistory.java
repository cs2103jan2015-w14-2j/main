package itinerary.history;

//@author A0121437N
public class InputHistory extends AbstractHistory<String> {

	private HistoryNode latest;
	
	public InputHistory () {
		latest = new HistoryNode(null, null);
		current = new HistoryNode(latest, null);
	}
	
	@Override
	public void add (String t) {
		latest = new HistoryNode(latest, t);
		current = new HistoryNode(latest, null);
	}
}
