
package wargame.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

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
	protected boolean passingToNextTurn = false;

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
		if (gameContext.isLoaded) {
			load(gameContext.loadedFile);
			nextScreenID = GameScreen.MAIN_MENU_SCREEN ;
			screenTermination();
			return ;
		} else {
			this.mapWidget = new MapWidget(this.gameContext.getMap(), this.gameContext.getWidth() - 150,
					this.gameContext.getHeight(), gameContext.getSpriteHandler(), actionManager);
		}
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
		mapWidget.setRevealed();
	}

	/**
	 * Prepare the play game screen, displayed when the player is ... playing.
	 */
	public void prepare() {
		// the loading doesn't work.
		if (gameContext.isLoaded)
			return ;
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
		HashMap<String, GameWidget> descCtnt;
		GameWidget currentComponent;
		int index = 0;

		sidePanel.addMouseListener(sidePanelMouseManager);
		sidePanel.addMouseMotionListener(sidePanelMouseMotionManager);
		sidePanel.addKeyListener(sidePanelKeyboardManager);
		sidePanel.setBackground(new Color(153, 108, 57));
		sidePanel.setOpaque(true);
		sidePanel.setLayout(null);
		sidePanel.addWidget(new ButtonWidget("Next turn", 10, 20 + sidePanel.getMinimapHeight(),
				sidePanel.getMinimapWidth(), 20 + index++ * 30, 12, Color.black, sidePanelActionManager));
		sidePanel.addWidget(new ButtonWidget("Auto-play: " + (engine.isAutoGame() ? "on" : "off"), 10,
				20 + index++ * 30 + sidePanel.getMinimapHeight(), sidePanel.getMinimapWidth(), 20, 12,
				Color.black, sidePanelActionManager));
		descCtnt = new HashMap<String, GameWidget>();
		sidePanel.addWidget(new ButtonWidget("Save", 10, 20 + index++ * 30 + sidePanel.getMinimapHeight(),
				sidePanel.getMinimapWidth(), 20, 12, Color.black, actionManager));
		sidePanel.addWidget(new ButtonWidget("Quit", 10, 20 + index++ * 30 + sidePanel.getMinimapHeight(),
				sidePanel.getMinimapWidth(), 20, 12, Color.black, actionManager));
		index++;
		for (String name : new String[] { "name", "life", "attack", "defPierce", "defBlunt", "defSlash",
				"defMagic" }) {
			currentComponent = new TextWidget("", new Rectangle(10,
					20 + index++ * 30 + sidePanel.getMinimapHeight(), sidePanel.getMinimapWidth(), 20));
			descCtnt.put(name, currentComponent);
			((JLabel) currentComponent).setForeground(Color.white);
			sidePanel.addWidget(currentComponent);
		}
		this.addWidgets(sidePanel);
		sidePanel.setDescriptionContent(descCtnt);
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
		mapWidget.unitInAction = null ;
		if (isPassingToNextTurn()) {
			if (engine.makeCurrentUnitAction()) {
				return;
			} else {
				turnTimer = System.currentTimeMillis();
				engine.nextTurn();
				turnTimer = System.currentTimeMillis();
				setPassingToNextTurn(false);
			}
		}
		if (engine.isAutoGame() && System.currentTimeMillis() - turnTimer > turnDuration) {
			setPassingToNextTurn(true);
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

	public boolean isPassingToNextTurn() {
		return passingToNextTurn;
	}

	public void setPassingToNextTurn(boolean passingToNextTurn) {
		this.passingToNextTurn = passingToNextTurn;
		if (passingToNextTurn) {
			if (engine.getPlayerUnits().size() != 0)
				engine.currentActingUnit = 0;
			if (engine.getEnnemyUnits().size() != 0)
				engine.currentActingEnemy = 0;
		}
	}

	public void save(File selectedFile) {
		ObjectOutputStream oos = null;
		try {
			final FileOutputStream fichier = new FileOutputStream(selectedFile);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(mapWidget);
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void load(File file) {
		ObjectInputStream ois = null;
		try {
			final FileInputStream fichier = new FileInputStream(file);
			ois = new ObjectInputStream(fichier);
			mapWidget = (MapWidget) ois.readObject();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
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
		if (e.getActionCommand().equals("YOU WIN") || e.getActionCommand().equals("YOU LOSE")) {
			this.gameScreen.nextScreenID = GameScreen.MAIN_MENU_SCREEN;
			this.gameScreen.screenTermination();
		} else if (e.getActionCommand().equals("Save")) {
			JFileChooser fc = new JFileChooser() {
				private static final long serialVersionUID = -718754014460685192L;
			};
			fc.setCurrentDirectory(new java.io.File("."));
			fc.setDialogTitle("Choose a file name");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			System.out.println("make save");
			if (fc.showSaveDialog(gameScreen) == JFileChooser.APPROVE_OPTION)
				gameScreen.save(fc.getSelectedFile());
		} else if (e.getActionCommand().equals("Quit")) {
			this.gameScreen.nextScreenID = GameScreen.MAIN_MENU_SCREEN;
			this.gameScreen.screenTermination();
		}
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
			gameScreen.setPassingToNextTurn(true);
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
		}
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
