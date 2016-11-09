
package wargame ;

import wargame.screens.ConfigScreen;
import wargame.screens.GameScreen;
import wargame.screens.MainScreen;
import wargame.screens.NewGameScreen;

public class GameScreenGenerator {

  private GameContext gameContext = null ;
  private GameScreen preparedGameScreen = null ;

  public GameScreenGenerator (GameContext gameContext) {
    this.gameContext = gameContext ;
  }

  /**
   * Create a GameScreen instance and make it ready to run in the game context.
   *  Store the game screen in preparedGameScreen.
   * @param gameScreenID   The id of the game screen to prepare.
   */
  public void prepareGameScreen (int gameScreenID) {
    switch (gameScreenID) {
      case GameScreen.MAIN_MENU_SCREEN:
        preparedGameScreen = new MainScreen (gameContext) ;
        break ;
      case GameScreen.NEW_GAME_SCREEN:
          preparedGameScreen = new NewGameScreen (gameContext) ;
          break ;
      case GameScreen.CONFIGURATION_SCREEN:
          preparedGameScreen = new ConfigScreen(gameContext) ;
          break ;
      default:
        this.gameContext.getErrorManager ().exitError (String.format (
          "Unknown screen id: %d", gameScreenID)) ;
        break ;
    }
    preparedGameScreen.prepare () ;
  }

  /**
   * @return GameScreen he game screen previously prepared, or null.
   */ 
  public GameScreen getGameScreen () {
    return preparedGameScreen ;
  }

  /**
   * Call the good screen initializer in function of the game screen id
   * @param gameScreenID  The id of the screen to display.
   * @param preparedGameScreen The screen partialy prepared.
   */
}
