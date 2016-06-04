package commands;

/**
 * Representation of a command that moves or renames files/directories to their
 * specified destinations/names.
 */
public class CommandMV extends CommandCP {

  /**
   * Initializes new CommandMV with no arguments.
   */
  public CommandMV() {
    super();
  }

  /**
   * Initializes new CommandMV with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandMV(String[] arguments) {
    super(arguments);
  }
  
  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "mv";
  }

  /**
   * Returns the documentation for this CommandMV object.
   */
  @Override
  public String commandDocumentation() {
    return ("Moves or renames files and directories to their specified "
        + "destinations.");
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    this.copyMove(false); // Moves file.
  }

}
