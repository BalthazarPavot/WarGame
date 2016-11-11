

package wargame.map ;

import java.util.Random;

public abstract class Shape {

	protected int spotNumber = 0 ;
	protected int spotSurface = 0 ;
	protected double spotSurfaceError = 1 ;
	protected MapGeneratorParameter parameters ;
	private Random rand ;

	public Shape () {
		rand = new Random () ;
	}

	public double getSurface () {
		return spotNumber * spotSurface ;
	}

	public double rangeDouble (double min, double max) {
		return min + (max - min) * rand.nextDouble () ;
	}

	protected void generate () {
		double spotSurfaceError ;

		spotSurfaceError = rangeDouble (1/this.spotSurfaceError, this.spotSurfaceError) ;
		// error decrease spot number and increase spot surface or does the inverse.
		this.spotNumber = (int) (parameters.getSurface() * parameters.waterRatio / spotSurfaceError) ;
		this.spotSurface *= spotSurfaceError ;
	}

}
