
package wargame.screens;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This action manager does nothing. Its throws an error if it is not replaced
 * by the current action manager.
 * @author Balthazar Pavot
 *
 */
public class GameScreenKeyboardManager implements KeyListener {

	protected GameScreen gameScreen = null;

	public GameScreenKeyboardManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
