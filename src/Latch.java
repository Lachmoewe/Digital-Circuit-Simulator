
public class Latch extends Gate {
	
	public Latch(int delay) {
		super(1, delay);
	}
	public boolean logic() {
		boolean result = false;
		boolean enable = inputSignals[0].getValue();
		boolean data = inputSignals[1].getValue();
		if (enable) {
			result = data;
		} else {
			result = outputSignals.getValue();
		}
		return result;
	}
}
