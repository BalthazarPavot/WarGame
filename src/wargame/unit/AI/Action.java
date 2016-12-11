package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;

/**
 * A class which wrap the action the AI do
 *
 */
public class Action {

	/* Enumerations */
	public enum operation {
		MOVE, ATTACK, HEAL, REST;
	}

	/* Attribute of the class */
	public ArrayList<Position> position;
	public operation ope;
	
	/* Methods */
	
	public String toString (){
		String out = "";
		out += ope + " ";
		if (this.position != null){
			for (Position pos : this.position)
				out += pos.toString();
		} else {
			out += " No position for that action !";
		}
		return out;
	}
}
