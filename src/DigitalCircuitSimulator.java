import java.util.HashMap;

public class DigitalCircuitSimulator {
	private EventQueue queue;
	private HashMap<String, Gate> gateList;
	private HashMap<String, Signal> signalList;

	public DigitalCircuitSimulator(String circuitFile, String eventFile) {
		queue = new EventQueue();
		Event.setEventQueue(queue);
		gateList = new HashMap();
		signalList = new HashMap();
		System.out.print("Reading Circuitfile: " + circuitFile + " ... ");
		readFile(circuitFile);
		System.out.println("done.");

		System.out.print("Reading Eventfile: " + eventFile + " ... ");
		readFile(eventFile);
		System.out.println("done.");
	}

	public void readFile(String filename) {
		DateiLeser file = new DateiLeser(filename);
		while (file.nochMehrZeilen()) {
			if (filename.contains(".cir")) {
				String zeile = file.gibZeile();
				buildCircuit(zeile);
			} else if (filename.contains(".events")) {
				addEvents(file.gibZeile());
			}
		}
	}

	private void addEvents(String in) {
		in = in.replaceAll(" +", " ");
		String[] result = in.split("\\s");

		if (result[0].equals("#") || result.length <= 1) {

		} else {
			int time = Integer.parseInt(result[0]);

			if (result[2].equals("1")) {
				new Event(signalList.get(result[1]), time, true);
			} else if (result[2].equals("0")) {
				new Event(signalList.get(result[1]), time, false);
			}
		}
	}

	public void buildCircuit(String in) {
		in = in.replaceAll(";", ""); // these methods
		in = in.replaceAll(", ", ","); // remove all
		in = in.replaceAll(" +", " "); // unwanted symbols
		in = in.replaceAll("\\.", " ");// from our String
		String[] result = in.split(" "); // String gets split @ space

		if (result.length > 1) {
			if (result[0].equals("Signal")) { // when it finds Signal
				String[] multIn = result[1].split(","); // handling multiple
														// inputs
														// per line
				for (String s : multIn) {
					Signal signalToAdd = new Signal(s);
					signalList.put(s, signalToAdd);
				}
			}

			else if (result[0].equals("Input") || result[0].equals("Output")) { // when it finds Output,
				String[] multIn = result[1].split(","); 
				for (String s : multIn) {
					if (!signalList.containsKey(s)) {
						Signal signalToAdd = new Signal(s);
						signalList.put(s, signalToAdd);
					}
					signalList.get(s).setPrintValue();
				}
			}

			else if (result[0].equals("Gate")) { // when it finds Gate
				Gate gateToAdd;
				String gateType = result[2];
				int delay = Integer.parseInt(result[4]);
				int numInputs = gateType.charAt(gateType.length() - 1) - 48;
				gateType = gateType.replaceAll("[0-9]", "");
				if (gateType.equals("AND")) {
					gateToAdd = new And(numInputs, delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("BUF")) {
					gateToAdd = new Buf(delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("EXOR")) {
					gateToAdd = new Exor(numInputs, delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("FF")) {
					gateToAdd = new FF(delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("LATCH")) {
					gateToAdd = new Latch(delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("NAND")) {
					gateToAdd = new Nand(numInputs, delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("NOR")) {
					gateToAdd = new Nor(numInputs, delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("NOT")) {
					gateToAdd = new Not(delay);
					gateList.put(result[1], gateToAdd);
				}
				if (gateType.equals("OR")) {
					gateToAdd = new Or(numInputs, delay);
					gateList.put(result[1], gateToAdd);
				}
			}

			else if (result[0].equals("#")) {
				// do nothing because it's a comment
			}

			else if (result[1].charAt(0)==('i')) {
				String inputNum = result[1].replaceAll("[a-z]", "");
				gateList.get(result[0]).setInput(Integer.parseInt(inputNum)-1,
						signalList.get(result[3]));
			} else if (result[1].equals("o")) {
				gateList.get(result[0]).setOutput(signalList.get(result[3]));
			}
		}

	}

	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}

	public static void main(String[] args) {
		String circuitFile = "/home/timo/workspace/Digital-Circuit-Simulator/src/circuits/beispiel1o2.cir";
		String eventFile = "/home/timo/workspace/Digital-Circuit-Simulator/src/circuits/beispiel1o.events";
		DigitalCircuitSimulator t = new DigitalCircuitSimulator(circuitFile,eventFile);
		t.simulate();
	}
}
