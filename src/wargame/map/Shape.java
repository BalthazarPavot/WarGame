
package wargame.map;

import java.util.ArrayList;
import java.util.Random;

public abstract class Shape {

	protected int spotNumber = 0;
	protected int spotSurface = 1;
	protected double spotSurfaceError = 1;
	protected MapGeneratorParameter parameters;
	ArrayList<Spot> spots = new ArrayList<Spot>();
	private Random rand;

	public Shape() {
		rand = new Random();
	}

	public double getSurface() {
		return spotNumber * spotSurface;
	}

	public double getSquareNumber() {
		return spotNumber;
	}

	public double rangeDouble(double min, double max) {
		return min + (max - min) * rand.nextDouble();
	}

	public int rangeInt(int min, int max) {
		return rand.nextInt(max - min) + min;
	}

	protected void generateSpots(double ratio) {
		int spotNumber;

		this.spotNumber = (int) (parameters.getSquareNumber() * ratio / spotSurface);
		for (spotNumber = 0; spotNumber < this.spotNumber; spotNumber++) {
			spots.add(new Spot(
					rangeInt(0, (int) parameters.getDimensions().getWidth()) / spotSurface * spotSurface,
					rangeInt(0, (int) parameters.getDimensions().getHeight()) / spotSurface * spotSurface,
					(int) (this.spotSurface * rangeDouble(1 / this.spotSurfaceError, this.spotSurfaceError))
							/ spotSurface * spotSurface,
					(int) (this.spotSurface * rangeDouble(1 / this.spotSurfaceError, this.spotSurfaceError))
							/ spotSurface * spotSurface));
		}
	}

}
