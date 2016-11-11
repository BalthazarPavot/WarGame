
package wargame.map ;

import java.util.ArrayList;

public class MapGenerator {

	private MapGeneratorParameter parameters ;
	private Map map ;

	public MapGenerator () {
		this (null) ;
	}

	public MapGenerator (MapGeneratorParameter parameters) {
		this.parameters = parameters ;
		if (this.parameters == null) {
			this.parameters = new MapGeneratorParameter () ;
			this.parameters.generateRandomParameters () ;
		}
		map = new Map () ;
	}

	public Map getMap () {
		return this.map ;
	}

	public void Generate () {
		ArrayList<TreeShape> treeShapes = new ArrayList<TreeShape> () ;
		ArrayList<RockShape> rockShapes = new ArrayList<RockShape> () ;
		ArrayList<WaterShape> waterShapes = new ArrayList<WaterShape> () ;

		this.map = new Map (this.parameters.getDimensions ()) ;
		generateShapes (treeShapes, rockShapes, waterShapes) ;
	}

	private void generateShapes (ArrayList<TreeShape> treeShapes, 
			ArrayList<RockShape> rockShapes,
			ArrayList<WaterShape> waterShapes) {
		double treeRatio = 0 ;
		double waterRatio = 0 ;
		double rockRatio = 0 ;

		while (treeRatio < this.parameters.treeRatio) {
			treeShapes.add (new TreeShape ().generate (this.parameters)) ;
			treeRatio += treeShapes.get (treeShapes.size () - 1).getSurface () / this.map.getSurface () ;
		}
		while (rockRatio < this.parameters.rockRatio) {
			rockShapes.add (new RockShape ().generate (this.parameters)) ;
			rockRatio += rockShapes.get (rockShapes.size () - 1).getSurface () / this.map.getSurface () ;
		}
		while (waterRatio < this.parameters.waterRatio) {
			waterShapes.add (new WaterShape ().generate (this.parameters)) ;
			waterRatio += waterShapes.get (waterShapes.size () - 1).getSurface () / this.map.getSurface () ;
		}
	}
}
