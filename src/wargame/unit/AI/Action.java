package wargame.unit.AI;


import wargame.basic_types.Position;

/**
 * A class which wrap the action the AI do
 * 
 * @author Romain Pelegrin
 */
public class Action {

	/* Enumerations */
	public enum operation {
		MOVE, ATTACK, HEAL, REST;
	}

	/* Attribute of the class */
	public Position position;
	public operation ope;
	
	/* Methods */

	public Action ()  {
	}

	public Action (Position pos, operation operation)  {
		ope = operation ;
		position = pos ;
	}

//	public String toString (){
//		String out = "";
//		out += ope + " ";
//		if (this.position != null){
//			for (Position pos : this.position)
//				out += pos.toString();
//		} else {
//			out += " No position for that action !";
//		}
//		return out;
//	}
}
