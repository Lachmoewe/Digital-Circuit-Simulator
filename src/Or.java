

public class Or extends Gatter {

	public Or(int eingänge, int delay) {
		super(eingänge, delay);
	}

	public boolean logic() {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = ergebnis || s.getValue();
		}
		return ergebnis;
	}
}