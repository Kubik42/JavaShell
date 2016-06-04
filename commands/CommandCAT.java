package commands;

import sys_files.File;
import sys_files.FileSystem;
import sys_files.TextFile;

/**
 * Representation of a command that displays the contents of files and
 * directories.
 */
public class CommandCAT extends Command {

  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance.

  /**
   * Initializes new CommandCAT with no arguments.
   */
  public CommandCAT() {
    super();
  }

  /**
   * Initializes new CommandCAT with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandCAT(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "cat";
  }

  /**
   * Returns the documentation for this CommandCAT object.
   */
  @Override
  public String commandDocumentation() {
    return ("Displays the contents of files.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandCAT object.
   * 
   * Arguments must be in form: cat FILE, where FILE is a File object that
   * exists in the file system.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 1;

    // Checking if the number of arguments is correct.
    if (arguments.length == MANDATORY_NUM_OF_ARGUMENTS) {
      // Checking if file exists.
      if (!disc.contains(arguments[0])) {
        return new ValidationResults(false, "No such file or directory");
      } else {
        // Checking if file is a text file.
        if (!(disc.fileAt(arguments[0]) instanceof TextFile)) {
          return new ValidationResults(false, arguments[0]
              + ": Is not a text file.");
        }
        return new ValidationResults(true, null);
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
    String contents = this.executeReturn(); // File contents.

    // Checking if output is to be redirected.
    if (this.getRedirector() != null) {
      Redirector r = this.getRedirector();
      r.setToWrite(contents); // Adding output to Redirector.
      r.redirect(); // Redirecting to file.
    } else {
      // Printing out contents.
      if (!contents.isEmpty()) {
        System.out.println(contents);
      }
    }
  }

  /**
   * Returns command output.
   * 
   * @return contents of the specified file.
   */
  public String executeReturn() {
    File toShow = disc.fileAt(this.getArguments()[0]);
    return toShow.fileContents();
  }
}
