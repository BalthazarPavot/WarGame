package UnitPackage;

import wargame.basic_types.Position;

public class Ranger extends Terrestre {

	
	
	Ranger(int pvmax, double atkSlaching, double defSlaching, double atkBlunt, double defBlunt, double atkPercing,
			double defPercing, double atkMagic, double defMagic, Position position) {
		super(pvmax, atkSlaching, defSlaching, atkBlunt, defBlunt, atkPercing, defPercing, atkMagic, defMagic, position);
		// TODO Auto-generated constructor stub
	}

	public void makeDamage(Unit u) {
		if(u.caracteristique.isFlying()==false) 
			super.makeDamage(u);
		else
			u.takeDamagePercing((int)(10*caracteristique.getAtkPercing()));
	}

}
