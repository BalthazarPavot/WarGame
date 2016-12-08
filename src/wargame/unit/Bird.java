package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;

public class Bird extends Aerial {

	protected final static int LIFE = 50;

	protected final static int ATTACK_SLASH = 0;
	protected final static int ATTACK_BLUNT = 30;
	protected final static int ATTACK_PIERCE = 0;
	protected final static int ATTACK_MAGIC = 0;

	protected final static int DEFENSE_SLASH = 0;
	protected final static int DEFENSE_BLUNT = 10;
	protected final static int DEFENSE_PIERCE = -10;
	protected final static int DEFENSE_MAGIC = 0;

	protected final static int RANGE = 1;
	protected final static int SIGHT = 7;
	protected final static int MOVE_POINTS = 6*3;

	public Bird(Position position, GameContext gameContext) {
		super(position, gameContext);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
	}

	public boolean inflictDamage(Unit unit) {
		return unit.takeBluntDamages(characteristics.attackBlunt);
	}

}
