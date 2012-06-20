
public class Gatter {
	private int eingänge; // Anzhal der Eingänge
	private Signal[] eingangssignale; // Instanzen der Klasse Signale werden
	// hier gespeichert.
	private Signal ausgangssignal;
	private int delay;

	// private int time;

	public Gatter(int eingänge, int delay) { // Konstruktor konstruiert ...
		this.setDelay(delay);
		this.eingänge = eingänge; // Datenfeld Eingänge wird mit dem Wert der
		// Lokalen Variable Eingänge gefällt.
		setEingangssignale(new Signal[eingänge]); // Ein Array mit der Größe
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
			getEingangssignale()[eingangsNummer] = sig; // Speichert ein Signal *sig*
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
		setAusgangssignal(sig);

	}

	public void verbinden(Signal sig)// die methode speichern aus der Klasse
	// Signal aufrufen und dadurch das
	// gatter in die ArrayList eintragen
	{
		sig.speichern(this);
	}

	public Signal[] getEingangssignale() {
		return eingangssignale;
	}

	public void setEingangssignale(Signal[] eingangssignale) {
		this.eingangssignale = eingangssignale;
	}

	public Signal getAusgangssignal() {
		return ausgangssignal;
	}

	public void setAusgangssignal(Signal ausgangssignal) {
		this.ausgangssignal = ausgangssignal;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void berechne() {
		// TODO Auto-generated method stub
		
	}

	public void berechne(int time) {
		// TODO Auto-generated method stub
		
	}
}
