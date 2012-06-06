import java.util.ArrayList;
public class Signal {
	private boolean value = false;	//current state of this signal instance, either true or false
	private String name = "";		//name of this signal, gets used in setValue() to output readable text
	private ArrayList<Nand> connectedGates = new ArrayList();		//list of gates connected to this signal cable
	
	public Signal(String name) {	//initialize save the given name of this object
		this.name = name;			//in the name var
	}
	public String getName() {	//returns the name of this object
		return this.name;		//booooooring...
	}
	public void setValue(boolean value) {	//set the state of this signal wire
		if (this.value!=value) {			//to a given one, but only, if it
			this.value = value;				//is different from the current one
			if (connectedGates.isEmpty()) {
				System.out.println(this.name+"'s state: "+this.value);	//prints value to
			}															//console when not
		}																//connected to anything
		call(); //tell gates to recalculate their output because of new value
	}
	public boolean getValue() {	//returns current state of object (true/false)
		return value;
	}
	public void call() { 						//call every gate registered
		for (Nand gatter : connectedGates) {	//in the arraylist. Add a gate
			gatter.calculate();					//by calling callMe()
		}
	}
	public void callMe(Nand gate) {	//is called by a gate when the signal object is added to it
		connectedGates.add(gate);	//add a gate by making it call Signal.callMe(this)
	}
}
