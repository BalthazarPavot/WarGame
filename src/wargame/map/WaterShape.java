

package wargame.map ;

public class WaterShape extends Shape {
	
	public WaterShape () {
		super () ;
	}

	public WaterShape generate (MapGeneratorParameter parameters) {
		this.parameters = parameters ;
		if (parameters.isolatedWaterSpots) {
			this.spotSurface = 4 ;
			this.spotSurfaceError = 0.5 ;
		} else {
			this.spotSurface = 20 ;
			this.spotSurfaceError = 4 ;
		}
		this.generate() ;
		return this ;
	}


}
