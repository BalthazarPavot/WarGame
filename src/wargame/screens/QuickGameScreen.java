
package wargame.screens;

import wargame.GameContext;
import wargame.widgets.*;

public class QuickGameScreen extends GameScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuickGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new QuickGameScreenActionManager(this);
	}

}
