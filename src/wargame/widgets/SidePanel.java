
package wargame.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import wargame.basic_types.Position;
import wargame.map.Map;
import wargame.unit.Characteristic;
import wargame.unit.Unit;

public class SidePanel extends JPanel implements GameWidget, Serializable {

	private static final long serialVersionUID = 1L;
	protected Rectangle boundRect;
	private ArrayList<GameWidget> widgets;
	private BufferedImage minimap;
	private Map map;
	private int minimapX = 10;
	private int minimapY = 10;
	private int minimapWidth = Map.defaultWidth / 30;
	private int minimapHeight = Map.defaultHeight / 30;
	private Rectangle minimapRectangle = new Rectangle(minimapX, minimapY, getMinimapWidth(),
			getMinimapHeight());
	private Rectangle miniFrame;
	private BufferedImage background;
	private BufferedImage fogImage;
	private Rectangle frame;
	private HashMap<String, GameWidget> descriptionContent;

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
		miniFrame.width = (int) (frame.width / (double) map.getWidth() * getMinimapWidth());
		miniFrame.height = (int) (frame.height / (double) map.getHeight() * getMinimapHeight());
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
		g.drawImage(minimap, x + minimapX, y + minimapY, this);
		g.drawImage(fogImage, x + minimapX, y + minimapY, this);
		g.drawLine(miniFrame.x, miniFrame.y, miniFrame.x + miniFrame.width, miniFrame.y);
		g.drawLine(miniFrame.x, miniFrame.y, miniFrame.x, miniFrame.y + miniFrame.height);
		g.drawLine(miniFrame.x, miniFrame.y + miniFrame.height, miniFrame.x + miniFrame.width,
				miniFrame.y + miniFrame.height);
		g.drawLine(miniFrame.x + miniFrame.width, miniFrame.y, miniFrame.x + miniFrame.width,
				miniFrame.y + miniFrame.height);
	}

	public void addWidget(GameWidget widget) {
		widgets.add(widget);
		this.add((Component) widget);
		widget.bind();
	}

	private void buildImage() {
		BufferedImage image = new BufferedImage(getMinimapWidth(), getMinimapHeight(),
				BufferedImage.TYPE_INT_RGB);
		int x;
		int y;
		int dx = map.getWidth() / getMinimapWidth();
		int dy = map.getHeight() / getMinimapHeight();
		int pixel;

		for (x = 0; x < getMinimapWidth(); x += 1) {
			for (y = 0; y < getMinimapHeight(); y += 1) {
				pixel = map.colorAt(x * dx / Map.squareWidth * Map.squareWidth,
						y * dy / Map.squareHeight * Map.squareHeight);
				/* 13490654 : snow tree 1842462 : rock 462861 : fir */
				if (pixel == 1842462 || pixel == 462861) {// 462861
					pixel = enlightColor(pixel);
				}
				image.setRGB(x, y, pixel);
			}
		}
		this.minimap = image;
		fogImage = new BufferedImage(getMinimapWidth(), getMinimapHeight(), BufferedImage.TYPE_INT_ARGB);
		freeFog();
	}

	public void freeFog() {
		for (int x = 0; x < getMinimapWidth(); x += 1)
			for (int y = 0; y < getMinimapHeight(); y += 1)
				fogImage.setRGB(x, y, 0xff000000);
	}

	public void buildFog(MapWidget mapWidget) {
		int x;
		int y;
		int dx = map.getWidth() / getMinimapWidth();
		int dy = map.getHeight() / getMinimapHeight();

		for (x = 0; x < getMinimapWidth(); x += 1) {
			for (y = 0; y < getMinimapHeight(); y += 1) {
				switch (mapWidget.fogAt(x * dx / Map.squareWidth * Map.squareWidth,
						y * dy / Map.squareHeight * Map.squareHeight) + (mapWidget.isRevealed() ? 2 : 0)) {
				case 0:
					setFog(x, y);
					break;
				case 1:
					setSemiFog(x, y);
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
		miniFrame.width = (int) (frame.width / (double) map.getWidth() * getMinimapWidth());
		miniFrame.height = (int) (frame.height / (double) map.getHeight() * getMinimapHeight());
		miniFrame.x = (int) (frame.x / (double) map.getWidth() * getMinimapWidth() + minimapX);
		miniFrame.y = (int) (frame.y / (double) map.getHeight() * getMinimapHeight() + minimapY);
	}

	/**
	 * @return the minimapRectangle
	 */
	public Rectangle getMinimapRectangle() {
		return minimapRectangle;
	}

	public void setFramePosition(int x, int y) {
		miniFrame.x = x;
		miniFrame.y = y;
	}

	public Rectangle getMinimapFrame() {
		return miniFrame;
	}

	/**
	 * @return the minimapHeight
	 */
	public int getMinimapHeight() {
		return minimapHeight;
	}

	/**
	 * @param minimapHeight
	 *            the minimapHeight to set
	 */
	public void setMinimapHeight(int minimapHeight) {
		this.minimapHeight = minimapHeight;
	}

	/**
	 * @return the minimapWidth
	 */
	public int getMinimapWidth() {
		return minimapWidth;
	}

	/**
	 * @param minimapWidth
	 *            the minimapWidth to set
	 */
	public void setMinimapWidth(int minimapWidth) {
		this.minimapWidth = minimapWidth;
	}

	public void setSelectedAllie(Unit selectedAllie) {
		if (selectedAllie != null)
			((JLabel) descriptionContent.get("name")).setText(selectedAllie.getAllieName());
		setDescriptionTexts(selectedAllie);
	}

	public void setSelectedEnemy(Unit selectedEnemy) {
		if (selectedEnemy != null)
			((JLabel) descriptionContent.get("name")).setText(selectedEnemy.getAllieName());
		setDescriptionTexts(selectedEnemy);
	}

	private void setDescriptionTexts(Unit unit) {
		if (unit != null) {
			Characteristic chars = unit.getCharacteristics();
			((JLabel) descriptionContent.get("life"))
					.setText(String.format("%d / %d", chars.currentLife, chars.life));
			((JLabel) descriptionContent.get("attack")).setText(unit.getAttackDescription());
			((JLabel) descriptionContent.get("defPierce")).setText("Def Pierce: " + chars.defensePercing);
			((JLabel) descriptionContent.get("defBlunt")).setText("Def Blunt: " + chars.defenseBlunt);
			((JLabel) descriptionContent.get("defSlash")).setText("Def Slash: " + chars.defenseSlashing);
			((JLabel) descriptionContent.get("defMagic")).setText("Def Magic: " + chars.defenseMagic);
		} else {
			((JLabel) descriptionContent.get("name")).setText("");
			((JLabel) descriptionContent.get("life")).setText("");
			((JLabel) descriptionContent.get("attack")).setText("");
			((JLabel) descriptionContent.get("defPierce")).setText("");
			((JLabel) descriptionContent.get("defBlunt")).setText("");
			((JLabel) descriptionContent.get("defSlash")).setText("");
			((JLabel) descriptionContent.get("defMagic")).setText("");
		}
	}

	public void setDescriptionContent(HashMap<String, GameWidget> descCtnt) {
		descriptionContent = descCtnt;

	}
}
