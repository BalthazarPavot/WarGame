
package wargame.widgets ;

import java.awt.Rectangle;

public interface GameWidget {


  /**
   * The function to put the widget at its place.
   */
  void bind () ;

  /**
   * Set the position of the bound rectangle.
   * @param x The position in the x axis
   * @param y The position in the y axis
   */
  void setPosition (int x, int y) ;

  /**
   * Set the dimensions of the bound rectangle.
   * @param w The width of the rectangle
   * @param h The height of the rectangle
   */
  void setDimention (int w, int h) ;

  /**
   * Set the bound rectangle.
   * @param x The position in the x axis
   * @param y The position in the y axis
   * @param w The width of the rectangle
   * @param h The height of the rectangle
   */
  void setBinding (int x, int y, int w, int h) ;

  /**
   * Set the bound rectangle.
   * @param Copy the given rect and it as the bound rect.
   */
  void setBinding (Rectangle binds) ;

}
