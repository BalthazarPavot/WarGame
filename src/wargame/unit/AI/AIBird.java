package wargame.unit.AI;

import java.util.ArrayList;
import java.util.Arrays;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Unit;
import wargame.unit.AI.Action.operation;

/**
 * 
 * @author Romain Pelegrin
 *
 */
public class AIBird extends AI implements IAI {

	public AIBird(Unit unit) {
		super(unit);
	}

	/**
	 * Execute the list of action contained in the AI's attribute.
	 */
	public void executeActions(ArrayList<Unit> allyList, ArrayList<Unit> enemyList) {
		Unit u;
		int i;
		for (Action action : this.getActList()) {
			if (action.ope == operation.MOVE) {
				this.unitLinked.setMove((ArrayList<Position>) Arrays.asList(action.position));
				this.unitLinked.move();
			} else if (action.ope == operation.ATTACK) {
				u = getUnitAtPos(action.position, enemyList);
				i = enemyList.indexOf(u);
				enemyList.get(i).takeBluntDamages(this.unitLinked.getCharacteristics().attackBlunt);
				if (enemyList.get(i).getCharacteristics().currentLife == 0)
					enemyList.remove(i);
			}
		}
	}

	/**
	 * Method which fill the list of action of the IA.
	 * 
	 * @param enemyList
	 */
	public void fillAction(ArrayList<Unit> enemyList, ArrayList<Unit> allyList, Map map) {
		this.actList.clear();
		if (isSafe(enemyList, map) && (this.getLife() == healhPoint.MEDIUM || this.getLife() == healhPoint.LOW
				|| this.getLife() == healhPoint.VERY_LOW))
			rest();
		else if (!this.blocked && (this.getLife() == healhPoint.MEDIUM || this.getLife() == healhPoint.LOW
				|| this.getLife() == healhPoint.VERY_LOW))
			flee(enemyList, allyList, map);
		else
			fight(enemyList, allyList, map);
	}

	/**
	 * Sort enemy depending on their distance, their life and their weakness.
	 */
	public void fight(ArrayList<Unit> enemyList, ArrayList<Unit> allyList, Map map) {
		ArrayList<Unit> enemyInRange = new ArrayList<Unit>();
		ArrayList<Unit> enemyInRangeSorted = new ArrayList<Unit>();
		enemyInRange = getAllTargetInRange(enemyList);
		enemyInRangeSorted = getSortedTarget(enemyInRange);
		this.actList = getBestAction(enemyInRangeSorted, enemyList, allyList, map);
	}

	/**
	 * Sort by Weakness the list of enemy.
	 * 
	 * @param enemyInRange
	 * @return
	 */
	public ArrayList<Unit> getSortedTarget(ArrayList<Unit> enemyInRange) {
		int i;
		float damageLifeRate;
		float bestDamageLifeRate;
		Unit bestTarget = null;

		ArrayList<Unit> enemyInRangeCopy = new ArrayList<Unit>(enemyInRange);
		ArrayList<Unit> enemyInRangeSorted = new ArrayList<Unit>();
		for (i = 0; i < enemyInRange.size(); ++i) {
			bestDamageLifeRate = 0;
			if (!enemyInRangeCopy.isEmpty()) {
				for (Unit u : enemyInRangeCopy) {
					damageLifeRate = getDamage(u) / u.getCharacteristics().currentLife;
					if (bestDamageLifeRate <= damageLifeRate) {
						bestTarget = u;
						bestDamageLifeRate = damageLifeRate;
					}
					if (!enemyInRangeSorted.contains(bestTarget))
						enemyInRangeSorted.add(bestTarget);
				}
			} else {
				return new ArrayList<Unit>();
			}
		}
		return enemyInRangeSorted;
	}

	/**
	 * Compute the best action the AI can do.
	 * 
	 * @param enemyInRangeSorted
	 * @param enemyList
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Action> getBestAction(ArrayList<Unit> enemyInRangeSorted, ArrayList<Unit> enemyList,
			ArrayList<Unit> allyList, Map map) {
		ArrayList<Action> bestActionList = new ArrayList<Action>();
		float currentScore;
		float bestScore;
		ArrayList<Unit> units;
		units = (ArrayList<Unit>) allyList.clone();
		units.addAll(enemyList);

		Position oldPos = this.unitLinked.position;
		bestScore = 0;

		for (Unit u : enemyInRangeSorted) {
			for (Position pos : this.unitLinked.movePossibilities(map, units)) {
				currentScore = 0;
				if (isSafe(enemyList, map)) {
					currentScore += SCORE_SAFE_BIRD;
					if (canKill(u)) {
						this.unitLinked.position = oldPos;
						return getActionAttackList(map, oldPos, pos, u, units);
					} else if (this.unitLinked.canHit(map, enemyList).contains(u)) {
						currentScore += ((this.unitLinked.getCharacteristics().attackBlunt
								- u.getCharacteristics().defenseBlunt) * getDamage(u))
								/ u.getCharacteristics().life;
					} else {
						currentScore += SCORE_GO_TO_THE_FIGHT;
					}
				}
				if (currentScore > bestScore) {
					bestScore = currentScore;
					if (currentScore != SCORE_GO_TO_THE_FIGHT)
						bestActionList = getActionAttackList(map, oldPos, pos, u, units);
				}
			}
		}
		if (bestScore <= SCORE_GO_TO_THE_FIGHT) {
			bestActionList.clear();
			bestActionList = goToTheFight(enemyList, allyList, map);
		}
		this.unitLinked.position = oldPos;
		return bestActionList;
	}

	public ArrayList<Action> getActionAttackList(Map map, Position oldPos, Position pos, Unit u,
			ArrayList<Unit> units) {
		ArrayList<Action> actionList = new ArrayList<Action>();

		for (Position position : map.pathByFlying(oldPos, pos, units))
			actionList.add (new Action (position, operation.MOVE)) ;
		actionList.add(new Action (u.position, operation.ATTACK));
		return actionList;
	}

	/**
	 * Indicate if the AI can kill the unit given in parameter.
	 */
	public boolean canKill(Unit u) {
		return this.unitLinked.getCharacteristics().attackBlunt >= (u.getCharacteristics().defenseBlunt
				+ u.getCharacteristics().currentLife);
	}
}