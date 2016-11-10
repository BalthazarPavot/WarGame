
package wargame.map ;

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
}
