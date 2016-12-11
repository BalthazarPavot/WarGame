package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;

public abstract class Terrestre extends Unit {

	private static final long serialVersionUID = 2253238873598482222L;

	public Terrestre(Position position, GameContext gameContext) {
		super (position, gameContext) ;
	}

	public boolean canFly () {
		return false ;
	}
/*
	@Override
	public void makeDamage(Unit u) {
		double val = Math.random()*4;
		int val2=(int)val;
		if (u.caracteristique.isFlying()==true)
			val2=0;
		switch (val2)
		{
		case 1:
			u.takeDamageBlunt((int)(10*this.caracteristique.getAtkSlashing()));
			break;
		case 2:
			u.takeDamagePercing((int)(10*this.caracteristique.getAtkPercing()));
			break;
		case 3:
				u.takeDamageSlaching((int)(10*this.caracteristique.getAtkBlunt()));
			break;
		default:
			break;
		}
	}

	@Override
	public void makeheal(Unit u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gainLife(int val) {
		this.caracteristique.setPv(this.caracteristique.getPv() + val);

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
*/
}
