
package wargame.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameScreenActionManager implements ActionListener {

	protected GameScreen gameScreen = null;

	public GameScreenActionManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void actionPerformed(ActionEvent e) throws IllegalStateException {
		throw new IllegalStateException();
	}
}
