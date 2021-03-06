package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.unit.AI.AIKnight;

public class Knight extends Terrestre {

	private static final long serialVersionUID = -8944817027393817236L;

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

	public Knight(int x, int y, GameContext gameContext) {
		this (new Position(x, y), gameContext) ;
	}

	public Knight(Position position, GameContext gameContext) {
		super(position, gameContext);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
		if (position.equals(gameContext.getMap().getEnnemyPopArea()))
			this.ai = new AIKnight(this);
		else
			this.ai = null;
	}

	public boolean inflictDamage(Unit unit) {
		return unit.takeBluntDamages(characteristics.attackBlunt);
	}

}
