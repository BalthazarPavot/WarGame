
package wargame.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import wargame.ErrorManager;
import wargame.widgets.ImageWidget;

/**
 * The aim of this class is to load the sprites and contain them. <br />
 * One can access a sprite by giving the sprite sheet name, and the number of the sprite.
 * 
 * @author Balthazar Pavot
 *
 */
public class SpriteHandler extends HashMap<String, ArrayList<BufferedImage>> {

	private static final long serialVersionUID = 1L;
	private final static String spriteIndex = "/spriteIndex.data";

	private boolean loaded = false;
	private ErrorManager errorManager;

	public SpriteHandler(ErrorManager errorManager) {
		this.errorManager = errorManager;
		loadSprites();
	}

	/**
	 * Load the sprites using the default path as the sprite index file path
	 */
	public void loadSprites() {
		if (loaded == false) {
			loaded = true;
			loadSprites(spriteIndex);
		}
	}

	/**
	 * Load the sprites using the given path as the sprite index file path
	 * 
	 * @param indexPath
	 */
	private void loadSprites(String indexPath) {
		File confFile = null;

		confFile = new File(indexPath);
		try {
			loadSprites(confFile);
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			errorManager.exitError("Could not load sprite index file. Verify its content, please.\n");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			e.printStackTrace();
			errorManager.exitError(
					"Could not open sprite index file. Verify it's there with good rights, please.\n");
		}
		confFile = null;
	}

	/**
	 * load the sprites using the given file as the sprite index file
	 * 
	 * @param confFile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void loadSprites(File confFile) throws IOException, IllegalArgumentException {
		Properties confProperties = null;

		confProperties = new Properties();
		confProperties.load(this.getClass().getResourceAsStream(spriteIndex));
		loadSprites(confProperties);
		loaded = true;
		confProperties = null;
	}

	/**
	 * Load the sprites using the given properties as the sprite index file properties.
	 * 
	 * @param confProperties
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void loadSprites(Properties confProperties) throws IOException, IllegalArgumentException {
		String allSpritesNames;
		String[] spriteNames;

		allSpritesNames = confProperties.getProperty("all_sprites_names");
		spriteNames = allSpritesNames.split(";");
		loadSprites(confProperties, spriteNames);
	}

	/**
	 * Place in the given array all the sprites extracted from each spriteNames
	 * 
	 * @param confProperties
	 * @param spriteNames
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void loadSprites(Properties confProperties, String[] spriteNames)
			throws IOException, IllegalArgumentException {

		for (String spriteName : spriteNames) {
			this.put(spriteName, new ArrayList<BufferedImage>());
			loadSprites(spriteName, this.get(spriteName), confProperties);
		}
	}

	/**
	 * Extract all the sprite from spriteName, using the path, x, y, width and heigh given in the sprite
	 * property file.
	 * 
	 * @param spriteName
	 * @param spriteList
	 * @param confProperties
	 */
	private void loadSprites(String spriteName, ArrayList<BufferedImage> spriteList,
			Properties confProperties) {
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		int imageWidth;
		int imageHeight;
		int initX = 0;
		BufferedImage wholeImage;

		try {
			initX = x = Integer.parseInt(confProperties.getProperty(spriteName + "_x"));
		} catch (NumberFormatException e) {
			errorManager.exitError(
					String.format("Couln not find property %s_x in sprite index file\n", spriteName));
		}
		try {
			y = Integer.parseInt(confProperties.getProperty(spriteName + "_y"));
		} catch (NumberFormatException e) {
			errorManager.exitError(
					String.format("Couln not find property %s_y in sprite index file\n", spriteName));
		}
		try {
			w = Integer.parseInt(confProperties.getProperty(spriteName + "_width"));
		} catch (NumberFormatException e) {
			errorManager.exitError(
					String.format("Couln not find property %s_width in sprite index file\n", spriteName));
		}
		try {
			h = Integer.parseInt(confProperties.getProperty(spriteName + "_height"));
		} catch (NumberFormatException e) {
			errorManager.exitError(
					String.format("Couln not find property %s_height in sprite index file\n", spriteName));
		}
		try {
			imageWidth = Integer.parseInt(confProperties.getProperty(spriteName + "_image_width"));
		} catch (NumberFormatException e) {
			imageWidth = Map.squareWidth;
		}
		try {
			imageHeight = Integer.parseInt(confProperties.getProperty(spriteName + "_image_height"));
		} catch (NumberFormatException e) {
			imageHeight = Map.squareHeight;
		}
		wholeImage = ImageWidget.loadImage(confProperties.getProperty(spriteName + "_path"));
		if (wholeImage == null)
			errorManager.exitError(
					String.format("Couln not find file at %s_path in sprite index file\n", spriteName));
		while (y < h) {
			while (x < w) {
				spriteList.add(wholeImage.getSubimage(x, y, imageWidth, imageHeight));
				System.out.printf("Loaded %s %d;%d\n", spriteName, x, y);
				x += imageWidth;
			}
			x = initX;
			y += imageHeight;
		}
	}
}
