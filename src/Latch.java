
public class Latch extends Gate {
	
	public Latch(int delay) {
		super(2, delay);
	}
	public boolean logic() {
		boolean result = outputSignal.getValue();
		boolean enable = inputSignals[0].getValue();
		boolean data = inputSignals[1].getValue();
		if (enable) {
			result = data;
		} else {
			result = outputSignal.getValue();
		}
		return result;
	}
}
