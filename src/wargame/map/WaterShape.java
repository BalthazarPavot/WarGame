
package wargame.map;

import java.util.ArrayList;

/**
 * Defines a water set shape.
 * 
 * @author Balthazar Pavot
 *
 */
public class WaterShape extends Shape {

	public WaterShape() {
		super();
	}

	public WaterShape(ArrayList<WaterShape> waterShapes) {
		for (WaterShape waterShape:waterShapes) {
			for (Spot spot:waterShape.spots)
				otherSpots.add(spot) ;
		}
	}

	/**
	 * Generate a water spot in function of the given parameters.
	 * 
	 * @param parameters
	 * @return this
	 */
	public WaterShape generate(MapGeneratorParameter parameters) {
		this.parameters = parameters;
		if (parameters.isolatedWaterSpots) {
			this.spotSurface = 4;
			this.spotSurfaceError = 1;
		} else {
			this.spotSurface = (int) Math.pow(9, 2);
			this.spotSurfaceError = (int) Math.pow(2, 2);
		}
		this.generateSpots(parameters.waterRatio, parameters.isolatedWaterSpots);
		return this;
	}

}
