

package wargame ;

import java.awt.event.* ;

public class LoadGameScreenActionManager extends GameScreenActionManager {

  public LoadGameScreenActionManager (GameScreen gameScreen) {
    super (gameScreen) ;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(MainScreen.QUICK_GAME_STRING)) {
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
