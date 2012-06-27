

public abstract class Gatter {

	protected int eingänge; // Anzhal der Eing�nge
	protected Signal[] eingangssignale; // Instanzen der Klasse Signale werden
	// hier gespeichert.
	protected Signal ausgangssignal;
	protected int delay;

	public Gatter(int eingänge, int delay) { // Konstruktor konstruiert ...
		this.delay = delay;
		this.eingänge = eingänge; // Datenfeld Eing�nge wird mit dem Wert der
		// Lokalen Variable Eing�nge gef�llt.
		eingangssignale = new Signal[eingänge]; // Ein Array mit der Gr��e
		// *Eing�nge* wird hergestellt
		// in dem die versch.
		// Signalinstanzen gespeichert
		// werden k�nnen.
	}


	public void setInput(int eingangsNummer, Signal sig) {
		if ((eingangsNummer >= 0) && (eingangsNummer < eingänge)) // Nur
		// zul�ssige
		// Eingangsnummern
		// verwenden
		{
			this.verbinden(sig);// interner methodenaufruf
			eingangssignale[eingangsNummer] = sig; // Speichert ein Signal *sig*
			// an die Stelle X, welche
			// durch *eingangsNummer* in
			// einem Array, welches an
			// der
		} else {
			System.out.println("Falsche Eingangsnummer");

		}
	}
	public void setOutput(Signal sig) {
		// Defensiver Kram
		ausgangssignal = sig;

	}
	public void verbinden(Signal sig)// die methode speichern aus der Klasse
	// Signal aufrufen und dadurch das
	// Gatter in die ArrayList eintragen
	{
		sig.speichern(this);
	}
	public void berechne() {
		boolean ergebnis=logic();
		if (ausgangssignal.getValue()!=ergebnis) {
			ausgangssignal.setValue(ergebnis);
		}
	}
	public void berechne(int t) {
		boolean ergebnis=logic();
		if (ausgangssignal.getValue()!=ergebnis) {
			new Event(ausgangssignal, t += delay, logic());
		}
	}
	public boolean logic() {
		return true;
	}
}
		