
package wargame.screens;

import wargame.GameContext;
import wargame.widgets.*;

/**
 * Defines a screen of the game.
 * @author Balthazar Pavot
 *
 */
public class PlayGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	public PlayGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new PlayGameScreenActionManager(this);
	}

	/**
	 * Prepare the main screen, the one display at the launch of the game.
	 */
	public void prepare() {
		this.addWidgets(new MapWidget(this.gameContext.getMap(), this.gameContext.getWidth()-150, this.gameContext.getHeight()));
		this.addWidgets(new SidePanel (this.gameContext.getWidth()-150, 0, 150, this.gameContext.getHeight())) ;
	}
}
