package wargame.unit.AI;

import java.util.ArrayList;

import wargame.map.Map;
import wargame.unit.Unit;

public interface IAI {

	public void fillAction(ArrayList<Unit> enemyList, Map map);

	public void executeActions(ArrayList<Unit> allyList,
			ArrayList<Unit> enemyList);

	public boolean canKill(Unit u);
}
