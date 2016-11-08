

package wargame ;

import java.lang.Thread ;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList ;

import wargame.widgets.* ;

public class GameScreen extends JPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  final public static int MAIN_MENU_SCREEN = 1 ;
  final public static int QUIT_SCREEN = 255 ;

  protected GameContext gameContext = null ;
  protected JFrame mainFrame = null ;
  protected ArrayList<Component> gameWidgets = new ArrayList<Component> () ;
  protected int nextScreenID = GameScreen.MAIN_MENU_SCREEN ;
  protected boolean screenHasFinished = false ;
  protected GameScreenActionManager actionManager = null ;

  public GameScreen (GameContext gameContext) {
    this.gameContext = gameContext ;
  }

  /**
   * Make the screen to run.
   * @return The id of the next game screen.
   */
  public int run () throws IllegalStateException {
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if (actionManager == null)
      throw new IllegalStateException () ;
    this.initRun () ;
    while (! this.screenHasFinished) {
      //this.display () ;
      try {
        Thread.sleep (500) ;
      } catch (InterruptedException e) {
        this.gameTermination () ;
      }
    }
    return this.nextScreenID ;
  }

  /**
   * Add the given wiget to the widget to display list
   * @param widget A Component to display.
   */
  public void addWidgets (Component widget) {
    this.gameWidgets.add (widget) ;
  }

  public void prepare () throws IllegalStateException {
    throw new IllegalStateException () ;  
  }

  public void screenTermination () {
    screenHasFinished = true ;
  }

  /**
   * Initialize the screen with a new frame, set the screen size, 
   * layout and trigger the first display.
   */
  private void initRun () {
    this.mainFrame = new JFrame (GameContext.TITLE) ;
    this.mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    this.setPreferredSize (this.gameContext.getDimension ()) ;
    this.setLayout (null) ; // deletion of layout manager
    this.mainFrame.setBounds (0, 0, this.gameContext.getWidth (),
      this.gameContext.getHeight ()) ;
    this.mainFrame.getContentPane ().add (this) ;
    this.screenHasFinished = false ;
    this.display () ;
    this.mainFrame.setVisible (true) ;  
  }

  /**
   * Trigger the game termination by finishing the display loop and saying
   * that the new screen is the exit screen.
   */
  private void gameTermination () {
    this.nextScreenID = GameScreen.QUIT_SCREEN ;
    this.screenHasFinished = true ;
  }

  /**
   * Remove all widgets, add again all the widgets to their position.
   */
  private void display () {
    this.removeAll () ;
    for (Component widget: gameWidgets) {
      this.add (widget) ;
      ((GameWidget)widget).bind () ;
    }
  }

}
