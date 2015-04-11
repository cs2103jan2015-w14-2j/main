package itinerary.history;

//@author A0121437N
/**
 * An exception that is thrown when the bounds of the History are met
 * i.e. there are no nodes in that direction
 */
public class HistoryBoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private BoundType boundType;
	
	/**
	 * Used to identify the reason behind the exception.
	 */
	public enum BoundType {
		UPPER_BOUND,
		LOWER_BOUND,
		EMPTY
	}
	
	public HistoryBoundException (BoundType b) {
		this.boundType = b;
	}
	
	public BoundType getBoundType () {
		return this.boundType;
	}
}
