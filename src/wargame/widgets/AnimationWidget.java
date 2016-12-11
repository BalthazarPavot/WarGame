package wargame.widgets;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import wargame.basic_types.Position;
import wargame.basic_types.SerializableBufferedImage;

/**
 * This class represents an animation to be displayed on the screen.
 * All frames have the same duration, given to the constructor. One can add any amount of frame, and
 * eventually give a position corresponding to the move from the first one.
 * We also can move the AnimationWidget to move all frames (the whole animation) at a given position. 
 * @author Balthazar Pavot
 *
 */
public class AnimationWidget extends JPanel implements Serializable {
	private static final long serialVersionUID = 5692785884158017868L;

	protected ArrayList<SerializableBufferedImage> imageList;
	protected ArrayList<Position> positionList;
	protected long frameDuration;
	private int currentImage = 0;
	private long lastFrame = 0;
	private int dx = 0;
	private int dy = 0;

	/**
	 * Take the duration of each frame.
	 * @param frameDuration
	 */
	public AnimationWidget(long frameDuration) {
		imageList = new ArrayList<SerializableBufferedImage>();
		positionList = new ArrayList<Position>();
		this.frameDuration = frameDuration;
	}

	/**
	 * Add an image to the animation, in the same place to the first image.
	 * @param image
	 */
	public void addImage(SerializableBufferedImage image) {
		addImage(image, new Position(0, 0));
	}

	/**
	 * Add an animation, with the given displacement.
	 * @param image
	 * @param p
	 */
	public void addImage(SerializableBufferedImage image, Position p) {
		imageList.add(image);
		positionList.add(p);
	}

	/**
	 * Make the animation to begin again.
	 */
	public void replay() {
		currentImage = 0;
	}

	/**
	 * Move the whole animation in a given point.
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		dx = x;
		dy = y;
	}

	/**
	 * Move the whole animation in a given point.
	 * @param position
	 */
	public void move(Position position) {
		move(position.getX(), position.getY());
	}

	/**
	 * Display the current frame 
	 * @param g
	 * @param zoom
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean paintComponent(Graphics g, int zoom, int x, int y) {
		super.paintComponent(g);
		g.drawImage(ImageWidget.zoomImage(imageList.get(currentImage), zoom), x, y, this);//currentImage++;//
		if (System.currentTimeMillis() - lastFrame > frameDuration) {
			lastFrame = System.currentTimeMillis();
			currentImage += 1 ;
		}
		return currentImage < imageList.size();
	}

	/**
	 * Returns the position the animation will be displayed at.
	 * @return
	 */
	public Position displayPosition() {
		return new Position(positionList.get(currentImage).getX() + dx,
				positionList.get(currentImage).getY() + dy);
	}

}
