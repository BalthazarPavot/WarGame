

package wargame ;

import java.awt.*;
import wargame.widgets.* ;


public class LoadGameScreen extends GameScreen {


  private static final long serialVersionUID = 1L;

  public LoadGameScreen (GameContext gameContext) {
    super (gameContext) ;
    this.actionManager = new LoadGameScreenActionManager (this) ;
  }

}
