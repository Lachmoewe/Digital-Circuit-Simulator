

import java.util.ArrayList;
import java.util.Iterator;

public class Signal {
	private String signalName;
	private boolean value;
	private ArrayList<Gate> listenGates; // = new ArrayList();;
	private int previousTime;

	public Signal(String signalName) {
		this.signalName = signalName;
		value = false;
		listenGates = new ArrayList<Gate>();
		previousTime=0;
	}

	public void save(Gate name)// Die Arrayliste mit Spannergattern
	// bef���llen
	{
		listenGates.add(name);
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean wert) {
		value = wert;
		if (listenGates.isEmpty()) {
				System.out.println("Value of " + signalName + " is: " + value);
		} else {
			for (Gate gatter : listenGates) { // in the arraylist. Add a gate
				gatter.calculate(); // by calling callMe()
			}
		}
	}
	
	public void setValue(boolean v, int t) {
		boolean oldValue=value;
		value = v;
		if (listenGates.isEmpty()) {
			if (oldValue!=value) {
				System.out.println(t + ": " + signalName + " = " + value);
			}
			
		}

		else {
				Iterator<Gate> it = listenGates.iterator();
				while (it.hasNext()) {
					it.next().calculate(t);
			}
		}
		previousTime=t;
	}

	public String getName() {
		return signalName; // Wird von der methode getName in Event aufgerufen,
							// welche von system.out.println in Timingsimulator
							// aufgeruffen wird
	}
	public int getPreviousTime() {
		return previousTime;
	}

	
}