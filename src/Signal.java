
import java.util.ArrayList;
import java.util.Iterator;

public class Signal {
	private String signalName;
	private boolean value;
	private ArrayList<Nand> spannergatter; // = new ArrayList();;

	public Signal(String signalName) {
		this.signalName = signalName;
		value = false;
		spannergatter = new ArrayList<Nand>();
	}

	public void speichern(Nand name)// Die Arrayliste mit Spannergattern
	// befüllen
	{
		spannergatter.add(name);
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean wert) {
		value = wert;
		if (spannergatter.isEmpty()) {
			System.out.println("Wert von " + signalName + " ist: " + value);
		} else {
			for (Nand gatter : spannergatter) { // in the arraylist. Add a gate
				gatter.berechne(); // by calling callMe()
			}
		}
	}
	
	public String getName(){
		return signalName;   //Wird von der methode getName in Event aufgerufen, welche von system.out.println in Timingsimulator aufgeruffen wird
	}
	
	public void setValue(boolean wert, int time) {
		// spannergatter = new ArrayList<Nand>();
		value = wert;
		if (spannergatter.isEmpty()) {
			System.out.println(time+": " + signalName + "->" + value);
		}

		else {
			Iterator<Nand> it = spannergatter.iterator();
			while (it.hasNext()) {
				it.next().berechne(time);
			}
		}
	}
}