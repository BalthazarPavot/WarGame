
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
		paintComponent(g, zoom, 0, 0);
	}

	/**
	 * draw the image at its position
	 * 
	 * @param g
	 * @param zoom
	 */
	public void paintComponent(Graphics g, int zoom, int dx, int dy) {
		super.paintComponent(g);
		int x;
		int y;
		for (Position p : map.getPositions()) {
			x = p.getX();
			y = p.getY();
			if (!(x / zoom < (int) frame.getX() - Map.squareWidth
					|| y / zoom < (int) frame.getY() - Map.squareHeight
					|| x / zoom > (int) frame.getX() + frame.getWidth()
					|| y / zoom > (int) frame.getY() + frame.getHeight()))
				for (MapElement me : map.getReal(x, y))
					me.paintComponent(g, zoom, x / zoom - (int) frame.getX() + dx,
							y / zoom - (int) frame.getY() + dy);
		}
		if (this.drawGrid && zoom == 1) {
			g.setColor(new Color(0, 0, 0, 96));
			int beginX = -(int) (frame.getX() < 0 ? frame.getX() : frame.getX() % (Map.squareWidth / zoom));
			int beginY = -(int) (frame.getY() < 0 ? frame.getY() : frame.getY() % (Map.squareHeight / zoom));
			int overX = (int) (frame.getX() + frame.getWidth() > map.getWidth()
					? frame.getX() + frame.getWidth() - map.getWidth() : 0);
			int overY = (int) (frame.getY() + frame.getHeight() > map.getHeight()
					? frame.getY() + frame.getHeight() - map.getHeight() : 0);
			for (x = beginX; x <= frame.getWidth() - overX; x += Map.squareWidth / zoom)
				g.drawLine(x+dx, beginY - overY+dy, x, (int) frame.getHeight() - 1 - overY);
			for (y = beginY; y <= frame.getHeight() - overY; y += Map.squareHeight / zoom)
				g.drawLine(beginX - overX+dx, y+dy, (int) frame.getWidth() - 1 - overX, y);
		}
	}

	public Rectangle getFrame() {
		return frame;
	}

	public void moveFrame(int x, int y) {
		int resx;
		int resy;

		resx = frame.x + x * scollSpeed / zoom;
		resy = frame.y + y * scollSpeed / zoom;
		if ((x >= 0 && resx <= map.getWidth() / zoom - frame.width * 3 / 4)
				|| (x <= 0 && resx > -frame.width / 4))
			frame.x = resx;
		if ((y >= 0 && resy <= map.getHeight() / zoom - frame.height * 3 / 4)
				|| (y <= 0 && resy > -frame.height / 4))
			frame.y = resy;
	}

	public void increaseZoom() {
		if (zoom > 1) {
			zoom /= 2;
			frame.x += boundRect.width / 2 * zoom;
			frame.y += boundRect.height / 2 * zoom;
			frame.width *= zoom;
			frame.height *= zoom;
		}
	}

	public void decreaseZoom() {
		if (zoom < 2) {
			frame.x -= boundRect.width / 2 * zoom;
			frame.y -= boundRect.height / 2 * zoom;
			frame.width /= zoom;
			frame.height /= zoom;
			zoom *= 2;
		}
	}
}
