

package wargame ;


public class GameScreenGenerator {

  private GameContext gameContext = null ;
  private GameScreen preparedGameScreen = null ;

  public GameScreenGenerator (GameContext gameContext) {
    this.gameContext = gameContext ;
  }

  /**
   * Create a GameScreen instance and make it ready to run in the game context.
   *  Store the game screen in preparedGameScreen.
   * @param game_screen_id   The id of the game screen to prepare.
   */
  public void prepareGameScreen (int game_screen_id) {
    preparedGameScreen = new GameScreen (gameContext) ;
    prepareGameScreen (game_screen_id, preparedGameScreen) ;
  }

  /**
   * @return The game screen previously prepared, or null.
   */ 
  public GameScreen getGameScreen () {
    return preparedGameScreen ;
  }

  private void prepareGameScreen (int game_screen_id, GameScreen preparedGameScreen) {
    
  }
}
