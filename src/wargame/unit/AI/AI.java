package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Characteristic;
import wargame.unit.Unit;

public class AI {

	/* Enumerations */
	protected enum healhPoint {
		HIGH, MEDIUM, LOW, VERY_LOW
	}

	/* Constants used for the state "life" */
	protected static final int LIMIT_LIFE_HIGH = 50;
	protected static final int LIMIT_LIFE_MEDIUM = 25;
	protected static final int LIMIT_LIFE_LOW = 10;
	/* Constants used for the state "grouped" */
	protected static final int THRESHOLD_GROUP = 3;

	/* Attribut of the class */
	/**
	 * blocked is true when the unit can't flee (because of the map) grouped is
	 * true if the unit is cloth to its team Aggressive is deduced using
	 * previous states
	 */
	private healhPoint life;
	private boolean blocked;
	private boolean agressive;

	private Unit unitLinked;
	private Squad squad;
	private Action act;

	/* Constructor */
	public AI() {

		/*
		 * TODO Only enemy covered by the sight of the ally army
		 * TODO initialize squads when it's unit move
		 * TODO compute the method canHit(pos) every time a unit of the player move
		 */

	}

	/**
	 * Set the state "life" of the unit
	 * 
	 * @param c
	 */
	public void setLife() {
		int hitPointRate;
		Characteristic c;

		c = unitLinked.getMaCara()();
		hitPointRate = (int) (c.currentLife * 100) / c.life;

		if (c.life > LIMIT_LIFE_HIGH) {
			this.life = healhPoint.HIGH;
		} else if (c.life > LIMIT_LIFE_MEDIUM) {
			this.life = healhPoint.MEDIUM;
		} else if (c.life > LIMIT_LIFE_LOW) {
			this.life = healhPoint.HIGH;
		} else {
			this.life = healhPoint.VERY_LOW;
		}
	}

	/* Methods */
	public void setSquad(Squad squad) {
		this.squad = squad;
	}

	public void searchSquad(ArrayList<Unit> allyList, Unit thisUnit, Map map) {

		for (Unit ally : allyList) {
			if (ally != this.unitLinked
					&& (ally.canReach(map, this.unitLinked.getPosition()) || (this.unitLinked
							.canReach(map, ally.getPosition())))) {
				if (this.squad == null && ally.ai.squad != null)
					ally.ai.squad.add(this.unitLinked);
				else if (this.squad != null && this.squad == null)
					this.squad.add(ally.ai.unitLinked);
				else if (this.squad != null && this.squad == null)
					new Squad(ally.ai.unitLinked, this.unitLinked);
				else if (this.squad != null && this.squad == null)
					this.squad.merge(ally.ai.squad);
			}
		}
	}

	/**
	 * The AI in considered as "blocked" if it can't flee against enemy
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

	/**
	 * Give a list of all positions at max range of the AI's unit movement
	 * 
	 * @param u
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
		Position pos = null;

		dx = 1;
		dy = -1;
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
				switch (i) {
				case 0:
					dx = -1;
					break;
				case 1:
					dy = 1;
					break;
				case 2:
					dx = 1;
					break;
				}
				++changeOrientation;
			}
		}
		return movementPerimeter;
	}

	public boolean isSafe(ArrayList<Unit> enemyList, Map map) {
		for (Unit enemy : enemyList) {
			if (enemy.canHit(this.unitLinked.getCharacteristics().currentMovePoints, map) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method which return a list of target who can be reached by the AI, using
	 * it's movement + range but ignoring obstacle (to decrease the complexity
	 * of the algorithm. This methods have to be used as a first test and then
	 * be completed using the method TODO / insert the name of the method here/
	 * 
	 * @param enemyList
	 * @return targetList
	 */
	public ArrayList<Unit> getAllTargetInRange(ArrayList<Unit> enemyList) {
		ArrayList<Unit> targetList = new ArrayList<Unit>();
		for (Unit enemy : enemyList) {
			if (enemy.position.distance(this.unitLinked.position) <= (this.unitLinked
					.getCharacteristics().currentMovePoints + this.unitLinked
					.getCharacteristics().range))
				targetList.add(enemy);
		}
		return targetList;
	}

	public ArrayList<Action> getActList() {
		return actList;
	}

	public void setActList(ArrayList<Action> actList) {
		this.actList = actList;
	}

	public static Unit getUnitAtPos(Position pos, ArrayList<Unit> unitList) {
		for (Unit u : unitList) {
			if (u.position == pos)
				return u;
		}
		return null;
	}

	// TODO
	public void flee(ArrayList<Unit> enemyList) {

	}
}
