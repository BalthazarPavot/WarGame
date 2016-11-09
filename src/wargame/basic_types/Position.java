

package wargame.basic_types ;

import java.util.ArrayList;

import wargame.map.Map;

public class Position {

	private int x = 0 ;
	private int y = 0;

	public Position () {
	}

	public Position (int x, int y) {
		this.x = x ;
		this.y = y ;
	}

	public void move (int x, int y) {
		this.x += x ;
		this.y += y ;
	}

	public int getX () {
		return x ;
	}

	public int getY () {
		return y ;
	}

	public double distance (int x, int y) {
		return Math.sqrt (Math.pow ((double)(x - this.x), (double)2) +
				Math.pow ((double) (y - this.y), (double)2)) ;
	}

	public double distance (Position position) {
		return distance (position.getX (), position.getY ()) ;
	}

	public boolean equals (Object o) {
		return o.getClass () == this.getClass () &&
				this.x == ((Position)o).getX () &&
				this.y == ((Position)o).getY () ;
	}

	public ArrayList<Position> getNeighbor () {
		ArrayList<Position> neighbor ;

		neighbor = new ArrayList<Position> () ;
		neighbor.add(new Position (x, y-Map.squareHeight)) ;
		neighbor.add(new Position (x+Map.squareWidth, y-Map.squareHeight)) ;
		neighbor.add(new Position (x+Map.squareWidth, y)) ;
		neighbor.add(new Position (x+Map.squareWidth, y+Map.squareHeight)) ;
		neighbor.add(new Position (x, y+Map.squareHeight)) ;
		neighbor.add(new Position (x-Map.squareWidth, y+Map.squareHeight)) ;
		neighbor.add(new Position (x-Map.squareWidth, y)) ;
		neighbor.add(new Position (x-Map.squareWidth, y-Map.squareHeight)) ;
		return neighbor ;
	}

}
