
package wargame.screens;

import java.awt.Color;
import java.awt.Graphics;
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
 * Defines the main screen of the game (when we are playing).
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
		engine = new Engine(gameContext, mapWidget, sidePanel);
		sidePanelActionManager = new SidePanelActionManager(this, sidePanel, engine);
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
		buildMapWidget();
		buildSidePanel();
		engine.updateFog();
	}

	/**
	 * Create and initialise the map widget
	 */
	private void buildMapWidget() {
		mapWidget.addMouseListener(this.mouseManager);
		mapWidget.addMouseMotionListener(this.mouseMotionManager);
		mapWidget.addKeyListener(keyboardManager);
		mapWidget.setBackground(Color.BLACK);
		mapWidget.setOpaque(true);
		this.addWidgets(mapWidget);
	}

	/**
	 * Create and initialise the side panel
	 */
	private void buildSidePanel() {
		sidePanel.addMouseListener(sidePanelMouseManager);
		sidePanel.addMouseMotionListener(sidePanelMouseMotionManager);
		sidePanel.addKeyListener(sidePanelKeyboardManager);
		sidePanel.setBackground(new Color(153, 108, 57));
		sidePanel.setOpaque(true);
		sidePanel.setLayout(null);
		sidePanel.addWidget(new ButtonWidget("Next turn", 10, 20 + sidePanel.getMinimapHeight(),
				sidePanel.getMinimapWidth(), 20, 12, Color.black, sidePanelActionManager));
		sidePanel.addWidget(new ButtonWidget("Auto-play: " + (engine.isAutoGame() ? "on" : "off"), 10,
				50 + sidePanel.getMinimapHeight(), sidePanel.getMinimapWidth(), 20, 12, Color.black,
				sidePanelActionManager));
		this.addWidgets(sidePanel);
	}

	/**
	 * return the map widget
	 * 
	 * @return
	 */
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	/**
	 * handle looping action like the map scrolling, the next turn actions etc.
	 */
	protected void windowManagement() {
		mapWidget.moveFrame(leftScrolling ? -1 : rightScrolling ? 1 : 0,
				upScrolling ? -1 : downScrolling ? 1 : 0);
		sidePanel.updateFrame();
		if (mapWidget.interfaceWidget.inAnimationLoop())
			return;
		if (engine.isAutoGame() && System.currentTimeMillis() - turnTimer > turnDuration) {
			engine.nextTurn();
			turnTimer = System.currentTimeMillis();
		}
	}

	/**
	 * repaint the component.
	 */
	protected void display() {
		repaint();
	}

	/**
	 * Display the map widget and the side panel
	 */
	protected void paintComponent(Graphics g) {
		sidePanel.paintComponent(g);
		mapWidget.paintComponent(g);
	}
}

/**
 * Class used to manage the mouse on the map screen.
 * 
 * @author Balthazar Pavot
 *
 */
class PlayGameScreenMouseManager extends GameScreenMouseManager {

	protected PlayGameScreen gameScreen = null;

	public PlayGameScreenMouseManager(GameScreen gameScreen) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
	}

	public void mouseClicked(MouseEvent event) {
		Position mapPosition;

		mapPosition = new Position(event.getX(), event.getY());
		if (event.getButton() == MouseEvent.BUTTON1)
			gameScreen.engine.mapLeftClicked(mapPosition);
		if (event.getButton() == MouseEvent.BUTTON3)
			gameScreen.engine.mapRightClicked(mapPosition);
	}
}

/**
 * Class used to manage mouse motions on the map screen.
 * 
 * @author Balthazar Pavot
 *
 */
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

/**
 * Class used to manage button action on the map screen.
 * 
 * @author Balthazar Pavot
 *
 */
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

/**
 * Class used to manage the keyboard inputs on the map screen.
 * 
 * @author Balthazar Pavot
 *
 */
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

/**
 * Class used to manage buttons actions on the side panel.
 * 
 * @author Balthazar Pavot
 *
 */
class SidePanelActionManager extends GameScreenActionManager {

	protected PlayGameScreen gameScreen = null;
	protected SidePanel sidePanel;
	protected Engine engine;

	public SidePanelActionManager(GameScreen gameScreen, SidePanel sidePanel, Engine engine) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.sidePanel = sidePanel;
		this.engine = engine;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getActionCommand().equals("Next turn")) {
			engine.nextTurn();
			;
		} else if (e.getActionCommand().equals("Auto-play: on")) {
			((ButtonWidget) e.getSource()).setText("Auto-play: off");
			engine.unSetAutoGame();
		} else if (e.getActionCommand().equals("Auto-play: off")) {
			((ButtonWidget) e.getSource()).setText("Auto-play: on");
			engine.setAutoGame();
		}
	}

}

/**
 * Class used to manage the keyboard inputs on the side panel.
 * 
 * @author Balthazar Pavot
 *
 */
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

/**
 * Class used to manage mouse clicks on the side panel.
 * 
 * @author Balthazar Pavot
 *
 */
class SidePanelMouseManager extends GameScreenMouseManager {

	protected PlayGameScreen gameScreen = null;
	protected SidePanel sidePanel;
	protected MapWidget mapWidget;

	public SidePanelMouseManager(GameScreen gameScreen, SidePanel sidePanel, MapWidget mapWidget) {
		super(gameScreen);
		this.gameScreen = (PlayGameScreen) gameScreen;
		this.sidePanel = sidePanel;
		this.mapWidget = mapWidget;
	}

	public void mouseClicked(MouseEvent event) {
		if (event.getX() > sidePanel.getMinimapRectangle().getX()
				&& event.getY() > sidePanel.getMinimapRectangle().getY()
				&& event.getX() < sidePanel.getMinimapRectangle().getX()
						+ sidePanel.getMinimapRectangle().getWidth()
				&& event.getY() < sidePanel.getMinimapRectangle().getY()
						+ sidePanel.getMinimapRectangle().getHeight()) {
			sidePanel.setFramePosition((int) (event.getX() - sidePanel.getMinimapFrame().getWidth() / 2),
					(int) (event.getY() - sidePanel.getMinimapFrame().getHeight() / 2));
			mapWidget.updateFramePositionFromMinimap(sidePanel.getMinimapFrame(),
					sidePanel.getMinimapRectangle());
		} else
			System.out.println("Clicked! (panel)");
	}
}

/**
 * Class used to manage the mouse motion on the side panel.
 * 
 * @author Balthazar Pavot
 *
 */
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
