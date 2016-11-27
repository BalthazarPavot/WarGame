package wargame.unit;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.map.SpriteHandler;
import wargame.widgets.AnimationWidget;

/**
 * Represent a unit, and all its characteristics: its position, direction, stacked positions, animation...
 * 
 * @author Balthazar Pavot
 *
 */
public class Unit {

	public static final int NO_ACTION = 0;
	public static final int MOVE_ACTION = 1;
	public static final int ATTACK_ACTION = 2;

	public static final int UPWARD_DIRECRTION = 0;
	public static final int RIGHTWARD_DIRECRTION = 1;
	public static final int DOWNWARD_DIRECRTION = 2;
	public static final int LEFTWARD_DIRECRTION = 3;

	public int staticPosition = DOWNWARD_DIRECRTION;
	public Position position;
	public ArrayList<Position> stackedPositions;
	public int curentPosition;
	public boolean hasPlayed = false;
	public AnimationWidget[] walkAnimations ;

	public Unit(Position position, SpriteHandler spriteHandler) {
		this.position = position;
		this.walkAnimations = spriteHandler.getUnitWalkSprites(this) ;
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

	public boolean move() {
		Position nextPosition;
		if (stackedPositions != null) {
			if (curentPosition < stackedPositions.size()) {
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
			} else {
				stackedPositions = null;
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public ArrayList<Position> canMove(Map map, Position destination) {
		return null;
	}

	public int play(ArrayList<Unit> playerUnits, ArrayList<Unit> ennemyUnits, Map map) {
		if (!hasPlayed) {
			hasPlayed = true;
			return move() ? MOVE_ACTION : NO_ACTION ;
		}
		return NO_ACTION;
	}

	public Object getMaCara() {
		return null;
	}

	public Position getPosition() {
		return position;
	}

	public AnimationWidget getCurrentWalkAnimation() {
		System.out.println(staticPosition);
		System.out.println(walkAnimations);
		System.out.println(walkAnimations[staticPosition]);
		return walkAnimations[staticPosition];
	}
}