

package wargame ;

import java.awt.*;
import wargame.widgets.* ;


public class MainScreen extends GameScreen {


  private static final long serialVersionUID = 1L;
  public static String QUICK_GAME_STRING = "Quick Game" ;
  public static String NEW_GAME_STRING = "New Game" ;
  public static String LOAD_GAME_STRING = "Load Game" ;
  public static String QUIT_GAME_STRING = "Quit" ;


  public MainScreen (GameContext gameContext) {
    super (gameContext) ;
    this.actionManager = new MainScreenActionManager (this) ;
  }

  /**
   * Prepare the main screen, the one display at the launch of the game.
   */
  public void prepare () {
    String[] buttonTexts = {QUICK_GAME_STRING, NEW_GAME_STRING, LOAD_GAME_STRING, QUIT_GAME_STRING} ;
    int w ;
    int h ;

    w = gameContext.getWidth () ;
    h = gameContext.getHeight () ;
    this.addWidgets (new TextWidget ("War Game",
      w/2-150, h/10*2, 300, 200, 50, new Color (128, 128, 128))) ;
    for (int i = 5 ; i < 9 ; i ++ ) {
      ButtonWidget button ;

      button = new ButtonWidget (buttonTexts[i-5],
        w/2-100, h/10*i, 200, 40, 20, new Color (128, 128, 128)) ;
      button.addActionListener (this.actionManager) ;
      button.setActionCommand (buttonTexts[i-5]) ;
      this.addWidgets (button) ;
    } 
  }
}
