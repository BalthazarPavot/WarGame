
package wargame.screens;

import java.awt.event.ActionEvent;

import wargame.GameContext;
import wargame.map.MapGenerator;

/**
 * This screen is never displayed.
 * It make the map generator to generate a random map,
 * and launch the game with this map.
 * @author Balthazar Pavot
 *
 */
public class QuickGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	public QuickGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new QuickGameScreenActionManager(this);
	}


	public void prepare() {
		MapGenerator mapGenerator ;

		mapGenerator = new MapGenerator() ;
		mapGenerator.generate (gameContext.getSpriteHandler()) ;
		this.gameContext.setMap(mapGenerator.getMap());
		this.nextScreenID = GameScreen.PLAY_GAME_SCREEN ;
	}

	/**
	 * Initialize the screen with a new frame, a layout and trigger the first display.
	 */
	protected void initRun() {
		super.initRun () ;
		this.setLayout(null); // deletion of layout manager
		GameScreen.mainFrame.getContentPane().add(this);
		this.screenHasFinished = false;
		this.display();
		GameScreen.mainFrame.setVisible(true);
		this.screenTermination();
	}

}

class QuickGameScreenActionManager extends GameScreenActionManager {

	public QuickGameScreenActionManager(GameScreen gameScreen) {
		super(gameScreen);
	}

	public void actionPerformed(ActionEvent e) {
	}

}

