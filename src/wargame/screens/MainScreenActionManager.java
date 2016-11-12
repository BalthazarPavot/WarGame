
package wargame.screens;

import java.awt.event.ActionEvent;

/**
 * Action manager of the main menu screen.
 * @author Balthazar Pavot
 *
 */
public class MainScreenActionManager extends GameScreenActionManager {

	public MainScreenActionManager(GameScreen gameScreen) {
		super(gameScreen);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(MainScreen.QUICK_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.QUICK_GAME_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.NEW_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.NEW_GAME_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.LOAD_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.LOAD_GAME_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.CONFIGURATION_STRING)) {
			gameScreen.nextScreenID = GameScreen.CONFIGURATION_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.QUIT_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.QUIT_SCREEN;
		} else {
			return;
		}
		this.gameScreen.screenTermination();
	}

}
