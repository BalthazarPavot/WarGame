
package wargame;

/**
 * Singleton class. <br />
 * Defines error codes. <br />
 * Defines methods that allow to exit the game giving an error message, or just logging them if these are not
 * fatal errors.
 * 
 * @author Balthazar Pavot
 */
public class ErrorManager {

	static int EARLY_TERMINATION_ERROR = 1;
	static int UNKNOWN_ERROR = 255;

	private static ErrorManager singleton = new ErrorManager();

	/**
	 * Getter of the singleton error manager.
	 */
	public static ErrorManager get() {
		return singleton;
	}

	/**
	 * Exit the program in an early state, before the error manager has been initialised.
	 */
	public static void earlyTermination() {
		ErrorManager.earlyTermination("Unknown error");
	}

	/**
	 * Exit the program in an early state, before the error manager has been initialised, writing the given
	 * message.
	 * 
	 * @param message
	 */
	public static void earlyTermination(String message) {
		ErrorManager.earlyTermination(message, EARLY_TERMINATION_ERROR);
	}

	/**
	 * Exit the program in an early state, before the error manager has been initialised, writing the given
	 * message, giving the given error code.
	 * 
	 * @param message
	 * @param errorCode
	 */
	public static void earlyTermination(String message, int errorCode) {
		System.err.println(message);
		System.exit(errorCode);
	}

	/**
	 * Exit the program with unknown error.
	 */
	public void exitError() {
		exitError("Unknown error");
	}

	/**
	 * Exit the program with giving the given message.
	 * 
	 * @param message
	 */
	public void exitError(String message) {
		exitError(message, UNKNOWN_ERROR);
	}

	/**
	 * Exit the program with the given error code, writing the given message.
	 * 
	 * @param message
	 * @param errorCode
	 */
	public void exitError(String message, int errorCode) {
		System.err.println(message);
		System.exit(errorCode);
	}

}
