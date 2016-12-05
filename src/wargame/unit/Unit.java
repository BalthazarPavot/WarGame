package wargame.unit;

import java.lang.Math;
import java.util.ArrayList;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.AI.AI;

public class Unit {
	public static int UPWARD_DIRECRTION = 0;
	public static int DOWNWARD_DIRECRTION = 2;
	public static int RIGHTWARD_DIRECRTION = 1;
	public static int LEFTWARD_DIRECRTION = 3;
	public int staticPosition = DOWNWARD_DIRECRTION;
	public Position position;
	private ArrayList<Position> movementArea;
	public ArrayList<Position> stackedPositions;
	public int curentPosition;
	public boolean hasPlayed = false;
	public AI ai;
	private boolean canFire;

	public Unit(Position position) {
		this.position = position;
		this.setCanFire();
	}

	private void setCanFire() {
		// TODO check if only ranger can fire
		// TODO add a field "range" for rangers
		if (this instanceof Ranger)
			this.canFire = true;
		else
			this.canFire = false;

	}

	public boolean isClicked(Position pos) {
		return pos.getX() >= position.getX() && pos.getY() >= position.getY()
				&& pos.getX() <= position.getX() + Map.squareWidth
				&& pos.getY() <= position.getY() + Map.squareHeight;
	}

	ArrayList<Position> getMovementArea() {
		return this.movementArea;
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
		Position nextPosition;
		if (stackedPositions != null
				&& curentPosition < stackedPositions.size()) {
			if (curentPosition < stackedPositions.size() - 1) {
				nextPosition = stackedPositions.get(curentPosition + 1);
				if (nextPosition.getX() > position.getX())
					staticPosition = LEFTWARD_DIRECRTION;
				else if (nextPosition.getX() < position.getX())
					staticPosition = RIGHTWARD_DIRECRTION;
				else if (nextPosition.getY() > position.getY())
					staticPosition = DOWNWARD_DIRECRTION;
				else if (nextPosition.getY() < position.getY())
					staticPosition = UPWARD_DIRECRTION;
			}
			this.position = stackedPositions.get(curentPosition++);
		} else
			stackedPositions = null;
	}

	/**
	 * Return the path to go to the destination (different for Terrestre and
	 * Aerien
	 * 
	 * @param map
	 * @param destination
	 * @return
	 */
	public ArrayList<Position> canMove(Map map, Position destination) {
		ArrayList<Position> path = new ArrayList<Position>();
		if (this instanceof Terrestre) {
			path = map.pathByWalking(this.position, destination);
		} else {
			path = map.pathByWalking(this.position, destination);
		}
		return path;
	}

	public Object getMaCara() {
		return null;
	}

	public Position getPosition() {
		return position;
	}

	/**
	 * Return true if the unit can reach the position endPos in 1 round
	 * 
	 * @param map
	 * @param endPos
	 * @return
	 */
	public boolean canReach(Map map, Position endPos) {
		int distance;
		ArrayList<Position> path;

		path = this.canMove(map, endPos);
		distance = Position.movementCounter(path);
		// TODO Check if there are error at the next line
		return distance <= this.getMaCara().getNbCaseDep();
	}

	/**
	 * Return true if the unit can hit the unit at the pos posTarget, it can
	 * make move the unit and also fire
	 * 
	 * @param posTarget
	 * @param map
	 * @return
	 */
	public boolean canHit(Position posTarget, Map map) {
		if (this.canFire) {
			for (Position posThis : getMovementArea()) {
				if (this.canFireFrom(posThis, posTarget, map))
					return true;
			}
		} else {
			for (Position posThis : getMovementArea()) {
				if (this.position.distance(posThis) == 1)
					return true;
			}
		}
		return false;
	}

	/* TODO Simplifie ça static ou pas */
	public static boolean canFireFrom(Position posThis, Position posTarget,
			Map map) {

		int xUnit = posThis.getY();
		int xTarget = posThis.getX();
		int yUnit = posThis.getY();
		int yTarget = posThis.getX();

		int dx = java.lang.Math.abs(xUnit) - java.lang.Math.abs(xTarget);
		int dy = java.lang.Math.abs(yUnit) - java.lang.Math.abs(yTarget);

		boolean[][] aPos = new boolean[java.lang.Math.abs(dx)][java.lang.Math
				.abs(dy)];

		int xStart;
		int yStart;

		int xShift;
		int yShift;

		int xCnt;
		int yCnt;
		int xCnt2;
		int yCnt2;
		int xCntBehind;
		int yCntBehind;

		int xPtr;
		int yPtr;
		int xPtr2;
		int yPtr2;
		int xPtrBehind;
		int yPtrBehind;

		int dxObstacle;
		int dyObstacle;

		int xShadeLength;
		int yShadeLength;

		int xPxThis;
		int yPxThis;

		int xPxObstacleLimit1;
		int yPxObstacleLimit1;
        int xPxObstacleLimit2;
		int yPxObstacleLimit2;
		
		int pxRange;
		
		float xyRate;
		float yxRate;
		
		Position thisPosCenter = null;
		Position posEnd1 = null;
		Position posEnd2 = null;
		Position posInArray = null;
		ArrayList<Position> vertexList = new ArrayList<Position>();
		
/*TODO use that line		pxRange = this.range * Map.squareHeight;*/
		pxRange = 5;
		
		if (0 <= dx) {
			xStart = 0;
			xShift = 1;
		} else {
			xStart = java.lang.Math.abs(dx - 1);
			xShift = -1;
		}
		if (0 <= dy) {
			yStart = 0;
			yShift = 1;
		} else {
			yStart = java.lang.Math.abs(dx - 1);
			yShift = -1;
		}
		xPxThis = (Map.squareWidth / 2) * xShift;
		yPxThis = (Map.squareHeight / 2) * yShift;
		
		vertexList.add(thisPosCenter);
		vertexList.add(posEnd1);
		vertexList.add(posEnd2);
		/* Inisialization of the Array 1 => can shot */
		for (xCnt = 0; xCnt < java.lang.Math.abs(dx); ++xCnt) {
			xPtr = xCnt * xShift + xStart;
			for (yCnt = 0; yCnt < java.lang.Math.abs(dy); ++yCnt) {
				yPtr = yCnt * yShift + yStart;
				if (map.canShotThrough(xPtr, yPtr)) {
					aPos[xPtr][yPtr] = true;
				} else {
					aPos[xPtr][yPtr] = false;
				}
			}
		}

		// TODO supprimer ça c'est de l'affichage
		for (xCnt = 0; xCnt < java.lang.Math.abs(dx); ++xCnt) {
			for (yCnt = 0; yCnt < java.lang.Math.abs(dy); ++yCnt) {
				System.out.print(aPos[xCnt][yCnt]);
			}
			System.out.println();
		}

		for (xCnt = 0; xCnt < java.lang.Math.abs(dx); ++xCnt) {
			xPtr = xCnt * xShift + xStart;
			for (yCnt = 0; yCnt < java.lang.Math.abs(dy); ++yCnt) {
				yPtr = yCnt * yShift + yStart;
				if (!aPos[xPtr][yPtr]) {
					xPxObstacleLimit1 = (xPtr + xShift) * Map.squareWidth;
					yPxObstacleLimit1 = yPtr * Map.squareHeight;
					xPxObstacleLimit2 = xPtr * Map.squareWidth;
					yPxObstacleLimit2 = (yPtr + yShift) * Map.squareHeight;
					yxRate = yPxObstacleLimit1 / xPxObstacleLimit1;
					xyRate = xPxObstacleLimit1 / yPxObstacleLimit1;
					posEnd1.setX((int) (xyRate * pxRange + xPxThis));
					posEnd1.setY((int) (yxRate * pxRange + yPxThis));
					yxRate = yPxObstacleLimit2 / xPxObstacleLimit2;
					xyRate = xPxObstacleLimit2 / yPxObstacleLimit2;
					posEnd2.setX((int) (xyRate * pxRange + xPxThis));
					posEnd2.setY((int) (yxRate * pxRange + yPxThis));
					for (xCnt2 = xCnt; xCnt2 < java.lang.Math.abs(dx); ++xCnt2) {
						xPtr2 = xCnt2 * xShift + xStart;
						for (yCnt2 = yCnt; yCnt2 < java.lang.Math.abs(dy); ++yCnt2) {
							yPtr2 = yCnt2 * yShift + yStart;
							posInArray.setX((int)(xPtr2 + 0.5) * Map.squareWidth);
							posInArray.setY((int)(yPtr2 + 0.5) * Map.squareHeight);
							if (posInArray.isInPolygone(vertexList))
								aPos[xPtr2][yPtr2] = false;
						}	
					}
				}
			}
		}

		return search(aPos, xUnit, yUnit, xShift, yShift, xTarget, yTarget);
	}

	public static Boolean search(boolean[][] aPos, int xCnt, int yCnt,
			int xShift, int yShift, int xTarget, int yTarget) {
		if (xCnt == xTarget && yCnt == yTarget)
			return true;
		if (aPos[xCnt + xShift][yCnt])
			return search(aPos, xCnt + xShift, yCnt, xShift, yShift, xTarget,
					yTarget);
		else if (aPos[xCnt][yCnt + yShift])
			return search(aPos, xCnt, yCnt + yShift, xShift, yShift, xTarget,
					yTarget);
		return null;
	}

	public ArrayList<Position> setMovemmentArea(Map map) {
		ArrayList<Position> movementArea = new ArrayList();

	}

}