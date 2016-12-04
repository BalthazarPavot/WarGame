
package wargame.engine;

import java.util.ArrayList;

import wargame.GameContext;
import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Unit;
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

	ArrayList<Unit> playerUnits = null;
	ArrayList<Unit> ennemyUnits = null;
	private MapWidget mapWidget = null;
	private SidePanel sidePanel = null;
	private Map map = null;
	private Unit selectedAllie = null;
	private Unit selectedEnnemy = null;
	public ArrayList<Position> currentPath = null;
	private boolean autoGameMode = false;
	private GameContext context = null;
	private int enemyAppearanceFrequency = 4;
	private int enemyHerdAppearanceFrequency = 20;
	private int bigEnemyAppearanceFrequency = 50;
	private int turnBeforeEnemyArearance = enemyAppearanceFrequency;
	private int turnBeforeHerdArearance = 0;
	private int turnBeforeBigEnemyArearance = bigEnemyAppearanceFrequency + 10 ;
	private int enemyNumberInHerd = 5 ;

	public Engine(GameContext context, MapWidget mapWidget, SidePanel sidePanel) {
		this.map = context.getMap();
		this.mapWidget = mapWidget;
		this.sidePanel = sidePanel;
		this.context = context;
		initPlayerUnits();
		initEnnemyUnits();
	}

	/**
	 * Dispatch all action to the given characters, update the fog and pass to the next turn.
	 */
	public void nextTurn() {
		for (Unit allie : playerUnits) {
			displayAction(allie.play(playerUnits, ennemyUnits, map), allie);
			allie.hasPlayed = false;
		}
		for (Unit ennemy : ennemyUnits) {
			displayAction(ennemy.play(playerUnits, ennemyUnits, map), ennemy);
			ennemy.hasPlayed = false;
		}
		makeEnemiesPop();
		updateFog(playerUnits);
		selectAllie(null);
		selectEnnemy(null);
		turnBeforeEnemyArearance--;
		turnBeforeHerdArearance--;
		turnBeforeBigEnemyArearance--;
	}

	private void makeEnemiesPop() {
		if (turnBeforeEnemyArearance == 0) {
			turnBeforeEnemyArearance = enemyAppearanceFrequency;
			makeEnemyPop(1);
		}
		if (turnBeforeHerdArearance == 0) {
			turnBeforeHerdArearance = enemyHerdAppearanceFrequency;
			makeEnemyPop(enemyNumberInHerd);
		}
		if (turnBeforeBigEnemyArearance == 0) {
			turnBeforeBigEnemyArearance = bigEnemyAppearanceFrequency;
			bigEnemyPop();
		}
	}

	private void bigEnemyPop() {
		// TODO Auto-generated method stub

	}

	private void makeEnemyPop(int i) {
		while (i-- != 0) {
			ennemyUnits.add(new Unit(map.getEnnemyPopArea(), context.getSpriteHandler()));
			mapWidget.addUnitDisplayer(new UnitDisplayer(ennemyUnits.get(ennemyUnits.size()-1),
					context.getSpriteHandler().getUnitStaticPositionSprites(ennemyUnits.get(ennemyUnits.size()-1))));
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
				else {
					selectAllie(unit);
				}
				return;
			}
		}
		for (Unit unit : ennemyUnits) {
			if (unit.isClicked(position)) {
				if (selectedAllie != null)
					allieActOnEnnemy(unit);
				else {
					selectedAllie = null;
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
		selectedAllie = unit;
		selectedEnnemy = null;
		mapWidget.interfaceWidget
				.setPathToDisplay(selectedAllie == null ? null : selectedAllie.getPathToEnd());
	}

	/**
	 * set the given unit selected
	 * 
	 * @param selectedEnnemy
	 */
	public void selectEnnemy(Unit selectedEnnemy) {
		selectAllie(null);
		this.selectedEnnemy = selectedEnnemy;
	}

	/**
	 * Create and initialise the units of the player.
	 */
	private void initPlayerUnits() {
		playerUnits = new ArrayList<Unit>();
		playerUnits.add(new Unit(map.getAlliePopArea(), context.getSpriteHandler()));
		mapWidget.addUnitDisplayer(new UnitDisplayer(playerUnits.get(0),
				context.getSpriteHandler().getUnitStaticPositionSprites(playerUnits.get(0))));
		playerUnits.get(0).setMove(map.pathByWalking(map.getAlliePopArea(), map.getEnnemyPopArea()));
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

	/**
	 * make the selected unit to move to the given point.
	 * 
	 * @param position
	 */
	private void allieMoveTo(Position position) {
		if (currentPath != null)
			selectedAllie.setMove(currentPath);
		currentPath = null;
	}

	/**
	 * make an ally attack or whatever he can do to the enemy which we clicked on.
	 * 
	 * @param unit
	 */
	private void allieActOnEnnemy(Unit unit) {
		if (!selectedAllie.inflictDamage(unit)) {
		}
	}

	/**
	 * make an ally attack or whatever he can do to the ally which we clicked on.
	 * 
	 * @param unit
	 */
	private void allieActOnAllie(Unit unit) {
		if (!selectedAllie.heal(unit)) {

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
