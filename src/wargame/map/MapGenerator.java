
package wargame.map;

import java.util.ArrayList;

/**
 * This class generates a map, using the map generator parameter.
 * @author Balthazar Pavot
 *
 */
public class MapGenerator {

	private MapGeneratorParameter parameters;
	private Map map;

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
	public void generate() {
		ArrayList<TreeShape> treeShapes = new ArrayList<TreeShape>();
		ArrayList<RockShape> rockShapes = new ArrayList<RockShape>();
		ArrayList<WaterShape> waterShapes = new ArrayList<WaterShape>();

		this.map = new Map(this.parameters.getDimensions());
		generateShapes(treeShapes, rockShapes, waterShapes);
	}

	/**
	 * Generate each spot of tree, rock, water using the parameters.
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
		System.out.printf("Tree ratio: %f;%f (%d)\n", this.parameters.treeRatio, treeRatio,
				treeShapes.size());
		System.out.printf("Rock ratio: %f;%f (%d)\n", this.parameters.rockRatio, rockRatio,
				rockShapes.size());
		System.out.printf("Water ratio: %f;%f (%d)\n", this.parameters.waterRatio, waterRatio,
				waterShapes.size());
	}
}
