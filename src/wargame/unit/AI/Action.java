package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;

public class Action {

	/* Enumerations */
	public enum operation {
		MOVE, ATTACK, HEAL, WAIT;
	}
	
	/* Attribute of the class */
	public ArrayList<Position> position;
	public operation ope;
}
