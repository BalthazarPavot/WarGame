
package wargame.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.widgets.*;

/**
 * Screen configuration.
 * 
 * @author Balthazar Pavot
 *
 */
public class ConfigScreen extends GameScreen {

	private static final long serialVersionUID = 1L;
	public static String QUICK_GAME_STRING = "Quick Game";
	public static String NEW_GAME_STRING = "New Game";
	public static String LOAD_GAME_STRING = "Load Game";
	public static String CONFIGURATION_STRING = "Configuration";
	public static String QUIT_GAME_STRING = "Quit";

	private JRadioButton[] resolutionButtons;
	private JRadioButton[] autosaveButtons;
	private JRadioButton[] soundButtons;
	private String[] resolutions = { "800x600    ", "1360x740  ", "1600x1200" };

	public ConfigScreen(GameContext gameContext) {
		super(gameContext);
		this.actionManager = new ConfigScreenActionManager(this);
		resolutionButtons = new JRadioButton[3];
		autosaveButtons = new JRadioButton[2];
		soundButtons = new JRadioButton[2];
	}

	/**
	 * Prepare the main screen, the one display at the launch of the game.
	 */
	public void prepare() {
		ButtonWidget button;
		Form form;
		Form subForm;
		int i = 1;
		ButtonGroup group;

		this.addWidgets(new TextWidget("Configuration", gameContext.getWidth() / 2 - 200,
				gameContext.getHeight() / 10 * 2 - 100, 400, 200, 50, new Color(128, 128, 128)));
		button = new ButtonWidget("Previous", 20, gameContext.getHeight() - 100, 100, 20, 14,
				new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		this.addWidgets(button);
		button = new ButtonWidget("Save", gameContext.getWidth() - 120, gameContext.getHeight() - 100, 100,
				20, 14, new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		this.addWidgets(button);
		button = new ButtonWidget("Quit", 20, gameContext.getHeight() - 60, 100, 20, 14,
				new Color(128, 128, 128));
		button.addActionListener(this.actionManager);
		this.addWidgets(button);
		form = new Form(gameContext.getWidth() / 4, gameContext.getHeight() / 10 * 2 + 100,
				gameContext.getWidth() / 2, gameContext.getHeight() / 2);
		form.setLayout(new GridLayout(0, 1));
		subForm = new Form(gameContext.getWidth() / 2, gameContext.getHeight() / 10 * 2 + 100,
				gameContext.getWidth() / 2, gameContext.getHeight() / 4);
		subForm.setLayout(new GridLayout(2, resolutions.length));
		subForm.add(new JLabel("Resolution:"));
		for (i = 1; i < resolutions.length; i++)
			subForm.add(new JLabel(""));
		i = 0;
		for (String resolution : resolutions)
			subForm.add((resolutionButtons[i++] = new JRadioButton(resolution)));
		form.add(subForm);
		subForm = new Form(gameContext.getWidth() / 4, gameContext.getHeight() / 10 * 2 + 100,
				gameContext.getWidth() / 2, gameContext.getHeight() / 4);
		subForm.setLayout(new GridLayout(2, 0));
		subForm.add(new JLabel("Auto-save:"));
		subForm.add(new JLabel(""));
		subForm.add((autosaveButtons[0] = new JRadioButton("Yes")));
		subForm.add((autosaveButtons[1] = new JRadioButton("No")));
		form.add(subForm);
		subForm = new Form(gameContext.getWidth() / 4, gameContext.getHeight() / 10 * 2 + 100,
				gameContext.getWidth() / 2, gameContext.getHeight() / 4);
		subForm.setLayout(new GridLayout(2, 0));
		subForm.add(new JLabel("Sound:"));
		subForm.add(new JLabel(""));
		subForm.add((soundButtons[0] = new JRadioButton("Yes")));
		subForm.add((soundButtons[1] = new JRadioButton("No")));
		form.add(subForm);
		group = new ButtonGroup();
		this.addWidgets(form);
		for (i = 0; i < resolutionButtons.length; i++) {
			resolutionButtons[i].addItemListener(new ButtonsListener(resolutionButtons, i));
			group.add(resolutionButtons[i]);
		}
		group = new ButtonGroup();
		for (i = 0; i < autosaveButtons.length; i++) {
			autosaveButtons[i].addItemListener(new ButtonsListener(autosaveButtons, i));
			group.add(autosaveButtons[i]);
		}
		group = new ButtonGroup();
		for (i = 0; i < soundButtons.length; i++) {
			soundButtons[i].addItemListener(new ButtonsListener(soundButtons, i));
			group.add(soundButtons[i]);
		}
		resolutionButtons[gameContext.getWidth()==800?0:gameContext.getWidth()==1360?1:2].setSelected(true);
		autosaveButtons[0].setSelected(gameContext.getAutoSave());
		soundButtons[0].setSelected(gameContext.getSound());
		autosaveButtons[1].setSelected(!gameContext.getAutoSave());
		soundButtons[1].setSelected(!gameContext.getSound());
	}

	public void doSaveAction () {
		String resolution ;
		boolean autoSave ;
		boolean sound ;
		int i ;

		for (i=0;i<resolutionButtons.length;i++)
			if (resolutionButtons[i].isSelected())
				break ;
		resolution = resolutions[i] ;
		autoSave = autosaveButtons[0].isSelected() ;
		sound = soundButtons[0].isSelected() ;
		gameContext.setConfiguration (resolution, autoSave, sound) ;
	}
}

class ButtonsListener implements ItemListener {

	public JRadioButton[] allButtons;
	public int indexOfThis;

	public ButtonsListener(JRadioButton[] allButtons, int indexOfThis) {
		this.allButtons = allButtons;
		this.indexOfThis = indexOfThis;
	}

	public void itemStateChanged(ItemEvent e) {
	}

}

class Form extends JPanel implements GameWidget {
	private static final long serialVersionUID = -3867109949785876041L;
	protected Rectangle boundRect;

	public Form(int x, int y, int w, int h) {
		this.boundRect = new Rectangle(x, y, w, h);
	}

	/**
	 * Set the position of the bound rectangle.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		this.boundRect = new Rectangle(x, y, this.boundRect.width, this.boundRect.height);
	}

	/**
	 * Set the position of the bound rectangle.
	 * 
	 * @param position
	 */
	public void setPosition(Position position) {
		this.boundRect = new Rectangle(position.getX(), position.getY(), this.boundRect.width,
				this.boundRect.height);
	}

	/**
	 * Return widget's position
	 */
	public Position getPosition() {
		return new Position(this.boundRect.x, this.boundRect.y);
	}

	/**
	 * Set the dimensions of the bound rectangle.
	 * 
	 * @param w
	 * @param h
	 */
	public void setDimension(int w, int h) {
		this.boundRect = new Rectangle(this.boundRect.x, this.boundRect.y, w, h);
	}

	/**
	 * Return the dimensions of the rectangle
	 */
	public Dimension getDimension() {
		return new Dimension(this.boundRect.width, this.boundRect.height);
	}

	/**
	 * Set the bound rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setBinding(int x, int y, int w, int h) {
		this.boundRect = new Rectangle(x, y, w, h);
	}

	/**
	 * Set the bound rectangle.
	 * 
	 * @param Copy
	 */
	public void setBinding(Rectangle binds) {
		this.boundRect = new Rectangle(binds);
	}

	/**
	 * Place the widget at its place.
	 */
	public void bind() {
		this.setBounds(this.boundRect);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paintComponent(Graphics g, int zoom, int x, int y) {
		super.paintComponent(g);
	}
}
