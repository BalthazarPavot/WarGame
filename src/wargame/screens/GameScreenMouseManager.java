
package wargame.screens;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This action manager does nothing. Its throws an error if it is not replaced
 * by the current action manager.
 * @author Balthazar Pavot
 *
 */
public class GameScreenMouseManager implements MouseListener {

	protected GameScreen gameScreen = null;

	public GameScreenMouseManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void mouseDragged(MouseEvent arg0) {
	}

	public void  mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
