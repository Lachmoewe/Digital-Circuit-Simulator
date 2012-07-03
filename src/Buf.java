

public class Buf extends Gate {

	public Buf(int delay) {
		super(1,delay);

	}
	public boolean logic() {
		boolean result = inputSignals[0].getValue();
		
		return result;
	}
	
}
