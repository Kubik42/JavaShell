package commands;

import sys_files.FileSystem;

/**
 * Representation of a command that displays the current working directory path.
 */
public class CommandPWD extends Command {

  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance.

  /**
   * Initializes new CommandPWD with no arguments.
   */
  public CommandPWD() {
    super();
  }

  /**
   * Initializes new CommandPWD with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandPWD(String[] arguments) {
    super(arguments);
  }
  
  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "pwd";
  }

  /**
   * Returns the documentation for this CommandPWD object.
   */
  @Override
  public String commandDocumentation() {
    return ("Displays the absolute path of the current working directory.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandPWD object.
   * 
   * CommandPWD does not take in any arguments.
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
    // Checking if output is to be redirected.
    if (this.getRedirector() != null) {
      Redirector r = this.getRedirector();
      r.setToWrite(this.executeReturn()); // Adding output to Redirector.
      r.redirect(); // Redirecting to file.
    } else {
      // Printing out current directory path.
      System.out.println(this.executeReturn());
    }
  }

  /**
   * Returns command output.
   * 
   * @return path of the current directory.
   */
  public String executeReturn() {
    return disc.getPath();
  }
}
