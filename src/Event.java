
public class Event {
	private static EventQueue masterqueue;
	
	private Signal 	sig;
	private int 	time;
	private boolean value;
	
	public Event(Signal s, int t, boolean v) {
		sig = s;
		time = t;
		value = v;
		
		//in queue eintragen
		masterqueue.addEvent(this);
	}
	
	public static void setEventQueue (EventQueue e) {
		masterqueue = e;
		
	}

	public void propagate() {
		// TODO Auto-generated method stub
		
	}

	public int getTime() {
		return time;
	}
}
