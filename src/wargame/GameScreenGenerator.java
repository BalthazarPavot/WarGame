
package wargame;

import wargame.screens.ConfigScreen;
import wargame.screens.GameScreen;
import wargame.screens.LoadGameScreen;
import wargame.screens.MainScreen;
import wargame.screens.NewGameScreen;
import wargame.screens.PlayGameScreen;
import wargame.screens.QuickGameScreen;

/**
 * This class is the transition between each screens. It creates and initialise the asked screen.
 * 
 * @author Balthazar Pavot
 *
 */
public class GameScreenGenerator {

	private GameContext gameContext = null;
	private GameScreen preparedGameScreen = null;

	public GameScreenGenerator(GameContext gameContext) {
		this.gameContext = gameContext;
	}

	/**
	 * Create a GameScreen instance and make it ready to run in the game context. Store the game screen in
	 * preparedGameScreen, then, call the screen's initialiser.
	 * 
	 * @param gameScreenID
	 */
	public void prepareGameScreen(int gameScreenID) {
		switch (gameScreenID) {
		case GameScreen.MAIN_MENU_SCREEN:
			preparedGameScreen = new MainScreen(gameContext);
			break;
		case GameScreen.NEW_GAME_SCREEN:
			preparedGameScreen = new NewGameScreen(gameContext);
			break;
		case GameScreen.LOAD_GAME_SCREEN:
			preparedGameScreen = new LoadGameScreen(gameContext);
			break;
		case GameScreen.QUICK_GAME_SCREEN:
			preparedGameScreen = new QuickGameScreen(gameContext);
			break;
		case GameScreen.CONFIGURATION_SCREEN:
			preparedGameScreen = new ConfigScreen(gameContext);
			break;
		case GameScreen.PLAY_GAME_SCREEN:
			preparedGameScreen = new PlayGameScreen(gameContext);
			break ;
		default:
			this.gameContext.getErrorManager()
					.exitError(String.format("Unknown screen id: %d", gameScreenID));
			break;
		}
		preparedGameScreen.prepare();
	}

	/**
	 * @return GameScreen he game screen previously prepared, or null.
	 */
	public GameScreen getGameScreen() {
		return preparedGameScreen;
	}

}
