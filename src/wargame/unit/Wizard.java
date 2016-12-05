package wargame.unit;

import wargame.basic_types.Position;
import wargame.map.SpriteHandler;

public class Wizard extends Terrestre {

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

	public Wizard(Position position, SpriteHandler spriteHandler) {
		super(position, spriteHandler);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
	}

}
