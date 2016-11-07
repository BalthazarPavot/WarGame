

package wargame ;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 */
public class GameContext {

  static String defaultConfigPath = "./config.conf" ;
  private ErrorManager errorManager = null ;
  private int width = 0 ;
  private int height = 0 ;
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
   * @return width The value of the current width
   */
  public int getWidth () {
    return width ;
  }

  /**
   * @return width The value of the current height
   */
  public int getHeight () {
    return width ;
  }

  private void loadConf (File confFile) throws IOException, IllegalArgumentException {
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
    loadConf (confFile, confStream, confProperties) ;
    confStream.close () ;
    confStream.close () ;
    confStream = null ;
    confProperties = null ;
    confLoaded = true ;
  }

  private void loadConf (File confFile, FileInputStream confStream,
      Properties confProperties) throws IOException, IllegalArgumentException {
    width = Integer.parseInt (confProperties.getProperty ("width")) ;
    height = Integer.parseInt (confProperties.getProperty ("height")) ;
    checkConf();
  }

  private void checkConf () {
    if (width < 600)
      errorManager.exitError ("The width defined in the conf file is less than 600.\n") ;
    if (height < 400)
      errorManager.exitError ("The height defined in the conf file is less than 400.\n") ;
    if (width > 1900)
      errorManager.exitError ("The width defined in the conf file is more than 1900.\n") ;
    if (height > 1200)
      errorManager.exitError ("The width defined in the conf file is more than 1200.\n") ;
  }

}
