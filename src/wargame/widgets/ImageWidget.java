
package wargame.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import wargame.basic_types.Position;
import wargame.basic_types.SerializableBufferedImage;

public class ImageWidget extends JLabel implements GameWidget, Serializable {

	private static HashMap<SerializableBufferedImage, ArrayList<SerializableBufferedImage>> zoomedImageBuffer = new HashMap<SerializableBufferedImage, ArrayList<SerializableBufferedImage>>();
	private static HashMap<SerializableBufferedImage, SerializableBufferedImage> invertedImageBuffer = new HashMap<SerializableBufferedImage, SerializableBufferedImage>();
	private static final long serialVersionUID = 1L;
	protected Rectangle boundRect;
	protected SerializableBufferedImage image;
	protected Color bgColor;

	public static SerializableBufferedImage loadImage(String path) {
		BufferedImage image;
		SerializableBufferedImage newImage = null;

		try {
			image = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
			newImage = new SerializableBufferedImage(image.getWidth(), image.getHeight(),
					SerializableBufferedImage.TYPE_INT_ARGB);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
		} catch (IOException ex) {
			image = null;
		}
		return newImage;
	}

	/**
	 * Return the zoomed image (1/zoom)
	 * 
	 * @param image
	 * @param zoom
	 * @return
	 */
	public static SerializableBufferedImage zoomImage(SerializableBufferedImage image, int zoom) {
		return zoomImage(image, zoom, true);
	}

	/**
	 * Return the zoomed image (1/zoom if unzoom)
	 * 
	 * @param image
	 * @param zoom
	 * @return
	 */
	public static SerializableBufferedImage zoomImage(SerializableBufferedImage image, int zoom,
			boolean unzoom) {
		SerializableBufferedImage newImage = null;
		ArrayList<SerializableBufferedImage> imageBuffer;

		imageBuffer = zoomedImageBuffer.get(image);
		if (imageBuffer == null) {
			imageBuffer = new ArrayList<SerializableBufferedImage>();
			zoomedImageBuffer.put(image, imageBuffer);
			for (int x = 2; x < 8; x <<= 1) {
				Graphics2D g ;
				if (unzoom) {
					newImage = new SerializableBufferedImage(image.getWidth() / x, image.getHeight() / x,
							SerializableBufferedImage.TYPE_INT_ARGB);
					g = newImage.createGraphics();
					g.drawImage(image, 0, 0, image.getWidth() / x, image.getHeight() / x, null);
				} else {
					newImage = new SerializableBufferedImage(image.getWidth() * x, image.getHeight() * x,
							SerializableBufferedImage.TYPE_INT_ARGB);
					g = newImage.createGraphics();
					g.drawImage(image, 0, 0, image.getWidth() * x, image.getHeight() * x, null);
				}
				g.dispose();
				imageBuffer.add(newImage);
			}
		}
		switch (zoom) {
		case 2:
			return imageBuffer.get(0);
		case 4:
			return imageBuffer.get(1);
		default:
			break;
		}
		if (zoom == 1)
			return image;
		return image;
	}

	/**
	 * Return the inverted image
	 * 
	 * @param image
	 * @return
	 */
	public static SerializableBufferedImage invertImage(SerializableBufferedImage image) {
		SerializableBufferedImage inverted;

		inverted = invertedImageBuffer.get(image);
		if (inverted == null) {
			inverted = new SerializableBufferedImage(image.getWidth(), image.getHeight(),
					SerializableBufferedImage.TYPE_INT_ARGB);
			invertedImageBuffer.put(image, inverted);
			Graphics2D g = inverted.createGraphics();
			g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			g.dispose();
			for (int x = 0; x < inverted.getWidth(); x++) {
				for (int y = 0; y < inverted.getHeight(); y++) {
					Color current = new Color(inverted.getRGB(x, y), true);
					inverted.setRGB(x, y, new Color(255 - current.getRed(), 255 - current.getGreen(),
							255 - current.getBlue()).getRGB());
				}
			}
		}
		return inverted;
	}

	public ImageWidget() {
		this(new Rectangle(0, 0, 0, 0));
	}

	public ImageWidget(int x, int y) {
		this(new Rectangle(x, y, 0, 0));
	}

	public ImageWidget(int x, int y, int w, int h) {
		this(new Rectangle(x, y, w, h));
	}

	public ImageWidget(Rectangle boundRect) {
		this.boundRect = boundRect;
		// the background is set as transparent
		this.bgColor = new Color(0, 0, 0, 0);
	}

	public ImageWidget(int x, int y, int w, int h, String path) {
		this(new Rectangle(x, y, w, h), path);
	}

	public ImageWidget(Rectangle boundRect, String path) {
		this(boundRect, ImageWidget.loadImage(path));
	}

	public ImageWidget(int x, int y, int w, int h, SerializableBufferedImage image) {
		this(new Rectangle(x, y, w, h), image);
	}

	public ImageWidget(Rectangle boundRect, SerializableBufferedImage image) {
		// super(new ImageIcon(image));
		this.boundRect = boundRect;
		this.image = (SerializableBufferedImage) image;
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
	 * Return widget's position
	 */
	public SerializableBufferedImage getImage() {
		return this.image;
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

	/**
	 * draw the image at its position
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		this.paintComponent(g, 1);
	}

	/**
	 * draw the image at its position zoomed. The zoom can be 1,2,3 or 4, with 4 is the smaller.
	 * 
	 * @param g
	 * @param zoom
	 */
	public void paintComponent(Graphics g, int zoom) {
		this.paintComponent(g, zoom, 0, 0);
	}

	public void paintComponent(Graphics g, int zoom, int x, int y) {
		super.paintComponent(g);
		g.drawImage(zoomImage(image, zoom), x, y, this);
	}

	public int getColor() {
		int totalR = 0;
		int totalG = 0;
		int totalB = 0;
		int pixel;
		int nbPixel;

		nbPixel = image.getWidth() * image.getHeight();
		for (int x = 0; x < image.getWidth(); x += 1) {
			for (int y = 0; y < image.getHeight(); y += 1) {
				pixel = image.getRGB(x, y);
				totalR += ((pixel >> 16) & 0xff);// * ((pixel >> 24 & 0xff)/255);
				totalG += ((pixel >> 8) & 0xff);// * ((pixel >> 24 & 0xff)/255);
				totalB += (pixel & 0xff);// * ((pixel >> 24 & 0xff)/255);
			}
		}

		return totalR / nbPixel << 16 | totalG / nbPixel << 8 | totalB / nbPixel;
	}
}
