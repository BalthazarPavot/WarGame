package wargame.unit;

import wargame.basic_types.Position;
import wargame.map.SpriteHandler;

public abstract class Aerial extends Unit {

	public Aerial (Position position, SpriteHandler spriteHandler) {
		super (position, spriteHandler) ;
	}

	public boolean canFly () {
		return true ;
	}
}