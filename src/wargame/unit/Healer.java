package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;

public class Healer extends Terrestre {

	private static final long serialVersionUID = -883691851873714232L;

	protected final static int LIFE = 50;

	protected final static int ATTACK_SLASH = 0;
	protected final static int ATTACK_BLUNT = 1;
	protected final static int ATTACK_PIERCE = 0;
	protected final static int ATTACK_MAGIC = 50;

	protected final static int DEFENSE_SLASH = 0;
	protected final static int DEFENSE_BLUNT = 0;
	protected final static int DEFENSE_PIERCE = -5;
	protected final static int DEFENSE_MAGIC = 10;

	protected final static int RANGE = 3;
	protected final static int SIGHT = 4;
	protected final static int MOVE_POINTS = 4;

	public Healer(Position position, GameContext gameContext) {
		super(position, gameContext);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
	}

	public boolean inflictDamage(Unit unit) {
		return false;
	}

	public boolean heal(Unit unit) {
		unit.gainLife(this.characteristics.attackMagic);
		return true;
	}

}
