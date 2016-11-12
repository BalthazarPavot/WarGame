
package wargame.screens;

import java.awt.Color;

import wargame.GameContext;
import wargame.widgets.*;

public class NewGameScreen extends GameScreen {

	private static final long serialVersionUID = 1L;

	public NewGameScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new NewGameScreenActionManager(this);
	}

	public void prepare() {
		ButtonWidget button;

		this.addWidgets(new TextWidget("Map Generation", gameContext.getWidth() / 2 - 210,
				gameContext.getHeight() / 10 * 2 - 100, 420, 200, 50, new Color(128, 128, 128)));
		button = new ButtonWidget("Previous", 20, gameContext.getHeight() - 100, 100, 20, 14,
				new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		button.setActionCommand("Previous");
		this.addWidgets(button);
		button = new ButtonWidget("Quit", 20, gameContext.getHeight() - 60, 100, 20, 14,
				new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		button.setActionCommand("Quit");
		this.addWidgets(button);
	}

}
