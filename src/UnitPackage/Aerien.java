package UnitPackage;

public class Aerien extends Unit {
	Aerien()
	{
		maCara=new Caracteristique();
		maCara.setFlying(true);
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
		
		
	}

}
