package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;

public class Bowman extends Terrestre {

	protected final static int LIFE = 75;

	protected final static int ATTACK_SLASH = 0;
	protected final static int ATTACK_BLUNT = 0;
	protected final static int ATTACK_PIERCE = 15;
	protected final static int ATTACK_MAGIC = 0;

	protected final static int DEFENSE_SLASH = -10;
	protected final static int DEFENSE_BLUNT = 10;
	protected final static int DEFENSE_PIERCE = 0;
	protected final static int DEFENSE_MAGIC = 0;

	protected final static int RANGE = 5;
	protected final static int SIGHT = 6;
	protected final static int MOVE_POINTS = 4;

	public Bowman(Position position, GameContext gameContext) {
		super(position, gameContext);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
	}

	public boolean inflictDamage(Unit unit) {
		return unit.takePercingDamages(characteristics.attackPercing);
	}

}
