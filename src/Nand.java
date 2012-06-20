
public class Nand extends Gatter{

	public Nand(int eingänge, int delay) {
		super(eingänge, delay);
		// TODO Auto-generated constructor stub
	}

	public void berechne() {
		boolean ergebnis = true;
		for (Signal s : getEingangssignale()) {
			ergebnis = ergebnis & s.getValue();
		}
		ergebnis = !ergebnis;
		if (ergebnis != getAusgangssignal().getValue()) {
			getAusgangssignal().setValue(ergebnis);
		}
	}

	public void berechne(int t) {
		boolean ergebnis = true;
		for (Signal s : getEingangssignale()) {
			ergebnis = ergebnis & s.getValue();
		}
		ergebnis = !ergebnis;
		{
			if (ergebnis != getAusgangssignal().getValue()) { // nur ein event
			// erzeugen, wenn
			// sich der wert
			// geändert hat
				new Event(getAusgangssignal(), t += getDelay(), ergebnis);
			}
		}
	}
}