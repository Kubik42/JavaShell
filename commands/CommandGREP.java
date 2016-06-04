package commands;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sys_files.*;

/**
 * Representation of a command that displays lines in files that match the
 * specified pattern.
 */
public class CommandGREP extends Command {

  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance.
  private String errors = ""; // Contains stdErrors.
  private String pattern; // Regex pattern.

  /**
   * Initializes new CommandGREP with no arguments.
   */
  public CommandGREP() {
    super();
  }

  /**
   * Initializes new CommandGREP with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandGREP(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "grep";
  }

  /**
   * Returns the documentation for this CommandGREP object.
   */
  @Override
  public String commandDocumentation() {
    return ("Displays lines from files that match the pattern. If -R is\n"
        + "supplied, recursively traverses the directory tree and displays\n"
        + "all lines in all files that match the pattern.");
  }

  /**
   * Returns whether or not arguments are valid for this CommandGREP object.
   * 
   * Arguments must be in form: grep [-R] REGEX PATH..., where REGEX is a
   * regular expression (pattern) to be matched, and PATH is some file whose
   * contents are compared with REGEX. If -R is supplied, PATH can also be a
   * directory, whose contents will be recursively traversed in search of files
   * to compare with REGEX.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MIN_NUM_OF_ARGUMENTS = 2;

    // Checking if the number of arguments is correct.
    if (arguments.length >= MIN_NUM_OF_ARGUMENTS) {

      // Checking if the regex is valid.
      String regex = arguments[0];
      if (this.containsRecrusiveFlag(arguments)) {
        regex = arguments[1];
      }
      try {
        Pattern.compile(regex); // Attempting to compile the pattern.

        // Checking if regex is in string format.
        if (Checker.isString(regex)) {
          return new ValidationResults(true, null);
        }
        return new ValidationResults(false,
            "Regex argument must be surrounded by quotation marks.");
      } catch (PatternSyntaxException ex) {
        return new ValidationResults(false, regex + ": Invalid pattern.");
      }

    } else {
      return new ValidationResults(false, "Requires at least "
          + MIN_NUM_OF_ARGUMENTS + " arguments.");
    }
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    String info = this.executeReturn(); // Matched lines.

    // Checking if output is to be redirected.
    if (this.getRedirector() != null) {
      Redirector r = this.getRedirector();
      r.setToWrite(info); // Adding output to Redirector.
      r.redirect(); // Redirecting to file.
    } else {
      // Printing out matches.
      if (!info.isEmpty()) {
        System.out.println(info);
      }
    }
    // Printing out any stdErrors.
    if (!this.errors.isEmpty()) {
      System.out.println(this.getErrors());
    }
  }

  /**
   * Returns command output.
   * 
   * @return the lines that match the specific pattern.
   */
  public String executeReturn() {
    // Making arguments easier to work with by excluding the regex.
    String[] arguments =
        Arrays.copyOfRange(this.getArguments(), 1, this.getArguments().length);

    // Validation results for all paths.
    ValidationResults[] pathResults = Checker.validatePaths(arguments);

    // Setting the pattern.
    this.setPattern();

    // Iterating though every path.
    String regexMatches = "";
    for (int i = 0; i < arguments.length; i++) {
      // Checking if path was valid.
      if (pathResults[i].isValid()) {
        File file = disc.fileAt(arguments[i]); // File to compare with regex.

        // Checking if command was recursive.
        if (this.isRecursive()) {
          // NOTE: depth > 99 might result in a stack overflow.
          regexMatches += this.traverse(file, 99);
        } else {
          // Depth of 0, no need to recurse.
          regexMatches += (this.traverse(file, 0));
        }
      } else {
        // If path is invalid.
        this.errors += ("grep: " + pathResults[i].getMessage() + "\n");
      }
    }
    return regexMatches.trim();
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
   * Sets the regex pattern.
   */
  private void setPattern() {
    this.pattern = this.getArguments()[0].replaceAll("\"", "");
  }

  /**
   * Recursively traverses file as long as depth is > 0. Returns the lines that
   * matched the specified pattern.
   */
  private String traverse(File file, int depth) {
    // Checking if file is a text file.
    if (file instanceof TextFile) {
      // Matched lines.
      String matched = this.getMatchedLines((TextFile) file);

      // Checking if matched is empty; going to next line if its not.
      if (!matched.isEmpty()) {
        return matched += "\n";
      } else {
        return "";
      }
    } else {
      // File is a directory, checking if depth allows for further traversing.
      String regexMatches = "";
      if (depth > 0) {
        for (File f : ((Directory) file).getContents()) {
          depth--; // Decreasing depth with each traverse.
          regexMatches += (this.traverse(f, depth));
        }
      }
      return regexMatches; // Removing any blank lines.
    }
  }

  /**
   * Returns the lines that matched the specified pattern.
   */
  private String getMatchedLines(TextFile file) {
    // Setting up pattern and matcher to perform matching.
    Pattern p = Pattern.compile(this.pattern);
    Matcher m = p.matcher(""); // Match currently set to empty string.

    // Contents of file to be used by matcher.
    String contents = file.fileContents();
    String matched = "";

    // Scanner to iterate through each line in the file contents.
    Scanner reader = new Scanner(contents);

    // Iterating through the file contents line by line.
    while (reader.hasNext()) {
      String line = reader.nextLine();
      m.reset(line); // Matcher reset to the specific line.
      if (m.find()) { // If the line matches the pattern, save that line.
        matched += (file.getPath() + ": " + line + "\n");
      }
    }
    reader.close(); // Closing the scanner.
    return matched.trim(); // Removing any blank lines.
  }

}
