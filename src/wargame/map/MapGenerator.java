
package wargame.map;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import wargame.basic_types.SerializableBufferedImage;
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
	private SpriteHandler spriteHandler;

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
		ArrayList<TreeShape> treeShapes;
		ArrayList<RockShape> rockShapes;
		ArrayList<WaterShape> waterShapes;

		do {
			treeShapes = new ArrayList<TreeShape>();
			rockShapes = new ArrayList<RockShape>();
			waterShapes = new ArrayList<WaterShape>();
			this.map = new Map(this.parameters.getDimensions());
			this.spriteHandler = spriteHandler;
			generateShapes(treeShapes, rockShapes, waterShapes);
			distordShapes(treeShapes, rockShapes, waterShapes);
			generateGround();
			applySprites(treeShapes, rockShapes, waterShapes);
			removePopAreas(4);
		} while (!this.map.canCrossByWalking(map.getAlliePopArea(), map.getEnnemyPopArea()));
//		for (Position pos:map.pathByWalking (map.getAlliePopArea(), map.getEnnemyPopArea()))
//			map.getReal(pos).remove(map.getReal(pos).size()-1) ;
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
			waterShapes.add(new WaterShape(waterShapes).generate(this.parameters));
			waterRatio += waterShapes.get(waterShapes.size() - 1).getSquareNumber()
					/ this.map.getSquareNumber();
		}
	}

	private void distordShapes(ArrayList<TreeShape> treeShapes, ArrayList<RockShape> rockShapes,
			ArrayList<WaterShape> waterShapes) {
		for (TreeShape treeShape : treeShapes) {
			treeShape.distord();
		}
		// for (WaterShape waterShape : waterShapes) {
		// waterShape.distord();
		// }
		for (RockShape rockShape : rockShapes) {
			rockShape.distord();
		}
	}

	private void generateGround() {
		ArrayList<SerializableBufferedImage> groundImages;
		Dimension mapDimensions;

		mapDimensions = parameters.getDimensions();
		switch (parameters.climate) {
		case MapGeneratorParameter.TOUNDRA_CLIMATE:
		case MapGeneratorParameter.SAND_DESERT_CLIMATE:
			groundImages = spriteHandler.get("snow_textures");
			break;
		case MapGeneratorParameter.ROCKS_DESERT_CLIMATE:
			groundImages = spriteHandler.get("rock_textures");
			break;
		case MapGeneratorParameter.DARK_FOREST_CLIMATE:
		case MapGeneratorParameter.SWAMP_CLIMATE:
		default:
			groundImages = spriteHandler.get("grass_textures");
			break;
		}
		for (int y = (int) mapDimensions.getHeight() / Map.squareHeight * Map.squareHeight
				- Map.squareHeight; y > -1; y -= Map.squareHeight) {
			for (int x = 0; x < (int) mapDimensions.getWidth() / Map.squareWidth
					* Map.squareWidth; x += Map.squareWidth) {
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
			ArrayList<WaterShape> waterShapes) {
		ArrayList<SerializableBufferedImage> treeImageList;
		ArrayList<SerializableBufferedImage> rockImageList;

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
		rockImageList = spriteHandler.get("rock_snow_set");
		for (TreeShape treeShape : treeShapes) {
			for (Spot spot : treeShape.getSpots()) {
				if (map.getReal(spot.getPosition().getX(), spot.getPosition().getY()).size() > 1)
					continue;
				try {
					map.add(spot.getPosition().getX(), spot.getPosition().getY(),
							new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									treeImageList.get(rand.nextInt(treeImageList.size()))),
									MapElement.FLYABLE | MapElement.REMOVABLE));
				} catch (IndexOutOfBoundsException e) {
					/**
					 * the resource in creation is out of the map ignore it.
					 */
				}
			}
		}
		for (RockShape rockShape : rockShapes) {
			for (Spot spot : rockShape.getSpots()) {
				if (map.getReal(spot.getPosition().getX(), spot.getPosition().getY()).size() > 1)
					continue;
				try {
					map.add(spot.getPosition().getX(), spot.getPosition().getY(),
							new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									rockImageList.get(rand.nextInt(rockImageList.size()))),
									MapElement.FLYABLE | MapElement.REMOVABLE));
				} catch (IndexOutOfBoundsException e) {
					/**
					 * the resource in creation is out of the map ignore it.
					 */
				}
			}
		}
		applyWaterSprites(waterShapes);
	}

	/**
	 * Apply water on the water spots. First apply the same water sprite, then apply the good sprites in
	 * function of the curves that the water makes.
	 * 
	 * @param waterShapes
	 * @param spriteHandler
	 */
	private void applyWaterSprites(ArrayList<WaterShape> waterShapes) {
		for (WaterShape waterShape : waterShapes) {
			for (Spot spot : waterShape.getSpots()) {
				if (map.getReal(spot.getPosition().getX(), spot.getPosition().getY()).size() > 1) {
					// continue;
					map.getReal(spot.getPosition().getX(), spot.getPosition().getY()).remove(
							map.getReal(spot.getPosition().getX(), spot.getPosition().getY()).size() - 1);
				}
				try {
					map.add(spot.getPosition().getX(), spot.getPosition().getY(),
							new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_full").get(0)),
									MapElement.FLYABLE | MapElement.SHOT_THROUGH | MapElement.SWIMMABLE));
				} catch (IndexOutOfBoundsException e) {
					/**
					 * the resource in creation is out of the map ignore it.
					 */
				}
			}
		}
		for (WaterShape waterShape : waterShapes) {
			ArrayList<MapElement> elements;
			int numberOfConsecutiveWaterSprites;

			for (Spot spot : waterShape.getSpots()) {
				elements = map.getReal(spot.getPosition().getX(), spot.getPosition().getY());
				numberOfConsecutiveWaterSprites = countConsecutiveSpriteNumberAround(
						spot.getPosition().getX(), spot.getPosition().getY(), "water_deep_full");
				switch (numberOfConsecutiveWaterSprites) {
				/* case 0: case 1: case 2: if (elements.size() > 1) elements.remove(elements.size() - 1);
				 * break; */
				case 3:
				case 4:
					updateWaterWith3Neighbor(spot, elements, "water_sand_curve_out");
					break;
				case 5:
				case 6:
					updateWaterWith5Neighbor(spot, elements, "water_sand_straight");
					break;
				case 7:
					updateWaterWith7Neighbor(spot, elements, "water_sand_curve_in");
					break;
				/* case 8: case 16: // all squares around are counted 2x! */
				default:
					break;
				}
			}
		}
		for (WaterShape waterShape : waterShapes) {
			ArrayList<MapElement> elements;
			int numberOfStraightSand;
			int numberOfCurveOut;
			int numberOfDeepWater;

			for (Spot spot : waterShape.getSpots()) {
				elements = map.getReal(spot.getPosition().getX(), spot.getPosition().getY());
				numberOfStraightSand = countConsecutiveSpriteNumberAround(spot.getPosition().getX(),
						spot.getPosition().getY(), "water_sand_straight");
				numberOfCurveOut = countConsecutiveSpriteNumberAround(spot.getPosition().getX(),
						spot.getPosition().getY(), "water_sand_curve_out");
				numberOfDeepWater = countConsecutiveSpriteNumberAround(spot.getPosition().getX(),
						spot.getPosition().getY(), "water_deep_full");
				if (numberOfDeepWater == 8 || numberOfDeepWater == 16) {
					if (numberOfCurveOut == 1) {
						if (positionContains(spot.getPosition().getX() - 64, spot.getPosition().getY() - 64,
								"water_sand_curve_out"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_curve_out").get(0)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
						else if (positionContains(spot.getPosition().getX() + 64,
								spot.getPosition().getY() - 64, "water_sand_curve_out"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_curve_out").get(1)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
						else if (positionContains(spot.getPosition().getX() - 64,
								spot.getPosition().getY() + 64, "water_sand_curve_out"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_curve_out").get(2)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
						else if (positionContains(spot.getPosition().getX() + 64,
								spot.getPosition().getY() + 64, "water_sand_curve_out"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_curve_out").get(3)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
					} else if (numberOfStraightSand == 3) {
						if (positionContains(spot.getPosition().getX() - 64, spot.getPosition().getY(),
								"water_sand_straight"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_straight").get(3)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
						else if (positionContains(spot.getPosition().getX() + 64, spot.getPosition().getY(),
								"water_sand_straight"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_straight").get(2)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
						else if (positionContains(spot.getPosition().getX(), spot.getPosition().getY() - 64,
								"water_sand_straight"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_straight").get(1)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
						else if (positionContains(spot.getPosition().getX(), spot.getPosition().getY() + 64,
								"water_sand_straight"))
							elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(),
									spot.getPosition().getY(), Map.squareWidth, Map.squareHeight,
									spriteHandler.get("water_deep_straight").get(0)),
									MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE)); // good!!
					}
				} else if (numberOfDeepWater != 0 && elements.size() > 1) {
					// elements.remove(elements.size() - 2);
				}

			}
		}
		for (WaterShape waterShape : waterShapes) {
			int numberOfDeepWater;

			ArrayList<MapElement> elements;
			for (Spot spot : waterShape.getSpots()) {
				elements = map.getReal(spot.getPosition().getX(), spot.getPosition().getY());
				numberOfDeepWater = countConsecutiveSpriteNumberAround(spot.getPosition().getX(),
						spot.getPosition().getY(), "water_deep_full");
				if (numberOfDeepWater != 8 && numberOfDeepWater != 16 && elements.size() > 2)
					elements.remove(elements.size() - 2);
			}
		}
	}

	private void updateWaterWith3Neighbor(Spot spot, ArrayList<MapElement> elements, String spriteName) {
		int x;
		int y;

		x = spot.getPosition().getX();
		y = spot.getPosition().getY();
		// if (elements.size() != 0)
		// elements.remove(elements.size() - 1);
		if (positionContains(x - Map.squareWidth, y - Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(3)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else if (positionContains(x + Map.squareWidth, y - Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(2)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else if (positionContains(x - Map.squareWidth, y + Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(1)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(0)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		}
	}

	private void updateWaterWith5Neighbor(Spot spot, ArrayList<MapElement> elements, String spriteName) {
		int x;
		int y;

		x = spot.getPosition().getX();
		y = spot.getPosition().getY();
		if (!positionContains(x - Map.squareWidth, y, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(3)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else if (!positionContains(x + Map.squareWidth, y, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(2)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else if (!positionContains(x, y + Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(0)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(1)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		}
	}

	private void updateWaterWith7Neighbor(Spot spot, ArrayList<MapElement> elements, String spriteName) {
		int x;
		int y;

		x = spot.getPosition().getX();
		y = spot.getPosition().getY();
		if (!positionContains(x - Map.squareWidth, y - Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(3)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else if (!positionContains(x + Map.squareWidth, y - Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(2)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else if (!positionContains(x - Map.squareWidth, y + Map.squareHeight, "water_deep_full")) {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(1)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		} else {
			elements.add(new MapElement(new ImageWidget(spot.getPosition().getX(), spot.getPosition().getY(),
					Map.squareWidth, Map.squareHeight, spriteHandler.get(spriteName).get(0)),
					MapElement.FLYABLE|MapElement.SHOT_THROUGH|MapElement.SWIMMABLE));
		}
	}

	private int countConsecutiveSpriteNumberAround(int x, int y, String spriteName) {
		int counter;
		int max;
		int listPosition[][] = { { -Map.squareWidth, -Map.squareHeight }, { -Map.squareWidth, 0 },
				{ -Map.squareWidth, Map.squareHeight }, { 0, Map.squareHeight },
				{ Map.squareWidth, Map.squareHeight }, { Map.squareWidth, 0 },
				{ Map.squareWidth, -Map.squareHeight }, { 0, -Map.squareHeight } };

		counter = max = 0;
		for (int __ = 0; __ < 2; __++) { // 2.times () {}
			for (int position[] : listPosition) {
				if (positionContains(x + position[0], y + position[1], spriteName)) {
					if (++counter > max)
						max = counter;
				} else
					counter = 0;
			}
		}
		return max;
	}

	private boolean positionContains(int x, int y, String spriteCathegory) {
		return positionContains(x, y, spriteCathegory, false);
	}

	private boolean positionContains(int x, int y, String spriteCathegory, boolean acceptNull) {
		ArrayList<MapElement> allME;

		allME = map.getReal(x, y);
		if (acceptNull && allME.size() == 0)
			return true;
		for (MapElement me : allME) {
			for (BufferedImage image : spriteHandler.get(spriteCathegory)) {
				if (me.getImage().getImage() == image)
					return true;
			}
		}
		return false;
	}

	private void removePopAreas(int rayOfErase) {
		ArrayList<MapElement> elements;

		for (int x =  map.getAlliePopArea().getX() - rayOfErase * Map.squareWidth; x < map.getAlliePopArea().getX()
				+ rayOfErase * Map.squareWidth; x += Map.squareWidth) {
			for (int y = map.getAlliePopArea().getY() - rayOfErase * Map.squareHeight; y < map.getAlliePopArea().getY()
					+ rayOfErase * Map.squareHeight; y += Map.squareHeight) {
				elements = this.map.getReal(x, y);
				while (elements.size() > 1)
					elements.remove(elements.get(elements.size() - 1));
			}
		}
		for (int x =  map.getEnnemyPopArea().getX() - rayOfErase * Map.squareWidth; x <  map.getEnnemyPopArea().getX()
				+ rayOfErase * Map.squareWidth; x += Map.squareWidth) {
			for (int y =  map.getEnnemyPopArea().getY() - rayOfErase * Map.squareHeight; y <  map.getEnnemyPopArea().getY()
					+ rayOfErase * Map.squareHeight; y += Map.squareHeight) {
				elements = this.map.getReal(x, y);
				while (elements.size() > 1)
					elements.remove(elements.get(elements.size() - 1));
			}
		}

	}
}
