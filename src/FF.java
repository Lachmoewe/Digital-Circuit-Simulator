public class FF extends Gate {

	private boolean previousClk;
	boolean result;
	public FF(int delay) {
		super(2, delay);
		previousClk = false;
	}

	public boolean logic() {
		boolean clk = inputSignals[0].getValue();
		boolean data = inputSignals[1].getValue();
		
		//boolean result = outputSignal.getValue();

		if (clk && (!previousClk) /*&& time==inputSignals[0].getPreviousTime()*/) {

			//if (data != result) {
				result = data;
			//}
			
		}
		previousClk=clk;
		return result;
	}
 
}
