
package wargame.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;

import wargame.basic_types.Position;

public class TextWidget extends JLabel implements GameWidget {

	private static final long serialVersionUID = 1L;
	protected Rectangle boundRect;
	private int size = 14;

	public TextWidget(String text, int x, int y, int w, int h) {
		this(text, new Rectangle(x, y, w, h));
	}

	public TextWidget(String text, Rectangle boundRect) {
		this(text, boundRect, 14);
	}

	public TextWidget(String text, int x, int y, int w, int h, int size) {
		this(text, new Rectangle(x, y, w, h), size, new Color(0, 0, 0));
	}

	public TextWidget(String text, Rectangle boundRect, int size) {
		this(text, boundRect, size, new Color(0, 0, 0));
	}

	public TextWidget(String text, int x, int y, int w, int h, int size, Color color) {
		super(text);
		this.boundRect = new Rectangle(x, y, w, h);
		this.setTextSize(size);
		this.setForeground(color);
	}

	public TextWidget(String text, Rectangle boundRect, int size, Color color) {
		super(text);
		this.boundRect = boundRect;
		this.setTextSize(size);
		this.setForeground(color);
	}

	/**
	 * Set the size of the text.
	 * 
	 * @param size
	 */
	public void setTextSize(int size) {
		if (size != this.size) {
			this.setFont(new Font("Serif", Font.PLAIN, size));
			this.size = size;
		}
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
	 * Set the dimensions of the bound rectangle.
	 * 
	 * @param w
	 * @param h
	 */
	public void setDimension(int w, int h) {
		this.boundRect = new Rectangle(this.boundRect.x, this.boundRect.y, w, h);
	}

	/**
	 * Return the dimensions of the rectangle
	 */
	public Dimension getDimension() {
		return new Dimension(this.boundRect.width, this.boundRect.height);
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
	 * @param binds
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

}
