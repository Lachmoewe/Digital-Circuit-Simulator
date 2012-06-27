

public class And extends Gatter {

	public And(int eingänge, int delay) {
		super(eingänge, delay);
	}

	public boolean logic() {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = ergebnis & s.getValue();
		}
		return ergebnis;
	}
}