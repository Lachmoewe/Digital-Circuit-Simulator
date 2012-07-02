import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Diese Klasse erlaubt Ihnen das Lesen einer Datei ohne dass Sie mit den
 * Details der Ein-/Ausgabe von Java vertraut sein m�ssen.
 * 
 * F�r die Neugierigen: Diese Klasse benutzt einen FileReader zum Lesen aus der
 * Datei und einen BufferedReader zum Puffern der Eingaben und zum Zerlegen in
 * Zeilen.
 * 
 * Wenn Sie eine nicht existente oder f�r Sie nicht zugreifbare Datei lesen
 * wollen, dann wirkt das so als h�tten Sie eine leere Datei versucht zu lesen
 * 
 * @author Christian Hochberger
 * @version 1.0 (Juni 2004)
 */
public class DateiLeser {
	private BufferedReader file;
	private boolean lesenVersucht;
	private String gelesenerString;

	/**
	 * Erzeuge einen DateiLeser. Es gibt nur diesen einen Konstuktor, der
	 * unbedingt einen Parameter braucht
	 * 
	 * @param dateiname
	 *            ist ein String, der den Dateinamen beschreibt. Sollte ein Pfad
	 *            erforderlich sein, um auf die Datei zuzugreifen, dann muss
	 *            dieser Pfad mit im String enthalten sein und sollte dann den
	 *            Betriebssystem Konventionen gen�gen. Beispiele:
	 *            <UL>
	 *            <LI>DateiLeser("Schaltung.txt")</LI>
	 *            <LI>DateiLeser("C:\Temp\inputs.liste");</LI>
	 *            </UL>
	 */
	public DateiLeser(String dateiname) {
		FileReader fr;
		try {
			fr = new FileReader(dateiname);
			file = new BufferedReader(fr);
			lesenVersucht = false;
		} catch (IOException e) {
			lesenVersucht = true;
		}
		gelesenerString = null;
	}

	/**
	 * Pr�fe, ob noch eine Zeile lesbar ist.
	 */
	public boolean nochMehrZeilen() {
		if (!lesenVersucht)
			leseVersuch();
		return (gelesenerString != null);
	}

	/**
	 * Gib die n�chste Zeile der Datei als String zur�ck. Wenn keine Zeile mehr
	 * vorhanden ist, dann wird null zur�ckgegeben. Das gleiche passiert auch,
	 * wenn eine nicht vorhandene oder nicht lesbare Datei beim Konstruktor
	 * angegeben wurde.
	 */
	public String gibZeile() {
		String ergebnis;
		if (!lesenVersucht)
			leseVersuch();
		ergebnis = gelesenerString;
		gelesenerString = null;
		lesenVersucht = false;
		return ergebnis;
	}

	private void leseVersuch() {
		try {
			gelesenerString = file.readLine();
			lesenVersucht = true;
		} catch (IOException e) {
			lesenVersucht = true;
			gelesenerString = null;
		}
	}
}
