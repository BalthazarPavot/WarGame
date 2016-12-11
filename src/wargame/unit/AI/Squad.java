package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.unit.Unit;

/**
 * @author Romain Pelegrin
 */
public class Squad {

	/* Attribute of the class */
	public ArrayList<Unit> unitList;
	public Position center;

	/* Constructors */
	Squad(Unit u1, Unit u2) {
		unitList = new ArrayList<Unit>();
		center = new Position();
		unitList.add(u1);
		unitList.add(u2);
		u1.ai.squad = this;
		u2.ai.squad = this;
		computeCenter();
	}

	Squad(Unit u1) {
		unitList = new ArrayList<Unit>();
		center = new Position();
		unitList.add(u1);
		u1.ai.squad = this;
		computeCenter();
	}

	/* Methods */
	public void add(Unit u) {
		this.unitList.add(u);
		u.ai.squad = this;
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

		float averageX;
		float averageY;

		amountX = 0;
		amountY = 0;

		for (Unit u : this.unitList) {
			amountX += u.getPosition().getX();
			amountY += u.getPosition().getY();
		}
		averageX = amountX / this.unitList.size();
		averageY = amountY / this.unitList.size();
		center.setX((int) averageX);
		center.setY((int) averageY);
	}
}
