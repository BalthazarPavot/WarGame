
package wargame.screens;

import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

/**
 * This action manager does nothing. Its throws an error if it is not replaced by the current action manager.
 * 
 * @author Balthazar Pavot
 *
 */
//public class PlayGameScreenKeyboardManager extends GameScreenKeyboardManager {
public class SidePanelKeyboardManager extends GameScreenKeyboardManager implements KeyEventDispatcher  {
	protected PlayGameScreen gameScreen = null;
	protected KeyboardFocusManager focusManager ;

	public SidePanelKeyboardManager(GameScreen gameScreen, KeyboardFocusManager focusManager) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.focusManager = focusManager ;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '+':
			gameScreen.getMapWidget().increaseZoom();
			break;
		case '-':
			gameScreen.getMapWidget().decreaseZoom();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		this.focusManager.redispatchEvent (gameScreen.mapWidget, e) ;
		return true;
	}
}
