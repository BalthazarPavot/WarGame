
package wargame.widgets ;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import wargame.basic_types.Position;


public class ImageWidget extends JLabel implements GameWidget {

  private static final long serialVersionUID = 1L;
  protected Rectangle boundRect ;
  protected BufferedImage image;
  protected Color bgColor ;

  public static BufferedImage loadImage (String path) {
    BufferedImage image ;

    try {
      image = ImageIO.read (new File (path)) ;
    } catch (IOException ex) {
      image = null ;
    }
    return image ;
  }

  public ImageWidget (int x, int y, int w, int h) {
    this (new Rectangle (x, y, w, h)) ;
  }

  public ImageWidget (Rectangle boundRect) {
    this.boundRect = boundRect ;
    // the background is set as transparent
    this.bgColor = new Color (0, 0, 0, 0) ;
  }

  public ImageWidget (int x, int y, int w, int h, String path) {
    this (new Rectangle (x, y, w, h), path) ;
  }

  public ImageWidget (Rectangle boundRect, String path) {
    this (boundRect, ImageWidget.loadImage (path)) ;
  }

  public ImageWidget (int x, int y, int w, int h, BufferedImage image) {
    this (new Rectangle (x, y, w, h), image) ;
  }

  public ImageWidget (Rectangle boundRect, BufferedImage image) {
    super (new ImageIcon(image)) ;
//    this (boundRect) ;
    this.boundRect = boundRect ;
    this.image = image ;
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
   * Set the position of the bound rectangle.
   * @param position The position to go on
   */
  public void setPosition (Position position) {
    this.boundRect = new Rectangle (position.getX (), position.getY (),
    		this.boundRect.width, this.boundRect.height) ;
  }

  /**
   * Return widget's position
   */
  public Position getPosition () {
	  return new Position (this.boundRect.x, this.boundRect.y) ;
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

  /**
   * draw the image at its position
   * @param g Graphics in which display the image.
   */
  public void paintComponent (Graphics g) {
	  this.paintComponent(g, 1) ;
  }

  /**
   * draw the image at its position
   * @param g Graphics in which display the image.
   * @param zoom The zoom image at which the image must be.
   */
  public void paintComponent (Graphics g, int zoom) {
      g.drawImage (image, 0, 0,
    		  this.boundRect.width * zoom,
    		  this.boundRect.height * zoom, this.bgColor, this) ;
  }

}
