package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.unit.Unit;

public class Squad {

	ArrayList<Unit> unitList;
	Position center;

	Squad(Unit u1, Unit u2) {
		ArrayList<Unit> unitList = new ArrayList<Unit>();
		unitList.add(u1);
		unitList.add(u2);
		u1.ai.setSquad(this);
		u2.ai.setSquad(this);
		computeCenter();
	}

	public void add(Unit u) {
		this.unitList.add(u);
		u.ai.setSquad(this);
		computeCenter();
	}

	public void remove(Unit u) {
		this.unitList.remove(u);
		computeCenter();
	}
	
	public void merge(Squad s) {
		for (Unit u : s.unitList) {
			add(u);
		}
		computeCenter();
	}

	public void computeCenter() {
		int amountX;
		int amountY;

		amountX = 0;
		amountY = 0;
		for (Unit u : this.unitList) {
			amountX += u.getPosition().getX();
			amountY += u.getPosition().getY();
		}
		this.center.setX((int) amountX / this.unitList.size());
		this.center.setY((int) amountY / this.unitList.size());
	}
}
