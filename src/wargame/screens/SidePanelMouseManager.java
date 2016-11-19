
package wargame.screens;

import java.awt.event.MouseEvent;

/**
 * This action manager does nothing. Its throws an error if it is not replaced by the current action manager.
 * 
 * @author Balthazar Pavot
 *
 */
public class SidePanelMouseManager extends GameScreenMouseManager {

	protected PlayGameScreen gameScreen = null;

	public SidePanelMouseManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseClicked(MouseEvent arg0) {
		System.out.println("Clicked!");

	}
}
