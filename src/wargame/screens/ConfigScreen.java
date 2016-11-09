

package wargame.screens ;


import java.awt.Color;

import wargame.GameContext;
import wargame.widgets.* ;


public class ConfigScreen extends GameScreen {


  private static final long serialVersionUID = 1L;
  public static String QUICK_GAME_STRING = "Quick Game" ;
  public static String NEW_GAME_STRING = "New Game" ;
  public static String LOAD_GAME_STRING = "Load Game" ;
  public static String CONFIGURATION_STRING = "Configuration" ;
  public static String QUIT_GAME_STRING = "Quit" ;


  public ConfigScreen (GameContext gameContext) {
    super (gameContext) ;
    this.actionManager = new ConfigScreenActionManager (this) ;
  }

  /**
   * Prepare the main screen, the one display at the launch of the game.
   */
  public void prepare () {
    ButtonWidget button ;

    button = new ButtonWidget ("Previous",
      20, gameContext.getHeight ()-100, 100, 20, 14, new Color (128, 128, 128)) ;
    button.addActionListener (this.actionManager) ;
    button.setActionCommand ("Previous") ;
    this.addWidgets (button) ;
    button = new ButtonWidget ("Quit",
      20, gameContext.getHeight ()-60, 100, 20, 14, new Color (128, 128, 128)) ;
    button.addActionListener (this.actionManager) ;
    button.setActionCommand ("Quit") ;
    this.addWidgets (button) ;
  }
}
