package wargame.unit;

public class Characteristic {

	public int life;
	public int currentLife;
	public int attackSlashing;
	public int defenseSlashing;
	public int attackPercing;
	public int defensePercing;
	public int attackBlunt;
	public int defenseBlunt;
	public int attackMagic;
	public int defenseMagic;
	public int range;
	public int sight;
	public int movePoints;
	public int currentMovePoints;

	public Characteristic(int life, int attackSlaching, int defenseSlaching, int attackBlunt,
			int defenseBlunt, int attackPercing, int defensePercing, int attackMagic, int defenseMagic) {
		this(life, attackSlaching, defenseSlaching, attackBlunt, defenseBlunt, attackPercing, defensePercing,
				attackMagic, defenseMagic, 1);
	}

	public Characteristic(int life, int attackSlaching, int defenseSlaching, int attackBlunt,
			int defenseBlunt, int attackPercing, int defensePercing, int attackMagic, int defenseMagic,
			int range) {
		this(life, attackSlaching, defenseSlaching, attackBlunt, defenseBlunt, attackPercing, defensePercing,
				attackMagic, defenseMagic, range, 3);
	}

	public Characteristic(int life, int attackSlaching, int defenseSlaching, int attackBlunt,
			int defenseBlunt, int attackPercing, int defensePercing, int attackMagic, int defenseMagic,
			int range, int sight) {
		this(life, attackSlaching, defenseSlaching, attackBlunt, defenseBlunt, attackPercing, defensePercing,
				attackMagic, defenseMagic, range, sight, 2);
	}

	public Characteristic(int life, int attackSlaching, int defenseSlaching, int attackBlunt,
			int defenseBlunt, int attackPercing, int defensePercing, int attackMagic, int defenseMagic,
			int range, int sight, int movePoints) {
		this.life = currentLife = life;
		this.attackPercing = attackPercing;
		this.defensePercing = defensePercing;
		this.attackBlunt = attackBlunt;
		this.defenseBlunt = defenseBlunt;
		this.attackMagic = attackMagic;
		this.defenseMagic = defenseMagic;
		this.attackSlashing = attackSlaching;
		this.defenseSlashing = defenseSlaching;
		this.range = range;
		this.sight = sight;
		this.movePoints = currentMovePoints = movePoints;
	}
}
