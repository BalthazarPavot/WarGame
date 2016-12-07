package wargame.unit.AI;

import java.util.ArrayList;

import wargame.unit.Unit;
import wargame.unit.AI.Action.operation;

public class AIBowman extends AI implements IMarksman {

	AIBowman() {
		super();
	}

	public void executeActions(ArrayList<Unit> allyList,
			ArrayList<Unit> enemyList) {
		Unit u;
		int i;
		for (Action action : this.getActList()) {
			if (action.ope == operation.MOVE) {
				this.unitLinked.setMove(action.position);
			} else if (action.ope == operation.ATTACK) {
				u = getUnitAtPos(
						action.position.get(action.position.size()),
						enemyList);
				enemyList.indexOf(u);
				enemyList.get(i)
						.takePercingDamages(this.unitLinked.damage);
			}
		}
	}

	/**
	 * Method which fill the list of action of the IA
	 * 
	 * @param enemyList
	 */
	public void fillAction(ArrayList<Unit> enemyList) {
		if (!this.blocked && (this.getLife() == healhPoint.VERY_LOW || this.getLife() == healhPoint.LOW)) {
			flee(enemyList);
		} else {
			fight(enemyList);
		}
	}

	public void fight (ArrayList<Unit> enemyList){
		ArrayList<Unit> enemyInRange = new ArrayList<Unit>();
		ArrayList<Unit> enemyInRangeSorted = new ArrayList<Unit>();
		enemyInRange = getAllTargetInRange(enemyList);
		enemyInRangeSorted = getSortedTarget(enemyInRange);

	
	}
	
	public ArrayList<Unit> getSortedTarget(ArrayList<Unit> enemyInRange) {
		float damageLifeRate;
		float bestDamageLifeRate;
		Unit bestTarget;
		
		ArrayList<Unit> enemyInRangeCopy = new ArrayList<Unit>(enemyInRange);
		ArrayList<Unit> enemyInRangeSorted = new ArrayList<Unit>();
		for (Unit u : enemyInRange){
			bestDamageLifeRate = 0;
			for (Unit u : enemyInRangeCopy){
				if (u)
				damageLifeRate = u.damage / u.currentLife;
				if (bestDamageLifeRate <= damageLifeRate){
					bestTarget = u;
					bestDamageLifeRate = damageLifeRate;
				}
				enemyInRangeSorted.add(bestTarget);
				enemyInRangeCopy.remove(bestTarget);
			}
		}
		return enemyInRangeSorted;
	}
	
	
	
	
	
}