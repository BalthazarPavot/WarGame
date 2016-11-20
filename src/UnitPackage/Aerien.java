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
		maCara.setPv(maCara.getPv()+val);
		
	}

	@Override
	public void takedamage(int val, int type) {
		int val2=0;
		
		switch(type)
		{
		case 1:
			if(val-(maCara.getDefSlashing()*10)<0)
				val2=0;
			else
				val2=(int) (val-(maCara.getDefSlashing()*10));
			break;
		case 2:
			if(val-(maCara.getDefPercing()*10)<0)
				val2=0;
			else
				val2=(int) (val-(maCara.getDefPercing()*10));
			break;
		case 3:
			if(val-(maCara.getDefBlunt()*10)<0)
				val2=0;
			else
				val2=(int) (val-(maCara.getDefBlunt()*10));
			break;
		case 4:
			if(val-(maCara.getDefMagic()*10)<0)
				val2=0;
			else
				val2=(int) (val-(maCara.getDefMagic()*10));
			break;
		default:
			val2=0;
			break;
		}
		maCara.setPv(maCara.getPv() - val2);

	}

}
