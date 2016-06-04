package commands;

import java.util.ArrayList;

import sys_files.*;

/**
 * Representation of a command that prints out recent commands.
 */
public class CommandHISTORY extends Command {

  private History hist = History.createHistoryInstance(); // History instance

  /**
   * Initializes new CommandHISTORY with no arguments.
   */
  public CommandHISTORY() {
    super();
  }

  /**
   * Initializes new CommandHISTORY with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandHISTORY(String[] arguments) {
    super(arguments);
  }

  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "history";
  }

  /**
   * Returns the documentation for this CommandHISTORY object.
   */
  public String commandDocumentation() {
    return ("Prints out recent commands, one command per line. "
        + "If provided with an integer argument x, "
        + "then print out the last x commands ");
  }

  /**
   * Returns whether or not arguments are valid for this CommandHISTORY object.
   * 
   * Arguments must be in form: history (X), where X is an optional argument
   * that is an integer that is greater than or equal to 0 and less than or
   * equal to the size of History
   */
  public ValidationResults validArguments(String[] arguments) {
    final int MIN_NUM_OF_ARGUMENTS = 0;
    final int MAX_NUM_OF_ARGUMENTS = 1;
    if (arguments.length == MIN_NUM_OF_ARGUMENTS
        || arguments.length == MAX_NUM_OF_ARGUMENTS) {
      if (arguments.length == 1) {
        try {
          int newInt = Integer.parseInt(arguments[0]);
          if (newInt < 0 || newInt > hist.getSize()) {
            return new ValidationResults(false, "Argument is out of bounds");
          } else {
            return new ValidationResults(true, null);
          }
        } catch (NumberFormatException n) {
          return new ValidationResults(false, "Argument must be an integer");
        }
      } else {
        return new ValidationResults(true, null);
      }
    } else {
      return new ValidationResults(false, "Requires 0 or 1 argument(s)");
    }
  }

  /**
   * Runs this command.
   */
  public void execute() {
    // Checking if output is to be redirected.
    if (this.getRedirector() != null) {
      Redirector r = this.getRedirector();
      r.setToWrite(this.executeReturn()); // Adding output to Redirector.
      r.redirect(); // Redirecting to file.
    } else {
      // Printing out documentation.
      System.out.println(this.executeReturn());
    }
  }

  /**
   * Returns execute information.
   * 
   * @return String containing information from History
   */
  public String executeReturn() {
    String[] arguments = this.getArguments(); // Command arguments.
    // get all commands history array
    ArrayList<String> historyArray = hist.getCommands();
    // outcome the user wants
    String temp = "";
    if (arguments.length == 0) {// get all history
      // numbers before every printed command history
      int num = 1;
      for (String com : historyArray) {
        temp += Integer.toString(num) + " " + com + "\n";
        num += 1;
      }
    } else {
      // number of commands user wants
      int numOfHis = Integer.parseInt(this.getArguments()[0]);
      // get the index of the first wanted command
      int commandIndex = historyArray.size() - numOfHis;
      // get all command wanted
      for (int i = commandIndex; i < historyArray.size(); i++) {
        temp +=
            Integer.toString(i + 1) + " " + hist.getSpecificCommand(i) + "\n";
      }
    }
    return temp;

  }

}
