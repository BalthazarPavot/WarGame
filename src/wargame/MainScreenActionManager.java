

package wargame ;

import java.awt.event.* ;

public class MainScreenActionManager extends GameScreenActionManager {

  public MainScreenActionManager (GameScreen gameScreen) {
    super (gameScreen) ;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(MainScreen.QUICK_GAME_STRING)) {
      gameScreen.nextScreenID = 42;
    } else if (e.getActionCommand().equals(MainScreen.NEW_GAME_STRING)) {
    } else if (e.getActionCommand().equals(MainScreen.LOAD_GAME_STRING)) {
    } else if (e.getActionCommand().equals(MainScreen.QUIT_GAME_STRING)) {
      gameScreen.nextScreenID = GameScreen.QUIT_SCREEN ;
    } else {
      return ;
    }
    this.gameScreen.screenTermination () ;
  }

}
