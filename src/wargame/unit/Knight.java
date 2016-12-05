package wargame.unit;

import wargame.basic_types.Position;
import wargame.map.SpriteHandler;

public class Knight extends Terrestre {

	protected final static int LIFE = 150;

	protected final static int ATTACK_SLASH = 0;
	protected final static int ATTACK_BLUNT = 20;
	protected final static int ATTACK_PIERCE = 0;
	protected final static int ATTACK_MAGIC = 0;

	protected final static int DEFENSE_SLASH = 10;
	protected final static int DEFENSE_BLUNT = 0;
	protected final static int DEFENSE_PIERCE = -10;
	protected final static int DEFENSE_MAGIC = 0;

	protected final static int RANGE = 1;
	protected final static int SIGHT = 5;
	protected final static int MOVE_POINTS = 5;

	public Knight(Position position, SpriteHandler spriteHandler) {
		super(position, spriteHandler);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
	}

}
