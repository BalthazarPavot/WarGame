
package wargame.map ;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import wargame.basic_types.Position;


/**
 * This class represents a set MapElement associated to their position.
 * More simply, the hash map looks like the following:
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
	public static int squareWidth = 16 ;
	public static int squareHeight= 16 ;
	public static int infScore = Integer.MAX_VALUE ;

	/**
	 * Tell if a position is walkable
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean walkable (int x, int y) {
		return testFloor (x, y, "isWalkable") ;
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
		return testFloor (x, y, "isFlyable") ;
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
		return testFloor (x, y, "isSwimmable") ;
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
	 * Tell if a position is crossable by projectils.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canShotThrough (int x, int y) {
		return testFloor (x, y, "canShotThrough") ;
	}

	/**
	 * Tell if a position is crossable by projectils
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
		return AStart (begin_x, begin_y, end_x, end_y, "isWalkable") != null ;
	}

	/**
	 * Tell if two positions can be joined by walking
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean canCrossByWalking (Position begin, Position end) {
		return AStart (begin.getX (), begin.getY (), end.getX (), end.getY (), "isWalkable") != null ;
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
//	            currentScore = (testFloor (neighbor, methodName) ?
//	            		(fromStartScore.get (currentNode) + 
//	            		currentNode.distance (neighbor)) : Map.infScore) ;
	            if (!nodesToExplore.contains (neighbor))	// Discover a new node
	            	nodesToExplore.add (neighbor) ;
	            else if (currentScore >= fromStartScore.get(neighbor))
	                continue ;	// This is not a better path.
	            // This path is the best until now. Record it!
	            bestPreviousNode.put (neighbor, currentNode) ;
	            fromStartScore.put (neighbor, currentScore) ;
	            fromStartToEndScore.put (neighbor, fromStartScore.get(neighbor) +
	            		neighbor.distance (goalNode)) ;
//	            fromStartToEndScore.put (neighbor, (testFloor (neighbor, methodName) ?
//	            		(fromStartScore.get(neighbor) +
//	            		neighbor.distance (goalNode)) : Map.infScore)) ;
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
