
package wargame.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import wargame.map.Map;
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
		super();
		this.unit = unit;
		this.staticPositionImage = staticPositionImage;
	}

	public int getX() {
		return unit.position.getX();
	}

	public int getY() {
		return unit.position.getY();
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
		double lifeRatio;

		if (staticPositionImage[unit.staticPosition % 4] != null)
			g.drawImage(ImageWidget.zoomImage(staticPositionImage[unit.staticPosition % 4], zoom), x, y,
					this);
		g.setColor(Color.black);
		g.fillRect(x + 1, y - 10, Map.squareWidth / zoom - 2, 5);
		lifeRatio = ((double) unit.getCharacteristics().currentLife / unit.getCharacteristics().life);
		g.setColor(lifeRatio > 0.75 ? Color.green : lifeRatio > 0.5 ? Color.orange : Color.red);
		g.fillRect(x + 2, y - 9, (int) ((Map.squareWidth / zoom - 4) * lifeRatio), 3);
	}
}
