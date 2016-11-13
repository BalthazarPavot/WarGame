
package wargame.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.map.MapElement;

public class MapWidget extends JLabel implements GameWidget {

	private static final long serialVersionUID = 1L;
	protected Map map;
	protected Rectangle boundRect;
	protected Rectangle frame;

	public MapWidget(Map map, int width, int height) {
		this.map = map;
		this.boundRect = new Rectangle(0, 0, width, height);
		this.frame = new Rectangle(0, 0, width, height);
	}

	@Override
	public void bind() {
		this.setBounds(this.boundRect);
	}

	@Override
	public Position getPosition() {
		return new Position(boundRect.x, boundRect.y);
	}

	@Override
	public void setPosition(int x, int y) {
		this.boundRect.x = x;
		this.boundRect.y = y;
	}

	@Override
	public void setPosition(Position position) {
		this.setPosition(position.getX(), position.getY());
	}

	@Override
	public void setDimension(int w, int h) {
		this.boundRect.width = w;
		this.boundRect.height = h;
	}

	@Override
	public Dimension getDimension() {
		return new Dimension(this.boundRect.width, this.boundRect.height);
	}

	@Override
	public void setBinding(int x, int y, int w, int h) {
		this.setPosition(x, y);
		this.setDimension(w, h);
	}

	@Override
	public void setBinding(Rectangle binds) {
		this.setBinding(binds.x, binds.y, binds.width, binds.height);
	}

	/**
	 * draw the image at its position
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		this.paintComponent(g, 1);
	}

	/**
	 * draw the image at its position
	 * 
	 * @param g
	 * @param zoom
	 */
	public void paintComponent(Graphics g, int zoom) {
		for (int y = ((int) frame.getY() + (int) frame.getHeight() * zoom) / Map.squareHeight
				* Map.squareHeight; y >= (int) frame.getY(); y -= Map.squareHeight) {
			for (int x = (int) frame.getX(); x < (int) frame.getX()
					+ frame.getWidth() * zoom; x += Map.squareWidth) {
				for (MapElement me : map.getReal(x, y)) {
					me.paintComponent(g, zoom, x / zoom, y / zoom);
				}
			}
		}
	}
}
