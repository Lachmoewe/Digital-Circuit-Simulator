

import java.util.LinkedList;

//import java.util.ListIterator;

public class EventQueue {
	// Datenfelder
	private LinkedList<Event> eventqueue; // Liste der Events nach Zeit sortiert

	// Konstruktor
	public EventQueue() {
		eventqueue = new LinkedList<Event>(); // Die EQ erzeugen
	}

	// Methoden
	public boolean hasMore() {
		return !eventqueue.isEmpty();
	}

	public Event getFirst() {

		return eventqueue.pollFirst(); // 'return' UND L���SCHEN
	}

	public void addEvent(Event e) // Methode die Events in eine Liste speichert
	{

		if (eventqueue.size() == 0) // Das erste event muss direkt gespeichert
									// werden da sonst der iterator meckert
		{
			eventqueue.add(e);
		} 
		else 
		{
			boolean reallyAddEvent=true;
			int index = 0;
			for (Event event : eventqueue) {
				if (e.getTime() >= event.getTime()) {
					index++;
				}
				if (e.getTime()==event.getTime() && e.getValue()==event.getValue() && e.getName()==event.getName()) {
					reallyAddEvent=false;
				}
			}
			if (reallyAddEvent) {
				eventqueue.add(index, e);
			}
		}
	}

	public String toString() {
		String output = "    eventqueue contains:\n";
		int i = 0;
		for (Event e : eventqueue) {
			output +="        "+ i + ". " + e.toString() + "\n";
			i++;
		}

		return output;
	}

}