package commands;

/**
 * Representation of a command that displays the documentation for other
 * commands.
 */
public class CommandMAN extends Command {

  private Command toDocument; // Command object to print documentation for.

  /**
   * Initializes new CommandMAN with no arguments.
   */
  public CommandMAN() {
    super();
  }

  /**
   * Initializes new CommandMAN with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandMAN(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "man";
  }

  /**
   * Returns the documentation for this CommandMAN object.
   */
  @Override
  public String commandDocumentation() {
    return ("Displays the documentation of various commands.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandMAN object.
   * 
   * Arguments must be in form: man CMD, where CMD is another command.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 1;
    if (arguments.length == MANDATORY_NUM_OF_ARGUMENTS) {
      // Check if CommandMAN was called on itself (Checker doesn't need to
      // validate CommandMAN twice).
      if (arguments[0].equals(this.getCommandName())) {
        this.toDocument = this; // man will document itself.
      } else {
        // Man will document this command object.
        try {
          this.toDocument = Checker.getCommand(arguments[0], true);
        } catch (Exception ex) {
          return new ValidationResults(false, "No manual entry for "
              + arguments[0]); // CMD does not exist.
        }
      }
      return new ValidationResults(true, null);
    }
    return new ValidationResults(false, "Requires "
        + MANDATORY_NUM_OF_ARGUMENTS + " argument.");
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    // Checking if output is to be redirected.
    if (this.getRedirector() != null) {
      Redirector r = this.getRedirector();
      r.setToWrite(this.executeReturn()); // Adding output to Redirector.
      r.redirect(); // Redirecting to file.
    } else {
      // Printing out documentation.
      System.out.println(this.executeReturn());
    }
  }

  /**
   * Returns command output.
   * 
   * @return contents of the specified file.
   */
  public String executeReturn() {
    return (this.toDocument.toString());
  }

}
