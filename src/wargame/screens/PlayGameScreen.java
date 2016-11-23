
package wargame.screens;

import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.engine.Engine;
import wargame.widgets.*;

/**
 * Defines a screen of the game.
 * 
 * @author Balthazar Pavot
 *
 */
public class PlayGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	protected boolean leftScrolling = false;
	protected boolean rightScrolling = false;
	protected boolean upScrolling = false;
	protected boolean downScrolling = false;
	protected MapWidget mapWidget;
	protected SidePanel sidePanel;
	protected PlayGameScreenMouseManager mouseManager;
	protected PlayGameScreenMouseMotionManager mouseMotionManager;
	protected PlayGameScreenKeyboardManager keyboardManager;
	protected SidePanelActionManager sidePanelActionManager;
	protected SidePanelMouseManager sidePanelMouseManager;
	protected SidePanelMouseMotionManager sidePanelMouseMotionManager;
	protected SidePanelKeyboardManager sidePanelKeyboardManager;
	protected Engine engine;
	protected long turnTimer = System.currentTimeMillis();
	private long turnDuration = 1000;

	public PlayGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new PlayGameScreenActionManager(this);
		this.mouseManager = new PlayGameScreenMouseManager(this);
		this.mouseMotionManager = new PlayGameScreenMouseMotionManager(this);
		this.keyboardManager = new PlayGameScreenKeyboardManager(this,
				KeyboardFocusManager.getCurrentKeyboardFocusManager());
		this.mapWidget = new MapWidget(this.gameContext.getMap(), this.gameContext.getWidth() - 150,
				this.gameContext.getHeight(), gameContext.getSpriteHandler());
		this.sidePanel = new SidePanel(gameContext.getMap(), this.gameContext.getWidth() - 150, 0, 150,
				this.gameContext.getHeight(), gameContext.getSpriteHandler().get("side_panel_texture").get(0),
				mapWidget.getFrame());
		sidePanelActionManager = new SidePanelActionManager(this, sidePanel);
		sidePanelMouseManager = new SidePanelMouseManager(this, sidePanel, mapWidget);
		sidePanelMouseMotionManager = new SidePanelMouseMotionManager(this, sidePanel);
		sidePanelKeyboardManager = new SidePanelKeyboardManager(this,
				KeyboardFocusManager.getCurrentKeyboardFocusManager(), sidePanel);
		turnTimer = System.currentTimeMillis();
	}

	/**
	 * Prepare the play game screen, displayed when the player is ... playing.
	 */
	public void prepare() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboardManager);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(sidePanelKeyboardManager);
		mapWidget.addMouseListener(this.mouseManager);
		mapWidget.addMouseMotionListener(this.mouseMotionManager);
		mapWidget.addKeyListener(keyboardManager);
		mapWidget.setBackground(Color.BLACK);
		mapWidget.setOpaque(true);
		this.addWidgets(mapWidget);
		sidePanel.addMouseListener(sidePanelMouseManager);
		sidePanel.addMouseMotionListener(sidePanelMouseMotionManager);
		sidePanel.addKeyListener(sidePanelKeyboardManager);
		sidePanel.setBackground(new Color(153, 108, 57));
		sidePanel.setOpaque(true);
		this.addWidgets(sidePanel);
		engine = new Engine(gameContext.getMap(), mapWidget, sidePanel);
		engine.setAutoGame();
		engine.updateFog();
	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}

	protected void windowManagement() {
		mapWidget.moveFrame(leftScrolling ? -1 : rightScrolling ? 1 : 0,
				upScrolling ? -1 : downScrolling ? 1 : 0);
		sidePanel.updateFrame();
		if (engine.autoGameMode && System.currentTimeMillis() - turnTimer > turnDuration) {
			engine.nextTurn();
			turnTimer = System.currentTimeMillis();
		}
	}

	protected void display() {
		repaint();
	}
}

class PlayGameScreenMouseManager extends GameScreenMouseManager {

	protected PlayGameScreen gameScreen = null;

	public PlayGameScreenMouseManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseClicked(MouseEvent event) {
		Position mapPosition;

		mapPosition = new Position(event.getX(), event.getY());
		gameScreen.engine.mapClicked(mapPosition);
	}
}

class PlayGameScreenMouseMotionManager extends GameScreenMouseMotionManager {

	protected PlayGameScreen gameScreen = null;

	public PlayGameScreenMouseMotionManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseDragged(ActionEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		int x;
		int y;

		x = e.getX();
		y = e.getY();
		gameScreen.leftScrolling = x < 30;
		gameScreen.rightScrolling = x > gameScreen.gameContext.getWidth() - 30 - 150;
		gameScreen.upScrolling = y < 30;
		gameScreen.downScrolling = y > gameScreen.gameContext.getHeight() - 60;
	}
}

class PlayGameScreenActionManager extends GameScreenActionManager {

	protected PlayGameScreen gameScreen = null;

	public PlayGameScreenActionManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

}

class PlayGameScreenKeyboardManager extends GameScreenKeyboardManager implements KeyEventDispatcher {
	protected PlayGameScreen gameScreen = null;
	protected KeyboardFocusManager focusManager;

	public PlayGameScreenKeyboardManager(GameScreen gameScreen, KeyboardFocusManager focusManager) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.focusManager = focusManager;
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
		this.focusManager.redispatchEvent(gameScreen.mapWidget, e);
		return true;
	}
}

class SidePanelActionManager extends GameScreenActionManager {

	protected PlayGameScreen gameScreen = null;
	protected SidePanel sidePanel;

	public SidePanelActionManager(GameScreen gameScreen, SidePanel sidePanel) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.sidePanel = sidePanel;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

}

class SidePanelKeyboardManager extends GameScreenKeyboardManager implements KeyEventDispatcher {
	protected PlayGameScreen gameScreen = null;
	protected KeyboardFocusManager focusManager;
	protected SidePanel sidePanel;

	public SidePanelKeyboardManager(GameScreen gameScreen, KeyboardFocusManager focusManager,
			SidePanel sidePanel) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.focusManager = focusManager;
		this.sidePanel = sidePanel;
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
		this.focusManager.redispatchEvent(gameScreen.mapWidget, e);
		return true;
	}
}

class SidePanelMouseManager extends GameScreenMouseManager {

	protected PlayGameScreen gameScreen = null;
	protected SidePanel sidePanel;
	protected MapWidget mapWidget;

	public SidePanelMouseManager(GameScreen gameScreen, SidePanel sidePanel, MapWidget mapWidget) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.sidePanel = sidePanel;
		this.mapWidget = mapWidget ;
	}

	public void mouseClicked(MouseEvent event) {
		if (event.getX() > sidePanel.getMinimapRectangle().getX()
				&& event.getY() > sidePanel.getMinimapRectangle().getY()
				&& event.getX() < sidePanel.getMinimapRectangle().getX()
						+ sidePanel.getMinimapRectangle().getWidth()
				&& event.getY() < sidePanel.getMinimapRectangle().getY()
						+ sidePanel.getMinimapRectangle().getHeight()) {
			sidePanel.setFramePosition((int)(event.getX() - sidePanel.getMinimapFrame().getWidth() / 2),
					(int)(event.getY() - sidePanel.getMinimapFrame().getHeight() / 2));
			mapWidget.updateFramePositionFromMinimap (sidePanel.getMinimapFrame(), sidePanel.getMinimapRectangle()) ;
		} else
			System.out.println("Clicked! (panel)");
	}
}

class SidePanelMouseMotionManager extends GameScreenMouseMotionManager {

	protected PlayGameScreen gameScreen = null;
	protected SidePanel sidePanel;

	public SidePanelMouseMotionManager(GameScreen gameScreen, SidePanel sidePanel) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.sidePanel = sidePanel;
	}

	public void mouseDragged(ActionEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		gameScreen.leftScrolling = false;
		gameScreen.rightScrolling = false;
		gameScreen.upScrolling = false;
		gameScreen.downScrolling = false;
	}
}
