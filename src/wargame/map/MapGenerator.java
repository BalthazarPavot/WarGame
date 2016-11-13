
package wargame.map;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import wargame.widgets.ImageWidget;

/**
 * This class generates a map, using the map generator parameter.
 * 
 * @author Balthazar Pavot
 *
 */
public class MapGenerator {

	private MapGeneratorParameter parameters;
	private Map map;
	private Random rand;

	public MapGenerator() {
		this(null);
	}

	public MapGenerator(MapGeneratorParameter parameters) {
		this.parameters = parameters;
		if (this.parameters == null) {
			this.parameters = new MapGeneratorParameter();
			this.parameters.generateRandomParameters();
		}
		map = new Map();
		this.rand = new Random();
	}

	/**
	 * @return the newly generated map.
	 */
	public Map getMap() {
		return this.map;
	}

	/**
	 * Generate the map, using the parameters and store the result.
	 */
	public void generate(SpriteHandler spriteHandler) {
		ArrayList<TreeShape> treeShapes = new ArrayList<TreeShape>();
		ArrayList<RockShape> rockShapes = new ArrayList<RockShape>();
		ArrayList<WaterShape> waterShapes = new ArrayList<WaterShape>();

		this.map = new Map(this.parameters.getDimensions());
		generateShapes(treeShapes, rockShapes, waterShapes);
		distordShapes(treeShapes, rockShapes, waterShapes);
		generateGround(spriteHandler);
		applySprites(treeShapes, rockShapes, waterShapes, spriteHandler);
	}

	/**
	 * Generate each spot of tree, rock, water using the parameters.
	 * 
	 * @param treeShapes
	 * @param rockShapes
	 * @param waterShapes
	 */
	private void generateShapes(ArrayList<TreeShape> treeShapes, ArrayList<RockShape> rockShapes,
			ArrayList<WaterShape> waterShapes) {
		double treeRatio = 0;
		double waterRatio = 0;
		double rockRatio = 0;

		while (treeRatio < this.parameters.treeRatio) {
			treeShapes.add(new TreeShape().generate(this.parameters));
			treeRatio += treeShapes.get(treeShapes.size() - 1).getSquareNumber() / this.map.getSquareNumber();
		}
		while (rockRatio < this.parameters.rockRatio) {
			rockShapes.add(new RockShape().generate(this.parameters));
			rockRatio += rockShapes.get(rockShapes.size() - 1).getSquareNumber() / this.map.getSquareNumber();
		}
		while (waterRatio < this.parameters.waterRatio) {
			waterShapes.add(new WaterShape().generate(this.parameters));
			waterRatio += waterShapes.get(waterShapes.size() - 1).getSquareNumber()
					/ this.map.getSquareNumber();
		}
	}

	private void distordShapes(ArrayList<TreeShape> treeShapes, ArrayList<RockShape> rockShapes,
			ArrayList<WaterShape> waterShapes) {
		for (TreeShape treeShape : treeShapes) {
			treeShape.distord();
		}
		for (WaterShape waterShape : waterShapes) {
			waterShape.distord();
		}
	}

	private void generateGround(SpriteHandler spriteHandler) {
		ArrayList<BufferedImage> groundImages;
		Dimension mapDimensions;

		mapDimensions = parameters.getDimensions();
		switch (parameters.climate) {
		case MapGeneratorParameter.TOUNDRA_CLIMATE:
		case MapGeneratorParameter.SAND_DESERT_CLIMATE:
		case MapGeneratorParameter.ROCKS_DESERT_CLIMATE:
			groundImages = spriteHandler.get("snow_textures");
			break;
		case MapGeneratorParameter.DARK_FOREST_CLIMATE:
		case MapGeneratorParameter.SWAMP_CLIMATE:
		default:
			groundImages = spriteHandler.get("grass_textures");
			break;
		}
		for (int y = (int) mapDimensions.getHeight() / Map.squareHeight * Map.squareHeight
				- Map.squareHeight; y > -1; y -= Map.squareHeight) {
			for (int x = 0; x < (int) mapDimensions.getWidth() / Map.squareWidth * Map.squareWidth
					- Map.squareWidth; x += Map.squareWidth) {
				map.add(x, y,
						new MapElement(
								new ImageWidget(x, y, Map.squareWidth, Map.squareHeight,
										groundImages.get(rand.nextInt(groundImages.size()))),
								MapElement.WALKABLE | MapElement.FLYABLE | MapElement.REMOVABLE
										| MapElement.SHOT_THROUGH));
			}
		}
	}

	private void applySprites(ArrayList<TreeShape> treeShapes, ArrayList<RockShape> rockShapes,
			ArrayList<WaterShape> waterShapes, SpriteHandler spriteHandler) {
		ArrayList<BufferedImage> treeImageList;
		ArrayList<BufferedImage> rockImageList;
		ArrayList<BufferedImage> waterImageList;

		switch (parameters.climate) {
		case MapGeneratorParameter.TOUNDRA_CLIMATE:
		case MapGeneratorParameter.SAND_DESERT_CLIMATE:
		case MapGeneratorParameter.ROCKS_DESERT_CLIMATE:
			treeImageList = spriteHandler.get("tree_snow_set");
			break;
		case MapGeneratorParameter.DARK_FOREST_CLIMATE:
		case MapGeneratorParameter.SWAMP_CLIMATE:
		default:
			treeImageList = spriteHandler.get("tree_set");
			break;
		}
		rockImageList = spriteHandler.get("rock_snow_set") ;
		waterImageList = spriteHandler.get("water") ;
		for (TreeShape treeShape : treeShapes) {
			for (Spot spot : treeShape.getSpots()) {
				System.out.printf("tree at %d;%d\n", spot.getPosition().getX(), spot.getPosition().getY()) ;
				map.add(spot.getPosition().getX(), spot.getPosition().getY(),
						new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
								Map.squareWidth, Map.squareHeight,
								treeImageList.get(rand.nextInt(treeImageList.size())))));
			}
		}
		for (RockShape rockShape : rockShapes) {
			for (Spot spot : rockShape.getSpots()) {
				map.add(spot.getPosition().getX(), spot.getPosition().getY(),
						new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
								Map.squareWidth, Map.squareHeight,
								rockImageList.get(rand.nextInt(rockImageList.size())))));
			}
		}
		for (WaterShape waterShape : waterShapes) {
			for (Spot spot : waterShape.getSpots()) {
				System.out.printf("water at %d;%d\n", spot.getPosition().getX(), spot.getPosition().getY()) ;
				map.add(spot.getPosition().getX(), spot.getPosition().getY(),
						new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
								Map.squareWidth, Map.squareHeight,
								waterImageList.get(rand.nextInt(waterImageList.size())))));
			}
		}
	}
}
