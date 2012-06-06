public class Nand {
	private Signal[] input;
	private Signal output;
	int delay;

	public Nand(int numInputs, int delay) {	//initialize a new gate
		this.input = new Signal[numInputs]; // create input Signal array with
											// size numInputs
		this.delay = delay;
	}

	public void setInput(int num, Signal sig) { // connect the gate with a
												// signal input
		this.input[num] = sig; // add the signal to the input array
		sig.callMe(this); 	// tell the signal this gate is connected to it so
							// the signal can call calculate() when the it
							// changes value
	}

	public void setOutput(Signal sig) {
		output = sig; //connect the signal to output to to the gate
	}

	public void calculate() {
		boolean result = false;
		for (Signal s : input) { 	// iterate over input[] with s being the
									// running variable
			if (s.getValue() == false) { 	//if we find one false value,
				result = true;				//there won't be a false output
				break;						//on a nand gate, its only false 
											//when every input is true
			}
		}
		output.setValue(result);	//set the output signal value to our calculated result
	}
}
