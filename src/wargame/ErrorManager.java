

package wargame ;


/**
 * Singleton class that defines error codes. <br />
 * Defines methods that allow to exit the game giving an error message, or just
 * logging them if these are not fatal errors.
 */
public class ErrorManager {

  static int EARLY_TERMINATION_ERROR = 1 ;
  static int UNKNOWN_ERROR = 255 ;

  private static ErrorManager singleton = new ErrorManager () ;

  /**
   * Set internal variables to make it singleton.
   */
  public static ErrorManager get () {
    return singleton ;
  }

  /**
   * Exit the program in an early state, before the error manager has
   * been initialized.
   */
  public static void earlyTermination () {
    ErrorManager.earlyTermination ("Unknown error") ;
  }

  /**
   * Exit the program in an early state, before the error manager has
   * been initialized, writting the given message.
   */
  public static void earlyTermination (String message) {
    ErrorManager.earlyTermination (message, EARLY_TERMINATION_ERROR) ;
  }

  /**
   * Exit the program in an early state, before the error manager has
   * been initialized, writting the given message, giving the given error code.
   */
  public static void earlyTermination (String message, int errorCode) {
    System.err.println (message) ;
    System.exit (errorCode) ;
  }

  /**
   * Exit the program with unknown error.
   */
  public void exitError () {
    exitError ("Unknown error") ;
  }

  /**
   * Exit the program with giving the given message.
   */
  public void exitError (String message) {
    exitError (message, UNKNOWN_ERROR) ;
  }

  /**
   * Exit the program with the given error code, writting the given message.
   */
  public void exitError (String message, int errorCode) {
    System.err.println (message) ;
    System.exit (errorCode) ;
  }

}
