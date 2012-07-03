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
		System.out.println("Reading Circuitfile: " + circuitFile + " ... ");
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
		in = in.replaceAll("=", " = ");
		in = in.replaceAll(" +", " ");
		in = in.replaceAll(";", ""); // these methods
		in = in.replaceAll(", ", ","); // remove all // unwanted symbols
		in = in.replaceAll("\\.", " ");// from our String
		String[] result = in.split(" "); // String gets split @ space

		if (result.length > 1) { //if not an empty line
			if (result[0].equals("Signal")) { // when it finds Signal
				String[] multIn = result[1].split(","); // handling multiple
														// inputs
														// per line
				for (String s : multIn) {
					Signal signalToAdd = new Signal(s);
					signalList.put(s, signalToAdd);
					System.out.println("Adding Signal: "+s);
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
					System.out.println("Adding IOSignal: "+s);
				}
			}

			else if (result[0].equals("Gate")) { // when it finds Gate
				Gate gateToAdd = null;
				String gateName=result[1];
				String gateType = result[2];
				int delay = Integer.parseInt(result[4]);
				int numInputs = 1;
				int conversion = gateType.charAt(gateType.length() - 1);
				if (48<=conversion && conversion<=57) {
					numInputs = conversion - 48;
				}
				
				
				gateType = gateType.replaceAll("[0-9]", "");
				if (gateType.equals("AND"))   {gateToAdd = new And(numInputs, delay);} else
				if (gateType.equals("BUF"))   {gateToAdd = new Buf(delay);} else
				if (gateType.equals("EXOR"))  {gateToAdd = new Exor(numInputs, delay);} else
				if (gateType.equals("FF"))    {gateToAdd = new FF(delay);} else
				if (gateType.equals("LATCH")) {gateToAdd = new Latch(delay);} else
				if (gateType.equals("NAND"))  {gateToAdd = new Nand(numInputs, delay);} else
				if (gateType.equals("NOR"))   {gateToAdd = new Nor(numInputs, delay);} else
				if (gateType.equals("NOT"))   {gateToAdd = new Not(delay);} else
				if (gateType.equals("OR"))    {gateToAdd = new Or(numInputs, delay);}
				gateList.put(gateName, gateToAdd);
				System.out.println("Adding Gate: "+gateName);
			}

			else if (result[0].equals("#")) {
				// do nothing because it's a comment
			}
			//TODO für latch l[0-9] d e q nq und flipflop ff[0-9] c d q nq
			else if (result[2].equals("=")) {
				String gateName=result[0]; //can be l4, ff15, b3, g1 etc.
				String pinName=result[1]; //can be i9, o, e, d, c
				//String gateType=gateName.replaceAll("\\d", ""); //l, ff, b, g
				String signalName=result[3];
				int inputNum = 0;
				boolean isInput=false;
				Signal s = signalList.get(signalName);
				boolean outputIsInverted=false;
				
				if (pinName.equals("nq")) {outputIsInverted=true;isInput=false;} else
				if (pinName.equals("q"))  {isInput=false;} else
				if (pinName.equals("o"))  {isInput=false;} else
				if (pinName.equals("e"))  {inputNum=0;isInput=true;} else
				if (pinName.equals("c"))  {inputNum=0;isInput=true;} else
				if (pinName.equals("d"))  {inputNum=1;isInput=true;} else {
					String pinNum = pinName.replaceAll("[a-z]", "");
					inputNum = Integer.parseInt(pinNum) - 1;
					isInput=true;
				}
				if (isInput==true) {
					gateList.get(gateName).setInput(inputNum, s);
					System.out.println("Connecting "+gateName+"."+pinName+" with "+signalName );
				} else {
					if (!outputIsInverted) {
						gateList.get(gateName).setOutput(s);
						System.out.println("Connecting output of "+gateName+" with "+signalName );
					} else {
						gateList.get(gateName).setOutputNeg(s);
						System.out.println("Connecting inversed output of "+gateName+" with "+signalName );
					} 
				}
				/*String inputNum = result[1].replaceAll("[a-z]", "");
				gateList.get(result[0]).setInput(Integer.parseInt(inputNum)-1,
						signalList.get(result[3]));*/
			} 
			
			/*else if (result[1].equals("o")) {
				gateList.get(result[0]).setOutput(signalList.get(result[3]));
			}*/
		}

	}

	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}

	public static void main(String[] args) {
		String circuitFile = "/home/timo/workspace/Digital-Circuit-Simulator/src/circuits/beispiel-latch.cir";
		String eventFile = "/home/timo/workspace/Digital-Circuit-Simulator/src/circuits/beispiel-latch.events";
		DigitalCircuitSimulator t = new DigitalCircuitSimulator(circuitFile,eventFile);
		t.simulate();
	}
}
