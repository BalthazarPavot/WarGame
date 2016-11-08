

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
   * Launch the mainloop giving the first game screen id.
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
   *  it run, get the new game screen id and loop again while the player does not
   *  decide to quit.
   * @param   gameScreenID The id of the first game screen to launch.
   * @return  nothing
   */
  public static void mainloop (int gameScreenID) {
    GameScreenGenerator gameScreenGenerator ;
    GameScreen gameScreen ;
    GameContext gameContext ;
    ErrorManager errorManager ;

    errorManager = ErrorManager.get () ;
    gameContext = new GameContext (errorManager) ;
    gameContext.loadConf () ;
    gameScreenGenerator = new GameScreenGenerator (gameContext) ;
    do {
      gameScreenGenerator.prepareGameScreen (gameScreenID) ;
      gameScreen = gameScreenGenerator.getGameScreen () ;
      gameScreenID = gameScreen.run () ;
      System.out.printf ("Screen ID: %d\n", gameScreenID);
    } while (gameScreenID != GameScreen.QUIT_SCREEN) ;
    System.exit (0) ;
  }

}
