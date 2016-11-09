

package wargame ;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 */
public class GameContext {

  static String TITLE = "War Game" ;
  private static String defaultConfigPath = "./config.conf" ;
  private ErrorManager errorManager = null ;
  private int width = 800 ;
  private int height = 600 ;
  private boolean confLoaded = false ;

  public GameContext (ErrorManager errorManager) {
    if (errorManager == null)
      ErrorManager.earlyTermination (
        "Could not create the game context without the error manager.") ;
    this.errorManager = errorManager ;
  }

  /**
   * Load the default config file.
   */
  public void loadConf () {
    File confFile = null;

    if (confLoaded == false) {
      confFile = new File (defaultConfigPath) ;
      try {
        loadConf (confFile) ;
      } catch (IOException|IllegalArgumentException e) {
        errorManager.exitError ("Could not load config file. Verify it, please.\n") ;
      }
      confFile = null ;
    }
  }

  /**
   * @return int The value of the current width
   */
  public int getWidth () {
    return width ;
  }

  /**
   * @return int The value of the current height
   */
  public int getHeight () {
    return height ;
  }

  /**
   * Give the dimention of the screen
   * @return Dimension the screen dimension
   */
  public Dimension getDimension () {
    return new Dimension (getWidth (), getHeight ()) ;
  }

  /**
   * @return int The middle of the width of the screen
   */
  public int centerWidth () {
    return width / 2 ;
  }

  /**
   * @return the middle of the height of the screen
   */
  public int centerHeight () {
    return height / 2 ;
  }

  /**
   * @return ErrorManager The error manager of the game.
   */
  public ErrorManager getErrorManager () {
    return errorManager ;
  }

  /**
   * Open the config file and load the configuration.
   * @param confFile The file that contains the configuration.
   */
  private void loadConf (File confFile)
      throws IOException, IllegalArgumentException {
    FileInputStream confStream = null ;
    Properties confProperties = null ;

    if (!confFile.exists ())
      errorManager.exitError (
        String.format ("The conf file \"%s\" is missing.\n",
          confFile.getPath ())) ;
    if (confFile.isDirectory ())
      errorManager.exitError (
        String.format("The conf file \"%s\" is a directory.\n",
          confFile.getPath ())) ;
    confStream = new FileInputStream (confFile.getPath ()) ;
    confProperties = new Properties () ;
    confProperties.load (confStream) ;
    loadConf (confProperties) ;
    confStream.close () ;
    confStream.close () ;
    confStream = null ;
    confProperties = null ;
    confLoaded = true ;
  }

  /**
   * Read the properties of the conf file and extract the config.
   * @param confProperties The properties object of the conf file. 
   */
  private void loadConf (Properties confProperties)
      throws IOException, IllegalArgumentException {
    width = Integer.parseInt (confProperties.getProperty ("width")) ;
    height = Integer.parseInt (confProperties.getProperty ("height")) ;
    checkConf();
  }

  /**
   * Check the current configuration, like the max size of the screen.
   */
  private void checkConf () {
    if (width < 600)
      errorManager.exitError ("The width defined in the conf file is less than 600.\n") ;
    if (height < 400)
      errorManager.exitError ("The height defined in the conf file is less than 400.\n") ;
    if (width > 1900)
      errorManager.exitError ("The width defined in the conf file is more than 1900.\n") ;
    if (height > 1200)
      errorManager.exitError ("The height defined in the conf file is more than 1200.\n") ;
  }

}
