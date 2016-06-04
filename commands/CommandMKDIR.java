package commands;

import sys_files.FileSystem;
import sys_files.Directory;
import exceptions.InvalidFileNameException;
import exceptions.InvalidPathException;

/**
 * Representation of a command that creates directories.
 */

public class CommandMKDIR extends Command {

  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance.

  /**
   * Initializes new CommandMKDIR with no arguments.
   */
  public CommandMKDIR() {
    super();
  }

  /**
   * Initializes new CommandMKDIR with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandMKDIR(String[] arguments) {
    super(arguments);
  }
  
  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "mkdir";
  }

  /**
   * Returns the documentation for this CommandMKDIR object.
   */
  @Override
  public String commandDocumentation() {
    return ("Creates directories.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandMKDIR object.
   * 
   * Arguments must be in form: mkdir DIR..., where DIR is a Directory object to
   * be created. CommandMKDIR takes in any number of arguments as long as they
   * are valid paths and directories are not created before others.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 1;

    // Checking if the number of arguments is correct.
    if (arguments.length >= MANDATORY_NUM_OF_ARGUMENTS) {
      return new ValidationResults(true, null);
    } else {
      return new ValidationResults(false, "Require at least "
          + MANDATORY_NUM_OF_ARGUMENTS + " arguments");
    }
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    String[] arguments = this.getArguments(); // Command arguments.

    for (int i = 0; i < arguments.length; i++) {
      ValidationResults res = null; // Validation result for each directory.

      // Checking if directory already exists or if the user tried to create a
      // directory without the parent directory.
      if (disc.contains(arguments[i])) {
        res = new ValidationResults(false, "Cannot create directory: "
                + arguments[i] + ": Directory already exists.");
      } else if (!disc.contains(disc.retrieveParentPath(arguments[i]))) {
        res = new ValidationResults(false, "Cannot create directory "
                + arguments[i] + ": No such file or directory.");
      } else {
        res = new ValidationResults(true, null);
      }
      // Checking if path was valid.
      if (res.isValid()) {
        try {
          // Creating specific directory.
          Directory directory = new Directory(arguments[i]);
          disc.addFile(directory, directory.getParentPath());
        } catch (InvalidFileNameException ex) { // If directory name is invalid.
          System.out.println(ex.getMessage());
        } catch (InvalidPathException ex) { // If path for directory is invalid.
          System.out.println(ex.getMessage());
        }
      } else {
        // Error message for each invalid path.
        System.out.println(res.getMessage());
      }
    }
  }

}
