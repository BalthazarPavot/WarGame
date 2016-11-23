
package wargame.map;

import java.awt.Dimension;
import java.util.Random;


/**
 * This class defined the parameters used by the map generator to generate a map.
 * @author Balthazar Pavot
 *
 */
public class MapGeneratorParameter {

	public final static double MIN_TREE_RATIO = 0.01;
	public final static double MAX_TREE_RATIO = 0.4;

	public final static double MIN_WATER_RATIO = 0.0;
	public final static double MAX_WATER_RATIO = 0.3;

	public final static double MIN_ROCKS_RATIO = 0.0;
	public final static double MAX_ROCKS_RATIO = 0.3;

	public final static double MIN_TOTAL_RATIO = 0.2;
	public final static double MAX_TOTAL_RATIO = 0.7;

	public final static int NO_CLIMATE = 0;
	public final static int TOUNDRA_CLIMATE = 1;
	public final static int SAND_DESERT_CLIMATE = 2;
	public final static int ROCKS_DESERT_CLIMATE = 3;
	public final static int DARK_FOREST_CLIMATE = 4;
	public final static int SWAMP_CLIMATE = 5;
	public final static int CLIMATE_NUMBER = 6;

	public double treeRatio;
	public double waterRatio;
	public double rockRatio;

	public int climate;

	public boolean isolatedTrees = false;
	public boolean isolatedRockSets = false;
	public boolean isolatedWaterSpots = false;
	private Random rand;
	private Dimension dimensions = null;

	public MapGeneratorParameter() {
		rand = new Random();
	}

	/**
	 * Generate some random climate and generate the parameters to match this climate.
	 */
	public void generateRandomParameters() {
		this.setDimensions(new Dimension(Map.defaultWidth, Map.defaultHeight));
		this.generateRandomClimate();
		this.generateThisClimateParams();
	}

	/**
	 * @return The dimension defined by the parameters.
	 */
	public Dimension getDimensions() {
		return this.dimensions;
	}

	/**
	 * Set the parameter of dimension.
	 * @param dimension
	 */
	public void setDimensions(Dimension dimension) {
		this.dimensions = dimension;
	}

	/**
	 * @return The surface in pixel².
	 */
	public double getSurface() {
		return this.dimensions.getWidth() * this.dimensions.getHeight();
	}

	/**
	 * @return The number of squares defined by the parameters.
	 */
	public int getSquareNumber() {
		return (int) (this.dimensions.getWidth() / Map.squareWidth * this.dimensions.getHeight()
				/ Map.squareHeight);
	}

	/**
	 * Choose a random climate.
	 */
	private void generateRandomClimate() {
		climate = rand.nextInt() % CLIMATE_NUMBER;
	}

	/**
	 * Call the method to generate parameters depending on the climate.
	 */
	private void generateThisClimateParams() {
		switch (climate) {
		case TOUNDRA_CLIMATE:
			generateToundraParameters();
			break;
		case SAND_DESERT_CLIMATE:
			generateSandDesertParameters();
			break;
		case ROCKS_DESERT_CLIMATE:
			generateRocksDesertParameters();
			break;
		case DARK_FOREST_CLIMATE:
			generateDarkForestParameters();
			break;
		case SWAMP_CLIMATE:
			generateSwampParameters();
			break;
		case NO_CLIMATE:
		default:
			generateNoClimateParameters();
			break;
		}
	}

	/**
	 * Generate random parameters to match a toundra climate
	 */
	private void generateToundraParameters() {
		waterRatio = rangeDouble(0, 0.1);
		isolatedWaterSpots = true;
		treeRatio = rangeDouble(0.05, 0.15);
		isolatedTrees = true;
		rockRatio = rangeDouble(0.01, 0.1);
		isolatedRockSets = true;
	}

	/**
	 * Generate random parameters to match a sand desert climate
	 */
	private void generateSandDesertParameters() {
		waterRatio = 0;
		isolatedWaterSpots = true;
		treeRatio = rangeDouble(0.02, 0.05);
		isolatedTrees = true;
		rockRatio = rangeDouble(0.05, 0.15);
		isolatedRockSets = false;
	}

	/**
	 * Generate random parameters to match a rock desert climate
	 */
	private void generateRocksDesertParameters() {
		waterRatio = rangeDouble(0.0, 0.01);
		isolatedWaterSpots = true;
		treeRatio = rangeDouble(0.02, 0.15);
		isolatedTrees = true;
		rockRatio = rangeDouble(0.2, 0.35);
		isolatedRockSets = false;
	}

	/**
	 * Generate random parameters to match a forest climate
	 */
	private void generateDarkForestParameters() {
		waterRatio = rangeDouble(0.0, 0.02);
		isolatedWaterSpots = true;
		treeRatio = rangeDouble(0.2, 0.4);
		isolatedTrees = false;
		rockRatio = rangeDouble(0.02, 0.035);
		isolatedRockSets = true;
	}

	/**
	 * Generate random parameters to match a swamp climate
	 */
	private void generateSwampParameters() {
		waterRatio = rangeDouble(0.1, 0.2);
		isolatedWaterSpots = rand.nextInt() % 5 != 0;
		treeRatio = rangeDouble(0.075, 0.1);
		isolatedTrees = true;
		rockRatio = rangeDouble(0.02, 0.035);
		isolatedRockSets = true;
	}

	/**
	 * Generate random parameters.
	 */
	private void generateNoClimateParameters() {
		do {
			treeRatio = rangeDouble(MIN_TREE_RATIO, MAX_TREE_RATIO);
			waterRatio = rangeDouble(MIN_WATER_RATIO, MAX_WATER_RATIO);
			rockRatio = rangeDouble(MIN_ROCKS_RATIO, MAX_ROCKS_RATIO);
			if (rand.nextInt() % 10 > 7) {
				isolatedTrees = true;
				treeRatio /= 3;
			}
			if (rand.nextInt() % 10 > 7) {
				isolatedRockSets = true;
				rockRatio /= 3;
			}
			if (rand.nextInt() % 10 > 7) {
				isolatedWaterSpots = true;
				waterRatio /= 3;
			}
		} while (treeRatio + waterRatio + rockRatio > MAX_TOTAL_RATIO
				|| treeRatio + waterRatio + rockRatio < MIN_TOTAL_RATIO);
	}

	/**
	 * @param min
	 * @param max
	 * @return A random double between man and max.
	 */
	public double rangeDouble(double min, double max) {
		return min + (max - min) * rand.nextDouble();
	}

}