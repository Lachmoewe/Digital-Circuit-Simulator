

public class And extends Gate {

	public And(int numInputs, int delay) {
		super(numInputs, delay);
	}

	public boolean logic() {
		boolean result = true;
		for (Signal s : inputSignals) {
			result = result & s.getValue();
		}
		return result;
	}
}