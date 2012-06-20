
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
		
		
		return eventqueue.pollFirst(); // 'return' damit er was zur�ckliefert.
										// UND L�SCHEN
	}

	public void addEvent(Event e) // Methode die Events in eine Liste speichert
	{
		//int time = e.getTime();
		//ListIterator<Event> it = eventqueue.listIterator();

		if (eventqueue.size() == 0) // Das erste event muss direkt gespeichert
									// werden da sonst der iterator meckert
		{
			eventqueue.add(e);
		} else {
			//int currentTime=0;
			/*while (it.hasNext()) {
				currentTime=it.next().getTime();
				if ( currentTime<= time) {
					System.out.println(currentTime+"<="+time);
				} else {
					System.out.println("break");
					break;
				}
			}*/
			int index=0;
			for(Event event : eventqueue) {
				if(e.getTime()>=event.getTime()) {
					index++;
				}
			}
			//int index = it.previousIndex() +1;
			//System.out.println("will add at: "+index+":"+time);
			eventqueue.add(index, e);
		}

	}
	public String toString() {
		String output = "eventqueue contains:\n";
		int i=0;
		for (Event e : eventqueue) {
			output += i+". "+e.toString()+"\n";
			i++;
		}
		
		return output;
	}

}
