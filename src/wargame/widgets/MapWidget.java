
package wargame.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;
import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.map.MapElement;

public class MapWidget extends JPanel implements GameWidget {

	private static final long serialVersionUID = 1L;
	private static final int scollSpeed = 16;

	protected Map map;
	protected Rectangle boundRect;
	protected Rectangle frame;
	protected int zoom = 1;
	protected boolean drawGrid = true;

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
		this.paintComponent(g, zoom);
	}

	/**
	 * draw the image at its position
	 * 
	 * @param g
	 * @param zoom
	 */
	public void paintComponent(Graphics g, int zoom) {
		for (int y = ((int) frame.getY() + (int) frame.getHeight() * zoom); y >= (int) frame.getY()
				- Map.squareHeight; y -= 1) {
			for (int x = (int) frame.getX() - Map.squareWidth; x < (int) frame.getX()
					+ frame.getWidth() * zoom; x += 1) {
				for (MapElement me : map.getReal(x, y)) {
					me.paintComponent(g, zoom, x / zoom - (int) frame.getX(), y / zoom - (int) frame.getY());
				}
			}
		}
		if (drawGrid) {
			g.setColor(new Color(0, 0, 0, 96));
			for (int x = -(int) (frame.getX() % (Map.squareWidth/ zoom)); x < frame
					.getWidth(); x += Map.squareWidth / zoom) {
				g.drawLine(x, 0, x, (int) frame.getHeight());
			}
			for (int y = -(int) (frame.getY() % (Map.squareHeight/ zoom)); y < frame
					.getHeight(); y += Map.squareHeight / zoom) {
				g.drawLine(0, y, (int) frame.getWidth(), y);
			}
		}
	}

	public Rectangle getFrame() {
		return frame;
	}

	public void moveFrame(int x, int y) {
		int resx;
		int resy;

		resx = frame.x + x * scollSpeed;
		resy = frame.y + y * scollSpeed;
		if (resx > -frame.width / 4 && resx < map.getWidth() - frame.width * 3 / 4)
			frame.x = resx;
		if (resy > -frame.height / 4 && resy < map.getHeight() - frame.height * 3 / 4)
			frame.y = resy;
	}
}
