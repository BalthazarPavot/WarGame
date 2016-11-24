package UnitPackage;

import java.lang.reflect.Array;
import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;

public abstract class Unit implements IUnit {

	protected Caracteristique caracteristique;
	protected boolean isActive;
	public Position position;
	public ArrayList<Position> stackedPosition;
	public int currentPosition;

	public Unit(Position position) {
		this.position = position;
	}

	public Caracteristique getCaracteristique() {
		return caracteristique;
	}

	public void setCaracteristique(Caracteristique caracteristique) {
		this.caracteristique = caracteristique;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setmove(ArrayList<Position> currentPath) {
		stackedPosition = currentPath;
		this.currentPosition = 0;
	}

	public void move() {
		if (stackedPosition != null && currentPosition < stackedPosition.size()) {
			this.position = stackedPosition.get(currentPosition++);
		} else
			stackedPosition = null;
	}
	public ArrayList<Position> canMove(Map map,Position destination)
	{
		ArrayList<Position> distance=null;
		if(this.caracteristique.isFlying())
		{
			distance = map.pathByFlying(position, destination);
		}
		else
		{
			distance= map.pathByWalking(position, destination);
		}
		return distance;
	}
}
