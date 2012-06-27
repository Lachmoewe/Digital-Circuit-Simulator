

public class Or extends Gate {

	public Or(int numInputs, int delay) {
		super(numInputs, delay);
	}

	public boolean logic() {
		boolean result = true;
		for (Signal s : inputSignals) {
			result = result || s.getValue();
		}
		return result;
	}
}