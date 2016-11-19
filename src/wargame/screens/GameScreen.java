
package wargame.screens;

import java.awt.Component;
import java.lang.Thread;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import wargame.GameContext;
import wargame.widgets.*;

/**
 * Defines a screen of the game.
 * 
 * @author Balthazar Pavot
 *
 */
public abstract class GameScreen extends JPanel {

	private static final long serialVersionUID = 1L;
	final public static int MAIN_MENU_SCREEN = 1;
	final public static int NEW_GAME_SCREEN = 10;
	final public static int LOAD_GAME_SCREEN = 30;
	final public static int CONFIGURATION_SCREEN = 50;
	final public static int QUICK_GAME_SCREEN = 70;
	final public static int PLAY_GAME_SCREEN = 90;
	final public static int QUIT_SCREEN = 255;
	protected static JFrame mainFrame = null;

	protected GameContext gameContext = null;
	protected ArrayList<Component> gameWidgets = new ArrayList<Component>();
	protected int nextScreenID = GameScreen.MAIN_MENU_SCREEN;
	protected boolean screenHasFinished = false;
	protected GameScreenActionManager actionManager = null;

	public GameScreen(GameContext gameContext) {
		this.gameContext = gameContext;
		if (GameScreen.mainFrame == null) {
			GameScreen.mainFrame = new JFrame(GameContext.TITLE);
			this.initGameScreen();
		}
	}

	/**
	 * Make the screen to run.
	 * 
	 * @return The id of the next game screen.
	 */
	public int run() throws IllegalStateException {
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (actionManager == null)
			throw new IllegalStateException();
		this.initRun();
		while (!this.screenHasFinished) {
			// this.display () ;
			windowManagement();
			display();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				this.gameTermination();
			}
		}
		return this.nextScreenID;
	}

	/**
	 * Add the given wiget to the widget to display list
	 * 
	 * @param widget
	 */
	public void addWidgets(Component widget) {
		this.gameWidgets.add(widget);
	}

	/**
	 * Must be overwritten. TODO: export this into an interface?
	 */
	public void prepare() throws IllegalStateException {
		throw new IllegalStateException("The screen did not overwite the method prepare");
	}

	public void screenTermination() {
		for (Component widget : gameWidgets)
			this.remove(widget);
		screenHasFinished = true;
	}

	/**
	 * Set the default behaviours of every screens and the size.
	 */
	private void initGameScreen() {
		GameScreen.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(this.gameContext.getDimension());
		GameScreen.mainFrame.setBounds(0, 0, this.gameContext.getWidth(), this.gameContext.getHeight());
		GameScreen.mainFrame.setResizable(false);
	}

	/**
	 * Initialize the screen with a new frame, a layout and trigger the first display.
	 */
	protected void initRun() {
		this.setLayout(null); // deletion of layout manager
		GameScreen.mainFrame.getContentPane().add(this);
		this.screenHasFinished = false;
		for (Component widget : gameWidgets) {
			this.add(widget);
			((GameWidget) widget).bind() ;
		}
		this.display();
		GameScreen.mainFrame.setVisible(true);
	}

	protected void windowManagement() {
		// Do nothing here.y
	}

	/**
	 * Trigger the game termination by finishing the display loop and saying that the new screen is the exit
	 * screen.
	 */
	private void gameTermination() {
		this.nextScreenID = GameScreen.QUIT_SCREEN;
		this.screenHasFinished = true;
	}

	/**
	 * Remove all widgets, add again all the widgets to their position.
	 */
	protected void display() {
		// this.removeAll();
//		this.revalidate();
		this.repaint();
	}

}
