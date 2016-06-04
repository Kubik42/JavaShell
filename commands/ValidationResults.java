package commands;

/**
 * Represents a command-validation results object.
 */
public class ValidationResults {

  private boolean valid = false; // Whether command is valid.
  private String message; // Message in regards to validation results.

  /**
   * Initializes new ValidationResults with a validation status and an error
   * message.
   * 
   * @param valid validation status.
   * @param message error message.
   */
  public ValidationResults(boolean valid, String message) {
    this.valid = valid;
    this.message = message;
  }

  /**
   * Returns whether or not the command was valid.
   * 
   * @return true if a Command object was valid, otherwise false.
   */
  public boolean isValid() {
    return valid;
  }

  /**
   * Returns an error message describing as to why some command was invalid.
   * 
   * @return error message for some invalid Command object.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns whether or not this ValidationResults object is equivalent to
   * other.
   */
  @Override
  public boolean equals(Object other) {
    boolean result = false;
    if (other instanceof ValidationResults) {
      result = this.isValid() == ((ValidationResults) other).isValid();
      if (this.getMessage() != null) {
        result =
            this.getMessage().equals(((ValidationResults) other).getMessage());
      }
    }
    return result;
  }
}
