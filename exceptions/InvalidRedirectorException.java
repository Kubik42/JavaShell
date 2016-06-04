package exceptions;

/**
 * Invalid redirector error for when someone tries to create a Redirector with
 * an invalid set of arguments. 
 */
public class InvalidRedirectorException extends Exception {

  /**
   * Initialize InvalidRedirectorException.
   */
  public InvalidRedirectorException() {
    super();
  }

  /**
   * Initialize InvalidRedirectorException with a message.
   * 
   * @param message message regarding the error
   */
  public InvalidRedirectorException(String message) {
    super("Invalid Redirector: " + message);
  }

  /**
   * Initialize InvalidRedirectorException with a cause.
   * 
   * @param cause reason for error
   */
  public InvalidRedirectorException(Throwable cause) {
    super(cause);
  }

  /**
   * Initalize InvalidRedirectorException with both a message and a cause.
   * 
   * @param message message regarding the error
   * @param cause reason for error
   */
  public InvalidRedirectorException(String message, Throwable cause) {
    super("Invalid Redirector: " + message, cause);
  }

}
