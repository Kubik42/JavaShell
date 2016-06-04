package commands;

import sys_files.*;

/**
 * Representation of a command that displays the contents of directories. 
 */
public class CommandLS extends Command {

  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance.
  private String errors = ""; // Contains stdErrors.

  /**
   * Initializes new CommandLS with no arguments.
   */
  public CommandLS() {
    super();
  }

  /**
   * Initializes new CommandLS with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandLS(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "ls";
  }

  /**
   * Returns the documentation for this CommandLS object.
   */
  @Override
  public String commandDocumentation() {
    return ("Displays the contents of directories. If no paths are given,\n"
        + "displays the contents of the current working directory. For all\n"
        + "files specificecd by path, displays the name of the file only.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandLS object.
   * 
   * Arguments must be in form: ls [PATH...], where PATH is a path to some File
   * object. CommandLS takes in any combination and number of arguments. If
   * entered paths do not exist, validation still passes and error messages are
   * printed for each invalid path.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MIN_NUM_OF_ARGUMENTS = 0;
    if (arguments.length >= MIN_NUM_OF_ARGUMENTS) {
      return new ValidationResults(true, null);
    } else {
      return new ValidationResults(false, "Requires at least"
          + MIN_NUM_OF_ARGUMENTS + " arguments.");
    }
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    // Contents of all files.
    String contents = this.executeReturn();

    // Checking if output is to be redirected.
    if (this.getRedirector() != null) {
      Redirector r = this.getRedirector();
      r.setToWrite(contents); // Adding output to Redirector.
      r.redirect(); // Redirecting to file.
    } else {
      // Printing out contents.
      if (!(contents.isEmpty())) {
        System.out.println(contents);
      }
    }
    // Printing out any stdErrors.
    if (!this.errors.isEmpty()) {
      if (!contents.isEmpty()) { // Adding a blank line when needed.
        System.out.println();
      }
      System.out.println(this.getErrors());
    }
  }

  /**
   * Returns command output.
   * 
   * @return contents of the specified file.
   */
  public String executeReturn() {
    String[] arguments = this.getArguments(); // Command arguments.

    // Checking if ls was called on current directory.
    if (arguments.length == 0) {
      return this.runOnCurrentDir();
    } else {
      return this.runOnDir();
    }
  }

  /**
   * Runs ls on current directory.
   */
  private String runOnCurrentDir() {
    // Checking if command is recursive.
    if (this.isRecursive()) {
      return this.traverse(disc.getRoot(), 99).trim();
    } else {
      return disc.getCurrentDirectory().fileContents();
    }
  }

  /**
   * Runs ls on a non-current directory.
   */
  private String runOnDir() {
    String[] arguments = this.getArguments(); // Command arguments.

    // Validating paths.
    ValidationResults[] results = Checker.validatePaths(arguments);
    String accum = ""; // Holds descriptions of all files/directories.

    for (int i = 0; i < arguments.length; i++) {
      // Checking if this particular path was valid.
      if (results[i].isValid()) {
        File file = disc.fileAt(arguments[i]);

        // Checking if file is a text file.
        if (file instanceof TextFile) {
          accum += file.getName();
        } else {
          // Checking if command is recursive.
          if (this.isRecursive()) {
            // NOTE: depth > 99 might result in a stack overflow.
            accum += this.traverse((Directory) file, 99);
          } else {
            // Checking if ls was called on a single directory.
            if (arguments.length == 1) {
              accum += file.fileContents(); // Don't need to display dir name.
            } else {
              // Depth of 0, no need to recurse.
              accum += this.traverse((Directory) file, 0);
            }
          }
        }
      } else {
        // Storing the error message for each invalid path.
        this.errors += ("ls: " + results[i].getMessage() + "\n");
      }
    }
    return accum.trim();
  }

  /**
   * Recursively traverses file as long as depth is > 0. Returns the contents of
   * directories and names of files.
   */
  private String traverse(Directory dir, int depth) {
    // File is a directory, checking if depth allows for further traversing.
    String contents = this.dirDescription(dir);
    if (depth > 0) {
      for (File f : (dir).getContents()) {
        if (f instanceof Directory) {
          depth--; // Decreasing depth with each traverse.
          contents += (this.traverse((Directory) f, depth));
        }
      }
    }
    return contents;
  }

  /**
   * Returns any stdErrors collected when running executeReturn().
   * 
   * @return any stdErrors.
   */
  public String getErrors() {
    return this.errors.trim();
  }

  /**
   * Returns the description of a directory in form: 
   *    <directory name>:
   *    <directory contents> (if any)
   */
  private String dirDescription(Directory dir) {
    if (!(dir.isEmpty())) {
      return (dir.getPath() + ":\n" + dir.fileContents() + "\n\n");
    } else {
      return dir.getPath() + ":\n\n";
    }
  }

}
