package wargame.unit.AI;

import java.util.ArrayList;

import wargame.unit.Unit;

public interface IMarksman {

	public abstract void fight(ArrayList<Unit> enemyList);

}
