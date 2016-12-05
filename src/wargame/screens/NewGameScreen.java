
package wargame.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;

import wargame.GameContext;
import wargame.widgets.*;

/**
 * This screen displays the parameters to generate a map, select the
 * difficult of the game etc.
 * @author Balthazar Pavot
 *
 */
public class NewGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	public NewGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new NewGameScreenActionManager(this);
	}

	public void prepare() {
		ButtonWidget button;

		this.addWidgets(new TextWidget("Map Generation", gameContext.getWidth() / 2 - 210,
				gameContext.getHeight() / 10 * 2 - 100, 420, 200, 50, new Color(128, 128, 128)));
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

/**
 * Manage the actions of the buttons.
 * @author Balthazar Pavot
 *
 */
class NewGameScreenActionManager extends GameScreenActionManager {

	public NewGameScreenActionManager(GameScreen gameScreen) {
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

