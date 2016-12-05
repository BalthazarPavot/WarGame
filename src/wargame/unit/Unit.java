package wargame.unit;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.map.SpriteHandler;
import wargame.widgets.AnimationWidget;
import wargame.unit.AI.AI;

/**
 * Represent a unit, and all its characteristics: its position, direction, stacked positions, animation...
 */
public abstract class Unit implements IUnit {

	protected final static double CHARACTERISTIC_GAIN = 0.20;

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

	protected Characteristic characteristics;
	public int staticPosition = DOWNWARD_DIRECRTION;
	public Position position;
	private ArrayList<Position> movementArea;
	public ArrayList<Position> stackedPositions;
	public int currentPosition;
	public boolean hasPlayed = false;
	public AI ai;
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

	ArrayList<Position> getMovementArea() {
		return this.movementArea;
	}

	public boolean inflictDamage(Unit unit) {
		return false;
	}

	public void setMove(ArrayList<Position> currentPath) {
		stackedPositions = currentPath;
		this.currentPosition = 0;
	}

	public boolean move() {
		Position nextPosition;

		if (characteristics.currentMovePoints <= 0)
			return false;
		if (stackedPositions != null) {
			if (currentPosition < stackedPositions.size() - 1) {
				nextPosition = stackedPositions.get(++currentPosition);
				if (nextPosition.getX() > position.getX()) {
					if (nextPosition.getY() > position.getY()) {
						staticPosition = DOWNRIGHTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else if (nextPosition.getY() < position.getY()) {
						staticPosition = UPRIGHTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else {
						staticPosition = UPRIGHTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 1;
					}
				} else if (nextPosition.getX() < position.getX()) {
					if (nextPosition.getY() > position.getY()) {
						staticPosition = DOWNLEFTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else if (nextPosition.getY() < position.getY()) {
						staticPosition = UPLEFTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else {
						staticPosition = LEFTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 1;
					}
				} else if (nextPosition.getY() > position.getY()) {
					if (nextPosition.getX() > position.getX()) {
						staticPosition = DOWNLEFTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else if (nextPosition.getX() < position.getX()) {
						staticPosition = DOWNLEFTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else {
						staticPosition = DOWNWARD_DIRECRTION;
						characteristics.currentMovePoints -= 1;
					}
				} else if (nextPosition.getY() < position.getY()) {
					if (nextPosition.getX() > position.getX()) {
						staticPosition = UPRIGHTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else if (nextPosition.getX() < position.getX()) {
						staticPosition = UPLEFTWARD_DIRECRTION;
						characteristics.currentMovePoints -= 2;
					} else {
						staticPosition = UPWARD_DIRECRTION;
						characteristics.currentMovePoints -= 1;
					}
				}
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
		if (canFly())
			return map.pathByFlying(getPosition(), destination);
		return map.pathByWalking(getPosition(), destination);
	}

	/**
	 * Return true if the unit can reach the position endPos in 1 round
	 * 
	 * @param map
	 * @param endPos
	 * @return
	 */
	public boolean canReach(Map map, Position endPos) {
		int distance;
		ArrayList<Position> path;

		path = this.canMove(map, endPos);
		distance = Position.movementCounter(path);
		// TODO Check if there are error at the next line
		return distance <= this.getMaCara().getNbCaseDep();
	}

	public int play(ArrayList<Unit> playerUnits, ArrayList<Unit> ennemyUnits, Map map) {
		if (!hasPlayed) {
			return move() ? MOVE_ACTION : NO_ACTION;
		}
		return NO_ACTION;
	}

	public Characteristic getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Characteristic characteristics) {
		this.characteristics = characteristics;
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

	public void gainLife(int value) {
		if (value > 0)
			this.characteristics.currentLife += value;
		if (this.characteristics.currentLife > this.characteristics.life)
			this.characteristics.currentLife = this.characteristics.life;
	}

	public boolean heal(Unit unit) {
		return false;
	}

	public boolean takePercingDamages(int value) {
		return takeDamage(value - this.characteristics.defensePercing);
	}

	public boolean takeBluntDamages(int value) {
		return takeDamage(value - this.characteristics.defenseBlunt);
	}

	public boolean takeMagicDamages(int value) {
		return takeDamage(value - this.characteristics.defenseMagic);
	}

	public boolean takeSlachingDamages(int value) {
		return takeDamage(value - this.characteristics.defenseSlashing);
	}

	private boolean takeDamage(int value) {
		if (value > 0) {
			this.characteristics.currentLife -= value;
			if (this.characteristics.currentLife < 0)
				this.characteristics.currentLife = 0;
		}
		return this.characteristics.currentLife == 0;
	}

	public void moveTo(Position destination, Map map) {
		ArrayList<Position> path;

		path = canMove(map, destination);
		if (path != null)
			setMove(path);
	}

}