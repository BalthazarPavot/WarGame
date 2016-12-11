package wargame.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
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
public class InterfaceWidget implements Serializable {
	private static final long serialVersionUID = 3940437638758402909L;
	private static final Color pathColor = new Color(0, 0, 255, 64);
	private static final Color allieColor = new Color(0, 255, 0, 64);
	private static final Color ennemyColor = new Color(255, 0, 0, 64);
	private static final Color attackColor = ennemyColor;
	private static final Color moveColor = pathColor;
	private AnimationWidget currentAnimation;
	private ArrayList<Position> pathToDisplay = null;
	private ArrayList<Position> enemiesToHighlight = null;
	private ArrayList<Position> alliesToHighlight = null;
	private ArrayList<Position> movePossibilities = null;
	private ArrayList<Position> attackPossibilities = null;
	private Position selectedAllie = null;
	private Position selectedEnemy = null;

	public Position getSelectedEnemy() {
		return selectedEnemy;
	}

	public void setSelectedEnemy(Position selectedEnemy) {
		this.selectedEnemy = selectedEnemy;
	}

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
		if (getSelectedAllie() != null)
			displaySelectedAllie(g, getSelectedAllie(), zoom);
		if (getSelectedEnemy() != null)
			displaySelectedEnnemy(g, selectedEnemy, zoom);
		if (currentAnimation != null)
			displayAnimation(g, currentAnimation, zoom);
		if (getMovePossibilities() != null)
			displayMovePossibilities (g, getMovePossibilities(), zoom) ;
		if (getAttackPossibilities() != null)
			displayAttackPossibilities (g, getAttackPossibilities(), zoom) ;
	}

	private void displayMovePossibilities(Graphics g, ArrayList<Position> movePossibilities2, int zoom) {
		g.setColor(moveColor);
		for (Position position : getMovePossibilities()) {
			displaySquare(g, position, zoom);
		}
	}

	private void displayAttackPossibilities(Graphics g, ArrayList<Position> attackPossibilities2, int zoom) {
		g.setColor(attackColor);
		for (Position position : getAttackPossibilities()) {
			displaySquare(g, position, zoom);
		}
	}

	private void displaySelectedEnnemy(Graphics g, Position selectedEnemy, int zoom) {
		g.setColor(ennemyColor);
		displaySquare(g, selectedEnemy, zoom);
	}

	private void displaySelectedAllie(Graphics g, Position selectedAllie, int zoom) {
		g.setColor(allieColor);
		displaySquare(g, selectedAllie, zoom);
	}

	private void displayPath(Graphics g, ArrayList<Position> pathToDisplay, int zoom) {
		g.setColor(pathColor);
		for (Position position : pathToDisplay) {
			displaySquare(g, position, zoom);
		}
	}

	private void displaySquare(Graphics g, Position position, int zoom) {
		g.fillRect(position.getX() / zoom - (int) frame.x / zoom + 1,
				position.getY() / zoom - (int) frame.y / zoom + 1, Map.squareWidth / zoom - 2,
				Map.squareHeight / zoom - 2);
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

	/**
	 * @return the selectedAllie
	 */
	public Position getSelectedAllie() {
		return selectedAllie;
	}

	/**
	 * @param selectedAllie
	 *            the selectedAllie to set
	 */
	public void setSelectedAllie(Position selectedAllie) {
		this.selectedAllie = selectedAllie;
	}

	/**
	 * @return the enemiesToHighlight
	 */
	public ArrayList<Position> getEnemiesToHighlight() {
		return enemiesToHighlight;
	}

	/**
	 * @param enemiesToHighlight
	 *            the enemiesToHighlight to set
	 */
	public void setEnemiesToHighlight(ArrayList<Position> enemiesToHighlight) {
		this.enemiesToHighlight = enemiesToHighlight;
	}

	/**
	 * @return the alliesToHighlight
	 */
	public ArrayList<Position> getAlliesToHighlight() {
		return alliesToHighlight;
	}

	/**
	 * @param alliesToHighlight
	 *            the alliesToHighlight to set
	 */
	public void setAlliesToHighlight(ArrayList<Position> alliesToHighlight) {
		this.alliesToHighlight = alliesToHighlight;
	}

	/**
	 * @return the movePossibilities
	 */
	public ArrayList<Position> getMovePossibilities() {
		return movePossibilities;
	}

	/**
	 * @param movePossibilities the movePossibilities to set
	 */
	public void setMovePossibilities(ArrayList<Position> movePossibilities) {
		this.movePossibilities = movePossibilities;
	}

	/**
	 * @return the attackPossibilities
	 */
	public ArrayList<Position> getAttackPossibilities() {
		return attackPossibilities;
	}

	/**
	 * @param attackPossibilities the attackPossibilities to set
	 */
	public void setAttackPossibilities(ArrayList<Position> attackPossibilities) {
		this.attackPossibilities = attackPossibilities;
	}
}
