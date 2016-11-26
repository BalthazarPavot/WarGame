package wargame.unit.AI;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Unit;

public class AI {

	protected enum hitPoint { HIGH, MEDIUM, LOW, VERY_LOW }
	
	/* Constants used for the state "life" */
	protected static final int LIMIT_LIFE_HIGH   = 50 ; 
	protected static final int LIMIT_LIFE_MEDIUM = 25 ; 
	protected static final int LIMIT_LIFE_LOW    = 10 ;  

	/* Constants used for the state "grouped" */
	protected static final int THRESHOLD_GROUP   = 3 ; 

	
	/** States
	 * blocked   is true when the unit can't flee (because of the map)
	 * grouped   is true if the unit is cloth to its team
	 * agressive is deduced with previous states
	 */
	private hitPoint life ;
	private boolean  blocked ;
	private boolean  grouped ;
	private boolean  agressive ;
	
	private Action act ;
	
	public AI () {
		
	}
	
	/**
	 * Set the state "life" of the unit
	 * @param c
	 */
	public void setLife (Caracteristique c) {
		int hitPointRate ;
		hitPointRate = (int) (c.getPv() * 100) / c.getPvMax() ;
		
		if        (c.pv > LIMIT_LIFE_HIGH) {
			this.life =  hitPoint.HIGH ;
		} else if (c.pv > LIMIT_LIFE_MEDIUM) {
			this.life =  hitPoint.MEDIUM ;
		} else if (c.pv > LIMIT_LIFE_LOW) {
			this.life =  hitPoint.HIGH ;
		} else {
			this.life =  hitPoint.VERY_LOW ;
		}
	}

	public void setGrouped (ArrayList <Unit> allyList, Unit thisUnit) {
		int counterUnitNearby ;
		counterUnitNearby = 0 ;
		
		for (Unit u : allyList) {
			if (u != thisUnit) {
				if (u instanceof Terrestre && )
					canMove(map, destination) ;
				}
			}
		}
	}
}









