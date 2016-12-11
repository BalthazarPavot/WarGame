package wargame.unit;

import java.io.Serializable;
import java.util.ArrayList;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.widgets.AnimationWidget;

/**
 * Represent a unit, and all its characteristics: its position, direction, stacked positions, animation...
 */
public abstract class Unit implements IUnit, Serializable {

	private static final long serialVersionUID = 5393378027928712536L;

	protected final static double CHARACTERISTIC_GAIN = 0.20;

	public static String NAME = "unit";
	public static final int NO_ACTION = 0;
	public static final int MOVE_ACTION = 1;
	public static final int ATTACK_ACTION = 2;

	/* public static final int UPWARD_DIRECRTION = 0; public static final int LEFTWARD_DIRECRTION = 1; public
	 * static final int DOWNWARD_DIRECRTION = 2; public static final int RIGHTWARD_DIRECRTION = 3;
	 * 
	 * public static final int UPLEFTWARD_DIRECRTION = 4; public static final int UPRIGHTWARD_DIRECRTION = 5;
	 * public static final int DOWNRIGHTWARD_DIRECRTION = 6; public static final int DOWNLEFTWARD_DIRECRTION =
	 * 7; */
	public static final int UPWARD_DIRECRTION = 0;
	public static final int DOWNWARD_DIRECRTION = 1;
	public static final int RIGHTWARD_DIRECRTION = 2;
	public static final int LEFTWARD_DIRECRTION = 3;

	public static final int UPRIGHTWARD_DIRECRTION = 4;
	public static final int DOWNRIGHTWARD_DIRECRTION = 5;
	public static final int DOWNLEFTWARD_DIRECRTION = 6;
	public static final int UPLEFTWARD_DIRECRTION = 7;

	protected Characteristic characteristics;
	public int staticPosition = DOWNWARD_DIRECRTION;
	public Position position;
	public ArrayList<Position> stackedPositions;
	public int currentPosition;
	public boolean hasPlayed = false;
	public ArrayList<AnimationWidget> walkAnimations;

	public Unit(Position position, GameContext gameContext) {
		this.position = position;
		if (position.equals(gameContext.getMap().getEnnemyPopArea()))
			this.walkAnimations = gameContext.getAnimationHandler().getEnemyWalkSprites(this);
		else
			this.walkAnimations = gameContext.getAnimationHandler().getUnitWalkSprites(this);
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
						staticPosition = RIGHTWARD_DIRECRTION;
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
						staticPosition = DOWNRIGHTWARD_DIRECRTION;
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
		if (walkAnimations == null)
			return null;
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

	public void moveTo(int x, int y, Map map) {
		moveTo(new Position(x, y), map);
	}

	public ArrayList<Position> squaresInSight() {
		return position.inRange(characteristics.sight);
	}

	public ArrayList<Position> squaresInRange() {
		return position.inRange(characteristics.range);
	}

	public boolean isVisibleBy(ArrayList<Unit> units) {
		for (Unit unit : units)
			if (isVisibleBy(unit))
				return true;
		return false;
	}

	private boolean isVisibleBy(Unit unit) {
		return unit.getPosition().distance(position) <= unit.getCharacteristics().sight * Map.squareWidth;
	}

	public boolean inAttackRangeOf(Unit unit) {
		return unit.getPosition().distance(position) <= unit.getCharacteristics().range * Map.squareWidth;
	}

	public ArrayList<Position> movePossibilities(Map map) {
		ArrayList<Position> movePossibilities;

		movePossibilities = position.inRange(characteristics.currentMovePoints);
		for (Position pos : new ArrayList<Position>(movePossibilities.subList(0, movePossibilities.size())))
			if (canMove(map, pos) == null)
				movePossibilities.remove(pos);
		return movePossibilities;
	}

	public ArrayList<Position> attackPossibilities() {
		return squaresInRange();
	}

	public String getAllieName() {
		switch (this.getClass().getName()) {
		case "wargame.unit.Bird":
			return "Wyvern";
		case "wargame.unit.Bowman":
			return "Bowman";
		case "wargame.unit.Healer":
			return "White magican";
		case "wargame.unit.Knight":
			return "Knight";
		case "wargame.unit.Soldier":
			return "Soldier";
		case "wargame.unit.Wizard":
			return "Wizard";
		default:
			System.out.println(this.getClass().getName());
			return "Allie unit";
		}
	}

	public String getEnemyName() {
		switch (this.getClass().getName()) {
		case "wargame.unit.Bird":
			return "Dragon";
		case "wargame.unit.Bowman":
			return "Troll";
		case "wargame.unit.Healer":
			return "Shaman";
		case "wargame.unit.Knight":
			return "No Head Knight";
		case "wargame.unit.Soldier":
			return "Beetle";
		case "wargame.unit.Wizard":
			return "Wizard";
		default:
			return "Enemy unit";
		}
	}

	public String getAttackDescription() {
		switch (this.getClass().getName()) {
		case "wargame.unit.Bird":
			return "Blunt: " + characteristics.attackBlunt;
		case "wargame.unit.Bowman":
			return "Pierce: " + characteristics.attackPercing;
		case "wargame.unit.Healer":
			return "Heal: " + characteristics.attackMagic;
		case "wargame.unit.Knight":
			return "Blunt: " + characteristics.attackBlunt;
		case "wargame.unit.Soldier":
			return "Slash: " + characteristics.attackSlashing;
		case "wargame.unit.Wizard":
			return "Magic: " + characteristics.attackMagic;
		default:
			return "???: " + (characteristics.attackBlunt + characteristics.attackSlashing
					+ characteristics.attackPercing + characteristics.attackMagic);
		}
	}

}