

public abstract class Gate {

	protected int numInputs; // Anzhal der Eing���nge
	protected Signal[] inputSignals; // Instanzen der Klasse Signale werden
	// hier gespeichert.
	protected Signal outputSignal;
	protected int delay;
	protected int timer =10;
	protected boolean outputValue;

	public Gate(int inputs, int delay) { // Konstruktor konstruiert ...
		this.delay = delay;
		this.numInputs = inputs; // Datenfeld Eing���nge wird mit dem Wert der
		// Lokalen Variable Eing���nge gef���llt.
		inputSignals = new Signal[inputs]; // Ein Array mit der Gr������e
		// *Eing���nge* wird hergestellt
		// in dem die versch.
		// Signalinstanzen gespeichert
		// werden k���nnen.
	}


	public void setInput(int inputNum, Signal s) {
		if ((inputNum >= 0) && (inputNum < numInputs)) // Nur
		// zul���ssige
		// Eingangsnummern
		// verwenden
		{
			this.connect(s);// interner methodenaufruf
			inputSignals[inputNum] = s; // Speichert ein Signal *sig*
			// an die Stelle X, welche
			// durch *eingangsNummer* in
			// einem Array, welches an
			// der
		} else {
			System.out.println("Falsche Eingangsnummer");

		}
	}
	public void setOutput(Signal s) {
		// Defensiver Kram
		outputSignal = s;

	}
	public void connect(Signal s)// die methode speichern aus der Klasse
	// Signal aufrufen und dadurch das
	// Gatter in die ArrayList eintragen
	{
		s.save(this);
	}
	public void calculate() {
		boolean result=logic();
		if (outputValue!=result) {
			outputValue = result;
			outputSignal.setValue(result);
		}
		else if (timer > 0){
			timer--;
			outputValue = result;
			outputSignal.setValue(result);
		}
	}
	public void calculate(int t) {
		boolean result=logic();
		if (outputValue!=result) {
		    outputValue = result;
			new Event(outputSignal, t += delay, result);
		}
	}
	public boolean logic() {
		return true;
	}


	public void setOutputNeg(Signal s) {
	}
}
		