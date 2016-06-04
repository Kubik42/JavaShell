package commands;

import sys_files.History;

/**
 * Representation of a command that recalls user input from History and executes
 * it.
 */
public class CommandEXCLAIM extends Command {

  private History hist = History.createHistoryInstance(); // History instance

  /**
   * Initializes new CommandEXCLAIM with no arguments.
   */
  public CommandEXCLAIM() {
    super();
  }

  /**
   * Initializes new CommandEXCLAIM with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandEXCLAIM(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "!";
  }

  /**
   * Returns the documentation for this CommandEXCLAIM object.
   */
  @Override
  public String commandDocumentation() {
    return ("Recalls any of the commands in History and execute the command");
  }

  /**
   * Returns whether or not arguments are valid for this CommandEXCLAIM object.
   * 
   * Arguments must be in form: ! X, where X is an integer that is greater than
   * or equal to 1 and less than or equal to the size of History
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    // the number of arguments for valid command
    final int NUM_OF_ARGUMENTS = 1;
    // checking if the number of arguments is 1
    if (arguments.length == NUM_OF_ARGUMENTS) {
      try {
        // checking if the argument is an integer
        int newInt = Integer.parseInt(arguments[0]);
        if (newInt < 1 || newInt > hist.getSize()) { // checking for bounds
          return new ValidationResults(false, "Argument is out of bounds");
        } else {
          return new ValidationResults(true, null);
        }
      } catch (NumberFormatException n) {
        return new ValidationResults(false, "Argument must be an integer");
      }
    } else {
      return new ValidationResults(false, "Requires " + NUM_OF_ARGUMENTS
          + " argument(s)");
    }
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    // retrieving input command
    String commandIssued =
        hist.getSpecificCommand(Integer.parseInt(this.getArguments()[0]) - 1);
    try {
      Command cmd = Checker.getCommand(commandIssued, false);
      cmd.execute(); // Execute command if its valid.
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

}
