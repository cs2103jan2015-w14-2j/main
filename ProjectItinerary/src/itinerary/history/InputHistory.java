package itinerary.history;

//@author A0121437N
public class InputHistory extends AbstractHistory<String> {

	private HistoryNode latest;
	
	public InputHistory () {
		latest = new HistoryNode(null, null);
		current = new HistoryNode(latest, null);
	}
	
	/**
	 * Adds a String to the end of the History and an additional value which is null
	 * 
	 * @param t The String to be added
	 */
	@Override
	public void add (String t) {
		latest = new HistoryNode(latest, t);
		current = new HistoryNode(latest, null);
	}
}
