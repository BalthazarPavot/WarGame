
package wargame.map;

public class TreeShape extends Shape {

	public TreeShape() {
		super();
	}

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
