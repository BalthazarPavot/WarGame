package wargame.unit;

import wargame.GameContext;
import wargame.basic_types.Position;

public abstract class Aerial extends Unit {

	public Aerial (Position position, GameContext gameContext) {
		super (position, gameContext) ;
	}

	public boolean canFly () {
		return true ;
	}
}