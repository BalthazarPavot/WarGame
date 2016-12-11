
package wargame.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JButton;

import wargame.basic_types.Position;
import wargame.screens.GameScreenActionManager;

public class ButtonWidget extends JButton implements GameWidget, Serializable {

	private static final long serialVersionUID = 1L;
	protected Rectangle boundRect;
	private int size = 14;

	public ButtonWidget(int x, int y, int w, int h) {
		this("", x, y, w, h);
	}

	public ButtonWidget(String text, int x, int y, int w, int h) {
		this(text, new Rectangle(x, y, w, h), 14);
	}

	public ButtonWidget(String text, Rectangle boundRect) {
		this(text, boundRect, 14);
	}

	public ButtonWidget(String text, int x, int y, int w, int h, int size) {
		this(text, new Rectangle(x, y, w, h), size, new Color(128, 128, 128));
	}

	public ButtonWidget(String text, Rectangle boundRect, int size) {
		this(text, boundRect, size, new Color(128, 128, 128));
	}

	public ButtonWidget(String text, int x, int y, int w, int h, int size, Color color) {
		this(text, new Rectangle(x, y, w, h), size, color);
	}

	public ButtonWidget(String text, Rectangle boundRect, int size, Color color) {
		this(text, boundRect, size, color, null);
	}

	public ButtonWidget(String text, int x, int y, int w, int h, int size, Color color,
			GameScreenActionManager actionManager) {
		this(text, new Rectangle(x, y, w, h), size, color, actionManager);
	}

	public ButtonWidget(String text, Rectangle boundRect, int size, Color color,
			GameScreenActionManager actionManager) {
		super(text);
		this.boundRect = boundRect;
		this.setTextSize(size);
		this.setForeground(color);
		this.setActionCommand(text);
		this.addActionListener(actionManager);
	}

	public void setText (String text) {
		super.setText (text);
		this.setActionCommand(text);
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
	 * Return the dimensions of the rectangle
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

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paintComponent(Graphics g, int zoom, int x, int y) {
		super.paintComponent(g);
	}
}
