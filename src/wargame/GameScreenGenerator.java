

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
   * @param window_id   The id of the window to prepare.
   */
  public void prepareWindow (int window_id) {
    preparedGameScreen = new GameScreen (gameContext) ;
    prepareWindow (window_id, preparedGameScreen) ;
  }

  /**
   * @return The game screen previously prepared, or null.
   */ 
  public GameScreen getGameScreen () {
    return preparedGameScreen ;
  }

  private void prepareWindow (int window_id, GameScreen preparedGameScreen) {
    
  }
}
