
package wargame.map;

import java.util.ArrayList;
import java.util.Random;

/**
 * Defines a tree set shape.
 * 
 * @author Balthazar Pavot
 *
 */
public class TreeShape extends Shape {

	public TreeShape() {
		super();
	}

	/**
	 * Generate a tree spot in function of the given parameters.
	 * 
	 * @param parameters
	 * @return this
	 */
	public TreeShape generate(MapGeneratorParameter parameters) {
		this.parameters = parameters;
		if (parameters.isolatedTrees) {
			this.spotSurface = 1;
			this.spotSurfaceError = 1;
		} else {
			this.spotSurface = (int) Math.pow(10, 2);
			this.spotSurfaceError = (int) Math.pow(2, 2);
		}
		this.generateSpots(parameters.treeRatio);
		return this;
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
				if (rand.nextInt(2) == 0)
					spots.remove(spot);
			if (spot.getPosition().getX() - minx == Map.squareWidth
					|| spot.getPosition().getY() - miny == Map.squareHeight
					|| maxx - spot.getPosition().getY() == Map.squareWidth
					|| maxy - spot.getPosition().getY() == Map.squareHeight)
				if (rand.nextInt(4) == 0)
					spots.remove(spot);

		}
	}

}
