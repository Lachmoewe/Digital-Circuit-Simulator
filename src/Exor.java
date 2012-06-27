

public class Exor extends Gatter {

	public Exor(int eingänge, int delay) {
		super(eingänge, delay);
	}
	public boolean logic() {
		boolean ergebnis = false;
		int wert = 0;
		for (Signal s : eingangssignale) {
			if (s.getValue()) {
				wert++;
			}
		}
		if (wert==1) {
			ergebnis=true;
		}
		return ergebnis;
	}


}
