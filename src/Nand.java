

public class Nand extends Gate {

	public Nand(int numInputs, int delay) {
		super(numInputs, delay);
	}

	public boolean logic() {
		boolean result = true;
		for (Signal s : inputSignals) {
			result = result & s.getValue();
		}
		result = !result;
		return result;
	}
}