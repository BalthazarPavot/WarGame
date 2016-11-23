
package wargame.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;

import wargame.GameContext;
import wargame.widgets.*;

/**
 * The screen to load a saved game.
 * @author Balthazar Pavot
 *
 */
public class LoadGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	public LoadGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new LoadGameScreenActionManager(this);
	}

	/**
	 * Prepare the main screen, the one display at the launch of the game.
	 */
	public void prepare() {
		ButtonWidget button;

		this.addWidgets(new TextWidget("Load saved game", gameContext.getWidth() / 2 - 225,
				gameContext.getHeight() / 10 * 2 - 100, 450, 200, 50, new Color(128, 128, 128)));
		button = new ButtonWidget("Previous", 20, gameContext.getHeight() - 100, 100, 20, 14,
				new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		this.addWidgets(button);
		button = new ButtonWidget("Quit", 20, gameContext.getHeight() - 60, 100, 20, 14,
				new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		this.addWidgets(button);
	}

}

class LoadGameScreenActionManager extends GameScreenActionManager {

	public LoadGameScreenActionManager(GameScreen gameScreen) {
		super(gameScreen);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Previous")) {
			gameScreen.nextScreenID = GameScreen.MAIN_MENU_SCREEN;
		} else if (e.getActionCommand().equals("Quit")) {
			gameScreen.nextScreenID = GameScreen.QUIT_SCREEN;
		} else {
			return;
		}
		this.gameScreen.screenTermination();
	}

}

