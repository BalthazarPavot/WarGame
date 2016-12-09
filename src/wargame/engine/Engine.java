
package wargame.engine;

import java.util.ArrayList;
import java.util.Random;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.*;
import wargame.widgets.AnimationWidget;
import wargame.widgets.MapWidget;
import wargame.widgets.SidePanel;
import wargame.widgets.UnitDisplayer;

/**
 * The engine handle the actions done by the player to make it's characters to attack, move etc. It updates
 * the map widget and the side panel to make them display each action.
 * 
 * @author Balthazar Pavot
 *
 */
public class Engine {

	public ArrayList<Position> currentPath = null;
	public int currentActingUnit = -1;
	public int currentActingEnemy = -1;
	ArrayList<Unit> playerUnits = null;
	ArrayList<Unit> ennemyUnits = null;
	private MapWidget mapWidget = null;
	private SidePanel sidePanel = null;
	private Map map = null;
	private Unit selectedAllie = null;
	private Unit selectedEnnemy = null;
	private boolean autoGameMode = false;
	private GameContext context = null;
	private int enemyAppearanceFrequency = 4;
	private int enemyHerdAppearanceFrequency = 20;
	private int bigEnemyAppearanceFrequency = 50;
	private int turnBeforeEnemyArearance = enemyAppearanceFrequency;
	private int turnBeforeHerdArearance = 0;
	private int turnBeforeBigEnemyArearance = bigEnemyAppearanceFrequency + 10;
	private int enemyNumberInHerd = 5;

	public Engine(GameContext context, MapWidget mapWidget, SidePanel sidePanel) {
		this.map = context.getMap();
		this.mapWidget = mapWidget;
		this.sidePanel = sidePanel;
		this.context = context;
		initPlayerUnits();
		initEnnemyUnits();
	}

	public boolean makeCurrentUnitAction() {
		Unit activeUnit = null;
		int actionCode;

		if (currentActingUnit != -1) {
			activeUnit = playerUnits.get(currentActingUnit);
			actionCode = activeUnit.play(playerUnits, ennemyUnits, map);
			displayAction(actionCode, activeUnit);
			if (actionCode == Unit.NO_ACTION) {
				currentActingUnit += 1;
				activeUnit.hasPlayed = true;
			} else
				updateFog();
			if (currentActingUnit == playerUnits.size())
				currentActingUnit = -1;
			else if (currentActingUnit != -1)
				mapWidget.unitInAction = playerUnits.get(currentActingUnit);
		}
		if (currentActingUnit == -1 && currentActingEnemy != -1) {
			activeUnit = ennemyUnits.get(currentActingEnemy);
			while (activeUnit.getPathToEnd() == null)
				activeUnit.moveTo(new Random().nextInt(map.getWidth() / 64) * 64,
						new Random().nextInt(map.getHeight() / 64) * 64, map);
			actionCode = activeUnit.play(playerUnits, ennemyUnits, map);
			displayAction(actionCode, activeUnit);
			if (actionCode == Unit.NO_ACTION) {
				currentActingEnemy += 1;
				activeUnit.hasPlayed = true;
			} else
				updateFog();
			if (currentActingEnemy == ennemyUnits.size())
				currentActingEnemy = -1;
			else if (currentActingEnemy != -1)
				mapWidget.unitInAction = ennemyUnits.get(currentActingEnemy);
		}
		return currentActingUnit != -1 || currentActingEnemy != -1;
	}

	/**
	 * Dispatch all action to the given characters, update the fog and pass to the next turn.
	 */
	public void nextTurn() {
		Characteristic charac;

		for (Unit allie : playerUnits) {
			charac = allie.getCharacteristics();
			charac.currentMovePoints += charac.movePoints;
			if (charac.currentMovePoints > charac.movePoints + 1)
				charac.currentMovePoints = charac.movePoints + 1;
			allie.hasPlayed = false;
		}
		for (Unit ennemy : ennemyUnits) {
			charac = ennemy.getCharacteristics();
			charac.currentMovePoints += charac.movePoints;
			if (charac.currentMovePoints > charac.movePoints + 1)
				charac.currentMovePoints = charac.movePoints + 1;
			ennemy.hasPlayed = false;
		}
		makeEnemiesPop();
		updateFog(playerUnits);
		selectAllie(null);
		selectEnnemy(null);
		turnBeforeEnemyArearance--;
		turnBeforeHerdArearance--;
		turnBeforeBigEnemyArearance--;
		currentActingUnit = -1;
		currentActingEnemy = -1;
		mapWidget.unitInAction = null;
	}

	private void makeEnemiesPop() {
		if (turnBeforeEnemyArearance == 0) {
			turnBeforeEnemyArearance = enemyAppearanceFrequency;
			makeEnemyPop(1);
			turnBeforeEnemyArearance = enemyAppearanceFrequency;
		}
		if (turnBeforeHerdArearance == 0) {
			turnBeforeHerdArearance = enemyHerdAppearanceFrequency;
			makeEnemyPop(enemyNumberInHerd);
			turnBeforeHerdArearance = enemyHerdAppearanceFrequency;
		}
		if (turnBeforeBigEnemyArearance == 0) {
			turnBeforeBigEnemyArearance = bigEnemyAppearanceFrequency;
			bigEnemyPop();
			turnBeforeBigEnemyArearance = bigEnemyAppearanceFrequency;
		}
	}

	private void bigEnemyPop() {
		// TODO Auto-generated method stub

	}

	private void makeEnemyPop(int i) {
		while (i-- != 0) {
			Unit newUnit;
			newUnit = new Bowman(map.getEnnemyPopArea(), context);

			ennemyUnits.add(newUnit);
			mapWidget.addEnnemyDisplayer(new UnitDisplayer(newUnit,
					context.getSpriteHandler().getEnemyStaticPositionSprites(newUnit)));
		}
	}

	/**
	 * Update the fog using all ally units.
	 */
	public void updateFog() {
		updateFog(playerUnits);
	}

	/**
	 * 
	 * @param position
	 */
	public void mapMotioned(Position position) {
		if (selectedAllie != null)
			currentPath = selectedAllie.canMove(map, position);
	}

	/**
	 * Select/unselect units when the map is clicked, on execute actions.
	 * 
	 * @param position
	 */
	public void mapLeftClicked(Position position) {
		position = mapWidget.getInGamePosition(position);
		for (Unit unit : playerUnits) {
			if (unit.isClicked(position)) {
				if (selectedAllie != null)
					allieActOnAllie(unit);
				else if (!unit.hasPlayed) {
					selectAllie(unit);
				} else
					sidePanel.setSelectedAllie(unit);
				return;
			}
		}
		for (Unit unit : ennemyUnits) {
			if (unit.isClicked(position)) {
				if (selectedAllie != null)
					allieActOnEnnemy(unit);
				else {
					selectedAllie = null;
					mapWidget.interfaceWidget.setSelectedAllie(null);
					selectEnnemy(unit);
				}
				return;
			}
		}
		if (selectedAllie != null) {
			allieMoveTo(position);
			return;
		}
		// selectAllie (null);
		// selectEnnemy(null);
	}

	public void mapRightClicked(Position mapPosition) {
		selectAllie(null);
		selectEnnemy(null);
	}

	private void selectAllie(Unit unit) {
		ArrayList<Position> pathToEnd;
		ArrayList<Position> enemiesToHighlight = null;

		selectedAllie = unit;
		selectedEnnemy = null;
		updateEnemySelection();
		if (selectedAllie != null && !selectedAllie.hasPlayed) {
			mapWidget.interfaceWidget.setSelectedAllie(selectedAllie.getPosition());
			pathToEnd = selectedAllie.getPathToEnd();
			if (pathToEnd != null)
				mapWidget.interfaceWidget.setPathToDisplay(new ArrayList<Position>(pathToEnd.subList(0,
						Math.min(selectedAllie.getCharacteristics().sight, pathToEnd.size()))));
			else
				mapWidget.interfaceWidget.setPathToDisplay(null);
			enemiesToHighlight = new ArrayList<Position>();
			for (Unit enemy : ennemyUnits)
				if (enemy.inAttackRangeOf(selectedAllie))
					enemiesToHighlight.add(enemy.getPosition());
			mapWidget.interfaceWidget.setMovePossibilities(selectedAllie.movePossibilities(map));
			mapWidget.interfaceWidget.setAttackPossibilities(selectedAllie.attackPossibilities());
		} else {
			mapWidget.interfaceWidget.setSelectedAllie(null);
			mapWidget.interfaceWidget.setPathToDisplay(null);
			mapWidget.interfaceWidget.setMovePossibilities(null);
			mapWidget.interfaceWidget.setAttackPossibilities(null);
		}
		mapWidget.interfaceWidget.setEnemiesToHighlight(enemiesToHighlight);
		sidePanel.setSelectedAllie(selectedAllie);
	}

	/**
	 * set the given unit selected
	 * 
	 * @param selectedEnnemy
	 */
	public void selectEnnemy(Unit selectedEnnemy) {
		selectAllie(null);
		this.selectedEnnemy = selectedEnnemy;
		updateEnemySelection();
		sidePanel.setSelectedEnemy(selectedEnnemy);
	}

	private void updateEnemySelection() {
		if (selectedEnnemy != null)
			mapWidget.interfaceWidget.setSelectedEnemy(selectedEnnemy.getPosition());
		else
			mapWidget.interfaceWidget.setSelectedEnemy(null);
	}

	/**
	 * Create and initialise the units of the player.
	 */
	private void initPlayerUnits() {
		Unit unit;

		playerUnits = new ArrayList<Unit>();
		unit = new Wizard(map.getAlliePopArea(), context);
		playerUnits.add(unit);
		mapWidget.addUnitDisplayer(
				new UnitDisplayer(unit, context.getSpriteHandler().getUnitStaticPositionSprites(unit)));
		unit.moveTo(map.getEnnemyPopArea(), map);
		unit = new Bird(map.getAlliePopArea(), context);
		playerUnits.add(unit);
		mapWidget.addUnitDisplayer(
				new UnitDisplayer(unit, context.getSpriteHandler().getUnitStaticPositionSprites(unit)));
		unit.moveTo(map.getEnnemyPopArea(), map);
	}

	/**
	 * Create and initialise the enemy units.
	 */
	private void initEnnemyUnits() {
		ennemyUnits = new ArrayList<Unit>();
	}

	/**
	 * Display the action of a unit in function of its action code.
	 * 
	 * @param action
	 * @param unit
	 */
	private void displayAction(int action, Unit unit) {
		switch (action) {
		case Unit.MOVE_ACTION:
			setAnimation(unit.getCurrentWalkAnimation(), unit.getPreviousPosition());
			break;
		case Unit.ATTACK_ACTION:
			break;
		case Unit.NO_ACTION:
		default:
			break;
		}
	}

	/**
	 * For each unit update it's vision.
	 * 
	 * @param units
	 */
	private void updateFog(ArrayList<Unit> units) {
		mapWidget.freeFog();
		for (Unit unit : units) {
			updateFog(unit);
		}
		sidePanel.buildFog(mapWidget);
	}

	private void updateFog(Unit unit) {
		for (Position position : unit.squaresInSight()) {
			mapWidget.setFog(position, 2);

		}
	}

	/**
	 * make the selected unit to move to the given point.
	 * 
	 * @param position
	 */
	private void allieMoveTo(Position position) {
		int cost;
		Position previous;
		ArrayList<Position> currentPath;

		selectedAllie.moveTo(position.getX() / Map.squareWidth * Map.squareWidth,
				position.getY() / Map.squareHeight * Map.squareHeight, map);
		currentPath = selectedAllie.getPathToEnd();
		if (currentPath != null) {
			cost = 0;
			previous = currentPath.get(0);
			for (Position pos : currentPath.subList(1, currentPath.size())) {
				if (pos.getX() == previous.getX() || pos.getY() == previous.getY())
					cost += 1;
				else
					cost += 2;
				previous = pos;
			}
			if (cost <= selectedAllie.getCharacteristics().currentMovePoints) {
				displayAction(selectedAllie.play(playerUnits, ennemyUnits, map), selectedAllie);
				selectedAllie.hasPlayed = false;
				selectAllie(selectedAllie);
			} else
				mapWidget.interfaceWidget.setPathToDisplay(currentPath);
		}
	}

	/**
	 * make an ally attack or whatever he can do to the enemy which we clicked on.
	 * 
	 * @param unit
	 */
	private void allieActOnEnnemy(Unit unit) {
		if (!selectedAllie.hasPlayed && unit.inAttackRangeOf(selectedAllie)) {
			selectedAllie.hasPlayed = true;
			if (selectedAllie.inflictDamage(unit)) {
				ennemyUnits.remove(selectedAllie);
			}
		}
		selectAllie(null);
	}

	/**
	 * make an ally heal or whatever he can do to the ally which we clicked on.
	 * 
	 * @param unit
	 */
	private void allieActOnAllie(Unit unit) {
		if (selectedAllie.heal(unit)) {
			selectedAllie.hasPlayed = true;
		}
	}

	/**
	 * return the selected enemy
	 * 
	 * @return
	 */
	public Unit getSelectedEnnemy() {
		return selectedEnnemy;
	}

	/**
	 * return if the game is playing automatically
	 * 
	 * @return
	 */
	public boolean isAutoGame() {
		return autoGameMode;
	}

	/**
	 * make the game play automatically
	 */
	public void setAutoGame() {
		autoGameMode = true;
	}

	/**
	 * make the game not to play automatically
	 */
	public void unSetAutoGame() {
		autoGameMode = false;
	}

	/**
	 * Return all the player's units.
	 * 
	 * @return
	 */
	public ArrayList<Unit> getPlayerUnits() {
		return playerUnits;
	}

	/**
	 * Return all the enemies.
	 * 
	 * @return
	 */
	public ArrayList<Unit> getEnnemyUnits() {
		return ennemyUnits;
	}

	/**
	 * Make the map display the given animation.
	 * 
	 * @param animation
	 */
	public void setAnimation(AnimationWidget animation) {
		setAnimation(animation, 0, 0);
	}

	/**
	 * make the map display the animation at the given position
	 * 
	 * @param animation
	 * @param position
	 */
	public void setAnimation(AnimationWidget animation, Position position) {
		setAnimation(animation, position.getX(), position.getY());
	}

	/**
	 * make the map display the animation at the given position
	 * 
	 * @param animation
	 * @param x
	 * @param y
	 */
	public void setAnimation(AnimationWidget animation, int x, int y) {
		if (animation != null) {
			animation.move(x, y);
			animation.replay();
		}
		mapWidget.interfaceWidget.setCurrentAnimation(animation);
	}
}
