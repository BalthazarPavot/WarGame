
package wargame.map;

import java.awt.Dimension;

import wargame.basic_types.Position;
import wargame.widgets.ImageWidget;

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

	public boolean containsPosition(int x, int y) {
		Position position;
		Dimension dimension;

		position = getPosition();
		dimension = getDimention();
		return position.getX() <= x && x <= position.getX() + dimension.width && position.getY() <= y
				&& y <= position.getY() + dimension.height;
	}

	public boolean containsPosition(Position position) {
		return containsPosition(position.getX(), position.getY());
	}

	public Position getPosition() {
		return image.getPosition();
	}

	public Dimension getDimention() {
		return image.getDimension();
	}

	public void setPosition(Position position) {
		image.setPosition(position);
	}

	public void setDimension(Dimension dimension) {
		image.setDimension(dimension.width, dimension.height);
	}

	public void move(int x, int y) {
		Position finalPosition;

		finalPosition = image.getPosition();
		finalPosition.move(x, y);
		image.setPosition(finalPosition);
	}

	public boolean isWalkable() {
		return walkable;
	}

	public boolean isFlyable() {
		return flyable;
	}

	public boolean isSwimmable() {
		return swimmable;
	}

	public boolean canShotThrough() {
		return shotThrough;
	}

	public boolean isRemovable() {
		return removable;
	}
}
