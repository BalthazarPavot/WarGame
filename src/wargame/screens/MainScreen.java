
package wargame.screens;

import java.awt.Color;

import wargame.GameContext;
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
		this.addWidgets(new TextWidget("War Game", w / 2 - 150, h / 10 * 2 - 100, 300, 200, 50,
				new Color(128, 128, 128)));
		for (int i = 7, j = 0; i < 12; j++, i++) {
			ButtonWidget button;

			button = new ButtonWidget(buttonTexts[j], w / 2 - 100, h / 15 * i, 200, 30, 20,
					new Color(128, 128, 128));
			button.addActionListener(this.actionManager);
			button.setActionCommand(buttonTexts[j]);
			this.addWidgets(button);
		}
		this.addWidgets(new ImageWidget(100, 100, 100, 150, "resources/images/test_image.png"));
	}
}
