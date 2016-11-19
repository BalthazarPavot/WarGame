
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
	private int minimapWidth = 128;
	private int minimapHeight = 128;
	private BufferedImage background ;

	public SidePanel(Map map, int x, int y, int w, int h, BufferedImage backgroundImage) {
		this(map, new Rectangle(x, y, w, h), backgroundImage);
	}

	public SidePanel(Map map, Rectangle boundRect, BufferedImage backgroundImage) {
		this.boundRect = boundRect;
		widgets = new ArrayList<GameWidget>();
		this.map = map;
		buildImage();
		background = backgroundImage ;
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
		for (int x2=0;x2<boundRect.width;x2+=128)
			for (int y2=0;y2<boundRect.height;y2+=128)
				g.drawImage(background, x2, y2, 128, 128, this) ;
		g.drawImage(minimap, x + 10, y + 10, this);
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
				/*
				 * 13490654 : snow tree
				 * 1842462 : rock
				 * 462861 : fir
				 */
				if (pixel == 1842462 || pixel==462861) {// 462861
					pixel = enlightColor (pixel);
				}
				image.setRGB(x, y, pixel);
			}
		}
		this.minimap = image;
	}

	private int enlightColor (int color) {
		return enlightColor (color, 2);
	}
	private int enlightColor (int color, float degree) {
		return (int)((color >> 16 & 255)*degree) << 16 | (int)((color >> 8 & 255)*degree) << 8 | (int)((color& 255)*degree);
	}
}
