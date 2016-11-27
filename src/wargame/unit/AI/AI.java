package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Unit;

public class AI {

	protected enum healhPoint {
		HIGH, MEDIUM, LOW, VERY_LOW
	}

	/* Constants used for the state "life" */
	protected static final int LIMIT_LIFE_HIGH = 50;
	protected static final int LIMIT_LIFE_MEDIUM = 25;
	protected static final int LIMIT_LIFE_LOW = 10;
	/* Constants used for the state "grouped" */
	protected static final int THRESHOLD_GROUP = 3;
	/* Constants used for the state */
	private static final int BLOCK_NORTH = 1;
	private static final int BLOCK_EAST = 2;
	private static final int BLOCK_SOUTH = 4;
	private static final int BLOCK_WEST = 8;

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

	public AI() {

		/*
		 * TODO Only enemy covered by the sight of the ally army TODO initialize
		 * squads
		 */

	}

	/**
	 * Set the state "life" of the unit
	 * 
	 * @param c
	 */
	public void setLife() {
		int hitPointRate;
		Caracteristique c;

		c = unitLinked.getCaracteristique();
		hitPointRate = (int) (c.getPv() * 100) / c.getPvMax();

		if (c.pv > LIMIT_LIFE_HIGH) {
			this.life = healhPoint.HIGH;
		} else if (c.pv > LIMIT_LIFE_MEDIUM) {
			this.life = healhPoint.MEDIUM;
		} else if (c.pv > LIMIT_LIFE_LOW) {
			this.life = healhPoint.HIGH;
		} else {
			this.life = healhPoint.VERY_LOW;
		}
	}

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
		/*
		 * ArrayList<Position> = map.AStart(begin_x, int begin_y, int end_x, int
		 * end_y, String methodName) {
		 * 
		 * for (Unit enemy : enemyList) { block }
		 */
	}
}
