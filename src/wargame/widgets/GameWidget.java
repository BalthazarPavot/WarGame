
package wargame.widgets;

import java.awt.Dimension;
import java.awt.Rectangle;

import wargame.basic_types.Position;

public interface GameWidget {

	/**
	 * The function to put the widget at its place.
	 */
	void bind();

	/**
	 * Return widget's position
	 */
	Position getPosition();

	/**
	 * Set the position of the bound rectangle.
	 * @param x
	 * @param y
	 */
	void setPosition(int x, int y);

	/**
	 * Set the position of the bound rectangle.
	 * @param position
	 */
	void setPosition(Position position);

	/**
	 * Set the dimensions of the bound rectangle.
	 * @param w
	 * @param h
	 */
	void setDimension(int w, int h);

	/**
	 * Return the dimensions of the rectangle
	 */
	Dimension getDimension();

	/**
	 * Set the bound rectangle.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	void setBinding(int x, int y, int w, int h);

	/**
	 * Set the bound rectangle.
	 * @param Copy
	 */
	void setBinding(Rectangle binds);

}
