package wargame.basic_types;

import java.io.Serializable;
import java.util.ArrayList;

import wargame.map.Map;

/**
 * Simple class defining a entire position (x;y) in a grid.
 * 
 * @author Balthazar Pavot
 * 
 */
public class Position implements Comparable<Object>, Serializable {

	private static final long serialVersionUID = -8815402665057150965L;
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
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public void move(Position position) {
		move(position.getX(), position.getY());
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

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 * @return The distance between the current point and the given one.
	 */
	public double distance(int x, int y) {
		return Math.sqrt(Math.pow((double) (x - this.x), (double) 2)
				+ Math.pow((double) (y - this.y), (double) 2));
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
		return o.getClass() == this.getClass()
				&& this.x == ((Position) o).getX()
				&& this.y == ((Position) o).getY();
	}

	public boolean equals(Position p) {
		return this.x == p.getX() && this.y == p.getY();
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

	public int compareTo(Object o) {
		if (o.getClass() != this.getClass())
			return -1;
		return compareTo((Position) o);
	}

	public boolean after(Position p) {
		return compareTo(p) == 1;
	}

	public boolean before(Position p) {
		return compareTo(p) == -1;
	}

	public int compareTo(Position p) {
		if (this.y < p.getY())
			return -1;
		if (this.y > p.getY())
			return 1;
		if (this.x < p.getX())
			return -1;
		if (this.x > p.getX())
			return 1;
		return 0;
	}

	public String toString() {
		return String.format("[%d;%d]", x, y);
	}

	public ArrayList<Position> inRange(int range) {
		ArrayList<Position> inRange = new ArrayList<Position>();

		range *= Map.squareWidth;
		for (int x = getX() - range; x < range + getX() + Map.squareWidth; x += Map.squareWidth) {
			for (int y = getY() - range; y < range + getY() + Map.squareHeight; y += Map.squareHeight) {
				if (distance(x, y) <= range)
					inRange.add(new Position(x, y));
			}
		}
		return inRange;
	}

	public boolean isDiagonal(Position posEnd) {
		return this.getX() != posEnd.getX() && this.getY() != posEnd.getY();
	}

	public static int movementCounter(ArrayList<Position> path) {
		try {
			if (path.size() == 0)
				return 0;
			int counter;
			Position previousPos = path.get(0);
			counter = 0;
			for (Position currentPos : path) {
				if (currentPos.getX() != previousPos.getX())
					++counter;
				if (currentPos.getY() != previousPos.getY())
					++counter;
				previousPos = currentPos;
			}
			return counter;
		} catch (NullPointerException e){
			return 0;
		}
	}

	public boolean isInPolygone(ArrayList<Position> vertexList) {
		int n;
		int p1x;
		int p1y;
		int p2x;
		int p2y;
		int i;
		float xinters;
		boolean inside;

		n = vertexList.size();
		inside = false;
		xinters = 0;

		p1x = vertexList.get(0).getX();
		p1y = vertexList.get(0).getY();

		for (i = 0; i < n + 1; ++i) {
			p2x = vertexList.get(i % n).getX();
			p2y = vertexList.get(i % n).getY();
			if (this.y > Math.min(p1x, p2y)) {
				if (this.y <= Math.max(p1y,p2y)){
	            	if (this.x <= Math.max(p1x,p2x)){
	                    if (p1y != p2y){
	                        xinters = (this.y-p1y)*(p2x-p1x)/(p2y-p1y)+p1x;
	                    }
	                    if (p1x == p2x || this.x <= xinters){
	                    	inside = !inside;
	                    }
	                }
	            }
			}
			p1x = p2x;
			p1y = p2y;
		}
		return inside;
	}

}
