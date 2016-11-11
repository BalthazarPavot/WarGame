
package wargame.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import wargame.ErrorManager;
import wargame.widgets.ImageWidget;

public class SpriteHandler extends HashMap<String, ArrayList<BufferedImage>> {

	private static final long serialVersionUID = 1L;
	// http://gamedev.stackexchange.com/questions/53705/how-can-i-make-a-sprite-sheet-based-animation-system
	private final static String spriteIndex = "resources/spriteIndex.data";

	private boolean loaded = false;
	private ErrorManager errorManager;

	public SpriteHandler(ErrorManager errorManager) {
		this.errorManager = errorManager;
		loadSprites();
	}

	public void loadSprites() {
		if (loaded == false) {
			loaded = true;
			loadSprites(spriteIndex);
		}
	}

	private void loadSprites(String indexPath) {
		File confFile = null;

		confFile = new File(indexPath);
		try {
			loadSprites(confFile);
		} catch (IOException e) {
			errorManager.exitError("Could not load sprite index file. Verify its content, please.\n");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			e.printStackTrace();
			errorManager.exitError(
					"Could not open sprite index file. Verify it's there with good rights, please.\n");
		}
		confFile = null;
	}

	private void loadSprites(File confFile) throws IOException, IllegalArgumentException {
		FileInputStream confStream = null;
		Properties confProperties = null;

		if (!confFile.exists())
			errorManager.exitError(
					String.format("The sprite index file \"%s\" is missing.\n", confFile.getPath()));
		if (confFile.isDirectory())
			errorManager.exitError(
					String.format("The conf sprite index \"%s\" is a directory.\n", confFile.getPath()));
		confStream = new FileInputStream(confFile.getPath());
		confProperties = new Properties();
		confProperties.load(confStream);
		loadSprites(confProperties);
		loaded = true;
		confStream.close();
		confStream.close();
		confStream = null;
		confProperties = null;
	}

	private void loadSprites(Properties confProperties) throws IOException, IllegalArgumentException {
		String allSpritesNames;
		String[] spriteNames;

		allSpritesNames = confProperties.getProperty("all_sprites_names");
		spriteNames = allSpritesNames.split(";");
		loadSprites(confProperties, spriteNames);
	}

	private void loadSprites(Properties confProperties, String[] spriteNames)
			throws IOException, IllegalArgumentException {

		for (String spriteName : spriteNames) {
			this.put(spriteName, new ArrayList<BufferedImage>());
			loadSprites(spriteName, this.get(spriteName), confProperties);
		}
	}

	private void loadSprites(String spriteName, ArrayList<BufferedImage> spriteList,
			Properties confProperties) {
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		BufferedImage wholeImage;

		try {
			x = Integer.parseInt(confProperties.getProperty(spriteName + "_x"));
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
		wholeImage = ImageWidget.loadImage(confProperties.getProperty(spriteName + "_path"));
		if (wholeImage == null)
			errorManager.exitError(
					String.format("Couln not find file at %s_path in sprite index file\n", spriteName));
		while (y < h) {
			while (x < w) {
				spriteList.add(wholeImage.getSubimage(x, y, Map.squareWidth, Map.squareHeight));
				System.out.printf("Loaded %s %d;%d\n", spriteName, x, y);
				x += Map.squareWidth;
			}
			x = 0;
			y += Map.squareHeight;
		}
	}
}
