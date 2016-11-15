package UnitPackage;

public class Healer extends Terrestre {

	Healer() {
		
		this.maCara.setAtkBlunt(0.5);
		this.maCara.setAtkPercing(0);
		this.maCara.setAtkSlashing(0);
		this.maCara.setAtkMagic(1.5);
		
		this.maCara.setDefBlunt(0);
		this.maCara.setDefMagic(1.5);
		this.maCara.setDefPercing(0.5);
		this.maCara.setDefSlashing(0.5);

	}
	
	public void makeheal(Unit u) {
		if(this.maCara.getPv()<this.maCara.getPvMax())
		{
			u.gainLife((int)(10*this.maCara.getAtkMagic()));
		}
	}
	
	public void makedamage(Unit u)
	{
		u.takedamage((int)(10*this.maCara.getAtkBlunt()));
	}

}
