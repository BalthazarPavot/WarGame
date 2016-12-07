package wargame.unit.AI;

import java.util.ArrayList;

import wargame.unit.Unit;

public interface IAI {

	public void executeActions(ArrayList<Unit> allyList,
			ArrayList<Unit> enemyList);

	public void fillAction(ArrayList<Unit> enemyList);

	public abstract void flee();

}
