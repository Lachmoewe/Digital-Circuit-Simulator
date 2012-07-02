

public class Buf extends Gate {

	public Buf(int delay) {
		super(1,delay);

	}
	public boolean logic() {
		boolean result = inputSignals[0].getValue();
		/*for (Signal s : inputSignals) {
			result = s.getValue();
		}*/
		return result;
	}
	
}
