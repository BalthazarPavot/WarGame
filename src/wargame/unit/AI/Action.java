package wargame.unit.AI;

import wargame.basic_types.Position;

public class Action {

	/* Enumerations */
	public enum operation {
		ATTACK, HEAL, WAIT, FIRE, UNDEF ;
	}
	
	/* Attribut of the class */
	private Position positionFinal;
	private Position targetPosition;
	private operation ope;

	/* Getters and setters */
	public Position getPositionFinal() {
		return positionFinal;
	}

	public void setPositionFinal(Position positionFinal) {
		this.positionFinal = positionFinal;
	}

	public Position getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Position targetPosition) {
		this.targetPosition = targetPosition;
	}

	public operation getOpe() {
		return ope;
	}

	public void setOpe(operation ope) {
		this.ope = ope;
	}
	
	/* Methods */
	public void initialize(){
		this.ope = operation.UNDEF;
		this.positionFinal = null;
		this.targetPosition = null;
	}


}
