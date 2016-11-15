package UnitPackage;

public class Aerien extends Unit {
	Aerien()
	{
		setMaCara(new Caracteristique());
		getMaCara().setFlying(true);
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
		this.getMaCara().setPv(this.getMaCara().getPv()+val);
		
	}

	@Override
	public void takedamage(int val) {
		
		
	}

}
