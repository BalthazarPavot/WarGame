
package wargame.screens;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import wargame.GameContext;
import wargame.map.Map;
import wargame.widgets.*;

public class MainScreen extends GameScreen {

	private static final long serialVersionUID = 1L;
	public static String QUICK_GAME_STRING = "Quick Game";
	public static String NEW_GAME_STRING = "New Game";
	public static String LOAD_GAME_STRING = "Load Game";
	public static String CONFIGURATION_STRING = "Configuration";
	public static String QUIT_GAME_STRING = "Quit";

	public MainScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new MainScreenActionManager(this);
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
			button.setActionCommand(buttonTexts[j]);
			this.addWidgets(button);
		}
		this.createBackground();
	}

	private void createBackground() {
		ArrayList<BufferedImage> backgroundImageList;
		ArrayList<BufferedImage> treeImageList;
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
				else if (rand.nextInt(150) == 0) {// && randint == 0) {
					this.addWidgets(
							new ImageWidget(x + Map.squareWidth, y + Map.squareHeight, Map.squareWidth,
									Map.squareHeight, gameContext.getSpriteHandler().get("water").get(3)));
					this.addWidgets(new ImageWidget(x, y + Map.squareHeight, Map.squareWidth,
							Map.squareHeight, gameContext.getSpriteHandler().get("water").get(2)));
					this.addWidgets(new ImageWidget(x + Map.squareWidth, y, Map.squareWidth, Map.squareHeight,
							gameContext.getSpriteHandler().get("water").get(1)));
					this.addWidgets(new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
							gameContext.getSpriteHandler().get("water").get(0)));
				}
				this.addWidgets(new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
						backgroundImageList.get(rand.nextInt(backgroundImageList.size()))));
			}
		}
	}
}
