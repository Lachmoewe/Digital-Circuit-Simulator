

public class Exor extends Gate {

	public Exor(int numInputs, int delay) {
		super(numInputs, delay);
	}
	public boolean logic() {
		boolean result = false;
		int i = 0;
		for (Signal s : inputSignals) {
			if (s.getValue()) {
				i++;
			}
		}
		if (i==1) {
			result=true;
		}
		return result;
	}


}
