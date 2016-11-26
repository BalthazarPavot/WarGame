
package wargame.engine;

import java.awt.Graphics;
import java.util.ArrayList;


import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Unit;
import wargame.widgets.AnimationWidget;
import wargame.widgets.MapWidget;
import wargame.widgets.SidePanel;


public class Engine {

	ArrayList<Unit> playerUnits;
	ArrayList<Unit> ennemyUnits;
	private MapWidget mapWidget;
	private SidePanel sidePanel;
	private Map map = null;
	private Unit selectedAllie = null;
	private Unit selectedEnnemy = null;
	public ArrayList<Position> currentPath = null;
	private boolean autoGameMode = false;
	private AnimationWidget currentAnimation = null;

	public Engine(Map map, MapWidget mapWidget, SidePanel sidePanel) {
		this.map = map;
		this.mapWidget = mapWidget;
		this.sidePanel = sidePanel;
		initPlayerUnits();
		initEnnemyUnits();
	}

	public void initPlayerUnits() {
		playerUnits = new ArrayList<Unit>();
		playerUnits.add(new Unit(map.getAlliePopArea()));
		playerUnits.get(0).setMove(map.pathByWalking(map.getAlliePopArea(), map.getEnnemyPopArea()));
	}

	public void initEnnemyUnits() {
		ennemyUnits = new ArrayList<Unit>();
	}

	public void nextTurn() {
		for (Unit ennemy : ennemyUnits)
			ennemy.play(playerUnits, ennemyUnits, map);
		for (Unit allie : playerUnits)
			allie.play(playerUnits, ennemyUnits, map);
		updateFog(playerUnits);
	}

	public void updateFog() {
		updateFog(playerUnits);
	}

	private void updateFog(ArrayList<Unit> units) {
		mapWidget.freeFog();
		for (Unit unit : units) {
			Position position = unit.getPosition();
			int sight = 4 * Map.squareWidth;
			for (int x = position.getX() - sight; x < sight + position.getX()
					+ Map.squareWidth; x += Map.squareWidth) {
				for (int y = position.getY() - sight; y < sight + position.getY()
						+ Map.squareHeight; y += Map.squareHeight) {
					if (position.distance(x, y) <= sight)
						mapWidget.setFog(x, y, 2);
				}
			}
		}
		sidePanel.buildFog(mapWidget);
	}

	public void mapMotioned(Position position) {
		if (selectedAllie != null)
			currentPath = selectedAllie.canMove(map, position);
	}

	public void mapClicked(Position position) {
		for (Unit unit : playerUnits)
			if (unit.isClicked(position)) {
				if (selectedAllie != null)
					allieActOnAllie(unit);
				else {
					selectedAllie = unit;
					setSelectedEnnemy(null);
				}
				return;
			}
		for (Unit unit : ennemyUnits)
			if (unit.isClicked(position)) {
				if (selectedAllie != null)
					allieActOnEnnemy(unit);
				else {
					selectedAllie = null;
					setSelectedEnnemy(unit);
				}
				return;
			}
		if (selectedAllie != null) {
			allieMoveTo(position);
			return;
		}
		selectedAllie = null;
		setSelectedEnnemy(null);
	}

	private void allieMoveTo(Position position) {
		if (currentPath != null)
			selectedAllie.setMove(currentPath);
		currentPath = null;
	}

	private void allieActOnEnnemy(Unit unit) {
		if (!selectedAllie.inflictDamage(unit)) {
		}
	}

	private void allieActOnAllie(Unit unit) {
		if (!selectedAllie.heal(unit)) {

		}
	}

	public Unit getSelectedEnnemy() {
		return selectedEnnemy;
	}

	public void setSelectedEnnemy(Unit selectedEnnemy) {
		this.selectedEnnemy = selectedEnnemy;
	}

	public boolean isAutoGame() {
		return autoGameMode;
	}

	public void setAutoGame() {
		autoGameMode = true;
	}

	public void unSetAutoGame() {
		autoGameMode = false;
	}

	public ArrayList<Unit> getPlayerUnits() {
		return playerUnits;
	}

	public ArrayList<Unit> getEnnemyUnits() {
		return ennemyUnits;
	}

	public void setAnimation (AnimationWidget animation) {
		setAnimation (animation, 0, 0) ;
	}

	public void setAnimation (AnimationWidget animation, Position position) {
		setAnimation(animation, position.getX(), position.getY());
	}

	public void setAnimation (AnimationWidget animation, int x, int y) {
		currentAnimation = animation ;
		currentAnimation.move(x, y);
	}

	public void displayAnimations(Graphics g) {
		if (currentAnimation == null)
			return;
		if (!currentAnimation.paintComponent (g))
			currentAnimation = null ;
	}

	public boolean inAnimationLoop() {
		return currentAnimation != null ;
	}

}
