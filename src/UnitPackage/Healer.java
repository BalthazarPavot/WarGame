package UnitPackage;

public class Healer extends Terrestre {

	public Healer() {
		super();
		maCara.setAtkBlunt(0.5);
		maCara.setAtkPercing(0);
		maCara.setAtkSlashing(0);
		maCara.setAtkMagic(1.5);
		
		maCara.setDefBlunt(0);
		maCara.setDefMagic(1.5);
		maCara.setDefPercing(0.5);
		maCara.setDefSlashing(0.5);

	}
	
	public void makeheal(Unit u) {
		if(u.maCara.getPv()<u.maCara.getPvMax())
		{
			
			u.gainLife((int)(10*this.maCara.getAtkMagic()));
			if (u.maCara.getPv()>u.maCara.getPvMax())
					u.maCara.setPv(100);
		}
	}
	
	public void makedamage(Unit u)
	{
		u.takedamage((int)(10*this.maCara.getAtkBlunt()), 3);
	}

}
