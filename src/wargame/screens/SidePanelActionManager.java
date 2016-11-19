
package wargame.screens;

import java.awt.event.ActionEvent;

/**
 * This action manager does nothing. Its throws an error if it is not replaced by the current action manager.
 * 
 * @author Balthazar Pavot
 *
 */
public class SidePanelActionManager extends GameScreenActionManager {

	protected PlayGameScreen gameScreen = null;

	public SidePanelActionManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen ;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

}
