package itinerary.history;

import itinerary.history.HistoryBoundException.BoundType;

//@author A0121437N
public abstract class AbstractHistory <T> {
	
	protected HistoryNode current;
	
	/**
	 * Adds a value to the history
	 * 
	 * @param t the value to be added
	 */
	public void add (T t) {
		current = new HistoryNode(current, t);
	}
	
	/**
	 * Gets the previous value in the history while simultaneously
	 * setting the current pointer to that value
	 * 
	 * @return The previous value
	 * @throws HistoryBoundException if at the beginning of history or history is empty
	 */
	public T getPrevious () throws HistoryBoundException {
		if (current == null) {
			throw new HistoryBoundException(BoundType.EMPTY);
		}
		HistoryNode temp = current.getPrevious();
		if (temp == null) {
			throw new HistoryBoundException(BoundType.LOWER_BOUND);
		}
		current = temp;
		return current.getValue();
	}
	
	/**
	 * Gets the next value in the history while simultaneously
	 * setting the current pointer to that value
	 * 
	 * @return The next value
	 * @throws HistoryBoundException if at the end of history or history is empty
	 */
	public T getNext () throws HistoryBoundException {
		if (current == null) {
			throw new HistoryBoundException(BoundType.EMPTY);
		}
		HistoryNode temp = current.getNext();
		if (temp == null) {
			throw new HistoryBoundException(BoundType.UPPER_BOUND);
		}
		current = temp;
		return current.getValue();
	}
	
	protected class HistoryNode {
		private HistoryNode previous = null;
		private HistoryNode next = null;
		private T value;
		
		HistoryNode (HistoryNode back, T value) {
			if (back != null) {
				back.next = this;
			}
			this.previous = back;
			this.value = value;
		}

		protected HistoryNode getPrevious() {
			return previous;
		}

		protected HistoryNode getNext() {
			return next;
		}

		protected T getValue() {
			return value;
		}
	}
}
