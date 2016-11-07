

//package wargame ;

import wargame.* ;


/**
 * Entry point of the game.                 <br />
 * Initialize the game screen generator.    <br />
 * Request the main screen.                 <br />
 * Launch the main screen.                  <br />
 * while the player does not quit the game: <br />
 * Get the next screen to launch.           <br />
 * Launch the requested screen.             <br />
 * @author Pavot Balthazar ; Pelegrin Romain
 */

public class WarGame {

  /**
   * Launch the mainloop giving the first window id.
   * @param   args  Does nothing.
   * @return  nothing
   */
  public static void main(String[] args) {
    mainloop (GameScreen.MAIN_MENU_SCREEN) ;
  }

  /**
   * Create the game context and the error manager, load the config and create
   *  the game screen generator giving the context. <br />
   * Launch the mainloop that prepare the current screen, make
   *  it run, get the new window id and loop again while the player does not
   *  decide to quit.
   * @param   window_id The id of the first window to launch.
   * @return  nothing
   */
  public static void mainloop (int window_id) {
    GameScreenGenerator gameScreenGenerator ;
    GameScreen gameScreen ;
    GameContext gameContext ;
    ErrorManager errorManager ;

    errorManager = ErrorManager.get () ;
    gameContext = new GameContext (errorManager) ;
    gameContext.loadConf () ;
    gameScreenGenerator = new GameScreenGenerator (gameContext) ;
    do {
      gameScreenGenerator.prepareWindow (window_id) ;
      gameScreen = gameScreenGenerator.getGameScreen () ;
      window_id = gameScreen.run () ;
      System.out.println(window_id);
    } while (window_id != GameScreen.QUIT_SCREEN) ;
  }

}
