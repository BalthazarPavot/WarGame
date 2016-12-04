package wargame.unit;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.map.SpriteHandler;
import wargame.widgets.AnimationWidget;

/**
 * Represent a unit, and all its characteristics: its position, direction, stacked positions, animation...
 */
public abstract class Unit implements IUnit {

	public static final int NO_ACTION = 0;
	public static final int MOVE_ACTION = 1;
	public static final int ATTACK_ACTION = 2;

	public static final int UPWARD_DIRECRTION = 0;
	public static final int LEFTWARD_DIRECRTION = 1;
	public static final int DOWNWARD_DIRECRTION = 2;
	public static final int RIGHTWARD_DIRECRTION = 3;

	public static final int UPLEFTWARD_DIRECRTION = 4;
	public static final int UPRIGHTWARD_DIRECRTION = 5;
	public static final int DOWNRIGHTWARD_DIRECRTION = 6;
	public static final int DOWNLEFTWARD_DIRECRTION = 7;

	protected Caracteristique caracteristique;
	public int staticPosition = DOWNWARD_DIRECRTION;
	public Position position;
	public ArrayList<Position> stackedPositions;
	public int currentPosition;
	public boolean hasPlayed = false;
	public ArrayList<AnimationWidget> walkAnimations;

	public Unit(Position position, SpriteHandler spriteHandler) {
		this.position = position;
		this.walkAnimations = spriteHandler.getUnitWalkSprites(this);
	}

	public ArrayList<Position> getPathToEnd() {
		if (stackedPositions == null || !(currentPosition < stackedPositions.size() - 1))
			return null;
		return new ArrayList<Position>(
				stackedPositions.subList(currentPosition, stackedPositions.size() - 1));
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
		this.currentPosition = 0;
	}

	public boolean move() {
		Position nextPosition;
		if (stackedPositions != null) {
			if (currentPosition < stackedPositions.size() - 1) {
				nextPosition = stackedPositions.get(++currentPosition);
				if (nextPosition.getX() > position.getX())
					staticPosition = (nextPosition.getY() > position.getY()) ? DOWNRIGHTWARD_DIRECRTION
							: (nextPosition.getY() < position.getY()) ? UPRIGHTWARD_DIRECRTION
									: RIGHTWARD_DIRECRTION;
				else if (nextPosition.getX() < position.getX())
					staticPosition = (nextPosition.getY() > position.getY()) ? DOWNLEFTWARD_DIRECRTION
							: (nextPosition.getY() < position.getY()) ? UPLEFTWARD_DIRECRTION
									: LEFTWARD_DIRECRTION;
				else if (nextPosition.getY() > position.getY())
					staticPosition = (nextPosition.getX() > position.getX()) ? DOWNLEFTWARD_DIRECRTION
							: (nextPosition.getX() < position.getX()) ? DOWNLEFTWARD_DIRECRTION
									: DOWNWARD_DIRECRTION;
				else if (nextPosition.getY() < position.getY())
					staticPosition = (nextPosition.getX() > position.getX()) ? UPRIGHTWARD_DIRECRTION
							: (nextPosition.getX() < position.getX()) ? UPLEFTWARD_DIRECRTION
									: UPWARD_DIRECRTION;
				this.position = nextPosition;
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
			return move() ? MOVE_ACTION : NO_ACTION;
		}
		return NO_ACTION;
	}

	public Caracteristique getCaracteristique() {
		return caracteristique;
	}

	public void setCaracteristique(Caracteristique caracteristique) {
		this.caracteristique = caracteristique;
	}

	public Position getPosition() {
		return position;
	}

	public Position getPreviousPosition() {
		if (stackedPositions == null)
			return null;
		return currentPosition != 0 ? stackedPositions.get(currentPosition - 1) : position;
	}

	public AnimationWidget getCurrentWalkAnimation() {
		return walkAnimations.get(staticPosition);
	}
}