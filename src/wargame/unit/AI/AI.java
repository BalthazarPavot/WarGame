package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Bird;
import wargame.unit.Bowman;
import wargame.unit.Characteristic;
import wargame.unit.Healer;
import wargame.unit.Knight;
import wargame.unit.Soldier;
import wargame.unit.Unit;
import wargame.unit.Wizard;
import wargame.unit.AI.Action.operation;

/**
 * That class regroup methods and attribute common with all AI
 * 
 */
public abstract class AI implements IAI {

	protected final static int SCORE_GO_TO_THE_FIGHT = 0;
	protected final static int SCORE_SAFE_MARKSMAN = 25;
	protected final static int SCORE_SAFE_MELEE = 10;

	/* Enumerations */
	protected enum healhPoint {
		HIGH, MEDIUM, LOW, VERY_LOW
	}

	/* Constants used for the state "life" */
	protected static final int LIMIT_LIFE_HIGH = 50;
	protected static final int LIMIT_LIFE_MEDIUM = 25;
	protected static final int LIMIT_LIFE_LOW = 10;

	/* Attribute of the class */
	protected boolean blocked; /*
								 * blocked is true when the unit can't flee
								 * (because of the map and enemy)
								 */
	protected healhPoint life;
	protected Unit unitLinked;
	public Squad squad;
	protected ArrayList<Action> actList;
	protected ArrayList<Position> areaMovement;

	/* Constructor */
	public AI(Unit unit) {
		this.unitLinked = unit;
	}

	/**
	 * Set the state "life" of the unit.
	 * 
	 * @param c
	 */
	public void setLife() {
		int hitPointRate;
		Characteristic c;

		c = unitLinked.getCharacteristics();
		hitPointRate = (int) (c.currentLife * 100) / c.life;

		if (hitPointRate > LIMIT_LIFE_HIGH) {
			this.life = healhPoint.HIGH;
		} else if (hitPointRate > LIMIT_LIFE_MEDIUM) {
			this.life = healhPoint.MEDIUM;
		} else if (hitPointRate > LIMIT_LIFE_LOW) {
			this.life = healhPoint.HIGH;
		} else {
			this.life = healhPoint.VERY_LOW;
		}
	}

	public healhPoint getLife() {
		return life;
	}

	public ArrayList<Action> getActList() {
		return actList;
	}

	public void setActList(ArrayList<Action> actList) {
		this.actList = actList;
	}

	/**
	 * Group near unit in squad, it allows unit to make a better choice of
	 * action.
	 * 
	 * @param allyList
	 * @param map
	 */
	public void searchSquad(ArrayList<Unit> allyList, Map map) {
		for (Unit ally : allyList) {
			if (ally != this.unitLinked
					&& (ally.canReach(map, this.unitLinked.getPosition()) || (this.unitLinked
							.canReach(map, ally.getPosition())))) {
				if (this.squad == null && ally.ai.squad != null)
					ally.ai.squad.add(this.unitLinked);
				else if (this.squad != null && this.squad == null)
					this.squad.add(ally.ai.unitLinked);
				else if (this.squad == null && this.squad == null)
					new Squad(ally.ai.unitLinked, this.unitLinked);
				else if (this.squad != null && this.squad == null)
					this.squad.merge(ally.ai.squad);
			}
		}
		if (this.squad == null)
			this.squad = new Squad(this.unitLinked);
	}

	/**
	 * The AI in considered as "blocked" if it can't flee against enemy.
	 * 
	 * @param enemyList
	 * @param thisUnit
	 * @param map
	 */
	public void setBlocked(ArrayList<Unit> enemyList, Map map) {

		ArrayList<Position> reachablePosition = new ArrayList<Position>();

		reachablePosition = getMovemmentPerimeter(map);
		reachablePosition.add(this.unitLinked.getPosition());
		for (Position pos : reachablePosition) {
			for (Unit u : enemyList) {
				if (this.unitLinked.getPosition().distance(u.getPosition()) <= u
						.getCharacteristics().currentMovePoints)
					reachablePosition.remove(pos);
			}
		}
		if (reachablePosition.size() != 0)
			this.blocked = false;
		else
			this.blocked = true;
	}

	/* Methods */

	/**
	 * Methods called for each unit, it set its states, search the best action
	 * it can do and then execute it.
	 * 
	 * @param allyList
	 * @param enemyList
	 * @param map
	 */
	public void play(ArrayList<Unit> allyList, ArrayList<Unit> enemyList,
			Map map) {
		setActList(new ArrayList<Action>());
		searchSquad(allyList, map);
		setLife();
		setBlocked(enemyList, map);
		fillAction(enemyList, map);
		executeActions(allyList, enemyList);
	}

	/**
	 * Give a list of all positions at max range of the AI's unit movement
	 * 
	 * @param map
	 * @return movementPerimeter
	 */
	public ArrayList<Position> getMovemmentPerimeter(Map map) {
		ArrayList<Position> movementPerimeter = new ArrayList<Position>();
		int dx;
		int dy;
		int i;
		int changeOrientation;
		int movement;
		Unit u;
		Position pos = new Position();

		dx = 1 * Map.squareWidth;
		dy = -1 * Map.squareHeight;
		i = 0;
		u = this.unitLinked;
		changeOrientation = 0;
		movement = this.unitLinked.getCharacteristics().currentMovePoints;
		pos.setX(u.getPosition().getX() + movement);
		while (i < movement * 4) {
			pos.setX(pos.getX() + dx);
			pos.setX(pos.getY() + dy);
			if (u.canReach(map, pos)) {
				Position posToAdd = new Position(pos.getX(), pos.getY());
				movementPerimeter.add(posToAdd);
			}
			++i;
			if (i % movement == 0) {
				switch (changeOrientation) {
				case 0:
					dx = -Map.squareWidth;
					break;
				case 1:
					dy = Map.squareHeight;
					break;
				case 2:
					dx = Map.squareWidth;
					break;
				}
				++changeOrientation;
			}
		}
		return movementPerimeter;
	}

	/**
	 * Check if there are one or more enemy who can hit this unit.
	 * 
	 * @param enemyList
	 * @param map
	 * @return boolean
	 */
	public boolean isSafe(ArrayList<Unit> enemyList, Map map) {
		ArrayList<Unit> allyList = new ArrayList<Unit>();
		allyList.add(this.unitLinked);
		for (Unit enemy : enemyList) {
			if (enemy.canHit(map, allyList).contains(this.unitLinked))
				return false;
		}
		return true;
	}

	/**
	 * Method which return a list of target who can be reached by the AI, using
	 * it's movement + range but ignoring obstacle (to decrease the complexity
	 * of the algorithm. This methods have to be used as a first test and then
	 * be completed by an other to take care of obstacles
	 * 
	 * @param enemyList
	 * @return targetList
	 */
	public ArrayList<Unit> getAllTargetInRange(ArrayList<Unit> enemyList) {
		ArrayList<Unit> targetList = new ArrayList<Unit>();
		for (Unit enemy : enemyList) {
			if (enemy.position.distance(this.unitLinked.position) <= (this.unitLinked
					.getCharacteristics().currentMovePoints + this.unitLinked
					.getCharacteristics().range * Map.squareWidth))
				targetList.add(enemy);
		}
		return targetList;
	}

	public static Unit getUnitAtPos(Position pos, ArrayList<Unit> unitList) {
		for (Unit u : unitList) {
			if (u.position.equals(pos))
				return u;
		}
		return null;
	}

	/**
	 * Make the AI flee when the unit is low life. It go in the opposite
	 * direction of the enemy
	 * 
	 * @param enemyList
	 * @param map
	 */
	public void flee(ArrayList<Unit> enemyList, Map map) {
		Position enemyCenter = new Position();
		Position finalPos = new Position();
		ArrayList<Position> path = new ArrayList<Position>();
		ArrayList<Action> actionList = new ArrayList<Action>();
		Action act = new Action();

		int xFinalPos;
		int yFinalPos;
		enemyCenter = searchEnemyCenter(enemyList);
		xFinalPos = this.unitLinked.position.getX()
				+ (this.unitLinked.position.getX() - enemyCenter.getX());
		yFinalPos = this.unitLinked.position.getY()
				+ (this.unitLinked.position.getY() - enemyCenter.getY());
		if (xFinalPos < 0)
			finalPos.setX(0);
		else
			finalPos.setX(xFinalPos);
		if (yFinalPos < 0)
			finalPos.setY(yFinalPos);
		else
			finalPos.setY(yFinalPos);

		path = this.unitLinked.canMove(map, finalPos);
		if (path != null) {
			path.subList(0, this.unitLinked.getCharacteristics().movePoints - 1);
			act.position = path;
			act.ope = operation.MOVE;
			actionList.add(act);
		} else {
			path = new ArrayList<Position>();
			path.add(this.unitLinked.position);
			act.position = path;
			act.ope = operation.REST;
			actionList.add(act);
		}
	}

	/**
	 * Get the damage that the unit can inflict.
	 * 
	 * @param u
	 * @return
	 */
	static public int getDamage(Unit u) {
		if (u instanceof Bird || u instanceof Knight)
			return u.getCharacteristics().attackBlunt;
		if (u instanceof Wizard || u instanceof Healer)
			return u.getCharacteristics().attackMagic;
		if (u instanceof Bowman)
			return u.getCharacteristics().attackPercing;
		if (u instanceof Soldier)
			return u.getCharacteristics().attackPercing;
		return 0;
	}

	/**
	 * Make the AI heal itself
	 */
	public void rest() {
		Action act = new Action();
		act.ope = operation.REST;
		act.position.add(this.unitLinked.position);
		this.actList.add(act);
	}

	/**
	 * Find the path to go to the fight.
	 * 
	 * @param enemyList
	 * @param map
	 * @return path to fight
	 */
	public ArrayList<Action> goToTheFight(ArrayList<Unit> enemyList, Map map) {
		ArrayList<Position> path = new ArrayList<Position>();
		ArrayList<Position> safePos = new ArrayList<Position>();
		ArrayList<Action> actionList = new ArrayList<Action>();
		Action act = new Action();
		int i;
		Position posEnemy = new Position();
		Position oldPos;

		oldPos = this.unitLinked.position;
		i = 0;
		posEnemy = closestEnemyFromSquad(enemyList);
		path = this.unitLinked.canMove(map, posEnemy);
		if (path != null) {
			while (i < this.unitLinked.getCharacteristics().movePoints
					&& path != null && i < path.size()) {
				this.unitLinked.position = path.get(i);
				if (isSafe(enemyList, map))
					safePos.add(path.get(i));
				++i;
			}
			this.unitLinked.position = oldPos;
			act.position = safePos;
			act.ope = operation.MOVE;
			actionList.add(act);
			return actionList;
		}
		act.ope = operation.REST;
		actionList.add(act);
		return actionList;
	}

	/**
	 * Search the average position of each enemy units.
	 * 
	 * @param enemyList
	 * @return
	 */
	public Position searchEnemyCenter(ArrayList<Unit> enemyList) {
		Position centerEnemy = new Position();
		int x;
		int y;
		int xAverage;
		int yAverage;
		x = 0;
		y = 0;
		for (Unit u : enemyList) {
			x += u.position.getX();
			y += u.position.getY();
		}
		xAverage = x / enemyList.size();
		yAverage = y / enemyList.size();
		xAverage -= xAverage % Map.squareWidth;
		yAverage -= yAverage % Map.squareHeight;
		centerEnemy.setX(xAverage);
		centerEnemy.setY(yAverage);
		return centerEnemy;
	}

	/**
	 * Search the closest enemy from the squad to make unit of it focusing that
	 * target.
	 * 
	 * @param enemyList
	 * @return
	 */
	public Position closestEnemyFromSquad(ArrayList<Unit> enemyList) {
		double shortestDistance;
		double currentDistance;
		Position closestPosition;
		closestPosition = enemyList.get(0).position;
		shortestDistance = this.unitLinked.position.distance(closestPosition);
		for (Unit u : enemyList) {
			currentDistance = this.squad.center.distance(u.position);
			if (shortestDistance < currentDistance) {
				shortestDistance = currentDistance;
				closestPosition = u.position;
			}
		}
		return closestPosition;
	}
}
