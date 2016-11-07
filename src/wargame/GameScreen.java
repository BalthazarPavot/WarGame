

package wargame ;


public class GameScreen {

  public static int MAIN_MENU_SCREEN = 1 ;
  public static int QUIT_SCREEN = 255 ;

  private GameContext gameContext = null ;

  public GameScreen (GameContext gameContext) {
    this.gameContext = gameContext ;
  }

  /**
   * Make the screen to run.
   * @return The id of the next window.
   */
  public int run () {
//    return GameScreen.QUIT_SCREEN ;
	  return GameScreen.MAIN_MENU_SCREEN ;
  }

}
