

public class Nor extends Gate {

	public Nor(int numInputs, int delay) {
		super(numInputs, delay);
	}

	public boolean logic() {
		boolean result = true;
		for (Signal s : inputSignals) {
			result = result || s.getValue();
		}
		result= !result;
		return result;
	}
}