
package wargame.map ;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import wargame.basic_types.Position;


/**
 * This class represents two sets of MapElement associated to their position. <br />
 * The first hash set (Map) contains the positions that are rounded to fit
 * exactly one square. <br />
 * The second one (contained into Map, invisible, named realPositions) contains the
 * exact positions of each sprite. It is used to the display. x and y are stored
 * conversely to the first map, in order to display upper sprites first.	<br />
 * The first hash set (Map) is useful to know if a square is walkable, swimmable...
 * so, it is used for the path finding, and to know if a character can move one a square.<br />
 * The second one is useful for the display: sprites do not have to be on a specific
 * square, graphically speaking. It can be under two or more squares.<br />
 * More simply, the Map looks like the following hash map:
 *	{
 * 		pos_x : {
 *			pos_y : [ elements having position (pos_x;pos_y) ]
 *		}, {
 *			pos_y_2 : [ elements having position (pos_x;pos_y_2) ]
 *		},
 *    	pos_x_2 : {
 *			pos_y : [ elements having position (pos_x_2;pos_y) ]
 *		}, {
 *			pos_y_2 : [ elements having position (pos_x_2;pos_y_2) ]
 *		}
 *	}
 */
public class Map extends HashMap<Integer, HashMap<Integer, ArrayList<MapElement>>> {
	private static final long serialVersionUID = 1L;
	public static int squareWidth = 64 ;
	public static int squareHeight= 64 ;
	public static int defaultSquareNumberWidth = 60 ;
	public static int defaultSquareNumberHeight= 40 ;
	public static int defaultWidth = squareWidth * defaultSquareNumberWidth ;
	public static int defaultHeight= squareHeight * defaultSquareNumberHeight ;
	public static int infScore = Integer.MAX_VALUE ;

	private int width ;
	private int height ;
	private HashMap<Integer, HashMap<Integer, ArrayList<MapElement>>> realPositions ;


	public Map () {
		super () ;
		this.width = defaultWidth ;
		this.height = defaultHeight ;
	}

	public Map (Dimension dimensions) {
		this ((int)dimensions.getWidth (), (int)dimensions.getHeight ()) ;
	}

	public Map (int width, int height) {
		super () ;
		this.width = width ;
		this.height = height ;
	}

	/**
	 * Add the given element the the position
	 * @param position
	 * @param element
	 */
	public void add (Position position, MapElement element) throws IndexOutOfBoundsException {
		this.add (position.getX (), position.getY (), element);
	}

	/**
	 * Add the given element the the good square, and store its real
	 * position in the realPosition.
	 * @param x
	 * @param y
	 * @param element
	 */
	public void add (int x, int y, MapElement element) throws IndexOutOfBoundsException {
		int corneredX ;
		int corneredY ;

		/* 
		 * Set the corner of the sprite on the corner of a square.
		 * 	We use its center to know on which square it really is.
		 **/
		if (x < 0 || x > this.width -squareWidth ||
				y < 0 || y > this.height - squareHeight)
			throw new IndexOutOfBoundsException () ;
		corneredX = (x + squareWidth / 2) / squareWidth * squareWidth - squareWidth / 2 ;
		corneredY = (y + squareHeight / 2)  / squareHeight * squareHeight - squareHeight / 2;
		if (this.get (corneredX) == null)
			this.put (corneredX,  new HashMap<Integer, ArrayList<MapElement>> ()) ;
		if (this.get (corneredX).get (corneredY) == null)
			this.get (corneredX).put (corneredY, new ArrayList<MapElement> ()) ;
		this.get (corneredX).get (corneredY).add (element) ;
		// realPos[y][x]=sprite	in order to display upper sprites first.
		if (realPositions.get (y) == null)
			realPositions.put (y,  new HashMap<Integer, ArrayList<MapElement>> ()) ;
		if (realPositions.get (y).get (x) == null)
			realPositions.get (y).put (x, new ArrayList<MapElement> ()) ;
		realPositions.get (y).get (x).add (element) ;
	}

	/**
	 * Retrieve the list of element cornered at the given position.
	 * @param position
	 * @return
	 */
	public ArrayList<MapElement> get (Position position) {
		return this.get (position.getX (), position.getY ());
	}

	/**
	 * Retrieve the list of element cornered at the given position
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<MapElement> get (int x, int y) throws IndexOutOfBoundsException {
		if (this.get (x) == null)
			throw new IndexOutOfBoundsException() ;
		if (this.get (x).get (y) == null)
			throw new IndexOutOfBoundsException() ;
		return this.get (x).get (y) ;
	}

	/**
	 * Retrieve the list of element having the real given position.
	 * @param position
	 * @return The list of element having this position
	 */
	public ArrayList<MapElement> getReal (Position position) {
		return this.getReal (position.getX (), position.getY ());
	}

	/**
	 * Retrieve the list of element having the real given position
	 * @param x
	 * @param y
	 * @return The list of element having this position
	 */
	public ArrayList<MapElement> getReal (int x, int y) throws IndexOutOfBoundsException {
		if (this.realPositions.get (y) == null)
			throw new IndexOutOfBoundsException() ;
		if (this.realPositions.get (y).get (x) == null)
			throw new IndexOutOfBoundsException() ;
		return this.realPositions.get (y).get (x) ;
	}

	public double getSurface () {
		return (double) this.width * this.height ;
	}

	/**
	 * Tell if a position is walkable
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean walkable (int x, int y) {
		return testFloor (x, y, MapElement.isWalkableString) ;
	}

	/**
	 * Tell if a position is walkable
	 * @param position
	 * @return
	 */
	public boolean walkable (Position position) {
		return this.walkable(position.getX(), position.getY()) ;
	}

	/**
	 * Tell if a position is flyable
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean flyable (int x, int y) {
		return testFloor (x, y, MapElement.isFlyableString) ;
	}

	/**
	 * Tell if a position is flyable
	 * @param position
	 * @return
	 */
	public boolean flyable (Position position) {
		return this.flyable(position.getX(), position.getY()) ;
	}

	/**
	 * Tell if a position is swimmable
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean swimmable (int x, int y) {
		return testFloor (x, y, MapElement.isSwimmableString) ;
	}

	/**
	 * Tell if a position is swimmable
	 * @param position
	 * @return
	 */
	public boolean swimmable (Position position) {
		return this.swimmable(position.getX(), position.getY()) ;
	}

	/**
	 * Tell if a position is crossable by projectiles.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canShotThrough (int x, int y) {
		return testFloor (x, y, MapElement.canShotThroughString) ;
	}

	/**
	 * Tell if a position is crossable by projectiles
	 * @param position
	 * @return
	 */
	public boolean canShotThrough (Position position) {
		return this.canShotThrough (position.getX(), position.getY()) ;
	}

	/**
	 * Tell if two positions can be joined by walking
	 * @param begin_x
	 * @param begin_y
	 * @param end_x
	 * @param end_y
	 * @return
	 */
	public boolean canCrossByWalking (int begin_x, int begin_y, int end_x, int end_y) {
		return AStart (begin_x, begin_y, end_x, end_y, MapElement.isWalkableString) != null ;
	}

	/**
	 * Tell if two positions can be joined by walking
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean canCrossByWalking (Position begin, Position end) {
		return AStart (begin.getX (), begin.getY (), end.getX (), end.getY (), MapElement.isWalkableString) != null ;
	}

	/**
	 * This method implements The A* algorithm using the methodName to know if a spot is walkable.
	 * It was implemented using the pseudocode from https://en.wikipedia.org/wiki/A*_search_algorithm.
	 * @param begin_x
	 * @param begin_y
	 * @param end_x
	 * @param end_y
	 * @param methodName
	 * @return
	 */
	private ArrayList<Position> AStart (int begin_x, int begin_y, int end_x, int end_y, String methodName) {
		ArrayList<Position> evaluatedNodes = new ArrayList<Position> () ; // closedSet
		ArrayList<Position> nodesToExplore = new ArrayList<Position> () ; // openSet
		HashMap<Position, Position> bestPreviousNode = new HashMap<Position, Position> () ;	// cameFrom
		Position currentNode ;	// current
		Position goalNode ;	// goal node
		HashMap<Position, Double> fromStartScore = new HashMap<Position, Double> () ; // gScore
		HashMap<Position, Double> fromStartToEndScore = new HashMap<Position, Double> () ; // fScore
		Double currentScore ;
		ArrayList<Position> fullPath ;

		currentNode = new Position (begin_x, begin_y) ;
		nodesToExplore.add(currentNode);
		fullPath = new ArrayList<Position> () ;
		goalNode = new Position (end_x, end_y) ;
		fromStartScore.put (currentNode, 0.0) ;
		fromStartToEndScore.put (currentNode, currentNode.distance(goalNode)) ;
		while (!nodesToExplore.isEmpty ()) {
			currentNode = nodesToExplore.get (0) ;
	        if (currentNode.equals (goalNode)) 
	           return  reconstructPath (fullPath, bestPreviousNode, currentNode) ;
	        nodesToExplore.remove (currentNode) ;
	        evaluatedNodes.add (currentNode) ; 
	        for (Position neighbor:currentNode.getNeighbor ()) {
	            if (evaluatedNodes.contains (neighbor))
	                continue ;// Ignore the neighbor which is already evaluated.
	            // The distance from start to a neighbor
	            if (!testFloor (neighbor, methodName))
	            	continue ; // we ignore not walkable paths
	            currentScore = fromStartScore.get (currentNode) + 
	            		currentNode.distance (neighbor) ;
/*	            currentScore = (testFloor (neighbor, methodName) ?
  	            		(fromStartScore.get (currentNode) + 
  	            		currentNode.distance (neighbor)) : Map.infScore) ;
*/
	            if (!nodesToExplore.contains (neighbor))	// Discover a new node
	            	nodesToExplore.add (neighbor) ;
	            else if (currentScore >= fromStartScore.get(neighbor))
	                continue ;	// This is not a better path.
	            // This path is the best until now. Record it!
	            bestPreviousNode.put (neighbor, currentNode) ;
	            fromStartScore.put (neighbor, currentScore) ;
	            fromStartToEndScore.put (neighbor, fromStartScore.get(neighbor) +
	            		neighbor.distance (goalNode)) ;
/*	            fromStartToEndScore.put (neighbor, (testFloor (neighbor, methodName) ?
  	            		(fromStartScore.get(neighbor) +
  	            		neighbor.distance (goalNode)) : Map.infScore)) ;
*/
	        }
		}
		return null ;
	}

	/**
	 * Reconstruct the full path using the best scored nodes
	 * @param fullPath
	 * @param bestPreviousNode
	 * @param currentNode
	 * @return
	 */
	private ArrayList<Position> reconstructPath (ArrayList<Position> fullPath,
			HashMap<Position, Position> bestPreviousNode, Position currentNode) {
		fullPath.add (currentNode) ;
	    while (bestPreviousNode.get (currentNode) != null) {
	    	currentNode = bestPreviousNode.get (currentNode) ;
	    	fullPath.add (currentNode) ;
	    }
	    return fullPath ;
	}


	/**
	 * Return true if the position is crossable
	 * @param position
	 * @param methodName
	 * @return true if can cross
	 */
	private boolean testFloor (Position position, String methodName) {
		return testFloor(position.getX (),  position.getY (), methodName) ;
	}

	/**
	 * Return true if the position is crossable
	 * @param x
	 * @param y
	 * @param methodName
	 * @return true if can cross
	 */
	private boolean testFloor (int x, int y, String methodName) {
		java.lang.reflect.Method method;
		ArrayList<MapElement> elements ;

		if (this.get(x) == null)
			return false ;
		elements = this.get (x).get (y) ;
		if (elements == null)
			return false ;
		try {
		  method = this.get(0).getClass ().getMethod (methodName);
		} catch (SecurityException e) {
			return false ;
		} catch (NoSuchMethodException e) {
			return false ;
		}
		try {
			for (MapElement mapElement: elements) {
				if (mapElement.containsPosition (x, y)) {
					if (! (boolean)method.invoke(mapElement))
						return false ;
				}
			}
		} catch (IllegalArgumentException|IllegalAccessException|InvocationTargetException e) {
			return false ;
		}
		return true ;
	}

}
