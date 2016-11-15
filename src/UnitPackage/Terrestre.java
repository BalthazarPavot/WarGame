package UnitPackage;

public class Terrestre extends Unit {
	
	Terrestre()
	{
		maCara=new Caracteristique();
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeDamage(Unit u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeheal(Unit u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gainLife(int val) {
		this.maCara.setPv(this.maCara.getPv()+val);
		
	}

	@Override
	public void takedamage(int val) {
		
		getMaCara().setPv(getMaCara().getPv()-val);
		
	}



}
