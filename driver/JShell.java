package driver;

import java.util.Scanner;

import sys_files.FileSystem;
import sys_files.History;
import commands.Command;
import commands.Checker;

/**
 * Representation of a command-input interface, JShell (simple Lunix Shell).
 */
public class JShell {

  public static void main(String[] args) {

    // For getting user input.
    Scanner input = new Scanner(System.in);
    // For recording user input
    History inputHistory = History.createHistoryInstance();

    // Continuously prompts user until they exit.
    while (true) {
      System.out.print("/# ");

      // Getting user input and remove unwanted white space.
      String command = input.nextLine().trim().replaceAll("\\s+", " ");
      // Recording user input into History instance
      inputHistory.addCommand(command);

      // Can reset the file system.
      if ("rest".equals(command)) {
        FileSystem.getInstance().cleanUp();
      } else {
        // Checking if the entered command is valid.
        try {
          Command cmd = Checker.getCommand(command, false);
          cmd.execute(); // Execute command if its valid.
        } catch (Exception ex) {
          System.out.println(ex.getMessage());
        }
      }
    }
  }

}
