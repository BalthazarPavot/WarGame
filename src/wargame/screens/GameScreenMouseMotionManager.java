
package wargame.screens;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * This action manager does nothing. Its throws an error if it is not replaced
 * by the current action manager.
 * @author Balthazar Pavot
 *
 */
public class GameScreenMouseMotionManager implements MouseMotionListener {

	protected GameScreen gameScreen = null;

	public GameScreenMouseMotionManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void mouseDragged(MouseEvent arg0) {
	}

	public void  mouseMoved(MouseEvent arg0) {
	}
}
