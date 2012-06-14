import java.util.ArrayList;
import java.util.Iterator;

public class Signal {
	// private int time;
	private String signalName;
	private boolean value;
	private ArrayList<Nand> spannergatter;

	public Signal(String signalName) {
		this.signalName = signalName;
		value = false;
		spannergatter = new ArrayList<Nand>();
	}

	public void speichern(Nand name)// Die Arrayliste mit Spannergattern
									// bef�llen
	{
		spannergatter.add(name);
	}

	public boolean getValue() {
		return value;
	}

	public void setValue2(boolean wert, int time) {
		spannergatter = new ArrayList<Nand>();
		value = wert;
		if (spannergatter.isEmpty()) {
			System.out.println("Wert von" + signalName + " ist: " + value);
		}

		else {
			Iterator<Nand> it = spannergatter.iterator();
			while (it.hasNext()) {
				it.next().berechne(time, wert);
			}
		}
	}

}
