package commands;

/**
 * Representation of a command that displays text.
 */
public class CommandECHO extends Command {

  /**
   * Initializes new CommandECHO with no arguments.
   */
  public CommandECHO() {
    super();
  }

  /**
   * Initializes new CommandECHO with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandECHO(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "echo";
  }

  /**
   * Returns the documentation for this CommandECHO object.
   */
  @Override
  public String commandDocumentation() {
    return ("Displays text on screen.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandECHO object.
   * 
   * Arguments must be in form: echo STRING, where STRING is a some text in
   * string format.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 1;

    if (arguments.length == MANDATORY_NUM_OF_ARGUMENTS) {
      // Checking if str is in string format.
      if (!Checker.isString(arguments[0])) {
        return new ValidationResults(false,
            "String argument must be surrounded by quotation marks.");
      }
      return new ValidationResults(true, null);
    } else {
      return new ValidationResults(false, "Requires "
          + MANDATORY_NUM_OF_ARGUMENTS + " arguments.");
    }
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
      // Printing out string.
      System.out.println(this.executeReturn());
    }
  }

  /**
   * Returns command output.
   * 
   * @return string to be displayed.
   */
  public String executeReturn() {
    // Removing any quotation marks around the string.
    return this.getArguments()[0].replaceAll("\"", "");
  }

}
