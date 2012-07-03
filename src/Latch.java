
public class Latch extends Memory {

	public Latch(int delay) {
		super(delay);
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
