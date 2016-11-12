
package wargame.screens;

import wargame.GameContext;

/**
 * This screen is never displayed.
 * It make the map generator to generate a random map,
 * and launch the game with this map.
 * @author Balthazar Pavot
 *
 */
public class QuickGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	public QuickGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new QuickGameScreenActionManager(this);
	}


	public void prepare() {
		
	}

}
