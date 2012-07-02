import java.util.ArrayList;
import java.util.HashMap;

public class DigitalCircuitSimulator {
	private EventQueue	queue;
	private HashMap<String,Gate> gateList;
	private HashMap<String,Signal> signalList;
	
	
	public DigitalCircuitSimulator(String filename) {
		queue=new EventQueue();
		Event.setEventQueue(queue);
		gateList = new HashMap();
		signalList = new HashMap();
		
		
	}
	
	public void readFile(String filename) {
		DateiLeser file = new DateiLeser(filename);
		while (file.nochMehrZeilen()) {
			buildCircuit(file.gibZeile());
		}
	}
	
	public void buildCircuit(String in) {
		in.replaceAll(";", "");		//these methods
		in.replaceAll(" ,", ",");	//remove all
		in.replaceAll("   ", " ");	//unwanted symbols
		in.replaceAll("  ", " ");	//from our String
		String[] result = in.split("\\s"); //String gets split @ space
		
		if (result[0].equals("Signal") || result[0].equals("Input") || result[0].equals("Output")) {	//when it finds Signal
			String[] multIn = result[1].split(","); //handling multiple inputs per line
			for (String s : multIn) {
				Signal signalToAdd = new Signal(s);
				signalList.put(s, signalToAdd);
			}
		} 
		
		else if (result[0].equals("Gate")) { //when it finds Gate
			Gate gateToAdd;
			String gateType = result[1];
			int delay = Integer.parseInt(result[4]);
			int numInputs = gateType.charAt(gateType.length()-1)-48;
			gateType = gateType.replaceAll("[0-9]", "");
			if (gateType.equals("AND")) {
				gateToAdd = new And(numInputs,delay);
				gateList.put(result[1], gateToAdd);
			}
			if (gateType.equals("BUF")) {
				gateToAdd = new Buf(delay);
				gateList.put(result[1], gateToAdd);
			}
			if (gateType.equals("EXOR")) {
				gateToAdd = new Exor(numInputs,delay);
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
				gateToAdd = new Nand(numInputs,delay);
				gateList.put(result[1], gateToAdd);
			}
			if (gateType.equals("NOR")) {
				gateToAdd = new Nor(numInputs,delay);
				gateList.put(result[1], gateToAdd);
			}
			if (gateType.equals("NOT")) {
				gateToAdd = new Not(delay);
				gateList.put(result[1], gateToAdd);
			}
			if (gateType.equals("OR")) {
				gateToAdd = new Or(numInputs,delay);
				gateList.put(result[1], gateToAdd);
			}
		}
		
		else if (result[0].equals("#")) {
			//do nothing because it's a comment 
		}
		
	}
	//make gate
	//make signal
	//connect gates
	//add event
	
	
	public void simulate() {
		while (queue.hasMore()) {
			Event e = queue.getFirst();
			e.propagate();
		}
	}

	public static void main(String[] args) {
		String file = "filename.cis";
		DigitalCircuitSimulator	t=new DigitalCircuitSimulator(file);
		t.simulate();
	}
}
