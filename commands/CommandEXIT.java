package commands;

/**
 * Representation of a command that quits the program.
 */
public class CommandEXIT extends Command {
  
  /**
   * Initializes new CommandEXIT with no arguments.
   */
  public CommandEXIT() {
    super();
  }

  /**
   * Initializes new CommandEXIT with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandEXIT(String[] arguments) {
    super(arguments);
  }
  
  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "exit";
  }

  /**
   * Returns the documentation for this CommandEXIT object.
   */
  @Override
  public String commandDocumentation() {
    return ("Quits the program.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandEXIT object.
   * 
   * CommandEXIT does not take in any arguments.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 0;
    if (arguments.length == MANDATORY_NUM_OF_ARGUMENTS) {
      return new ValidationResults(true, null);
    } else {
      return new ValidationResults(false, "Does not take in any arguments.");
    }
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    System.exit(0);
  }

}
