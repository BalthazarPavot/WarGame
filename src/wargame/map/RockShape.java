
package wargame.map;

/**
 * Defines a rock set shape.
 * @author Balthazar Pavot
 *
 */
public class RockShape extends Shape {

	public RockShape() {
		super();
	}

	/**
	 * Generate a rock spot in function of the given parameters.
	 * @param parameters
	 * @return this
	 */
	public RockShape generate(MapGeneratorParameter parameters) {
		this.parameters = parameters;
		if (parameters.isolatedWaterSpots) {
			this.spotSurface = 1;
			this.spotSurfaceError = 0.5;
		} else {
			this.spotSurface = 5;
			this.spotSurfaceError = 1.5;
		}
		this.generateSpots(parameters.rockRatio);
		return this;
	}

}
