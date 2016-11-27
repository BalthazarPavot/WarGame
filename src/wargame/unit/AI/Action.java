package wargame.unit.AI;

import wargame.basic_types.Position;

public class Action {

	public enum operation {
		ATTACK, HEAL, WAIT, FIRE
	}

	private Position positionFinal;
	private Position targetPosition;
	private operation ope;

}
