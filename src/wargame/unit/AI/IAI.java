package wargame.unit.AI;

import java.util.ArrayList;

import wargame.map.Map;
import wargame.unit.Unit;


/**
 * @author Romain Pelegrin
 */
public interface IAI {

	public void fillAction(ArrayList<Unit> enemyList, ArrayList<Unit>allyList, Map map);

	public void executeActions(ArrayList<Unit> allyList,
			ArrayList<Unit> enemyList);

	public boolean canKill(Unit u);
	
	public abstract void fight(ArrayList<Unit> enemyList, ArrayList<Unit>allyList, Map map);

}
