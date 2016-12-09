
package wargame.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import wargame.ErrorManager;
import wargame.unit.*;
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
		String spriteOrder = "";
		BufferedImage wholeImage;
		ArrayList<BufferedImage> badOrder;

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
		try {
			if (confProperties.getProperty(spriteName + "_image_order") != null)
				spriteOrder = confProperties.getProperty(spriteName + "_image_order");
		} catch (NumberFormatException e) {
		}
		wholeImage = ImageWidget.loadImage(confProperties.getProperty(spriteName + "_path"));
		if (wholeImage == null)
			errorManager.exitError(
					String.format("Couln not find file at %s_path in sprite index file\n", spriteName));
		badOrder = new ArrayList<BufferedImage>();
		while (y < h) {
			while (x < w) {
				badOrder.add(wholeImage.getSubimage(x, y, imageWidth, imageHeight));
				System.out.printf("Loaded %s %d;%d\n", spriteName, x, y);
				x += imageWidth;
			}
			x = initX;
			y += imageHeight;
		}
		if (!spriteOrder.equals(""))
			for (String goodIndex : spriteOrder.split(";"))
				spriteList.add(badOrder.get(Integer.parseInt(goodIndex)));
		else
			for (BufferedImage img : badOrder)
				spriteList.add(img) ;
	}

	/**
	 * Return the static position of the given character
	 * 
	 * @param unit
	 * @return
	 */
	public BufferedImage[] getUnitStaticPositionSprites(Unit unit) {
		ArrayList<BufferedImage> unitWalkImages = null;
		if (unit.getClass() == Wizard.class || unit.getClass() == Bird.class) {
			unitWalkImages = get("magos_static_poses");
		}
		if (unitWalkImages == null || unitWalkImages.size() < 3)
			return null;
		return new BufferedImage[] { unitWalkImages.get(0), unitWalkImages.get(1), unitWalkImages.get(2),
				unitWalkImages.get(3) };
	}

	/**
	 * Return the static position of the given character
	 * 
	 * @param unit
	 * @return
	 */
	public BufferedImage[] getEnemyStaticPositionSprites(Unit unit) {
		ArrayList<BufferedImage> unitWalkImages = null;
		if (unit.getClass() == Soldier.class ||unit.getClass() == Bowman.class || unit.getClass() == Bird.class) {
			unitWalkImages = get("beetle_static_poses");
		}
		if (unitWalkImages == null || unitWalkImages.size() < 3)
			return null;
		return new BufferedImage[] { unitWalkImages.get(0), unitWalkImages.get(1), unitWalkImages.get(2),
				unitWalkImages.get(3) };
	}

	/**
	 * Return the animation used when the given unit is moving.
	 * 
	 * @param unit
	 * @return public ArrayList<AnimationWidget> getUnitWalkSprites(Unit unit) { ArrayList
	 *         <BufferedImage> unitWalkImages = null; ArrayList<AnimationWidget> animations = new ArrayList
	 *         <AnimationWidget>(); AnimationWidget currentAnimation; Double[][] vector = new Double[][] { {
	 *         0., -6.4 }, { -6.4, 0. }, { 0., 6.4 }, { 6.4, 0. }, { -6.4, -6.4 }, { -6.4, -6.4 }, { 6.4, 6.4
	 *         }, { 6.4, 6.4 } };
	 * 
	 *         if (unit.getClass() == Wizard.class || unit.getClass() == Bird.class) { unitWalkImages =
	 *         get("magos_walking_poses"); } if (unitWalkImages == null || unitWalkImages.size() < 3) return
	 *         null; for (int animNo = 0; animNo < 4; animNo++) { currentAnimation = new AnimationWidget(50);
	 *         double[] dPosition = { 0., 0. }; for (int i = 0; i < 9; i++) {
	 *         currentAnimation.addImage(unitWalkImages.get(animNo * 9 + i), new Position((int) dPosition[0],
	 *         (int) dPosition[1])); dPosition[0] += vector[animNo][0]; dPosition[1] += vector[animNo][1]; }
	 *         animations.add(currentAnimation); } for (int animNo = 0; animNo < 4; animNo++) {
	 *         currentAnimation = new AnimationWidget(50); double[] dPosition = { 0., 0. }; for (int i = 0; i
	 *         < 9; i++) { currentAnimation.addImage(unitWalkImages.get(animNo * 9 + i), new Position((int)
	 *         dPosition[0], (int) dPosition[1])); dPosition[0] += vector[animNo + 4][0]; dPosition[1] +=
	 *         vector[animNo + 4][1]; } animations.add(currentAnimation); } return animations; }
	 */
}
