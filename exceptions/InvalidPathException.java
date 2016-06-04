package exceptions;

/**
 * Invalid path error for when someone attempts to access a path that does not
 * exist in the file system.
 */
public class InvalidPathException extends Exception {

  /**
   * Initialize InvalidPathException.
   */
  public InvalidPathException() {
    super();
  }

  /**
   * Initialize InvalidPathException with a message.
   * 
   * @param message message regarding the error
   */
  public InvalidPathException(String message) {
    super("Invalid path: " + message);
  }

  /**
   * Initialize InvalidPathException with a cause.
   * 
   * @param cause reason for error
   */
  public InvalidPathException(Throwable cause) {
    super(cause);
  }

  /**
   * Initalize InvalidPathException with both a message and a cause.
   * 
   * @param message message regarding the error
   * @param cause reason for error
   */
  public InvalidPathException(String message, Throwable cause) {
    super("Invalid path: " + message, cause);
  }

}
