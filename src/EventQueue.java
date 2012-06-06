import java.util.ArrayList;

public class EventQueue {

	//Datenfelder
		//Liste der Events in time Reihenfolge
	private ArrayList<Event> eventQueue;
	
	public EventQueue () {
		eventQueue = new ArrayList();
	}
	
	
	
	public boolean hasMore() {
		return !eventQueue.isEmpty();
	}
	public Event getFirst() {
		return eventQueue.get(0);
	}
	//Methode die Events in die eventQueue speichert mit time als index
	public void addEvent(Event e) {
		eventQueue.add(e.getTime(), e);
	}

}
