import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collection;

public class FullTimingSimulator {
	private EventQueue queue;
	private LinkedHashMap<String, Gate> gateList;
	private ArrayList<Signal> inputSignalList;
	private static LinkedHashMap<String, Signal> printingSignalList;
	private static LinkedHashMap<String, Signal> signalList;
	private static Collection<Signal> c;
	private static String c_alt;
	

	public FullTimingSimulator(String circuitfile, String eventfile) 
	{
		signalList = new LinkedHashMap<String, Signal>();
		inputSignalList = new ArrayList<Signal>();
		printingSignalList = new LinkedHashMap<String,Signal>();
		queue = new EventQueue();
		gateList = new LinkedHashMap<String, Gate>();
		Event.setEventQueue(queue);
		readFile(circuitfile);
		readFile(eventfile);
		findSteadyState();

	}
	public void readFile(String filename) 
	{
		String ending[] = filename.split("\\.");
		DateiLeser file = new DateiLeser(filename);

		if (ending[1].contains("cir")) 
		{
			while (file.nochMehrZeilen()) {buildCircuit(file.gibZeile());}
		}

		else if (ending[1].contains("event")) 
		{
			while (file.nochMehrZeilen()) {setupQueue(file.gibZeile());}
		}
	}
	public void setupQueue(String in) //Events in die queue laden
		{
		if (in.contains("#")) {}
		
		else {
			in = in.replaceAll(" +", " ");//gleiche Anzahl von leerzeichen
			String[] result = in.split(" ");
			
			if (result.length != 1) /*falls die Länge 1 ist bedeutet dies, dass eine leere zeile eingelesen wurde. Diese soll nicht behandelt werden*/
				
				{
				int time = Integer.parseInt(result[0]); //
				String signalname = result[1];
				boolean value;
				if (result[2].equals("1")) {value = true;} /* "1" und "0" in booleans umwandeln*/
				else {value = false;}

				queue.addEvent(new Event(signalList.get(signalname), time,value));
				}
			}	
		}
	public void buildCircuit(String in) 
	{
		if (in.equals("\\s*")) {return;};//damit werden leere Zeilen übersprungen	
		
		in = in.replaceAll(";", ""		); /*these methods remove all unwanted symbols*/
		in = in.replaceAll(" ,", ","	);  
		in = in.replaceAll(",\\s+", ","	);
		in = in.replaceAll("=", " = "	);
		in = in.replaceAll("\\.", " "	);
		in = in.replaceAll("\\s+", " "	);// from our String
		
		String[] result = in.split("\\s"); // String gets split @ space

		if (result[0].equals("Signal")) // when it finds Signal
		{
			String[] multIn = result[1].split(","); /* handling multiple inputs per line*/										
				for (String s : multIn) 
				{
				Signal signalToAdd = new Signal(s);
				signalList.put(s, signalToAdd);
				}
		}
		else if (result[0].equals("Output")) // when it finds Output
		{
			String[] multIn = result[1].split(","); /* handling multiple inputs per line*/										
				for (String s : multIn) 
				{
				Signal signalToAdd = new Signal(s);
				signalList.put(s, signalToAdd);
				printingSignalList.put(s, signalToAdd);
				}
		}
		
		
		else if (result[0].equals("Input"))
		{
			String[] multIn = result[1].split(","); /* handling multiple inputs per line*/										
			for (String s : multIn) 
			{
			Signal signalToAdd = new Signal(s);
			signalList.put(s, signalToAdd);
			inputSignalList.add(signalToAdd);
			printingSignalList.put(s, signalToAdd);
			}
	
		}

		else if (result[0].equals("Gate") || result[0].equals("gate"))  // Wenn es gate findet
		{														
			Gate gateToAdd;
			String gateType = result[2];
			int delay = Integer.parseInt(result[4]);
			int numInputs = gateType.charAt(gateType.length() - 1) - 48;/*Direkt die inputanzahl auslesen*/			
			gateType = gateType.replaceAll("[0-9]", "");/*Den gatenamen von der nummer bereinigen, damit der richtige gatetyp gebeut wird. And3 --> And*/
			
			if (gateType.equals("AND")) 	{gateToAdd = new	And		(numInputs, delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("EXOR"))	{gateToAdd = new 	Exor	(numInputs, delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("NAND")) 	{gateToAdd = new 	Nand	(numInputs, delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("NOR")) 	{gateToAdd = new 	Nor		(numInputs, delay);gateList.put(result[1], gateToAdd);}			
			if (gateType.equals("OR")) 		{gateToAdd = new 	Or		(numInputs, delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("NOT")) 	{gateToAdd = new 	Not		(			delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("FF")) 		{gateToAdd = new 	FF		(			delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("LATCH")) 	{gateToAdd = new 	Latch	(			delay);gateList.put(result[1], gateToAdd);}
			if (gateType.equals("BUF")) 	{gateToAdd = new 	Buf		(			delay);gateList.put(result[1], gateToAdd);}
		}

		else if (result[0].equals("#") || result.length == 1) {return;}

		
		/*Die Signale werden hier gebaut. es wird unterschieden ob es sich um ein normales Signal i oder um ein enable etc handelt*/
		else {
			if (result[1].contains("i")) 
				{
				String inputNum = result[1].replaceAll("[a-z]","");
				int inputnum = Integer.parseInt(inputNum);
				inputnum = inputnum - 1; //-1 korrektur, da die Gattereingänge von 0 aus gezählt werden
				gateList.get(result[0]).setInput(inputnum,signalList.get(result[3]));
			}	
			
			else if (result[1].equals("d")) {gateList.get(result[0]).setInput(1,signalList.get(result[3]));}// d immer mit eingang 0 verbinden
			else if (result[1].equals("e")) {gateList.get(result[0]).setInput(0,signalList.get(result[3]));} // e immer mit eingang 1 verbinden
			else if (result[1].equals("c")) {gateList.get(result[0]).setInput(0,signalList.get(result[3]));}	
			else if (result[1].equals("q")) {gateList.get(result[0]).setOutput(signalList.get(result[3]));} 
			else if (result[1].equals("o")) {gateList.get(result[0]).setOutput(signalList.get(result[3]));}
			else if (result[1].equals("nq")){gateList.get(result[0]).setNegOutput(signalList.get(result[3]));}

			}	
		
		
	}
	
	private void findSteadyState() 
	{
		for(Signal s: inputSignalList)
		{
			s.setValue(false);
		}
	}
	
	public void simulate() 
	{
		while (queue.hasMore())	{Event e = queue.getFirst();e.propagate();}
	}
	public static void ganzeSignallisteAusgeben(int t)
	{
		//if(c==c_alt)
		String printThisLine = c.toString();
		if(!(printThisLine.equals(c_alt))){
			System.out.print(t + ": ");
			System.out.println(printThisLine);
		}
		
		c_alt=printThisLine;
		//c_alt.addAll(c);
	} 
	public static void main(String[] args) 
	{
		String homedir = "/home/timo/workspace/Digital-Circuit-Simulator/src/circuits/";
		String circuitfile = homedir+"blume.cir";
		String eventfile = homedir+"blume.events";
		FullTimingSimulator t = new FullTimingSimulator(circuitfile, eventfile);	
		c = printingSignalList.values();//Alle signale in die collection schreiben
		String format=printingSignalList.keySet().toString();
		//format= format.replaceAll(", ", "\t");
		format= format.replaceAll("\\[", "");
		format= format.replaceAll("\\]", "");
		String[] crop = format.split(", ");
		String result="";
		for (String c : crop) {
			if (c.length()>7) {
				result+=c.subSequence(0, 6);
			} else {
				result+=c;
			}
			result+="\t";
		}
		System.out.println("	"+result);
		t.simulate();	
	}
	public static void updateSignalList(String name,Signal signal) 
	{signalList.put(name,signal);}
}