package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.unit.AI.AISoldier;

public class Soldier extends Terrestre {

	private static final long serialVersionUID = 2097758820331198919L;

	protected final static int LIFE = 100 ;

	protected final static int ATTACK_SLASH = 25 ;
	protected final static int ATTACK_BLUNT = 0 ;
	protected final static int ATTACK_PIERCE = 0 ;
	protected final static int ATTACK_MAGIC = 0 ;

	protected final static int DEFENSE_SLASH = 0 ;
	protected final static int DEFENSE_BLUNT = -10 ;
	protected final static int DEFENSE_PIERCE = 10 ;
	protected final static int DEFENSE_MAGIC = 0 ;

	protected final static int RANGE = 1;
	protected final static int SIGHT = 3;
	protected final static int MOVE_POINTS = 3;

	public Soldier(int x, int y, GameContext gameContext) {
		this (new Position(x, y), gameContext) ;
	}

	public Soldier(Position position, GameContext gameContext) {
		super(position, gameContext);
		characteristics = new Characteristic(LIFE, ATTACK_SLASH, DEFENSE_SLASH, ATTACK_BLUNT, DEFENSE_BLUNT,
				ATTACK_PIERCE, DEFENSE_PIERCE, ATTACK_MAGIC, DEFENSE_MAGIC, RANGE, SIGHT, MOVE_POINTS);
		if (position.equals(gameContext.getMap().getEnnemyPopArea()))
			this.ai = new AISoldier(this);
		else
			this.ai = null;
	}

	public boolean inflictDamage(Unit unit) {
		return unit.takeSlachingDamages(characteristics.attackSlashing);
	}
	
}
