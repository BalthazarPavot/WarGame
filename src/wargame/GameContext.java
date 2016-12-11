
package wargame;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import wargame.map.AnimationHandler;
import wargame.map.Map;
import wargame.map.SpriteHandler;

/**
 * Defines the context in which the game is running, like the game state, its configuration, the loaded
 * sprites... All data that are beside the game itself.
 * 
 * @author Balthazar Pavot
 *
 */
public class GameContext {

	private static String defaultConfigPath = "config.conf";
	public static String TITLE = "War Game";
	static int MIN_WIDTH = 800;
	static int MAX_WIDTH = 1600;
	static int MIN_HEIGHT = 600;
	static int MAX_HEIGHT = 1200;

	private ErrorManager errorManager = null;
	private int width = MIN_WIDTH;
	private int height = MIN_HEIGHT;
	private boolean autoSave = true;
	private boolean sound = true;
	private boolean confLoaded = false;
	private SpriteHandler spriteHandler;
	private AnimationHandler animationHandler;
	private Map map;
	public boolean isLoaded = false;
	public File loadedFile;

	public GameContext(ErrorManager errorManager) {
		if (errorManager == null)
			ErrorManager.earlyTermination("Could not create the game context without the error manager.");
		this.errorManager = errorManager;
		this.loadConf();
		this.spriteHandler = new SpriteHandler(this.errorManager);
		this.setAnimationHandler(new AnimationHandler(this.errorManager));
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return this.map;
	}

	/**
	 * Load the default configuration file.
	 */
	public void loadConf() {
		File confFile = null;

		if (confLoaded == false) {
			confFile = new File(defaultConfigPath);
			try {
				loadConf(confFile);
			} catch (IOException | IllegalArgumentException e) {
				errorManager.exitError(ErrorManager.MISSING_CONFIG_FILE_ERROR_MESSAGE,
						ErrorManager.MISSING_CONFIG_FILE_ERROR);
			}
			confFile = null;
		}
	}

	/**
	 * Give the window width
	 * 
	 * @return The value of the current width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Give the window height
	 * 
	 * @return The value of the current height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Give the dimension of the window
	 * 
	 * @return The screen dimension
	 */
	public Dimension getDimension() {
		return new Dimension(getWidth(), getHeight());
	}

	/**
	 * Gives the horizontal centre of the window.
	 * 
	 * @return The middle of the width of the screen
	 */
	public int centerWidth() {
		return width / 2;
	}

	/**
	 * Gives the vertical centre of the window.
	 * 
	 * @return middle of the height of the screen
	 */
	public int centerHeight() {
		return height / 2;
	}

	/**
	 * Gives the error manager of the game.
	 * 
	 * @return The error manager of the game.
	 */
	public ErrorManager getErrorManager() {
		return errorManager;
	}

	/**
	 * Give the sprite handler of the game
	 * 
	 * @return The SpriteHandler instance, containing all the sprites.
	 */
	public SpriteHandler getSpriteHandler() {
		return this.spriteHandler;
	}

	/**
	 * Open the configuration file and load the configuration.
	 * 
	 * @param confFile
	 */
	private void loadConf(File confFile) throws IOException, IllegalArgumentException {
		Properties confProperties = null;

		confProperties = new Properties();
		confProperties.load(this.getClass().getResourceAsStream(defaultConfigPath));
		loadConf(confProperties);
		confProperties = null;
		confLoaded = true;
	}

	/**
	 * Read the properties of the configuration file and extract the configuration.
	 * 
	 * @param confProperties
	 */
	private void loadConf(Properties confProperties) throws IOException, IllegalArgumentException {
		width = Integer.parseInt(confProperties.getProperty("width"));
		height = Integer.parseInt(confProperties.getProperty("height"));
		setAutoSave(Boolean.parseBoolean(confProperties.getProperty("auto_save")));
		setSound(Boolean.parseBoolean(confProperties.getProperty("sound")));
		checkConf();
	}

	/**
	 * Check the current configuration, like the max size of the screen.
	 */
	private void checkConf() {
		if (width < GameContext.MIN_WIDTH)
			errorManager.exitError(String.format("The width defined in the conf file is less than %d.\n",
					GameContext.MIN_WIDTH));
		if (height < GameContext.MIN_HEIGHT)
			errorManager.exitError(String.format("The height defined in the conf file is less than %d.\n",
					GameContext.MIN_HEIGHT));
		if (width > GameContext.MAX_WIDTH)
			errorManager.exitError(String.format("The width defined in the conf file is more than %d.\n",
					GameContext.MAX_WIDTH));
		if (height > GameContext.MAX_HEIGHT)
			errorManager.exitError(String.format("The height defined in the conf file is more than %d.\n",
					GameContext.MAX_HEIGHT));
	}

	public void setConfiguration(String resolution, boolean autoSave, boolean sound) {
		int width;
		int height;

		width = Integer.parseInt(resolution.substring(0, resolution.indexOf('x')).trim());
		height = Integer.parseInt(resolution.substring(resolution.indexOf('x') + 1).trim());
		setConfiguration(width, height, autoSave, sound);
	}

	private void setConfiguration(int width, int height, boolean autoSave, boolean sound) {
		try {
			setConfiguration(width, height, autoSave, sound,
					new FileOutputStream(new File(this.getClass().getResource(defaultConfigPath).toURI())));
		} catch (FileNotFoundException e) {
			errorManager.exitError(ErrorManager.MISSING_CONFIG_FILE_ERROR_MESSAGE,
					ErrorManager.MISSING_CONFIG_FILE_ERROR);
		} catch (URISyntaxException e) {
			errorManager.exitError("Could not find config file", ErrorManager.BAD_CONFIG_FILE_ERROR);
		} catch (IOException e) {
			errorManager.exitError("Could not write config file", ErrorManager.BAD_CONFIG_FILE_ERROR);
		}
	}

	private void setConfiguration(Integer width, Integer height, Boolean autoSave, Boolean sound,
			FileOutputStream configStream) throws IOException {
		Properties properties = new Properties();
		properties.setProperty("width", width.toString());
		properties.setProperty("height", height.toString());
		properties.setProperty("auto_save", autoSave.toString());
		properties.setProperty("sound", sound.toString());
		try {
			properties.store(configStream, "Generated");
		} catch (IOException e) {
			errorManager.exitError("Could not write config file", ErrorManager.BAD_CONFIG_FILE_ERROR);
		}
		configStream.close();
	}

	public boolean getAutoSave() {
		return autoSave;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public boolean getSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	/**
	 * @return the animationHandler
	 */
	public AnimationHandler getAnimationHandler() {
		return animationHandler;
	}

	/**
	 * @param animationHandler
	 *            the animationHandler to set
	 */
	public void setAnimationHandler(AnimationHandler animationHandler) {
		this.animationHandler = animationHandler;
	}

}
