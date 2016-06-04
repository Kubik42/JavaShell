package sys_files;

import java.util.ArrayList;
import java.util.Arrays;

import commands.Redirector;
import commands.ValidationResults;
import exceptions.InvalidRedirectorException;

/**
 * Represents a builder object that can transform, create, or edit other
 * objects.
 */
public class Builder {

  /**
   * Initializes new Builder. Private in case someone tries to create a Builder
   * instance.
   */
  private Builder() {};

  /**
   * Returns an array representation of path where all separators (/) have been
   * removed and each element in the array is a file from path.
   * 
   * @param path some path.
   * @return path in array format where each element is a file name.
   */
  public static String[] pathToArray(String path) {

    // Separating path on each forward slash (/).
    ArrayList<String> inParts =
        new ArrayList<String>(Arrays.asList(path.split("/")));

    // Removing any empty or null elements; since calling split will leave an
    // empty element for the root directory.
    inParts.removeAll(Arrays.asList("", null));

    // Converting ArrayList into an array.
    String[] pathAsArray = new String[inParts.size()];
    return inParts.toArray(pathAsArray);
  }

  /**
   * Rebuilds path from an array.
   * 
   * @param pathAsArray some path represented by an array of file names.
   * @return some path rebuilt from array elements in pathAsArray.
   */
  public static String rebuildPath(String[] pathAsArray) {
    String path = "";
    for (String pathItem : pathAsArray) {
      path += ("/" + pathItem);
    }
    return path;
  }

  /**
   * Returns an array of arguments that contain everything before the
   * redirection symbol (ie. overwrite/append symbol ">" or ">>").
   * 
   * @param arguments array of command arguments.
   * @return array of pre-redirection arguments.
   */
  public static String[] buildPreRedirectionArguments(String[] arguments) {
    // Creating an ArrayList to add any ore-redirection arguments to.
    ArrayList<String> preRedirectionArgs = new ArrayList<String>();

    // Iterating through every argument and storing all pre-redirection ones.
    for (String args : arguments) {
      if (!(">".equals(args) || ">>".equals(args))) {
        preRedirectionArgs.add(args);
      } else {
        return preRedirectionArgs
            .toArray(new String[preRedirectionArgs.size()]);
      }
    }
    // Converting back to array.
    return preRedirectionArgs.toArray(new String[preRedirectionArgs.size()]);
  }

  /**
   * Returns a Redirector object built from output and arguments.
   * 
   * @param output string that the Redirector object will redirect.
   * @param arguments array of arguments for the Redirector object.
   * @return a Redirector object built from output and arguments.
   * @throws InvalidRedirectorException if a Redirector cannot be created with
   *         arguments.
   */
  public static Redirector buildRedirector(String output, String[] arguments)
      throws InvalidRedirectorException {

    // Validating arguments, throwing an exception if they are invalid.
    ValidationResults res = Redirector.validForRedirection(arguments);
    if (res.isValid()) {
      return new Redirector(output, arguments[0], arguments[1]);
    } else {
      throw new InvalidRedirectorException(res.getMessage());
    }
  }

  /**
   * Returns an array of arguments that contain everything after and including
   * the redirection symbol (ie. overwrite/append symbol ">" or ">>").
   * 
   * @param arguments array of command arguments.
   * @return array of redirection arguments.
   */
  public static String[] buildRedirectionArguments(String[] arguments) {
    // Checking if arguments contain either one of the redirection symbols.
    if (Redirector.isRedirectable(arguments)) {
      // Converting array to ArrayList.
      ArrayList<String> originalArgs =
          new ArrayList<String>(Arrays.asList(arguments));

      // Finding the index of a redirection symbol.
      int index = -1;
      if (originalArgs.contains(">")) {
        index = originalArgs.indexOf(">");
      } else if (originalArgs.contains(">>")) {
        index = originalArgs.indexOf(">>");
      }
      // Returning everything after and including the redirection symbol.
      return Arrays.copyOfRange(arguments, index, arguments.length);
    }
    // If there is no redirection symbol.
    return new String[0];
  }

}
