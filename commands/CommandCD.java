package commands;

import sys_files.Directory;
import sys_files.File;
import sys_files.FileSystem;

/**
 * Representation of a command that changes the current directory.
 */
public class CommandCD extends Command {

  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance

  /**
   * Initializes new CommandCD with no arguments.
   */
  public CommandCD() {
    super();
  }

  /**
   * Initializes new CommandCAT with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandCD(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "cd";
  }

  /**
   * Returns the documentation for this CommandCD object.
   */
  @Override
  public String commandDocumentation() {
    return ("Changes the shell's current working directory to the one "
        + "specified.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandCD object.
   * 
   * Arguments must be in form: CD DIR, where DIR is a Directory object that
   * exists in the file system.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 1;

    // Checking if the number of arguments is correct.
    if (arguments.length == MANDATORY_NUM_OF_ARGUMENTS) {
      final String FILE = arguments[0]; // New current directory.

      // Checking if file exists. File must also be a directory.
      if ((disc.contains(FILE))) {
        if (disc.fileAt(FILE) instanceof Directory) {
          return new ValidationResults(true, null);
        } else {
          return new ValidationResults(false, FILE + " is not a directory");
        }
      } else {
        return new ValidationResults(false,
            "No such file or directory or path " + "does not exist.");
      }
    } else {
      return new ValidationResults(false, "Requires "
          + MANDATORY_NUM_OF_ARGUMENTS + " argument.");
    }
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    File newFile = disc.fileAt(this.getArguments()[0]);
    disc.setCurrentDirectory((Directory) newFile);
  }

}
