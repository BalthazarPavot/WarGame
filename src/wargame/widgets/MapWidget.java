
package wargame.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.map.MapElement;
import wargame.map.SpriteHandler;

public class MapWidget extends JPanel implements GameWidget {

	private static final long serialVersionUID = 1L;
	private static final int scollSpeed = 16;

	protected Map map;
	protected Rectangle boundRect;
	protected Rectangle frame;
	protected int zoom = 1;
	protected boolean drawGrid = true;
	protected ImageWidget semiFog;
	protected HashMap<Integer, HashMap<Integer, Integer>> fog = new HashMap<Integer, HashMap<Integer, Integer>>();
	protected boolean revealed = false;
	protected ArrayList<UnitDisplayer> unitDisplayers = new ArrayList<UnitDisplayer>();
	protected ArrayList<UnitDisplayer> ennemyDisplayers = new ArrayList<UnitDisplayer>();
	public InterfaceWidget interfaceWidget;

	public MapWidget(Map map, int width, int height, SpriteHandler spriteHandler) {
		this.map = map;
		this.boundRect = new Rectangle(0, 0, width, height);
		this.frame = new Rectangle(0, 0, width, height);
		semiFog = new ImageWidget(new Rectangle(0, 0, 64, 64), spriteHandler.get("semi_fog").get(0));
		interfaceWidget = new InterfaceWidget (frame) ;
	}

	public void freeFog() {
		for (Integer x : fog.keySet())
			for (Integer y : fog.get(x).keySet())
				setFog(x, y, 1);
	}

	public void setFog(int x, int y) {
		setFog(x, y, 2);
	}

	public void setFog(int x, int y, Integer fogValue) {
		if (fog.get(x) == null)
			fog.put(x, new HashMap<Integer, Integer>());
		fog.get(x).put(y, fogValue);
	}

	public Integer fogAt(int x, int y) {
		if (fog.get(x) == null)
			return 0;
		if (fog.get(x).get(y) == null)
			return 0;
		return fog.get(x).get(y);
	}

	public Integer fogAt(Position p) {
		return fogAt(p.getX(), p.getY());
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
			if (!(x < (int) frame.x - Map.squareWidth || y < (int) frame.y - Map.squareHeight
					|| x > (int) frame.x + frame.width || y > (int) frame.y + frame.height
					|| (fogAt(p) == 0 && !revealed))) {
				for (MapElement me : map.getReal(x, y))
					me.paintComponent(g, zoom, x / zoom - (int) frame.x / zoom + dx,
							y / zoom - (int) frame.y / zoom + dy);
				if (fogAt(p) == 1 && !revealed)
					semiFog.paintComponent(g, zoom, x / zoom - (int) frame.x / zoom + dx,
							y / zoom - (int) frame.y / zoom + dy);
			}
		}
		if (this.drawGrid && zoom == 1) {
			g.setColor(new Color(0, 0, 0, 96));
			int beginX = -(int) (frame.x < 0 ? frame.x : frame.x % (Map.squareWidth / zoom));
			int beginY = -(int) (frame.y < 0 ? frame.y : frame.y % (Map.squareHeight / zoom));
			int overX = (int) (frame.x + frame.width > map.getWidth() ? frame.x + frame.width - map.getWidth()
					: 0);
			int overY = (int) (frame.y + frame.height > map.getHeight()
					? frame.y + frame.height - map.getHeight() : 0);
			for (x = beginX; x <= frame.width - overX; x += Map.squareWidth / zoom)
				g.drawLine(x + dx, beginY - overY + dy, x, (int) frame.height - 1 - overY);
			for (y = beginY; y <= frame.height - overY; y += Map.squareHeight / zoom)
				g.drawLine(beginX - overX + dx, y + dy, (int) frame.width - 1 - overX, y);
		}
		interfaceWidget.paintComponent(g, zoom);
		if (!interfaceWidget.inAnimationLoop())
			for (UnitDisplayer unitDisplayer : unitDisplayers) {
				x = unitDisplayer.getX();
				y = unitDisplayer.getY();
				if (!(x < (int) frame.x - Map.squareWidth || y < (int) frame.y - Map.squareHeight
						|| x > (int) frame.x + frame.width || y > (int) frame.y + frame.height))
					unitDisplayer.paintComponent(g, zoom, x / zoom - (int) frame.x / zoom + dx,
							y / zoom - (int) frame.y / zoom + dy);
			}
	}

	public void addUnitDisplayer(UnitDisplayer unitDisplayer) {
		unitDisplayers.add(unitDisplayer);
	}

	public void addEnnemyDisplayer(UnitDisplayer unitDisplayer) {
		ennemyDisplayers.add(unitDisplayer);
	}

	public Rectangle getFrame() {
		return frame;
	}

	public void setFramePosition(int x, int y) {
		frame.setLocation(x, y);
	}

	public void moveFrame(int x, int y) {
		int resx;
		int resy;

		resx = frame.x + x * scollSpeed * zoom;
		resy = frame.y + y * scollSpeed * zoom;
		if ((x > 0 && resx <= map.getWidth() - frame.width * 3 / 4) || (x < 0 && resx > -frame.width / 4))
			frame.x = resx;
		if ((y > 0 && resy <= map.getHeight() - frame.height * 3 / 4) || (y < 0 && resy > -frame.height / 4))
			frame.y = resy;
	}

	public void increaseZoom() {
		if (zoom > 1) {
			zoom /= 2;
			frame.x += boundRect.width / 2 * zoom;
			frame.y += boundRect.height / 2 * zoom;
			frame.width = boundRect.width * zoom;
			frame.height = boundRect.height * zoom;
		}
	}

	public void decreaseZoom() {
		if (zoom < 4) {
			frame.x -= boundRect.width / 2 * zoom;
			frame.y -= boundRect.height / 2 * zoom;
			zoom *= 2;
			frame.width = boundRect.width * zoom;
			frame.height = boundRect.height * zoom;
		}
	}

	public void updateFramePositionFromMinimap(Rectangle minimapFrame, Rectangle minimapRectangle) {
		this.frame.x = (int) ((double) ((minimapFrame.x - minimapRectangle.x)) / minimapRectangle.width
				* map.getWidth());
		this.frame.y = (int) ((double) ((minimapFrame.y - minimapRectangle.y)) / minimapRectangle.height
				* map.getHeight());
	}

	public boolean isRevealed() {
		return revealed;
	}

	public void setRevealed() {
		revealed = true;
	}

	public void setNotRevealed() {
		revealed = false;
	}

	public Position getInGamePosition(Position position) {
		return new Position (position.getX() * zoom + (int) frame.x,
							position.getY() * zoom + (int) frame.y) ;
	}
}
