
package wargame.engine;

import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.widgets.MapWidget;
import wargame.widgets.SidePanel;

// temp class, waiting for the other's code.
class Unit {
	public Position position;
	public ArrayList<Position> stackedPositions;
	public int curentPosition;

	public Unit(Position position) {
		this.position = position;
	}

	public boolean isClicked(Position pos) {
		return true;
	}

	public void actOn(Unit unit) {
	}

	public boolean inflictDamage(Unit unit) {
		return false;
	}

	public boolean heal(Unit unit) {
		return false;
	}

	public void setMove(ArrayList<Position> currentPath) {
		stackedPositions = currentPath;
		this.curentPosition = 0;
	}

	public void move() {
		if (stackedPositions != null && curentPosition < stackedPositions.size())
			this.position = stackedPositions.get(curentPosition++);
		else
			stackedPositions = null;
	}

	public ArrayList<Position> canMove(Map map, Position position) {
		return null;
	}

	public void play(ArrayList<Unit> playerUnits, ArrayList<Unit> ennemyUnits, Map map) {
		move();
	}

	public Object getMaCara() {
		return null;
	}

	public Position getPosition() {
		return position;
	}
}

public class Engine {

	ArrayList<Unit> playerUnits;
	ArrayList<Unit> ennemyUnits;
	private MapWidget mapWidget;
	private SidePanel sidePanel;
	private Map map = null;
	private Unit selectedAllie = null;
	private Unit selectedEnnemy = null;
	public ArrayList<Position> currentPath = null;
	public boolean autoGameMode = false;

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
			int rangeX = 5 * Map.squareWidth + position.getX();// ((Object) unit.getMaCara()).getRange() ;
			int rangeY = 5 * Map.squareHeight + position.getY();// ((Object) unit.getMaCara()).getRange() ;
			for (int x = rangeX - 10 * Map.squareWidth; x < rangeX; x += Map.squareWidth) {
				for (int y = rangeY - 10 * Map.squareHeight; y < rangeY; y += Map.squareHeight) {
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

	public void setAutoGame() {
		autoGameMode = true;
	}

	public void unSetAutoGame() {
		autoGameMode = false;
	}

}
