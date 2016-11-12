
package wargame.map;

import java.awt.Dimension;

import wargame.basic_types.Position;

/**
 * Represent a spot (a tree, a forest, ...) with its position and dimensions.
 * @author Balthazar Pavot
 *
 */
public class Spot {

	private Position position;
	private Dimension dimensions;

	public Spot(int x, int y, int w, int h) {
		position = new Position(x, y);
		dimensions = new Dimension(w, h);
	}

	/**
	 * @return The position of the spot.
	 */
	public Position getPosition() {
		return this.position;
	}

	/**
	 * @return The dimension of the spot.
	 */
	public Dimension getDimension() {
		return this.dimensions;
	}
}
