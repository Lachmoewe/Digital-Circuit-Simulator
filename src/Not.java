

public class Not extends Gatter {

	
	public Not(int delay) {
		super(1, delay);
	}
	

	public void berechne() {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = !s.getValue();
		}
		//ergebnis = !ergebnis;
		//if (ergebnis != ausgangssignal.getValue()) {
			ausgangssignal.setValue(ergebnis);
		//}
	}

	public void berechne(int t) {
		boolean ergebnis = true;
		for (Signal s : eingangssignale) {
			ergebnis = !s.getValue();
		}new Event(ausgangssignal, t += delay, ergebnis);
		/*ergebnis = !ergebnis;
		{
			if (ergebnis != ausgangssignal.getValue()) { // nur ein event
															// erzeugen, wenn
															// sich der wert
															// ge�ndert hat
				new Event(ausgangssignal, t += delay, ergebnis);
			} else { // System.out.println("keine �nderung");
			}
		}*/
	}
}