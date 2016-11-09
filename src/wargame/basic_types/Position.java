

package wargame.basic_types ;


public class Position {

	private int x = 0 ;
	private int y = 0;

	public Position () {
	}

	public Position (int x, int y) {
		this.x = x ;
		this.y = y ;
	}

	public void move (int x, int y) {
		this.x += x ;
		this.y += y ;
	}

	public int getX () {
		return x ;
	}

	public int getY () {
		return y ;
	}

}
