

public class Not extends Gate {

	
	public Not(int delay) {
		super(1, delay);
	}
	

	public boolean logic() {
		boolean result = true;
		for (Signal s : inputSignals) {
			result = !s.getValue();
		}
		return result;
	}
}