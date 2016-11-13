
package wargame.map;

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
			this.spotSurface = (int) Math.pow(15, 2);
			this.spotSurfaceError = (int) Math.pow(2, 2);
		}
		this.generateSpots(parameters.waterRatio);
		return this;
	}

}
