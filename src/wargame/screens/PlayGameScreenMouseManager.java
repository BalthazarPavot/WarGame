
package wargame.screens;

import java.awt.event.MouseEvent;

/**
 * This action manager does nothing. Its throws an error if it is not replaced by the current action manager.
 * 
 * @author Balthazar Pavot
 *
 */
public class PlayGameScreenMouseManager extends GameScreenMouseManager {

	protected PlayGameScreen gameScreen = null;

	public PlayGameScreenMouseManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseClicked(MouseEvent arg0) {
		System.out.println("Clicked!");

	}
}
