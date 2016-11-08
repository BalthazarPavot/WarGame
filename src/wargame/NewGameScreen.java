

package wargame ;


public class NewGameScreen extends GameScreen {



  private static final long serialVersionUID = 1L;

  public NewGameScreen (GameContext gameContext) {
    super (gameContext) ;
    this.actionManager = new NewGameScreenActionManager (this) ;
  }

}
