
package wargame.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;

import wargame.GameContext;
import wargame.basic_types.SerializableBufferedImage;
import wargame.map.Map;
import wargame.widgets.*;

/**
 * This screen is the first screen of the game, the main menu.
 * 
 * @author Balthazar Pavot
 *
 */
public class MainScreen extends GameScreen {

	private static final long serialVersionUID = 1L;
	public static String QUICK_GAME_STRING = "Quick Game";
	public static String NEW_GAME_STRING = "New Game";
	public static String LOAD_GAME_STRING = "Load Game";
	public static String CONFIGURATION_STRING = "Configuration";
	public static String QUIT_GAME_STRING = "Quit";

	public MainScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new MainScreenActionManager(this, gameContext);
	}

	/**
	 * Prepare the main screen, the one display at the launch of the game.
	 */
	public void prepare() {
		String[] buttonTexts = { QUICK_GAME_STRING, NEW_GAME_STRING, LOAD_GAME_STRING, CONFIGURATION_STRING,
				QUIT_GAME_STRING };
		int w;
		int h;

		w = gameContext.getWidth();
		h = gameContext.getHeight();
		this.addWidgets(new TextWidget("War Game", w / 2 - 134, h / 10 * 2 - 100, 268, 200, 50,
				new Color(128, 128, 128)));
		for (int i = 7, j = 0; i < 12; j++, i++) {
			ButtonWidget button;

			button = new ButtonWidget(buttonTexts[j], w / 2 - 100, h / 15 * i, 200, 30, 20,
					new Color(128, 128, 128));
			button.addActionListener(this.actionManager);
			this.addWidgets(button);
		}
		this.createBackground();
	}

	private void createBackground() {
		ArrayList<SerializableBufferedImage> backgroundImageList;
		ArrayList<SerializableBufferedImage> treeImageList;
		Random rand;
		int randint;

		rand = new Random();
		randint = rand.nextInt(2);
		backgroundImageList = gameContext.getSpriteHandler()
				.get(randint == 1 ? "snow_textures" : "grass_textures");
		treeImageList = gameContext.getSpriteHandler().get(randint == 1 ? "tree_snow_set" : "tree_set");
		for (int x = 0; x < gameContext.getWidth(); x += Map.squareWidth) {
			for (int y = 0; y < gameContext.getHeight(); y += Map.squareHeight) {
				if (rand.nextInt(20) == 0)
					this.addWidgets(new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
							treeImageList.get(rand.nextInt(treeImageList.size()))));
				else if (rand.nextInt(20) == 0 && randint == 1)
					this.addWidgets(new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
							gameContext.getSpriteHandler().get("rock_snow_set").get(0)));
				else if (rand.nextInt(150) == 0) {
					this.addWidgets(new ImageWidget(x + Map.squareWidth, y + Map.squareHeight,
							Map.squareWidth, Map.squareHeight,
							gameContext.getSpriteHandler().get("water_sand_curve_out").get(3)));
					this.addWidgets(
							new ImageWidget(x, y + Map.squareHeight, Map.squareWidth, Map.squareHeight,
									gameContext.getSpriteHandler().get("water_sand_curve_out").get(2)));
					this.addWidgets(new ImageWidget(x + Map.squareWidth, y, Map.squareWidth, Map.squareHeight,
							gameContext.getSpriteHandler().get("water_sand_curve_out").get(1)));
					this.addWidgets(new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
							gameContext.getSpriteHandler().get("water_sand_curve_out").get(0)));
				}
				this.addWidgets(new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
						backgroundImageList.get(rand.nextInt(backgroundImageList.size()))));
			}
		}
	}
}

/**
 * Manage the actions of the buttons.
 * 
 * @author Balthazar Pavot
 *
 */
class MainScreenActionManager extends GameScreenActionManager {

	private GameContext context;

	public MainScreenActionManager(GameScreen gameScreen, GameContext context) {
		super(gameScreen);
		this.context = context ;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(MainScreen.QUICK_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.QUICK_GAME_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.NEW_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.NEW_GAME_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.LOAD_GAME_STRING)) {
			JFileChooser fc = new JFileChooser() {
				private static final long serialVersionUID = -718754014460685192L;
			};
			fc.setCurrentDirectory(new java.io.File("."));
			fc.setDialogTitle("Choose a file name");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			System.out.println("make save");
			if (fc.showSaveDialog(gameScreen) == JFileChooser.APPROVE_OPTION) {
				context.isLoaded = true;
				context.loadedFile = fc.getSelectedFile() ;
				gameScreen.nextScreenID = GameScreen.PLAY_GAME_SCREEN;
			}
		} else if (e.getActionCommand().equals(MainScreen.CONFIGURATION_STRING)) {
			gameScreen.nextScreenID = GameScreen.CONFIGURATION_SCREEN;
		} else if (e.getActionCommand().equals(MainScreen.QUIT_GAME_STRING)) {
			gameScreen.nextScreenID = GameScreen.QUIT_SCREEN;
		} else {
			return;
		}
		this.gameScreen.screenTermination();
	}

}
