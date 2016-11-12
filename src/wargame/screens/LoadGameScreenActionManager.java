
package wargame.screens;

import java.awt.event.ActionEvent;

public class LoadGameScreenActionManager extends GameScreenActionManager {

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
