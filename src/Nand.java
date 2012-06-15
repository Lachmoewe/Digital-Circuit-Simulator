
public class Nand {

	private int eingänge; // Anzhal der Eingänge
	private Signal[] eingangssignale; // Instanzen der Klasse Signale werden
	// hier gespeichert.
	private Signal ausgangssignal;
	private int delay;

	// private int time;

	public Nand(int eingänge, int delay) { // Konstruktor konstruiert ...
		this.delay = delay;
		this.eingänge = eingänge; // Datenfeld Eingänge wird mit dem Wert der
		// Lokalen Variable Eingänge gefällt.
		eingangssignale = new Signal[eingänge]; // Ein Array mit der Größe
		// *Eingänge* wird hergestellt
		// in dem die versch.
		// Signalinstanzen gespeichert
		// werden kännen.
	}

	public void setInput(int eingangsNummer, Signal sig) {
		if ((eingangsNummer >= 0) && (eingangsNummer < eingänge)) // Nur
		// zulässige
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
	// gatter in die ArrayList eintragen
	{
		sig.speichern(this);
	}

	public void berechne() {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = ergebnis & s.getValue();
		}
		ergebnis = !ergebnis;
		if (ergebnis != ausgangssignal.getValue()) {
			ausgangssignal.setValue(ergebnis);
		}
	}

	public void berechne(int t) {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = ergebnis & s.getValue();
		}
		ergebnis = !ergebnis;
		{
			if (ergebnis != ausgangssignal.getValue()) { // nur ein event
			// erzeugen, wenn
			// sich der wert
			// geändert hat
				new Event(ausgangssignal, t += delay, ergebnis);
			}
		}
	}
}