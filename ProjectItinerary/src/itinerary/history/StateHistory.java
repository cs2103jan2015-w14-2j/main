package itinerary.history;

import itinerary.main.Task;

import java.util.List;

//@author A0121437N
public class StateHistory extends AbstractHistory<List<Task>> {
	public StateHistory (List<Task> initial) {
		super.add(initial);
	}
}
