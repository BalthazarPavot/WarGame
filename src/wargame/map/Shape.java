
package wargame.map;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a shape. Any shape, randomly generated using the parameters.
 * 
 * @author Balthazar Pavot
 *
 */
public abstract class Shape {

	protected int spotNumber = 0;
	protected int spotSurface = 1;
	protected double spotSurfaceError = 1;
	protected MapGeneratorParameter parameters;
	protected ArrayList<Spot> spots = new ArrayList<Spot>();
	protected ArrayList<Spot> otherSpots = new ArrayList<Spot>();
	private Random rand;

	public Shape() {
		rand = new Random();
	}

	/**
	 * @return The number of squares occupied by the shape
	 */
	public double getSurface() {
		return spotSurface;
	}

	/**
	 * @return The number of spot (a forest is one spot)
	 */
	public double getSquareNumber() {
		return spotNumber;
	}

	public ArrayList<Spot> getSpots() {
		return this.spots;
	}

	/**
	 * @param min
	 * @param max
	 * @return A random double between min and max
	 */
	public double rangeDouble(double min, double max) {
		return min + (max - min) * rand.nextDouble();
	}

	/**
	 * @param min
	 * @param max
	 * @return A random int between min and max
	 */
	public int rangeInt(int min, int max) {
		return rand.nextInt(max - min) + min;
	}

	/**
	 * Generate the position of each spot of the shape.
	 * 
	 * @param ratio
	 */
	protected void generateSpots(double ratio) {
		generateSpots(ratio, false);
	}

	/**
	 * Generate the position of each spot of the shape.
	 * 
	 * @param ratio
	 */
	protected void generateSpots(double ratio, boolean noOverlap) {
		int w;
		int h;
		w = (int) Math.sqrt(this.spotSurface * spotSurfaceError);
		h = (int) Math.sqrt(this.spotSurface * spotSurfaceError);
		int beginX = rangeInt(0, (int) parameters.getDimensions().getWidth()) / Map.squareWidth
				* Map.squareWidth;
		int beginY = rangeInt(0, (int) parameters.getDimensions().getHeight()) / Map.squareHeight
				* Map.squareHeight;
		if (noOverlap)
			for (int x = beginX; x < beginX + w * Map.squareWidth; x += Map.squareWidth)
				for (int y = beginY; y < beginY + h * Map.squareHeight; y += Map.squareHeight)
					for (int dw = -Map.squareWidth; dw < 2 * Map.squareWidth; dw += Map.squareWidth)
						for (int dh = -Map.squareHeight; dh < 2 * Map.squareHeight; dh += Map.squareHeight)
							if (otherSpots.contains(new Spot(x + dw, y + dh, Map.squareWidth, Map.squareHeight)))
								return;
		for (int x = beginX; x < beginX + w * Map.squareWidth; x += Map.squareWidth)
			for (int y = beginY; y < beginY + h * Map.squareHeight; y += Map.squareHeight)
				try {
					spots.add(new Spot(x, y, Map.squareWidth, Map.squareHeight));
					otherSpots.add(new Spot(x, y, Map.squareWidth, Map.squareHeight));
				} catch (IndexOutOfBoundsException e) {
					/**
					 * The resource is out of the map, ignore it.
					 */
				}

		spotNumber = w * h;
	}

	@SuppressWarnings("unchecked")
	public void distord() {
		Random rand;
		int minx;
		int miny;
		int maxx;
		int maxy;
		ArrayList<Spot> originalList;

		originalList = (ArrayList<Spot>) spots.clone();
		rand = new Random();
		minx = spots.get(0).getPosition().getX();
		miny = spots.get(0).getPosition().getY();
		maxx = spots.get(spots.size() - 1).getPosition().getX();
		maxy = spots.get(spots.size() - 1).getPosition().getY();
		for (Spot spot : originalList) {
			if (spot.getPosition().getX() == minx || spot.getPosition().getY() == miny
					|| spot.getPosition().getY() == maxx || spot.getPosition().getY() == maxy)
				if (rand.nextInt(3) == 0)
					spots.remove(spot);
		}
	}

}
