
package wargame.widgets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import wargame.unit.Unit;

public class UnitDisplayer extends ImageWidget {
	private static final long serialVersionUID = 447515459555241432L;
	public BufferedImage[] staticPositionImage;
	public AnimationWidget[] movingAnimations;
	public Unit unit;

	public UnitDisplayer(Unit unit) {
		this(unit, new BufferedImage[4]);
	}

	public UnitDisplayer(Unit unit, BufferedImage[] staticPositionImage) {
//		super(unit.position.getX(), unit.position.getY(), Map.squareWidth, Map.squareHeight);
		super () ;
		this.unit = unit;
		this.staticPositionImage = staticPositionImage;
	}

	public int getX () {
		return unit.position.getX() ;
	}

	public int getY () {
		return unit.position.getY() ;
	}

	@Override
	public void paintComponent(Graphics g) {
		this.paintComponent(g, 1);
	}

	@Override
	public void paintComponent(Graphics g, int zoom) {
		this.paintComponent(g, zoom, 0, 0);
	}

	@Override
	public void paintComponent(Graphics g, int zoom, int x, int y) {
		if (staticPositionImage[unit.staticPosition%4] != null)
			g.drawImage(ImageWidget.zoomImage(staticPositionImage[unit.staticPosition%4], zoom), x, y, this);
	}
}
