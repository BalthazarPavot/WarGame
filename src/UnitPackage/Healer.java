package UnitPackage;

import wargame.basic_types.Position;

public class Healer extends Terrestre {


	
	Healer(int pvmax, double atkSlaching, double defSlaching, double atkBlunt, double defBlunt, double atkPercing,
			double defPercing, double atkMagic, double defMagic,Position position) {
		super(pvmax, atkSlaching, defSlaching, atkBlunt, defBlunt, atkPercing, defPercing, atkMagic, defMagic, position);
		// TODO Auto-generated constructor stub
	}

	public void makeheal(Unit u) {
		if(u.caracteristique.getPv()<u.caracteristique.getPvMax())
		{
			
			u.gainLife((int)(10*this.caracteristique.getAtkMagic()));
			if (u.caracteristique.getPv()>u.caracteristique.getPvMax())
					u.caracteristique.setPv(100);
		}
	}
	
	public void makedamage(Unit u)
	{
		u.takeDamageBlunt((int)(10*this.caracteristique.getAtkBlunt()));
	}

}
