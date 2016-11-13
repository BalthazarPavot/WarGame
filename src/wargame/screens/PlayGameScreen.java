
package wargame.screens;

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
	protected MapWidget mapWidget ;
	protected PlayGameScreenMouseManager mouseManager;
	protected PlayGameScreenMouseMotionManager mouseMotionManager;

	public PlayGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new PlayGameScreenActionManager(this);
		this.mouseManager = new PlayGameScreenMouseManager(this);
		this.mouseMotionManager = new PlayGameScreenMouseMotionManager(this);
	}

	/**
	 * Prepare the play game screen, displayed when the player is ... playing.
	 */
	public void prepare() {
		mapWidget = new MapWidget(this.gameContext.getMap(), this.gameContext.getWidth() - 150,
				this.gameContext.getHeight());
		mapWidget.addMouseListener(this.mouseManager);
		mapWidget.addMouseMotionListener(this.mouseMotionManager);
		this.addWidgets(mapWidget);
		this.addWidgets(
				new SidePanel(this.gameContext.getWidth() - 150, 0, 150, this.gameContext.getHeight()));
	}

	public MapWidget getMapWidget () {
		return mapWidget ;
	}

	protected void windowManagement () {
		mapWidget.moveFrame(leftScrolling ? -1 : rightScrolling ? 1 : 0,
				upScrolling ? -1 : downScrolling ? 1 : 0);
		
	}
}
