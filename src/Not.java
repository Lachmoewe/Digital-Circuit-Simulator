

public class Not extends Gatter {

	
	public Not(int delay) {
		super(1, delay);
	}
	

	public boolean logic() {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = !s.getValue();
		}
		return ergebnis;
	}
}