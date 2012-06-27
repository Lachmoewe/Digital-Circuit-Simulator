

public class Exor extends Gate {

	public Exor(int eingänge, int delay) {
		super(eingänge, delay);
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
