
public class Event {

	private static EventQueue queue;
	private Signal signalname;
	private int time;
	private boolean value;

	public Event(Signal sig, int time, boolean value) {
		signalname = sig; // variablen initialisieren
		this.time = time;
		this.value = value;

		queue.addEvent(this); // Externer Methodenaufruf die diese Instanz in
		// die queue speichert.
	}

	public static void setEventQueue(EventQueue e) {

		queue = e; // speichert queue f�r ALLE Eventinstanzen als static
	}

	public int getTime() {
		return time;
	}

	public String getName() {
		return signalname.getName();

	}

	public boolean getValue() { // F�r die println methode in TimingSimulator
		return value;
	}

	public void propagate() {
		signalname.setValue(value, time);
	}

	public String toString() {
		String output = "";
		output = this.getTime() + ": " + this.getName() + "->"
				+ this.getValue();
		return output;
	}

}