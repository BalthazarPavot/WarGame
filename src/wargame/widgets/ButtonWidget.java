
package wargame.widgets ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JButton;

public class ButtonWidget extends JButton implements GameWidget {

  
  private static final long serialVersionUID = 1L;
  protected Rectangle boundRect ;
  private int size = 14 ;


  public ButtonWidget  (int x, int y, int w, int h) {
    this ("", x, y, w, h) ;
  }

  public ButtonWidget  (String text, int x, int y, int w, int h) {
    this (text, new Rectangle (x, y, w, h), 14) ;
  }

  public ButtonWidget  (String text, Rectangle boundRect) {
    this (text, boundRect, 14) ;
  }

  public ButtonWidget  (String text, int x, int y, int w, int h, int size) {
    this (text, new Rectangle (x, y, w, h), size, new Color (128, 128, 128)) ;
  }

  public ButtonWidget  (String text, Rectangle boundRect, int size) {
    this (text, boundRect, size, new Color (128, 128, 128)) ;
  }

  public ButtonWidget  (String text, int x, int y, int w, int h, int size,
      Color color) {
    this (text, new Rectangle (x, y, w, h), size, color) ;
  }

  public ButtonWidget  (String text, Rectangle boundRect, int size,
      Color color) {
    super (text) ;
    this.boundRect = boundRect ;
    this.setTextSize (size) ;
    this.setForeground (color) ;
  }

  /**
   * Set the size of the text.
   * @param size The int value of the text size.
   */
  public void setTextSize (int size) {
    if (size != this.size) {
      this.setFont (new Font ("Serif", Font.PLAIN, size)) ;
      this.size = size ;
    }
  }

  /**
   * Set the position of the bound rectangle.
   * @param x The position in the x axis
   * @param y The position in the y axis
   */
  public void setPosition (int x, int y) {
    this.boundRect = new Rectangle (x, y, this.boundRect.width,
      this.boundRect.height) ;
  }

  /**
   * Set the dimensions of the bound rectangle.
   * @param w The width of the rectangle
   * @param h The height of the rectangle
   */
  public void setDimention (int w, int h) {
    this.boundRect = new Rectangle (this.boundRect.x, this.boundRect.y, w, h) ;
  }

  /**
   * Set the bound rectangle.
   * @param x The position in the x axis
   * @param y The position in the y axis
   * @param w The width of the rectangle
   * @param h The height of the rectangle
   */
  public void setBinding (int x, int y, int w, int h) {
    this.boundRect = new Rectangle (x, y, w, h) ;
  }

  /**
   * Set the bound rectangle.
   * @param Copy the given rect and it as the bound rect.
   */
  public void setBinding (Rectangle binds) {
    this.boundRect = new Rectangle (binds) ;
  }

  /**
   * Place the widget at its place.
   */
  public void bind () {
    this.setBounds (this.boundRect) ;
  }
}
