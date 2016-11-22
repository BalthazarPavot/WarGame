
package wargame.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

import wargame.basic_types.Position;
import wargame.map.Map;

public class SidePanel extends JPanel implements GameWidget {

	private static final long serialVersionUID = 1L;
	protected Rectangle boundRect;
	private ArrayList<GameWidget> widgets;
	private BufferedImage minimap;
	private Map map;
	private int minimapWidth = Map.defaultWidth/30;
	private int minimapHeight = Map.defaultHeight/30;
	private Rectangle miniFrame;
	private BufferedImage background;
	private BufferedImage fogImage;
	private Rectangle frame;

	public SidePanel(Map map, int x, int y, int w, int h, BufferedImage backgroundImage, Rectangle frame) {
		this(map, new Rectangle(x, y, w, h), backgroundImage, frame);
	}

	public SidePanel(Map map, Rectangle boundRect, BufferedImage backgroundImage, Rectangle frame) {
		this.boundRect = boundRect;
		widgets = new ArrayList<GameWidget>();
		this.map = map;
		buildImage();
		background = backgroundImage;
		this.frame = frame;
		this.miniFrame = new Rectangle();
		miniFrame.width = (int) (frame.width / (double) map.getWidth() * minimapWidth);
		miniFrame.height = (int) (frame.height / (double) map.getHeight() * minimapHeight);
	}

	/**
	 * Set the position of the bound rectangle.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		this.boundRect = new Rectangle(x, y, this.boundRect.width, this.boundRect.height);
	}

	/**
	 * Set the position of the bound rectangle.
	 * 
	 * @param position
	 */
	public void setPosition(Position position) {
		this.boundRect = new Rectangle(position.getX(), position.getY(), this.boundRect.width,
				this.boundRect.height);
	}

	/**
	 * Return widget's position
	 */
	public Position getPosition() {
		return new Position(this.boundRect.x, this.boundRect.y);
	}

	/**
	 * Set the dimensions of the bound rectangle.
	 * 
	 * @param w
	 * @param h
	 */
	public void setDimension(int w, int h) {
		this.boundRect = new Rectangle(this.boundRect.x, this.boundRect.y, w, h);
	}

	/**
	 * Set the dimensions of the bound rectangle.
	 * 
	 * @param w
	 * @param h
	 */
	public Dimension getDimension() {
		return new Dimension(this.boundRect.width, this.boundRect.height);
	}

	/**
	 * Set the bound rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setBinding(int x, int y, int w, int h) {
		this.boundRect = new Rectangle(x, y, w, h);
	}

	/**
	 * Set the bound rectangle.
	 * 
	 * @param Copy
	 */
	public void setBinding(Rectangle binds) {
		this.boundRect = new Rectangle(binds);
	}

	/**
	 * Place the widget at its place.
	 */
	public void bind() {
		this.setBounds(this.boundRect);
	}

	public void paintComponent(Graphics g) {
		paintComponent(g, 1);
	}

	public void paintComponent(Graphics g, int zoom) {
		paintComponent(g, zoom, 0, 0);
	}

	public void paintComponent(Graphics g, int zoom, int x, int y) {
		super.paintComponent(g);
		for (int x2 = 0; x2 < boundRect.width; x2 += 128)
			for (int y2 = 0; y2 < boundRect.height; y2 += 128)
				g.drawImage(background, x2, y2, 128, 128, this);
		g.drawImage(minimap, x + 10, y + 10, this);
		g.drawImage(fogImage, x + 10, y + 10, this);
		g.drawLine(miniFrame.x, miniFrame.y, miniFrame.x + miniFrame.width, miniFrame.y); // ⁻
		g.drawLine(miniFrame.x, miniFrame.y, miniFrame.x, miniFrame.y + miniFrame.height);// |
		g.drawLine(miniFrame.x, miniFrame.y + miniFrame.height, miniFrame.x + miniFrame.width,
				miniFrame.y + miniFrame.height);// _
		g.drawLine(miniFrame.x + miniFrame.width, miniFrame.y, miniFrame.x + miniFrame.width,
				miniFrame.y + miniFrame.height);
	}

	public void addWidget(GameWidget widget) {
		widgets.add(widget);
		widget.bind();
	}

	private void buildImage() {
		BufferedImage image = new BufferedImage(minimapWidth, minimapHeight, BufferedImage.TYPE_INT_RGB);
		int x;
		int y;
		int dx = map.getWidth() / minimapWidth;
		int dy = map.getHeight() / minimapHeight;
		int pixel;

		for (x = 0; x < minimapWidth; x += 1) {
			for (y = 0; y < minimapHeight; y += 1) {
				pixel = map.colorAt(x * dx / Map.squareWidth * Map.squareWidth,
						y * dy / Map.squareHeight * Map.squareHeight);
				/* 13490654 : snow tree 1842462 : rock 462861 : fir */
				if (pixel == 1842462 || pixel == 462861) {// 462861
					pixel = enlightColor(pixel);
				}
				image.setRGB (x, y, pixel) ;
			}
		}
		this.minimap = image;
		fogImage = new BufferedImage(minimapWidth, minimapHeight, BufferedImage.TYPE_INT_ARGB);
		freeFog();
	}

	public void freeFog() {
		for (int x = 0; x < minimapWidth; x += 1)
			for (int y = 0; y < minimapHeight; y += 1)
				fogImage.setRGB(x, y, 0xff000000);
	}

	public void buildFog(MapWidget mapWidget) {
		int x;
		int y;
		int dx = map.getWidth() / minimapWidth;
		int dy = map.getHeight() / minimapHeight;

		for (x = 0; x < minimapWidth; x += 1) {
			for (y = 0; y < minimapHeight; y += 1) {
				switch(mapWidget.fogAt(x * dx / Map.squareWidth * Map.squareWidth,
						y * dy / Map.squareHeight * Map.squareHeight)){
				case 0:
					setFog(x, y);
					break;
				case 1:
					setSemiFog (x, y);
					break;
				default:
					unSetFog(x, y);
					break;
				}
			}
		}
	}

	private void setSemiFog(int x, int y) {
		if (x < 0 || y < 0 || x > fogImage.getWidth() - 1 || y > fogImage.getHeight() - 1)
			return;
		fogImage.setRGB(x, y, 0x88000000);
	}

	public void unSetFog(int x, int y) {
		if (x < 0 || y < 0 || x > fogImage.getWidth() - 1 || y > fogImage.getHeight() - 1)
			return;
		fogImage.setRGB(x, y, 0x00ffffff);
	}

	public void setFog(int x, int y) {
		if (x < 0 || y < 0 || x > fogImage.getWidth() - 1 || y > fogImage.getHeight() - 1)
			return;
		fogImage.setRGB(x, y, 0xff000000);
	}

	private int enlightColor(int color) {
		return enlightColor(color, 2);
	}

	private int enlightColor(int color, float degree) {
		return (int) ((color >> 16 & 255) * degree) << 16 | (int) ((color >> 8 & 255) * degree) << 8
				| (int) ((color & 255) * degree);
	}

	public void updateFrame() {
		miniFrame.width = (int) (frame.width / (double) map.getWidth() * minimapWidth);
		miniFrame.height = (int) (frame.height / (double) map.getHeight() * minimapHeight);
		miniFrame.x = (int) (frame.x / (double) map.getWidth() * minimapWidth + 10);
		miniFrame.y = (int) (frame.y / (double) map.getHeight() * minimapHeight + 10);
	}
}
