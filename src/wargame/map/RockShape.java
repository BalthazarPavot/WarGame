
package wargame.map;

/**
 * Defines a rock set shape.
 * 
 * @author Balthazar Pavot
 *
 */
public class RockShape extends Shape {

	public RockShape() {
		super();
	}

	/**
	 * Generate a rock spot in function of the given parameters.
	 * 
	 * @param parameters
	 * @return this
	 */
	public RockShape generate(MapGeneratorParameter parameters) {
		this.parameters = parameters;
		if (parameters.isolatedRockSets) {
			this.spotSurface = 1;
			this.spotSurfaceError = 1;
		} else {
			this.spotSurface = (int) Math.pow(3, 2); // 5x5
			this.spotSurfaceError = (int) Math.pow(2, 2);
		}
		this.generateSpots(parameters.rockRatio);
		return this;
	}

}
