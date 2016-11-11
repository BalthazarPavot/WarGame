
package wargame.map;

import java.awt.Dimension;

import wargame.basic_types.Position;

public class Spot {

	private Position position;
	private Dimension dimensions;

	public Spot(int x, int y, int w, int h) {
		position = new Position(x, y);
		dimensions = new Dimension(w, h);
	}

	public Position getPosition() {
		return this.position;
	}

	public Dimension getDimension() {
		return this.dimensions;
	}
}
