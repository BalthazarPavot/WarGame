package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Unit;
import wargame.unit.AI.Action.operation;

public class AIBowman extends AI implements IAI, IMarksman {

	public AIBowman(Unit unit) {
		super(unit);
	}

	/**
	 * Execute the list of action contained in the AI's attribute.
	 */
	public void executeActions(ArrayList<Unit> allyList,
			ArrayList<Unit> enemyList) {
		Unit u;
		int i;
		for (Action action : this.getActList()) {
			if (action.ope == operation.MOVE) {
				this.unitLinked.setMove(action.position);
				this.unitLinked.move();
			} else if (action.ope == operation.ATTACK) {
				u = getUnitAtPos(
						action.position.get(action.position.size() - 1),
						enemyList);
				i = enemyList.indexOf(u);
				enemyList.get(i).takePercingDamages(
						this.unitLinked.getCharacteristics().attackPercing);
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
	public void fillAction(ArrayList<Unit> enemyList, Map map) {
		this.actList.clear();
		if (isSafe(enemyList, map)
				&& (this.getLife() == healhPoint.LOW || this.getLife() == healhPoint.VERY_LOW))
			rest();
		else if (!this.blocked
				&& (this.getLife() == healhPoint.LOW || this.getLife() == healhPoint.VERY_LOW))
			flee(enemyList, map);
		else
			fight(enemyList, map);
	}

	/**
	 * Sort enemy depending on their distance, their life and their weakness.
	 */
	public void fight(ArrayList<Unit> enemyList, Map map) {
		ArrayList<Unit> enemyInRange = new ArrayList<Unit>();
		ArrayList<Unit> enemyInRangeSorted = new ArrayList<Unit>();
		enemyInRange = getAllTargetInRange(enemyList);
		enemyInRangeSorted = getSortedTarget(enemyInRange);
		this.actList = getBestAction(enemyInRangeSorted, enemyList, map);
	}

	/**
	 * Sort by Weakness the list of enemy.
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
					damageLifeRate = getDamage(u)
							/ u.getCharacteristics().currentLife;
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
	 * @param enemyInRangeSorted
	 * @param enemyList
	 * @param map
	 * @return
	 */
	public ArrayList<Action> getBestAction(ArrayList<Unit> enemyInRangeSorted,
			ArrayList<Unit> enemyList, Map map) {
		ArrayList<Action> bestActionList = new ArrayList<Action>();
		float currentScore;
		float bestScore;

		Position oldPos = this.unitLinked.position;
		bestScore = 0;
		
		for (Unit u : enemyInRangeSorted) {
			for (Position pos : this.unitLinked.movePossibilities(map)) {
				currentScore = 0;
				if (isSafe(enemyList, map)) {
					currentScore += SCORE_SAFE_MARKSMAN;
					if (canKill(u)) {
						this.unitLinked.position = oldPos;
						return getActionAttackList(map, oldPos, pos, u);
					} else if (this.unitLinked.canHit(map, enemyList).contains(
							u)) {
						currentScore += ((this.unitLinked.getCharacteristics().attackPercing - u
								.getCharacteristics().defensePercing) * getDamage(u))
								/ u.getCharacteristics().life;
					} else {
						currentScore += SCORE_GO_TO_THE_FIGHT;
					}
				}
				if (currentScore > bestScore) {
					bestScore = currentScore;
					if (currentScore != SCORE_GO_TO_THE_FIGHT)
						bestActionList = getActionAttackList(map, oldPos, pos,
								u);
				}
			}
		}
		if (bestScore <= SCORE_GO_TO_THE_FIGHT)
			bestActionList = goToTheFight(enemyList, map);
		this.unitLinked.position = oldPos;
		return bestActionList;
	}

	public ArrayList<Action> getActionAttackList(Map map, Position oldPos,
			Position pos, Unit u) {
		ArrayList<Action> actionList = new ArrayList<Action>();
		ArrayList<Position> moveList = new ArrayList<Position>();
		Action act1 = new Action();
		Action act2 = new Action();

		act1.position = map.pathByWalking(oldPos, pos);
		act1.ope = operation.MOVE;
		actionList.add(act1);
		moveList.add(u.position);
		act2.position = moveList;
		act2.ope = operation.ATTACK;
		actionList.add(act2);

		return actionList;
	}

	/**
	 * Indicate if the AI can kill the unit given in parameter.
	 */
	public boolean canKill(Unit u) {
		return this.unitLinked.getCharacteristics().attackPercing >= (u
				.getCharacteristics().defensePercing + u.getCharacteristics().currentLife);
	}
}