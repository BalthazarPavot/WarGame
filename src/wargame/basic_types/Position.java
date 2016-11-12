
package wargame.basic_types;

import java.util.ArrayList;

import wargame.map.Map;

/**
 * Simple class defining a entire position (x;y) in a grid.
 * @author Balthazar Pavot
 *
 */
public class Position {

	private int x = 0;
	private int y = 0;

	public Position() {
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Move the position using the gven vector.
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * @return The x value of the position
	 */
	public int getX() {
		return x;
	}
 
	/**
	 * @return The y value of the position
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param x
	 * @param y
	 * @return The distance between the current point and the given one.
	 */
	public double distance(int x, int y) {
		return Math.sqrt(
				Math.pow((double) (x - this.x), (double) 2) + Math.pow((double) (y - this.y), (double) 2));
	}

	/**
	 * @param position
	 * @return The distance between the current point and the given one.
	 */
	public double distance(Position position) {
		return distance(position.getX(), position.getY());
	}

	/**
	 * @return True if the positions are the same.
	 */
	public boolean equals(Object o) {
		return o.getClass() == this.getClass() && this.x == ((Position) o).getX()
				&& this.y == ((Position) o).getY();
	}

	/**
	 * @return All the positions around the current one.
	 */
	public ArrayList<Position> getNeighbor() {
		ArrayList<Position> neighbor;

		neighbor = new ArrayList<Position>();
		neighbor.add(new Position(x, y - Map.squareHeight));
		neighbor.add(new Position(x + Map.squareWidth, y - Map.squareHeight));
		neighbor.add(new Position(x + Map.squareWidth, y));
		neighbor.add(new Position(x + Map.squareWidth, y + Map.squareHeight));
		neighbor.add(new Position(x, y + Map.squareHeight));
		neighbor.add(new Position(x - Map.squareWidth, y + Map.squareHeight));
		neighbor.add(new Position(x - Map.squareWidth, y));
		neighbor.add(new Position(x - Map.squareWidth, y - Map.squareHeight));
		return neighbor;
	}

}
