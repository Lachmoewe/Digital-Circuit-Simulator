
public class Latch extends Gate {
	boolean result;
	public Latch(int delay) {
		super(2, delay);
	}
	public boolean logic() {
		 
		boolean enable = inputSignals[0].getValue();
		boolean data = inputSignals[1].getValue();
		if (enable) {
			result = data;
		}
		return result;
	}
}
