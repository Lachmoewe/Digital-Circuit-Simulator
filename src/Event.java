
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

		queue = e; // speichert queue für ALLE Eventinstanzen als static
	}

	public int getTime() {
		return time;
	}
	public String getName(){
		return signalname.getName();
		
	}
	public boolean getValue(){ //Für die println methode in TimingSimulator
		return value;
	}

	public void propagate() {
		// value = wert;
		// signalname.setTime(time);
		signalname.setValue(value, time);
	}
	
	public String toString() {
		String output = "";
		output =  "Wert von " + this.getName() + " ";
		output += "ist: " + this.getValue() + " ";
		output += "Zeit: " + this.getTime() + " ";
		return output;
	}

}
