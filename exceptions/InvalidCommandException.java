package exceptions;

/**
 * Invalid command error for when the user enters an invalid command (ie. the
 * command does not exist and/or its arguments are invalid).
 */
public class InvalidCommandException extends Exception {

  /**
   * Initialize InvalidCommandException.
   */
  public InvalidCommandException() {
    super();
  }

  /**
   * Initialize InvalidCommandException with a message.
   * 
   * @param message message regarding the error
   */
  public InvalidCommandException(String message) {
    super("Invalid command: " + message);
  }

  /**
   * Initialize InvalidCommandException with a cause.
   * 
   * @param cause reason for error
   */
  public InvalidCommandException(Throwable cause) {
    super(cause);
  }

  /**
   * Initalize InvalidCommandException with both a message and a cause.
   * 
   * @param message message regarding the error
   * @param cause reason for error
   */
  public InvalidCommandException(String message, Throwable cause) {
    super("Invalid command: " + message, cause);
  }

}
