
package wargame.screens;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * This action manager does nothing. Its throws an error if it is not replaced by the current action manager.
 * 
 * @author Balthazar Pavot
 *
 */
public class PlayGameScreenMouseMotionManager extends GameScreenMouseMotionManager {

	protected PlayGameScreen gameScreen = null;

	public PlayGameScreenMouseMotionManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseDragged(ActionEvent e) {
		System.out.println("dragged!");
	}

	public void mouseMoved(MouseEvent e) {
		int x;
		int y;

		x = e.getX();
		y = e.getY();
		gameScreen.leftScrolling = x < 30;
		gameScreen.rightScrolling = x > gameScreen.gameContext.getWidth() - 30 - 150;
		gameScreen.upScrolling = y < 30;
		gameScreen.downScrolling = y > gameScreen.gameContext.getHeight() - 30;
	}
}
