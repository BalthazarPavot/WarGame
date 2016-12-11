
package wargame.map;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import wargame.ErrorManager;
import wargame.basic_types.Position;
import wargame.basic_types.SerializableBufferedImage;
import wargame.unit.*;
import wargame.widgets.AnimationWidget;
import wargame.widgets.ImageWidget;

/**
 * The aim of this class is to load the animations and contain them. <br />
 * One can access a animation by giving the animation sheet name, and the number (id) of the animation.
 * 
 * @author Balthazar Pavot
 *
 */
public class AnimationHandler extends HashMap<String, ArrayList<AnimationWidget>> implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static String animationIndex = "/animationIndex.data";

	private boolean loaded = false;
	private ErrorManager errorManager;

	public AnimationHandler(ErrorManager errorManager) {
		this.errorManager = errorManager;
		loadAnimations();
	}

	/**
	 * Load the animations using the default path as the animation index file path
	 */
	public void loadAnimations() {
		if (loaded == false) {
			loaded = true;
			loadAnimations(animationIndex);
		}
	}

	/**
	 * Load the animations using the given path as the animation index file path
	 * 
	 * @param indexPath
	 */
	private void loadAnimations(String indexPath) {
		File confFile = null;

		confFile = new File(indexPath);
		try {
			loadAnimations(confFile);
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			errorManager.exitError("Could not load animation index file. Verify its content, please.\n");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			e.printStackTrace();
			errorManager.exitError(
					"Could not open animation index file. Verify it's there with good rights, please.\n");
		}
		confFile = null;
	}

	/**
	 * load the animations using the given file as the animation index file
	 * 
	 * @param confFile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void loadAnimations(File confFile) throws IOException, IllegalArgumentException {
		Properties confProperties = null;

		confProperties = new Properties();
		confProperties.load(this.getClass().getResourceAsStream(animationIndex));
		loadAnimations(confProperties);
		loaded = true;
		confProperties = null;
	}

	/**
	 * Load the animations using the given properties as the animation index file properties.
	 * 
	 * @param confProperties
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void loadAnimations(Properties confProperties) throws IOException, IllegalArgumentException {
		String allAnimationsNames;

		allAnimationsNames = confProperties.getProperty("all_animations_names");
		loadAnimations(confProperties, allAnimationsNames.split(";"));
	}

	/**
	 * Place in the given array all the animations extracted from each animationNames
	 * 
	 * @param confProperties
	 * @param animationNames
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void loadAnimations(Properties confProperties, String[] animationNames)
			throws IOException, IllegalArgumentException {

		for (String animationName : animationNames) {
			this.put(animationName, new ArrayList<AnimationWidget>());
			loadAnimations(animationName, this.get(animationName), confProperties);
		}
	}

	/**
	 * Extract all the animation from animationName, using the path, x, y, width and heigh given in the
	 * animation property file.
	 * 
	 * @param animationName
	 * @param animationList
	 * @param confProperties
	 */
	private void loadAnimations(String animationName, ArrayList<AnimationWidget> animationList,
			Properties confProperties) {
		int x = 0;
		int y = 0;
		int end_x = 0;
		int end_y = 0;
		int w = 0;
		int h = 0;
		int initX = 0;
		int initY = 0;
		int resize = 1;
		int frameDuration = 0;
		float frameNumber;
		boolean sameDiagonals;
		AnimationWidget currentAnimation;
		SerializableBufferedImage wholeImage;
		double[] framePosition = { 0., 0. };
		int vectorNumber;
		Double[][] vector = new Double[][] { { 0., -1.0 }, { 0., 1.0 }, { 1.0, 0. }, { -1.0, 0. },
				{ 1.0, -1.0 }, { 1.0, 1.0 }, { -1.0, 1.0 }, { -1.0, -1.0 } }; // good

		try {
			initX = x = Integer.parseInt(confProperties.getProperty(animationName + "_x"));
		} catch (NumberFormatException e) {
			errorManager.exitError(
					String.format("Couln not find property %s_x in animation index file\n", animationName));
		}
		try {
			initY = y = Integer.parseInt(confProperties.getProperty(animationName + "_y"));
		} catch (NumberFormatException e) {
			errorManager.exitError(
					String.format("Couln not find property %s_y in animation index file\n", animationName));
		}
		try {
			end_x = Integer.parseInt(confProperties.getProperty(animationName + "_end_x"));
		} catch (NumberFormatException e) {
			errorManager.exitError(String.format("Couln not find property %s_width in animation index file\n",
					animationName));
		}
		try {
			end_y = Integer.parseInt(confProperties.getProperty(animationName + "_end_y"));
		} catch (NumberFormatException e) {
			errorManager.exitError(String
					.format("Couln not find property %s_height in animation index file\n", animationName));
		}
		try {
			resize = Integer.parseInt(confProperties.getProperty(animationName + "_resize"));
		} catch (NumberFormatException e) {
			resize = 1;
		}
		try {
			w = Integer.parseInt(confProperties.getProperty(animationName + "_width"));
		} catch (NumberFormatException e) {
			w = Map.squareWidth;
		}
		try {
			h = Integer.parseInt(confProperties.getProperty(animationName + "_height"));
		} catch (NumberFormatException e) {
			h = Map.squareHeight;
		}
		try {
			sameDiagonals = Boolean
					.parseBoolean(confProperties.getProperty(animationName + "_same_diagonal"));
		} catch (NumberFormatException e) {
			sameDiagonals = false;
		}
		try {
			frameDuration = Integer.parseInt(confProperties.getProperty(animationName + "_duration"));
		} catch (NumberFormatException e) {
			errorManager.exitError(String
					.format("Couln not find property %s_duration in animation index file\n", animationName));
		}
		wholeImage = ImageWidget.loadImage(confProperties.getProperty(animationName + "_path"));
		if (wholeImage == null)
			errorManager.exitError(
					String.format("Couln not find file at %s_path in animation index file\n", animationName));
		vectorNumber = 0;
		frameNumber = (float) (end_x - x) / w + 1;
		while (y <= end_y) {
			currentAnimation = new AnimationWidget(frameDuration);
			framePosition[0] = framePosition[1] = 0.;
			animationList.add(currentAnimation);
			while (x <= end_x) {
				currentAnimation.addImage(
						ImageWidget.zoomImage(wholeImage.getSubimage(x, y, w, h), resize, false)
								.getSubimage(w - w / resize, h - h / resize, w, h),
						new Position((int) framePosition[0], (int) framePosition[1]));
				System.out.printf("Load animation %s %d;%d\n", animationName, x, y);
				framePosition[0] += vector[vectorNumber][0] * Map.squareWidth / frameNumber;
				framePosition[1] += vector[vectorNumber][1] * Map.squareHeight / frameNumber;
				x += w;
			}
			vectorNumber += 1;
			x = initX;
			y += h;
		}
		if (sameDiagonals) {
			y = initY;
			x = initX;
			for (Integer y2 : new int[] { 0, h, h, 0 }) {
				currentAnimation = new AnimationWidget(frameDuration);
				framePosition[0] = framePosition[1] = 0.;
				animationList.add(currentAnimation);
				while (x <= end_x) {
					currentAnimation.addImage(
							ImageWidget.zoomImage(wholeImage.getSubimage(x, y2, w, h), resize, false)
									.getSubimage(w - w / resize, h - h / resize, w, h),
							new Position((int) framePosition[0], (int) framePosition[1]));
					System.out.printf("Load animation %s %d;%d\n", animationName, x, y2);
					framePosition[0] += vector[vectorNumber][0] * Map.squareWidth / frameNumber;
					framePosition[1] += vector[vectorNumber][1] * Map.squareHeight / frameNumber;
					x += w;
				}
				vectorNumber += 1;
				x = initX;
				y2 += h;
			}
		}
	}

	/**
	 * Return the animation used when the given unit is moving.
	 * 
	 * @param unit
	 * @return
	 */
	public ArrayList<AnimationWidget> getUnitWalkSprites(Unit unit) {
		if (unit.getClass() == Bird.class) {
			return get("red_wyvern");
		} else if (unit.getClass() == Healer.class) {
			return get("white_magican");
		}
		return get("magos_walk");
	}

	/**
	 * Return the animation used when the given unit is moving.
	 * 
	 * @param unit
	 * @return
	 */
	public ArrayList<AnimationWidget> getEnemyWalkSprites(Unit unit) {
		if (unit.getClass() == Bird.class) {
			return get("green_wyvern");
		}
		return get("beetle_walk");
	}
}
