package commands;

import java.util.Arrays;

import sys_files.Builder;
import sys_files.FileSystem;
import exceptions.InvalidCommandException;
import exceptions.InvalidRedirectorException;

/**
 * Represents a command validation/checker object.
 */
public class Checker {

  // FileSystem instance.
  private static FileSystem disc = FileSystem.getInstance();

  /**
   * Initializes new Checker. Private in case someone tries to create a Checker
   * instance.
   */
  private Checker() {}

  /**
   * Returns a matching Command object if the passed-in command is a valid
   * command (i.e. command matches an existent command and contains correct
   * arguments for it).
   * 
   * @param command some command entered by user.
   * @param forMAN true if the command is to be checked solely for the purpose
   *        of getting its documentation in CommandMAN (arguments are ignored in
   *        this case), otherwise false.
   * @return Command specific object if command is a valid command.
   * @throws InvalidCommandException if command is not a valid command.
   * @throws InvalidRedirectorException if Redirector cannot be created with the
   *         provided set of arguments.
   */
  public static Command getCommand(String command, boolean forMAN)
      throws InvalidCommandException, InvalidRedirectorException {

    // Separating command into a command name and arguments. The regex below
    // splits command on every white space except on those in quotes (for echo).
    String[] inParts = command.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    String commandName = inParts[0];
    String[] arguments = Arrays.copyOfRange(inParts, 1, inParts.length);

    // Attempting to retrieve the specified Command object. If the command cant
    // be found or its arguments are invalid, throws an InvalidCommandException.
    Command commandObject = Checker.getCommandInstance(commandName);

    // Checking if the command is being validated solely for CommandMAN.
    if (!forMAN) {
      // Checking if user tried to redirect command output.
      if (Redirector.isRedirectable(arguments)
          && !"exit".equals(commandObject.getCommandName())) {
        // Building arguments for redirector.
        String[] redirectorArgs = Builder.buildRedirectionArguments(arguments);
        // Validation results for the redirector arguments.
        ValidationResults res = Redirector.validForRedirection(redirectorArgs);

        // Creating a redirector if arguments were valid.
        if (res.isValid()) {
          // Building redirection-free arguments.
          arguments = Builder.buildPreRedirectionArguments(arguments);
          commandObject.createRedirector(redirectorArgs);
        } else {
          throw new InvalidRedirectorException(res.getMessage());
        }
      }
      // Validating arguments. Throwing an exception with an appropriate
      // message if validation fails.
      ValidationResults result = commandObject.validArguments(arguments);
      if (!result.isValid()) {
        throw new InvalidCommandException(commandObject.getCommandName() + ": "
            + result.getMessage());
      }
    }
    // Returning appropriate command object if validation passed.
    commandObject.setArguments(arguments);
    return commandObject;
  }

  /**
   * Returns a Command instance specified by commandName if it exists.
   */
  private static Command getCommandInstance(String commandName)
      throws InvalidCommandException {

    // Attempts to retrieve a Command specific object. Throws an
    // InvalidCommandException if the user entered a command that doesnt exist.
    try {
      // Checking if user entered the !number command.
      if ("!".equals(commandName)) {
        commandName = "exclaim";
      }
      // Checking if there is a class "Command..." (where ... is the user
      // entered command) in the commands package.
      Class<?> someCommandClass =
          Class.forName("commands.Command" + commandName.toUpperCase());

      // Creating an instance of the Command object using the constructor.
      Object commandObject = someCommandClass.newInstance();

      return (Command) commandObject;
    } catch (Exception ex) {
      throw new InvalidCommandException("Command \"" + commandName
          + "\" does not exist.");
    }
  }

  /**
   * Checks if path exists. Returns a validation results for that path.
   * 
   * @param path some path to be validated.
   * @return ValidationResult object.
   */
  public static ValidationResults validatePath(String path) {
    if (disc.contains(path)) {
      return new ValidationResults(true, null);
    } else {
      return new ValidationResults(false, "cannot access " + path
          + ": No such file or directory.");
    }
  }

  /**
   * Checks if each path in paths is valid (ie. it exists). Returns an array of
   * validation results for all paths in paths.
   * 
   * @param paths array of paths to be validated.
   * @return array of ValidationResults.
   */
  public static ValidationResults[] validatePaths(String[] paths) {
    ValidationResults[] results = new ValidationResults[paths.length];

    // Iterating through every path and checking if its valid.
    for (int i = 0; i < paths.length; i++) {
      if (disc.contains(paths[i])) {
        results[i] = new ValidationResults(true, null);
      } else {
        results[i] =
            new ValidationResults(false, "cannot access " + paths[i]
                + ": No such file or directory.");
      }
    }
    return results;
  }

  /**
   * Returns whether or not s is in string format. A string is in string format
   * when its surrounded by quotation marks.
   * 
   * @param s some string to be validated.
   * @return true if s is in string format.
   */
  public static boolean isString(String s) {
    return (s.startsWith("\"") && s.endsWith("\""));
  }

}
