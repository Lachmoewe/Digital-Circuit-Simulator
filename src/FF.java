public class FF extends Memory {

	private boolean previousClk;
	public FF(int delay) {
		super(delay);
		previousClk = false;
	}

	
	public boolean logic() {
		boolean clk = inputSignals[0].getValue();
		boolean data = inputSignals[1].getValue();
		
		if (clk && (!previousClk)) {
				result = data;			
		}
		previousClk=clk;
		return result;
	}
 
}
