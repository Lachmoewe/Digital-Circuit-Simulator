

public class Nand extends Gatter {

	public Nand(int eingänge, int delay) {
		super(eingänge, delay);
	}

	public boolean logic() {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = ergebnis & s.getValue();
		}
		ergebnis = !ergebnis;
		return ergebnis;
	}
}