package wargame.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;

/**
 * This class is a layer of graphic sugar put on the map to make the game play more attractive and
 * interactive. It allow to display some animations, paths, squares, etc.
 * 
 * @author Balthazar Pavot
 *
 */
public class InterfaceWidget {
	private static final Color pathColor = new Color(0, 0, 255, 128);
	private static final Color allieColor = new Color(255, 0, 0, 128);
	private static final Color ennemyColor = new Color(0, 255, 0, 128);
	private AnimationWidget currentAnimation;
	private ArrayList<Position> pathToDisplay = null;
	private Position selectedAllie = null;
	private Position selectedEnemy = null;
	private Rectangle frame;

	public InterfaceWidget(Rectangle frame) {
		this.frame = frame;
	}

	/**
	 * Display all graphic sugar.
	 * 
	 * @param g
	 * @param zoom
	 */
	public void paintComponent(Graphics g, int zoom) {
		if (pathToDisplay != null)
			displayPath(g, pathToDisplay, zoom);
		if (selectedAllie != null)
			displaySelectedAllie(g, selectedAllie, zoom);
		if (selectedAllie != null)
			displaySelectedEnnemy(g, selectedEnemy, zoom);
		if (currentAnimation != null)
			displayAnimation(g, currentAnimation, zoom);
	}

	private void displaySelectedEnnemy(Graphics g, Position selectedEnemy, int zoom) {
		g.setColor(ennemyColor);
		displaySquare (g, selectedEnemy, zoom) ;
	}

	private void displaySelectedAllie(Graphics g, Position selectedAllie, int zoom) {
		g.setColor(allieColor);
		displaySquare (g, selectedAllie, zoom) ;
	}

	private void displayPath(Graphics g, ArrayList<Position> pathToDisplay, int zoom) {
		g.setColor(pathColor);
		for (Position position : pathToDisplay) {
			displaySquare (g, position, zoom) ;
		}
	}

	private void displaySquare(Graphics g, Position position, int zoom) {
		g.fillRect(position.getX() / zoom - (int) frame.x / zoom,
				position.getY() / zoom - (int) frame.y / zoom, Map.squareWidth / zoom,
				Map.squareHeight / zoom);
	}

	/**
	 * Display the current animation frame and update it.
	 * 
	 * @param g
	 * @param zoom
	 */
	public void displayAnimation(Graphics g, AnimationWidget currentAnimation, int zoom) {
		Position currentAnimationPosition;

		currentAnimationPosition = currentAnimation.displayPosition();
		int x = currentAnimationPosition.getX();
		int y = currentAnimationPosition.getY();
		if (!currentAnimation.paintComponent(g, zoom, x / zoom - (int) frame.x / zoom,
				y / zoom - (int) frame.y / zoom))
			this.currentAnimation = null;
	}

	/**
	 * Give the current animation
	 * 
	 * @param g
	 * @param zoom
	 */
	public AnimationWidget getCurrentAnimation() {
		return currentAnimation;
	}

	/**
	 * Set the current animation
	 * 
	 * @param g
	 * @param zoom
	 */
	public void setCurrentAnimation(AnimationWidget currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	/**
	 * Tell if an animation is currently displayed.
	 * 
	 * @param g
	 * @param zoom
	 */
	public boolean inAnimationLoop() {
		return currentAnimation != null;
	}

	public void setPathToDisplay(ArrayList<Position> path) {
		pathToDisplay = path;
	}
}
