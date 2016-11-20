package UnitPackage;

public class Ranger extends Terrestre {
	public Ranger()
	{
		maCara.setAtkBlunt(0);
		maCara.setAtkMagic(0);
		maCara.setAtkPercing(1.5);
		
		maCara.setDefSlashing(0.5);
		maCara.setDefBlunt(1.5);
	}
	
	
	public void makeDamage(Unit u) {
		if(u.maCara.isFlying()==false) 
			super.makeDamage(u);
		else
			u.takedamage((int)(10*maCara.getAtkPercing()), 2);
	}

}
