
public class Memory extends Gate {
	protected boolean result;
	protected Signal outputSignalNeg;
	
	public Memory(int delay) {
		super(2, delay);
	}

	public void setOutputNeg(Signal s) {
		// Defensiver Kram
		outputSignalNeg = s;

	}
	
	public void calculate() {
		boolean result=logic();
		if (outputValue!=result) {
			outputValue = result;
			outputSignal.setValue(result);
			outputSignalNeg.setValue(!result);
		}
		else if (timer > 0){
			timer--;
			outputValue = result;
			outputSignal.setValue(result);
			outputSignalNeg.setValue(!result);
		}
	}
	
	public void calculate(int t) {
		boolean result=logic();
		if (outputValue!=result) {
		    outputValue = result;
			new Event(outputSignal, t += delay, result);
			new Event(outputSignalNeg, t += delay, !result);
		}
	}
}
