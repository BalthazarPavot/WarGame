package UnitPackage;

public class Soldier extends Terrestre {
	
	public Soldier()
	{
		this.getMaCara().setAtkBlunt(0);
		this.getMaCara().setAtkMagic(0);
		this.getMaCara().setAtkSlashing(1.5);
		
		this.getMaCara().setDefBlunt(0.5);
		this.getMaCara().setDefPercing(1.5);
		
		
	}
	

	public void makeDamage(Unit u) {
		int val = (int)Math.random()*2;
		if (u.maCara.isFlying()==true)
			val=0;
		switch (val)
		{
		case 1:
			u.takedamage((int)(10*this.maCara.getAtkSlashing()));
			break;
		case 2:
			u.takedamage((int)(10*this.maCara.getAtkPercing()));
			break;
		default:
			u.takedamage(0);
			break;
		}
	}
	

}
