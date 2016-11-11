
package wargame.map;

public class RockShape extends Shape {

	public RockShape() {
		super();
	}

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
