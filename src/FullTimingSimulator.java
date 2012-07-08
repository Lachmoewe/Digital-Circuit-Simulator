//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Collection;

public class FullTimingSimulator {
	private EventQueue queue;
	private LinkedHashMap<String, Gate> gateList;
	private static LinkedHashMap<String, Signal> signalList;
	private static Collection<Signal> c;
	private static Collection<Signal> c_alt;
	

	public FullTimingSimulator(String circuitfile, String eventfile) {
		signalList = new LinkedHashMap<String, Signal>();
		queue = new EventQueue();
		gateList = new LinkedHashMap<String, Gate>();
		
		Event.setEventQueue(queue);
		readFile(circuitfile);
		readFile(eventfile);
		
		
	}

	public void readFile(String filename) {

		String ending[] = filename.split("\\.");
		DateiLeser file = new DateiLeser(filename);

		if (ending[1].contains("cir")) {
			while (file.nochMehrZeilen()) {
				buildCircuit(file.gibZeile());
			}
		}

		else if (ending[1].contains("event")) {
			while (file.nochMehrZeilen()) {
				setupQueue(file.gibZeile());
			}
		}
	}

	public void setupQueue(String in) {
		if (in.contains("#")) {
		}

		else {
			in = in.replaceAll(" +", " ");
			String[] result = in.split(" ");
			if (result.length != 1) {
				int time = Integer.parseInt(result[0]); //

				String signalname = result[1];
				boolean value;
				if (result[2].equals("1")) {
					value = true;
				} // "1" und "0" in booleans umwandeln
				else {
					value = false;
				}

				queue.addEvent(new Event(signalList.get(signalname), time,
						value));
			}
		}
	}

	public void buildCircuit(String in) {
		if (in.equals("\\s*")) {return;};//damit werden leere Zeilen übersprungen
		

		in = in.replaceAll(";", ""); // these methods
		in = in.replaceAll(" ,", ","); // remove all
		// unwanted symbols
		in = in.replaceAll(",\\s+", ",");
		in = in.replaceAll("=", " = ");
		in = in.replaceAll("\\.", " ");
		in = in.replaceAll("\\s+", " ");// from our String
		String[] result = in.split("\\s"); // String gets split @ space

		if (result[0].equals("Signal") || result[0].equals("Input")
				|| result[0].equals("Output")) {// when it finds Signal
			String[] multIn = result[1].split(","); // handling multiple inputs
													// per line
			for (String s : multIn) {
				Signal signalToAdd = new Signal(s);
				signalList.put(s, signalToAdd);
				System.out.println(signalList.toString());
			}
		}

		else if (result[0].equals("Gate") || result[0].equals("gate")) { // when
																			// it
																			// finds
																			// Gate
			Gate gateToAdd;
			String gateType = result[2];

			int delay = Integer.parseInt(result[4]);

			int numInputs = gateType.charAt(gateType.length() - 1) - 48;
			gateType = gateType.replaceAll("[0-9]", "");// /////////////////////////////////////////////
			System.out.println("Inhalt von gateList: " + gateList.size());

			if (gateType.equals("AND")) {gateToAdd = new And(numInputs, delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
	
			if (gateType.equals("BUF")) {gateToAdd = new Buf(delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("EXOR")) {gateToAdd = new Exor(numInputs, delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("FF")) {gateToAdd = new FF(delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("LATCH")) {gateToAdd = new Latch(delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("NAND")) {gateToAdd = new Nand(numInputs, delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("NOR")) {
				gateToAdd = new Nor(numInputs, delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("NOT")) {gateToAdd = new Not(delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
			if (gateType.equals("OR")) {gateToAdd = new Or(numInputs, delay);gateList.put(result[1], gateToAdd);
				System.out.println(result[1] + " wurde erstellt");
			}
		}

		else if (result[0].equals("#") || result.length == 1) {
			// do nothing because it's a comment
		}

		else {
			if (result[1].contains("i")) {
				String inputNum = result[1].replaceAll("[a-z]", "");
				int inputnum = Integer.parseInt(inputNum);
				inputnum = inputnum - 1; //-1 korrektur, da die Gattereingänge von 0 aus gezählt werden
				
				System.out.println("Gewähltes Signal ist: " + result[3]);
				System.out.println("Gewähltes Gatter ist: "+ gateList.get(result[0]));		
				System.out.println("Mögliche Anzahl von eingängen: "+ gateList.get(result[0]).getAnzahlvoneingängen());	
				System.out.println("Ausgewählter Eingang: " + inputnum);
				
				gateList.get(result[0]).setInput(inputnum,signalList.get(result[3]));
						
						
				System.out.println("gatter: " + result[0]
						+ " wurde an Eingang " + inputnum + " verbunden mit: "
						+ result[3]);	
			}	
			
			 else if (result[1].equals("d")) {
				gateList.get(result[0]).setInput(0,signalList.get(result[3])); // d immer mit eingang 0 verbinden										
			}

			else if (result[1].equals("e")) {
				gateList.get(result[0]).setInput(1,signalList.get(result[3])); // e immer mit eingang 1 verbinden
			}
			
			 else if (result[1].equals("q")) {
				gateList.get(result[0]).setOutput(signalList.get(result[3]));
			} 
			
			else if (result[1].equals("o")) {
				gateList.get(result[0]).setOutput(signalList.get(result[3]));
			}
			
			else if (result[1].equals("nq")) {
				gateList.get(result[0]).setOutputNeg(signalList.get(result[3]));
				System.out.println("Gewähltes Signal ist: " + result[3]);
				System.out.println("Gewähltes Gatter ist: "+ gateList.get(result[0]));		
				System.out.println("Verbinde mit nq.");
				
			}

		}

	}

	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}
	
	public static void ganzeSignallisteAusgeben(){
		//if(c==c_alt)
		{System.out.println(c);}
		//c_alt.addAll(c);
	} 

	public static void main(String[] args) {
		String homedir = "/home/timo/workspace/Digital-Circuit-Simulator/src/circuits/";
		String circuitfile = homedir+"beispiel-flipflop.cir";
		String eventfile = homedir+"beispiel-flipflop.events";
		FullTimingSimulator t = new FullTimingSimulator(circuitfile, eventfile);
	
		c = signalList.values();//Alle signale in die collection schreiben
		String format=""+signalList.keySet();
		format= format.replaceAll(", ", "\t");
		System.out.println("	"+format);
		
		t.simulate();
		
	}

	public static void updateSignalList(String name,Signal signal) {
		
		signalList.put(name,signal);
		//System.out.println(signalList.toString());
		
	}
}