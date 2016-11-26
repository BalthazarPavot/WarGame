package wargame.unit;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;

public class Unit {
	public static int UPWARD_DIRECRTION = 0;
	public static int DOWNWARD_DIRECRTION = 2;
	public static int RIGHTWARD_DIRECRTION = 1;
	public static int LEFTWARD_DIRECRTION = 3;
	public int staticPosition = DOWNWARD_DIRECRTION;
	public Position position;
	public ArrayList<Position> stackedPositions;
	public int curentPosition;
	public boolean hasPlayed = false;

	public Unit(Position position) {
		this.position = position;
	}

	public boolean isClicked(Position pos) {
		return pos.getX() >= position.getX() && pos.getY() >= position.getY()
				&& pos.getX() <= position.getX() + Map.squareWidth
				&& pos.getY() <= position.getY() + Map.squareHeight;
	}

	public boolean inflictDamage(Unit unit) {
		return false;
	}

	public boolean heal(Unit unit) {
		return false;
	}

	public void setMove(ArrayList<Position> currentPath) {
		stackedPositions = currentPath;
		this.curentPosition = 0;
	}

	public void move() {
		Position nextPosition;
		if (stackedPositions != null && curentPosition < stackedPositions.size()) {
			if (curentPosition < stackedPositions.size() - 1) {
				nextPosition = stackedPositions.get(curentPosition + 1);
				if (nextPosition.getX() > position.getX())
					staticPosition = LEFTWARD_DIRECRTION;
				else if (nextPosition.getX() < position.getX())
					staticPosition = RIGHTWARD_DIRECRTION;
				else if (nextPosition.getY() > position.getY())
					staticPosition = DOWNWARD_DIRECRTION;
				else if (nextPosition.getY() < position.getY())
					staticPosition = UPWARD_DIRECRTION;
			}
			this.position = stackedPositions.get(curentPosition++);
		} else
			stackedPositions = null;
	}

	public ArrayList<Position> canMove(Map map, Position destination) {
		return null;
	}

	public void play(ArrayList<Unit> playerUnits, ArrayList<Unit> ennemyUnits, Map map) {
		move();
	}

	public Object getMaCara() {
		return null;
	}

	public Position getPosition() {
		return position;
	}
}