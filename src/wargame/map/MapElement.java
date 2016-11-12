
package wargame.map;

import java.awt.Dimension;

import wargame.basic_types.Position;
import wargame.widgets.ImageWidget;


/**
 * This class represent an element of the map: the ground, a tree, water spot, etc <br />
 * It also defines if we can cross it, shot through, etc. <br />
 * It's position and dimensions are defined by the image's dimensions and position.
 * @author Balthazar Pavot
 *
 */
public class MapElement {

	final public static int WALKABLE = 2;
	final public static int FLYABLE = 4;
	final public static int SWIMMABLE = 8;
	final public static int SHOT_THROUGH = 16;
	final public static int REMOVABLE = 32;
	final public static String isWalkableString = "isWalkable";
	final public static String isFlyableString = "isFlyable";
	final public static String isSwimmableString = "isSwimmable";
	final public static String canShotThroughString = "canShotThrough";
	final public static String isRemovableString = "isRemovable";
	// final public static String String = "" ;
	// final public static String String = "" ;

	private boolean walkable = false;
	private boolean flyable = false;
	private boolean swimmable = false;
	private boolean shotThrough = false;
	private boolean removable = false;
	private ImageWidget image = null;

	MapElement(ImageWidget image) {
		this(image, 0);
	}

	MapElement(ImageWidget image, int flags) {
		this.image = image;
		walkable = (flags & WALKABLE) == WALKABLE;
		flyable = (flags & FLYABLE) == FLYABLE;
		swimmable = (flags & SWIMMABLE) == SWIMMABLE;
		shotThrough = (flags & SHOT_THROUGH) == SHOT_THROUGH;
		removable = (flags & REMOVABLE) == REMOVABLE;
	}

	/**
	 * Tell if a position is contained by the sprite.
	 * @param x
	 * @param y
	 * @return true if the given position is into the sprite.
	 */
	public boolean containsPosition(int x, int y) {
		Position position;
		Dimension dimension;

		position = getPosition();
		dimension = getDimension();
		return position.getX() <= x && x <= position.getX() + dimension.width && position.getY() <= y
				&& y <= position.getY() + dimension.height;
	}

	/**
	 * Tell if a position is contained by the sprite.
	 * @param position
	 * @return true if the given position is into the sprite.
	 */
	public boolean containsPosition(Position position) {
		return containsPosition(position.getX(), position.getY());
	}

	/**
	 * @return the position of the map element
	 */
	public Position getPosition() {
		return image.getPosition();
	}

	/**
	 * @return The dimension of the map element
	 */
	public Dimension getDimension() {
		return image.getDimension();
	}

	/**
	 * Set the image's position with the given one
	 * @param position
	 */
	public void setPosition(Position position) {
		image.setPosition(position);
	}

	/**
	 * Set the image's dimensions with the given one
	 * @param dimension
	 */
	public void setDimension(Dimension dimension) {
		image.setDimension(dimension.width, dimension.height);
	}

	/**
	 * Move the sprite using the given vector
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		Position finalPosition;

		finalPosition = image.getPosition();
		finalPosition.move(x, y);
		image.setPosition(finalPosition);
	}

	/**
	 * @return true if the map element is walkable.
	 */
	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * @return true if the map element is flyable.
	 */
	public boolean isFlyable() {
		return flyable;
	}

	/**
	 * @return true if the map element is swimmable
	 */
	public boolean isSwimmable() {
		return swimmable;
	}

	/**
	 * @return true if we can shot through this element
	 */
	public boolean canShotThrough() {
		return shotThrough;
	}

	/**
	 * @return true if this element can be displaced/removed.
	 */
	public boolean isRemovable() {
		return removable;
	}
}
