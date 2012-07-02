
public class Event {

	private static EventQueue queue;
	private Signal signalName;
	private int time;
	private boolean value;

	public Event(Signal s, int t, boolean v) {
		signalName = s; // variablen initialisieren
		this.time = t;
		this.value = v;

		queue.addEvent(this); // Externer Methodenaufruf die diese Instanz in
		// die queue speichert.
	}

	public static void setEventQueue(EventQueue e) {

		queue = e; // speichert queue f���r ALLE Eventinstanzen als static
	}

	public int getTime() {
		return time;
	}

	public String getName() {
		return signalName.getName();

	}

	public boolean getValue() { // F���r die println methode in TimingSimulator
		return value;
	}

	public void propagate() {
		signalName.setValue(value, time);
		//System.out.println(queue.toString());
	}

	public String toString() {
		String output = "";
		output = this.getTime() + ": " + this.getName() + "->"
				+ this.getValue();
		return output;
	}

}