package commands;

import java.util.Arrays;

import exceptions.InvalidRedirectorException;
import sys_files.Builder;

/**
 * A bare-bones Command object.
 */
public abstract class Command {

  private String[] arguments; // Array of command's arguments.
  private boolean recursive = false; // If command contains a recursive flag.
  private Redirector redirector = null; // Redirector for command output.

  /**
   * Initializes new Command with no arguments.
   */
  public Command() {
    this.arguments = new String[0];
  }

  /**
   * Initializes new Command with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public Command(String[] arguments) {
    this.setArguments(arguments);
  }

  /**
   * Returns a string representation of this Command object.
   */
  @Override
  public String toString() {
    return ("Command " + this.getCommandName().toUpperCase() + ":\n" + this
        .commandDocumentation());
  }

  /**
   * Returns whether or not this Command object is equivalent to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Command) {
      return (this.getCommandName().equals(((Command) other).getCommandName())
          && this.argumentsEquals((Command) other) && this
          .commandDocumentation().equals(
              ((Command) other).commandDocumentation()));
    }
    return false;
  }

  /**
   * Returns whether or not the arguments of this Command objects are equivalent
   * to the arguments of the other Command object.
   */
  private boolean argumentsEquals(Command other) {
    return Arrays.equals(this.arguments, other.arguments);
  }

  /**
   * Returns the arguments of this Command object.
   * 
   * @return array of arguments of this Command object.
   */
  public String[] getArguments() {
    return this.arguments;
  }

  /**
   * Sets arguments for this command to newArguments.
   * 
   * @param newArguments new array of arguments for this Command object.
   */
  public void setArguments(String[] newArguments) {
    // Checking if command is recursive. We don't need to include the recursive
    // flag in the command arguments.
    if (this.containsRecrusiveFlag(newArguments)) {
      this.arguments = Arrays.copyOfRange(newArguments, 1, newArguments.length);
      this.recursive = true;
    } else {
      this.arguments = newArguments;
    }
  }

  /**
   * Returns whether or not this command is recursive (ie. contains a recursive
   * flag in its arguments).
   * 
   * @return true if this Command object is recursive, otherwise false.
   */
  public boolean isRecursive() {
    return this.recursive;
  }

  /**
   * Creates a redirector for this command.
   * 
   * @param arguments arguments for the Redirector object.
   * @throws InvalidRedirectorException if a Redirector cannot be created with
   *         arguments.
   */
  public void createRedirector(String[] arguments)
      throws InvalidRedirectorException {
    this.redirector = Builder.buildRedirector("", arguments);
  }

  /**
   * Returns this command's redirector or null if there is no redirector for
   * this command.
   * 
   * @return Redirector object associated with this Command object, or null if
   *         there is no Redirector.
   */
  public Redirector getRedirector() {
    return this.redirector;
  }

  /**
   * Returns whether or not this command contains a recursive flag (-R or -r).
   * 
   * @param arguments array of arguments for this Command object.
   * @return true if this Command object has a recursive flag as its first
   *         argument, otherwise false.
   */
  public boolean containsRecrusiveFlag(String[] arguments) {
    final String RECURSIVE_FLAG = "-R";
    if (arguments.length > 0) {
      return arguments[0].toUpperCase().equals(RECURSIVE_FLAG);
    }
    return false;
  }

  /**
   * Returns the name of this command.
   * 
   * @return string representation of the name of this Command object.
   */
  public abstract String getCommandName();

  /**
   * Returns the documentation for this Command object.
   * 
   * Requires a subclass.
   * 
   * @return string representing the documentation of this Command object.
   */
  public abstract String commandDocumentation();

  /**
   * Returns whether or not the specified arguments are valid this Command
   * object.
   * 
   * Requires a subclass.
   * 
   * @param arguments array of arguments to be validated.
   * @return a ValidationResults object that is valid if the specified array of
   *         arguments is valid for this Command object, otherwise a
   *         ValidationResults object that is invalid and contains an error
   *         message explaining as to why.
   */
  public abstract ValidationResults validArguments(String[] arguments);

  /**
   * Runs this command.
   * 
   * Requires a subclass.
   */
  public abstract void execute();

}
