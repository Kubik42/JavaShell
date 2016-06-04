package exceptions;

/**
 * Invalid file name error for when someone tries to create a file using special
 * characters.
 */
public class InvalidFileNameException extends Exception {

  /**
   * Initialize InvalidFileNameException.
   */
  public InvalidFileNameException() {
    super();
  }

  /**
   * Initialize InvalidFileNameException with a message.
   * 
   * @param message message regarding the error
   */
  public InvalidFileNameException(String message) {
    super("Invalid file name: " + message);
  }

  /**
   * Initialize InvalidFileNameException with a cause.
   * 
   * @param cause reason for error
   */
  public InvalidFileNameException(Throwable cause) {
    super(cause);
  }

  /**
   * Initalize InvalidFileNameException with both a message and a cause.
   * 
   * @param message message regarding the error
   * @param cause reason for error
   */
  public InvalidFileNameException(String message, Throwable cause) {
    super("Invalid file name: " + message, cause);
  }

}
