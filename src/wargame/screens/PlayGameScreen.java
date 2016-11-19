
package wargame.screens;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import wargame.GameContext;
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
	protected PlayGameScreenMouseManager mouseManager;
	protected PlayGameScreenMouseMotionManager mouseMotionManager;
	protected PlayGameScreenKeyboardManager keyboardManager;
	protected SidePanelActionManager sidePanelActionManager;
	protected SidePanelMouseManager sidePanelMouseManager;
	protected SidePanelMouseMotionManager sidePanelMouseMotionManager;
	protected SidePanelKeyboardManager sidePanelKeyboardManager;

	public PlayGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new PlayGameScreenActionManager(this);
		this.mouseManager = new PlayGameScreenMouseManager(this);
		this.mouseMotionManager = new PlayGameScreenMouseMotionManager(this);
		this.keyboardManager = new PlayGameScreenKeyboardManager(this,
				KeyboardFocusManager.getCurrentKeyboardFocusManager());
		sidePanelActionManager = new SidePanelActionManager(this);
		sidePanelMouseManager = new SidePanelMouseManager(this);
		sidePanelMouseMotionManager = new SidePanelMouseMotionManager(this);
		sidePanelKeyboardManager = new SidePanelKeyboardManager(this,
				KeyboardFocusManager.getCurrentKeyboardFocusManager());
	}

	/**
	 * Prepare the play game screen, displayed when the player is ... playing.
	 */
	public void prepare() {
		SidePanel sidePanel;

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboardManager);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(sidePanelKeyboardManager);
		mapWidget = new MapWidget(this.gameContext.getMap(), this.gameContext.getWidth() - 150,
				this.gameContext.getHeight());
		mapWidget.addMouseListener(this.mouseManager);
		mapWidget.addMouseMotionListener(this.mouseMotionManager);
		mapWidget.addKeyListener(keyboardManager);
		mapWidget.setBackground(Color.BLACK);
		mapWidget.setOpaque(true);
		this.addWidgets(mapWidget);
		sidePanel = new SidePanel(gameContext.getMap(), this.gameContext.getWidth() - 150, 0, 150,
				this.gameContext.getHeight(), gameContext.getSpriteHandler().get("side_panel_texture").get(0));
		sidePanel.addMouseListener(sidePanelMouseManager);
		sidePanel.addMouseMotionListener(sidePanelMouseMotionManager);
		sidePanel.addKeyListener(sidePanelKeyboardManager);
		// sidePanel.addWidget(new MiniMap(this.gameContext.getMap(), 10, 10, 120, 120));
		sidePanel.setBackground(new Color(153, 108, 57));
		sidePanel.setOpaque(true);
		this.addWidgets(sidePanel);
		// this.addWidgets(new ImageWidget(0, 0, 120, 120,
		// new MiniMap(this.gameContext.getMap(), this.gameContext.getWidth() - 140, 10, 120, 120)
		// .getImage().getImage()));

	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}

	protected void windowManagement() {
		mapWidget.moveFrame(leftScrolling ? -1 : rightScrolling ? 1 : 0,
				upScrolling ? -1 : downScrolling ? 1 : 0);

	}
}
