package commands;

import java.util.ArrayList;
import java.util.Arrays;

import sys_files.*;

/**
 * Represents a command output redirection object.
 */
public class Redirector {

  private String toWrite; // Output to be redirected.
  private String action; // Redirection action (overwrite/append).
  private String outfile; // Outfile for output.

  // FileSystem instance.
  private FileSystem disc = FileSystem.getInstance();

  /**
   * Initializes new Redirector with output to be redirected, redirection
   * action, and a file to redirect to.
   * 
   * @param toRedirect output to be redirected.
   * @param action redirection action (overwrite/append).
   * @param file outfile to output.
   */
  public Redirector(String toRedirect, String action, String file) {
    this.toWrite = toRedirect;
    this.action = action;
    this.outfile = file;
  }

  /**
   * Sets the output to be redirected to newOutput.
   * 
   * @param newOutput new command output to be redirected.
   */
  public void setToWrite(String newOutput) {
    this.toWrite = newOutput;
  }

  /**
   * Returns whether or not arguments are valid for this Redirector object.
   * 
   * @param arguments array of arguments to be validated.
   * @return a ValidationResults object that is valid if the specified array of
   *         arguments is valid for this Redirector object, otherwise a
   *         ValidationResults object that is invalid and contains an error
   *         message explaining as to why.
   */
  public static ValidationResults validForRedirection(String[] arguments) {
    final int MAX_NUM_OF_ARGUMENTS = 2;

    // Checking if the number of arguments is correct.
    if (arguments.length == MAX_NUM_OF_ARGUMENTS) {

      // Checking if the first argument is an overwrite or an append symbol.
      if (!(">".equals(arguments[0]) || ">>".equals(arguments[0]))) {
        return new ValidationResults(false, "First argument must be > or >>");
      }
      // Checking if the outfile is a text file.
      if (FileSystem.getInstance().fileAt(arguments[1]) instanceof Directory) {
        return new ValidationResults(false, arguments[1] + ": Is a directory.");
      }
      // Checking if the outfile exists.
      ValidationResults result =
          Checker.validatePath(FileSystem.getInstance().retrieveParentPath(
              arguments[1]));
      if (!result.isValid()) {
        return new ValidationResults(false, result.getMessage());
      }
      return new ValidationResults(true, null);
    } else {
      return new ValidationResults(false,
          "To redirect to a file, you must provide a redirection action and an "
              + "outfile.");
    }
  }

  /**
   * Redirects command output to a file using the appropriate redirection action
   * (overwrite/append).
   */
  public void redirect() {
    // Checking if outfile exists.
    if (disc.contains(this.outfile)) {
      // Getting outfile and writing to it.
      TextFile redirectTo = (TextFile) disc.fileAt(this.outfile);
      this.write(redirectTo, this.toWrite);
    } else {
      try {
        // Writing to a new file.
        TextFile redirectTo = new TextFile(this.outfile);
        disc.addFile(redirectTo, redirectTo.getParentPath());
        this.write(redirectTo, this.toWrite);
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  /**
   * Returns whether or not command output will overwrite outfile's contents.
   */
  private boolean overwrite() {
    return ">".equals(this.action);
  }

  /**
   * Returns whether or not command output will append to outfile's contents.
   */
  private boolean append() {
    return ">>".equals(this.action);
  }

  /**
   * Writes s to file.
   */
  private void write(TextFile file, String s) {
    if (this.overwrite()) {
      file.setContents(s);
    } else if (this.append()) {
      String newContents = file.fileContents() + "\n" + s;
      file.setContents(newContents);
    }
  }

  /**
   * Returns whether or not the following set of arguments contain a redirection
   * action (overwrite/append).
   * 
   * @param arguments array of command arguments.
   * @return true if arguments contain a redirection action, otherwise false.
   */
  public static boolean isRedirectable(String[] arguments) {
    // Converting to an ArrayList before checking.
    ArrayList<String> asList = new ArrayList<String>(Arrays.asList(arguments));
    return (asList.contains(">") || asList.contains(">>"));
  }

}
