
package wargame.map;

/**
 * Defines a tree set shape.
 * @author Balthazar Pavot
 *
 */
public class TreeShape extends Shape {

	public TreeShape() {
		super();
	}

	/**
	 * Generate a tree spot in function of the given parameters.
	 * @param parameters
	 * @return this
	 */
	public TreeShape generate(MapGeneratorParameter parameters) {
		this.parameters = parameters;
		if (parameters.isolatedWaterSpots) {
			this.spotSurface = 1;
			this.spotSurfaceError = 1;
		} else {
			this.spotSurface = 15;
			this.spotSurfaceError = 2;
		}
		this.generateSpots(parameters.treeRatio);
		return this;
	}

}
