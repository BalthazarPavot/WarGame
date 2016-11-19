
package wargame.screens;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * This action manager does nothing. Its throws an error if it is not replaced by the current action manager.
 * 
 * @author Balthazar Pavot
 *
 */
public class SidePanelMouseMotionManager extends GameScreenMouseMotionManager {

	protected PlayGameScreen gameScreen = null;

	public SidePanelMouseMotionManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseDragged(ActionEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		gameScreen.leftScrolling = false ;
		gameScreen.rightScrolling = false ;
		gameScreen.upScrolling = false ;
		gameScreen.downScrolling = false ;
	}
}
