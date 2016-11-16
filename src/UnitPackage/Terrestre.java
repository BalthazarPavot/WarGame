package UnitPackage;

public class Terrestre extends Unit {

	Terrestre() {
		maCara = new Caracteristique();
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeDamage(Unit u) {
		double val = Math.random()*5;
		int val2=(int)val;
		if (u.maCara.isFlying()==true)
			val2=0;
		switch (val2)
		{
		case 1:
			u.takedamage((int)(10*this.maCara.getAtkSlashing()), val2);
			break;
		case 2:
			u.takedamage((int)(10*this.maCara.getAtkPercing()), val2);
			break;
		case 3:
				u.takedamage((int)(10*this.maCara.getAtkBlunt()), val2);
			break;
		case 4:
			u.takedamage((int)(10*this.maCara.getAtkMagic()), val2);
			break;
		default:
			u.takedamage(0, val2);
			break;
		}
	}

	@Override
	public void makeheal(Unit u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gainLife(int val) {
		this.maCara.setPv(this.maCara.getPv() + val);

	}

	@Override
	public void takedamage(int val, int type) {
		int val2=0;
		
		switch(val)
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
