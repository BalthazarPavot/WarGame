
package wargame.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This action manager does nothing. Its throws an error if it is not replaced
 * by the current action manager.
 * @author Balthazar Pavot
 *
 */
public class GameScreenActionManager implements ActionListener {

	protected GameScreen gameScreen = null;

	public GameScreenActionManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void actionPerformed(ActionEvent e) throws IllegalStateException {
		throw new IllegalStateException();
	}
}
