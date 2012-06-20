/**
 * Klasse FullTimingSimulator ist ein Logiksimulator, der alle geforderten Gattertypen
 * benutzt und Zeitverzögerungen berücksichtigt.
 * Die zu simulierende Schaltung wird in der Methode
 * <CODE>buildCircuitX()</CODE> der Klasse erzeugt (mit X in 1,2,3).
 * Diese Methode verwendet weitere Hilfsmethoden um die Schaltung zu
 * erzeugen.  Versuchen Sie doch mal herauszubekommen, was die Schaltung3
 * macht und bis zu welcher Taktfrequenz sie zuverlässig arbeitet.
 * <BR>Nach der Konstruktion der Schaltung muss zunächst der Ruhezustand
 * der Schaltung berechnet werden. Dies übernimmt die Methode
 * <CODE>findSteadyStateX()</CODE>. Die Eingabe-Events zur Stimulation
 * der Schaltung werden durch die Methode <CODE>setInputEventsX()</CODE>
 * erzeugt.
 * Zum Testen Ihrer Klassen <CODE>Nand</CODE>, <CODE>Nor</CODE>,
 * <CODE>Or</CODE>, <CODE>And</CODE>, <CODE>Exor</CODE>,
 * <CODE>Buf</CODE>, <CODE>Not</CODE>, <CODE>FF</CODE>, <CODE>Latch</CODE>,
 * <CODE>Signal</CODE>, <CODE>Event</CODE>,und <CODE>EventQueue</CODE>
 * müssen Sie einfach nur eine Instanz dieser Klasse erzeugen und dann
 * die Methode <CODE>simulate()</CODE> aufrufen.
 * @author Christian Hochberger, TU Dresden
 * @version 1.0 Erste Fassung
 */
public class FullTimingSimulator {
    // EventQueue für diesen Simulator, wird im Konstruktor initialisiert
    private EventQueue	queue;
    // Die Eingangssignale aller möglichen Schaltungen. Es werden nicht in
    // jeder Schaltung alle Eingänge verwendet.
    private Signal writeEnable;
    private Signal clk;
    private Signal memIn[];
    private Signal memOAddr[];
    private Signal memIAddr[];
    private Signal a[],reset;

    // Die Ausgangssignale der dritten Schaltung
    private Signal memOut[];

    // Zähler für die aufgebauten Multiplexer und Zähler, um die Namen der internen
    // Signale besser generieren zu können.
    private int muxCnt;
    private int cntCnt;

    /**
     * Konstruktor, der die zu simulierende Schaltung aufbaut, den Ruhezustand
     * ermittelt und die Eingabe-Events erzeugt.
     * Simuliert wird je nach Argument eine der drei vorgegebenen Schaltungen
     *  1 = Einfacher Multiplexer 4 zu 1 
     *  2 = Einfacher synchroner, rücksetzbarer Zähler mit 4 Bit
     *  3 = Komplexe Schaltung mit einem Zähler vielen Latches und
     *      einigen Multiplexern
     */
    public FullTimingSimulator(int version) {
	// Erzeugt die EventQueue für diesen Simulator
	queue=new EventQueue();

	// Trägt diese EventQueue in ein statisches Feld der Klasse Event ein
	// Dazu muss Event die statische Methode setEventQueue(EventQueue e)
	// besitzen.
	Event.setEventQueue(queue);

	if (version==1) {
	    // Schaltung aufbauen
	    buildCircuit1();
	    // Ruhezustand berechnen
	    findSteadyState1();
	    // EventQueue mit Eingabe-Events füllen
	    setInputEvents1();
	} else if (version==2) {
	    // Schaltung aufbauen
	    buildCircuit2();
	    // Ruhezustand berechnen
	    findSteadyState2();
	    // EventQueue mit Eingabe-Events füllen
	    setInputEvents2();
	} else if (version==3) {
	    // Schaltung aufbauen
	    buildCircuit3();
	    // Ruhezustand berechnen
	    findSteadyState3();
	    // EventQueue mit Eingabe-Events füllen
	    setInputEvents3();
	}
    }

    /**
     * Diese Methode konstruiert einen Multiplexer aus den gegebenen
     * Eingangssignalen, den Steursignalen (und ihrer Negation) und legt den
     * Ausgang an das angegebene Signal. Die gegebene Methode funktioniert
     * maximal bis zu vier Eingängen.
     */
    private void buildMux(Signal[] inputs, Signal[] select, Signal[] nselect, Signal out) {
	muxCnt++;
	String prefix="Mux"+muxCnt;
	int numSBits=0,i;
	int numInputs=inputs.length;

	if (inputs.length>4) {
	    throw new RuntimeException("Too many mux inputs");
	}
	i=inputs.length>>1;
	while (i>0) {
	    numSBits++;
	    i>>=1;
	}
	
	Nand[] fstage=new Nand[numInputs];
	Nand   ostage=new Nand(numInputs,5);
	Signal[] maskedInp=new Signal[numInputs];
	for (i=0; i<numInputs; i++) {
	    maskedInp[i]=new Signal(prefix+"min"+i);
	    fstage[i]=new Nand(numSBits+1,5);
	    for (int j=0; j<numSBits; j++) {
		if ( (i&(1<<j)) != 0 ) {
		    fstage[i].setInput(j,select[j]);
		} else {
		    fstage[i].setInput(j,nselect[j]);
		}
	    }
	    fstage[i].setInput(numSBits,inputs[i]);
	    fstage[i].setOutput(maskedInp[i]);
	    ostage.setInput(i,maskedInp[i]);
	}
	ostage.setOutput(out);
    }

    /**
     * Diese Methode konstruiert einen synchronen, rücksetzbaren Zähler.
     * Übergeben werden ein Taktsignal, ein Reset-Signal und ein Array mit
     * Augangssignalen. Über die Größe dieses Arrays wird automatisch auch
     * die Breite des Zählers bestimmt.
     */
    private void buildSynCounter(Signal clk, Signal reset, Signal[] outputs) {
	cntCnt++;
	String prefix="Cnt"+cntCnt;
	int numBits=outputs.length;

	FF[] reg = new FF[numBits];
	Signal notRes=new Signal(prefix+"nReset");
	Not negCtrl=new Not(2);
	negCtrl.setInput(0,reset);
	negCtrl.setOutput(notRes);
	for (int i=0; i<numBits; i++) {
	    reg[i]=new FF(20);
	    reg[i].setInput(0,clk);
	    Signal regInput=new Signal(prefix+"regI"+i);
	    Signal newBit=new Signal(prefix+"muxI"+i);
	    if (i==0) {
		Not feedback=new Not(2);
		feedback.setInput(0,outputs[0]);
		feedback.setOutput(newBit);
	    } else if (i==1) {
		Exor feedback=new Exor(2,5);
		feedback.setInput(0,outputs[1]);
		feedback.setInput(1,outputs[0]);
		feedback.setOutput(newBit);
	    } else {
		Exor feedback=new Exor(2,5);
		And condition=new And(i,5);
		Signal conditionVal=new Signal(prefix+"CondVal"+i);
		for (int j=0; j<i; j++) {
		    condition.setInput(j,outputs[j]);
		}
		condition.setOutput(conditionVal);
		feedback.setInput(0,conditionVal);
		feedback.setInput(1,outputs[i]);
		feedback.setOutput(newBit);
	    }
		
	    And maskNewBit = new And(2,5);
	    Signal maskedBitVal=new Signal(prefix+"maskedBitVal"+i);
	    maskNewBit.setInput(0,notRes);
	    maskNewBit.setInput(1,newBit);
	    maskNewBit.setOutput(maskedBitVal);
	    reg[i].setInput(1,maskedBitVal);
	    reg[i].setOutput(outputs[i]);
	}
    }

    
    /**
     * Diese Methode konstruiert die Schaltung. Die erzeugten Gatter und die
     * inneren Signale sind nur in dieser Methode bekannt, da sie im Verlauf
     * der Simulation implizit durch die Events, bzw. die Signale angesprochen
     * werden.Simuliert wird ein einfacher Multiplexer der einen aus vier
     * Eingängen auswählt.
     */
    private void buildCircuit1() {
	memIn=new Signal[4];
	memIAddr=new Signal[2];
	Signal[] nmemIAddr=new Signal[2];
	for (int i=0; i<4; i++) {
	    memIn[i]=new Signal("memIn"+i);
	}
	for (int i=0; i<2; i++) {
	    memIAddr[i]=new Signal("memIAddr"+i);
	    nmemIAddr[i]=new Signal("nmemIAddr"+i);
	    Not negate=new Not(2);
	    negate.setInput(0,memIAddr[i]);
	    negate.setOutput(nmemIAddr[i]);
	}
	Signal out=new Signal("Out");
	buildMux(memIn,memIAddr,nmemIAddr,out);
    }

    /**
     * Diese Methode konstruiert eine weitere Schaltung.  Simuliert wird
     * ein einfacher vier Bit synchroner Zähler.
     */
    private void buildCircuit2() {
	reset=new Signal("Reset");
	clk=new Signal("Clk");
	a=new Signal[4];
	Signal[] out=new Signal[4];
	Buf[] buf = new Buf[4];
	for (int i=0; i<4; i++) {
	    a[i]=new Signal("CntOut"+i);
	    out[i]=new Signal("Out"+i);
	    buf[i]=new Buf(3);
	    buf[i].setInput(0,a[i]);
	    buf[i].setOutput(out[i]);
	}
	buildSynCounter(clk,reset,a);
    }

    /**
     * Diese Methode konstruiert die dritte mögliche Schaltung.  Diese ist
     * sehr kompliziert (enthält ca. 150 Gatter). Die genaue Funktion
     * können Sie ja mal versuchen herauszubekommen.
     */
    private void buildCircuit3() {
	writeEnable=new Signal("WriteEnable");
	clk=new Signal("Clk");
	memOut = new Signal[4];
	memIn = new Signal[4];
	memOAddr = new Signal[3];
	memIAddr = new Signal[3];

	// Werden intern für den Aufbau der Multiplexer gebraucht
	Signal nmemOAddr[] = new Signal[3];
	Signal nmemIAddr[] = new Signal[3];

	Signal memSelect[][] = new Signal[2][4];
	for (int i=0; i<4; i++) {
	    memSelect[0][i]=new Signal("memSelect0_"+i);
	    memSelect[1][i]=new Signal("memSelect1_"+i);
	    memOut[i]=new Signal("memOut"+i);
	    memIn[i]=new Signal("memIn"+i);
	}
	for (int i=0; i<3; i++) {
	    memOAddr[i]=new Signal("memOAddr"+i);
	    nmemOAddr[i]=new Signal("nmemOAddr"+i);
	    Not naddr=new Not(2);
	    naddr.setInput(0,memOAddr[i]);
	    naddr.setOutput(nmemOAddr[i]);
	    memIAddr[i]=new Signal("memIAddr"+i);
	    nmemIAddr[i]=new Signal("nmemIAddr"+i);
	    naddr=new Not(2);
	    naddr.setInput(0,memIAddr[i]);
	    naddr.setOutput(nmemIAddr[i]);
	}
	// Hier erfolgt die Instanziierung der eigentlichen Speichermatrix
	// Diese wird auch gleich mit den passenden Eingangssignalen 
	// und Gattern verbunden.
	Latch[][] mem=new Latch[8][4];
	for (int i=0; i<8; i++) {
	    for (int j=0;j<4; j++) {
		Latch l=new Latch(10);
		mem[i][j]=l;
		And enable=new And(4,5);
		Signal localEnable=new Signal("memLE"+i+"_"+j);
		for (int k=0; k<3; k++) {
		    if ( (i&(1<<k)) != 0 ) {
			enable.setInput(k,memIAddr[k]);
		    } else {
			enable.setInput(k,nmemIAddr[k]);
		    }
		}
		enable.setInput(3,writeEnable);
		enable.setOutput(localEnable);
		l.setInput(0,localEnable);
		l.setInput(1,memIn[j]);
	    }
	}
	// Hier wird nun der Ausgangsteil der Speichermatrix aufgebaut
	// Dazu werden zunächst die beiden Hälften des Speichers getrennt
	// gemultiplext, danach werden diese beiden Signale dann zusammen-
	// gefasst.
	Signal[] memDint=new Signal[4];
	for (int i=0; i<4; i++) {
	    for (int j=0; j<4; j++) {
		memDint[j] = new Signal("memDint"+j+"_"+i);
		mem[j][i].setOutput(memDint[j]);
	    }
	    buildMux(memDint, memOAddr, nmemOAddr, memSelect[0][i]);
	}
	for (int i=0; i<4; i++) {
	    for (int j=0; j<4; j++) {
		memDint[j] = new Signal("memDint"+(j+4)+"_"+i);
		mem[j+4][i].setOutput(memDint[j]);
	    }
	    buildMux(memDint, memOAddr, nmemOAddr, memSelect[1][i]);
	}
	Signal[] upperAddr=new Signal[1];
	upperAddr[0]=memOAddr[2];
	Signal[] nupperAddr=new Signal[1];
	nupperAddr[0]=nmemOAddr[2];
	Signal[] h=new Signal[2];
	for (int i=0; i<4; i++) {
	    h[0]=memSelect[0][i];
	    h[1]=memSelect[1][i];
	    buildMux(h, upperAddr, nupperAddr, memOut[i]);
	}

	// Hier wird nun die Adressgenerierung für den Speicher aufgebaut.
	// Im wesentlichen handelt es sich hier um einen synchronen,
	// rücksetzbaren Zähler, der immer dann zurückgesetzt wird, wenn
	// das oberste Bit des Speicherinhalts eine 1 liefert.
	buildSynCounter(clk, memOut[3], memOAddr);

	// Zählerausgänge auch auf Signale führen, damit man die Adressierung
	// nachvollziehen kann.
	Buf[] obuf=new Buf[3];
	for (int i=0; i<3; i++) {
	    obuf[i]=new Buf(1);
	    Signal out_n=new Signal("CntAddr"+i);
	    obuf[i].setInput(0,memOAddr[i]);
	    obuf[i].setOutput(out_n);
	}
    }
    
    /**
     * Diese Methode ermittelt den Ruhezustand der Schaltung. Dazu werden
     * vernünftige Initialwerte an die Eingänge angelegt. Diese Initialwerte
     * müssen mindestens einmal durch die Schaltung propagiert werden,
     * bis sich ein stabiler Zustand einstellt. Um das festzustellen gibt
     * es verschiedene Methoden (im Gatter mitzählen, wie oft sich der Wert
     * ändert. Im Signal mitzählen, wie oft es geändert wurde).
     * Bei diesem Propagieren darf natürlich nicht mit den Zeitverzögerungen
     * gearbeitet werden.  Sie können also im Grunde die Wert-Propagierung
     * aus der ersten Teilaufgabe benutzen.
     */
    private void findSteadyState1() {
	for (int i=0; i<4; i++) {
	    memIn[i].setValue(false);
	}
	for (int i=0; i<2; i++) {
	    memIAddr[i].setValue(false);
	}
    }

    /**
     * Genau wie die vorhergehende Methode nur für die Schaltung zwei
     */
    private void findSteadyState2() {
	clk.setValue(false);
	reset.setValue(false);
    }

    /**
     * Genau wie die vorhergehende Methode nur für die Schaltung drei
     */
    private void findSteadyState3() {
	for (int i=0; i<4; i++) {
	    memIn[i].setValue(false);
	}
	for (int i=0; i<3; i++) {
	    memIAddr[i].setValue(false);
	}
	writeEnable.setValue(false);
	clk.setValue(false);
    }

    /**
     * Diese Methode erzeugt eine Reihe von Eingabe-Events, die dann zur
     * Stimulation der Schaltung dienen.  Die Events werden durch ihren
     * eigenen Konstruktor in die EventQueue eingetragen, so dass hier nur
     * das Erzeugen der Events zu sehen ist.
     * Jedes Event bekommt beim Erzeugen das betroffene Signal, den Zeitpunkt
     * und den <bold>neuen Wert</bold> mit.
     */
    private void setInputEvents1() {
	new Event(memIn[0],50,true);
	new Event(memIn[2],100,true);
	new Event(memIAddr[0],150,true);
	new Event(memIAddr[1],200,true);
	new Event(memIn[3],250,true);
	new Event(memIAddr[0],300,false);
	new Event(memIn[2],350,false);
    }

    /**
     * Ebenfalls die Erzeugung der Stimuli für die zweite eingebaute Schaltung
     *
     */
    private void setInputEvents2() {
	for (int i=0; i<20; i++) {
	    new Event(clk, 50+i*100, true);
	    new Event(clk, (i+1)*100, false);
	}
	new Event(reset,1020,true);
	new Event(reset,1030,false);

	new Event(reset,1830,true);
	new Event(reset,1860,false);
    }

    /**
     * Ebenfalls die Erzeugung der Stimuli für die dritte eingebaute Schaltung
     *
     */
    private void setInputEvents3() {
	for (int i=0; i<8; i++) {
	    for (int j=0; j<3; j++) {
		new Event(memIn[j],i*100,((7-i)&(1<<j))!=0);
		new Event(memIAddr[j],i*100,(i&(1<<j))!=0);
	    }
	    new Event(memIn[3],i*100,(i==5));
	    new Event(writeEnable,i*100+20,true);
	    new Event(writeEnable,i*100+50,false);
	}

	for (int i=0; i<7; i++) {
	    new Event(clk,50+i*100+1000,true);
	    new Event(clk,(i+1)*100+1000,false);
	}
    }
    /**
     * Diese Methode führt die eigentliche Simulation durch. Dazu wird
     * geprüft, ob in der EventQueue noch weitere Events vorhanden sind. Ist
     * dies der Fall, dann wird das nächste anstehende Event behandelt. Dazu
     * muss das Event die Methode propagate() zur Verfügung stellen, die
     * dann das betroffene Signal informiert.
     */
    public void simulate() {
	while (queue.hasMore()) {
	    Event e=queue.getFirst();

	    e.propagate();
	}
    }
    
    /**
     * Main Methode dieser Klasse. Sie müssen das im Moment noch nicht
     * verstehen. Diese Methode wird benötigt, wenn Sie den Simulator ohne
     * BlueJ laufen lassen wollen. Wenn Sie diese Klasse in BlueJ nutzen,
     * dann ignorieren Sie diese Methode einfach.
     * 
     * Wenn Sie die verschiedenen Schaltungen testen wollen, dann müssen
     * Sie den Parameter des Konstruktors entsprechend ändern. Das können
     * Sie natürlich auch durch Auswertung der Kommandozeilenparameter tuen.
     */
    static public void main(String[] args) {
	FullTimingSimulator	t=new FullTimingSimulator(1);
	t.simulate();
    }
}
