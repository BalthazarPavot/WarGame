package UnitPackage;

public class Aerien extends Unit {
	Aerien(int pvmax, double atkSlaching, double defSlaching, double atkBlunt, double defBlunt,
			double atkPercing, double defPercing, double atkMagic, double defMagic)
	{
		caracteristique=new Caracteristique(pvmax,atkSlaching,defSlaching,atkBlunt,defBlunt,atkPercing,defPercing,atkMagic,defMagic);
		caracteristique.setFlying(true);
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
		caracteristique.setPv(caracteristique.getPv()+val);
		
	}



	@Override
	public void takeDamagePercing(int val) {
		int val2;
		if(val-(caracteristique.getDefPercing()*10)<0)
			val2=0;
		else
			val2=(int) (val-(caracteristique.getDefPercing()*10));
		caracteristique.setPv(caracteristique.getPv() - val2);
	}

	@Override
	public void takeDamageBlunt(int val) {
		int val2;
		if(val-(caracteristique.getDefBlunt()*10)<0)
			val2=0;
		else
			val2=(int) (val-(caracteristique.getDefBlunt()*10));
		caracteristique.setPv(caracteristique.getPv() - val2);
		
	}

	@Override
	public void takeDamageMagic(int val) {
		int val2;
		if(val-(caracteristique.getDefMagic()*10)<0)
			val2=0;
		else
			val2=(int) (val-(caracteristique.getDefMagic()*10));
		caracteristique.setPv(caracteristique.getPv() - val2);
		
	}

	@Override
	public void takeDamageSlaching(int val) {
		// TODO Auto-generated method stub
		int val2;
		if(val-(caracteristique.getDefSlashing()*10)<0)
			val2=0;
		else
			val2=(int) (val-(caracteristique.getDefSlashing()*10));
		caracteristique.setPv(caracteristique.getPv() - val2);
	}

};