

import java.util.LinkedList;
import java.util.ListIterator;

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
		return eventqueue.pollFirst(); // 'return' damit er was zur�ckliefert.
										// UND L�SCHEN
	}

	public void addEvent(Event e) // Methode die Events in eine Liste speichert
	{
		int time = e.getTime();
		ListIterator<Event> it = eventqueue.listIterator();

		if (eventqueue.size() == 0) // Das erste event muss direkt gespeichert
									// werden da sonst der iterator meckert
		{
			eventqueue.add(e);
		} else {
			while (it.hasNext()) {
				if (it.next().getTime() <= time) {
				} else {
					break;
				}
			}
			eventqueue.add(it.previousIndex() + 1, e);
		}

	}

}
