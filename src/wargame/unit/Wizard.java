package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;

public class Wizard extends Terrestre {

	private static final long serialVersionUID = 8905518988015497675L;

	protected final static int LIFE = 75;

	protected final static int ATTACK_SLASH = 0;
	protected final static int ATTACK_BLUNT = 0;
	protected final static int ATTACK_PIERCE = 0;
	protected final static int ATTACK_MAGIC = 35;

	protected final static int DEFENSE_SLASH = 0;
	protected final static int DEFENSE_BLUNT = 0;
	protected final static int DEFENSE_PIERCE = -5;
	protected final static int DEFENSE_MAGIC = 20;

	protected final static int RANGE = 3;
	protected final static int SIGHT = 4;
	protected final static int MOVE_POINTS = 4;

	public Wizard(Position position, GameContext gameContext) {
		super(position, gameContext);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
	}

	public boolean inflictDamage(Unit unit) {
		return unit.takeMagicDamages(characteristics.attackMagic);
	}

}
