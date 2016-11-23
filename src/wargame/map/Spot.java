
package wargame.map;

import java.awt.Dimension;

import wargame.basic_types.Position;

/**
 * Represent a spot (a tree, a forest, ...) with its position and dimensions.
 * 
 * @author Balthazar Pavot
 *
 */
public class Spot implements Comparable<Spot> {

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

	public boolean equals(Object o) {
		if (o.getClass() != this.getClass())
			return false;
		return equals((Spot) o);
	}

	public boolean equals(Spot s) {
		return position.getX() == s.getPosition().getX() && position.getY() == s.getPosition().getY()
				&& dimensions.getWidth() == s.getDimension().getWidth()
				&& dimensions.getHeight() == s.getDimension().getHeight();
	}

	public String toString() {
		return String.format("%d;%d %.2f;%.2f", position.getX(), position.getY(), dimensions.getWidth(),
				dimensions.getHeight());
	}

	@Override
	public int compareTo(Spot s) {
		return equals(s) ? 0 : 1;
	}
}
