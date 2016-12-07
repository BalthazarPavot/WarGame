package wargame.unit.AI;

import java.util.ArrayList;

import wargame.unit.Unit;

public interface IMelee {

	public abstract void fight(ArrayList<Unit> enemyList);

}
