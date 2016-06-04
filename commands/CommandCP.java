package commands;

import sys_files.*;
import exceptions.InvalidFileNameException;
import exceptions.InvalidPathException;

/**
 * Representation of a command that copies or renames files/directories to 
 * their specified destinations/names.
 */
public class CommandCP extends Command {

  private boolean rename = false; // If file is just being renamed.
  private FileSystem disc = FileSystem.getInstance(); // FileSystem instance.

  /**
   * Initializes new CommandCP with no arguments.
   */
  public CommandCP() {
    super();
  }

  /**
   * Initializes new CommandCP with an array of arguments.
   * 
   * @param arguments arguments for command.
   */
  public CommandCP(String[] arguments) {
    super(arguments);
  }
  
  /**
   * Returns the name of this command.
   */
  @Override
  public String getCommandName() {
    return "cp";
  }

  /**
   * Returns the documentation for this CommandCP object.
   */
  @Override
  public String commandDocumentation() {
    return ("Copies files and directories to the specified destinations.");
  }


  /**
   * Returns whether or not arguments are valid for this CommandCP object.
   * 
   * Arguments must be in form: cp/mv OLDPATH NEWPATH, where OLDPATH is a File
   * object to be copied/moved and NEWPATH is the destination for that File
   * object.
   */
  @Override
  public ValidationResults validArguments(String[] arguments) {
    final int MANDATORY_NUM_OF_ARGUMENTS = 2;
    // Checking if the number of arguments is correct.
    if (arguments.length == MANDATORY_NUM_OF_ARGUMENTS) {
      final String OLDPATH = arguments[0]; // File path.
      final String NEWPATH = arguments[1]; // Destination path.

      // Checking if file is only to be renamed.
      if (disc.contains(OLDPATH)) {
        if (disc.contains(NEWPATH)) {
          File toMove = disc.fileAt(OLDPATH); // File to move.
          File destination = disc.fileAt(NEWPATH); // Destination for that file.

          // Cannot move a directory into a text file.
          if (toMove instanceof Directory && destination instanceof TextFile) {
            return new ValidationResults(false, destination.getName()
                + ": Not a directory.");
          }
          // Checking if user attempted to move file into itself.
          if (destination.getPath().contains(toMove.getPath())) {
            return new ValidationResults(false,
                "Cannot move/copy to a subdirectory of itself.");
          }
          return new ValidationResults(true, null);
        }
        // Checking if OLDPATH is just being renamed.
        if (this.toRename(NEWPATH)) {
          return new ValidationResults(true, null);
        }
        // OLDPATH/NEWPATH does not exist and OLDPATH is not being renamed.
        return this.pathResult(NEWPATH);
      }
      // OLDPATH does not exist.
      return this.pathResult(OLDPATH);
    }
    return new ValidationResults(false, "Requires "
        + MANDATORY_NUM_OF_ARGUMENTS + " arguments.");
  }

  /**
   * Runs this command.
   */
  @Override
  public void execute() {
    this.copyMove(true); // Copies file.
  }

  /**
   * Returns invalid result for path with a appropriate error message.
   */
  private ValidationResults pathResult(String path) {
    return new ValidationResults(false, path + ": No such file or directory.");
  }

  /**
   * Returns whether or not file is just being renamed.
   */
  private boolean toRename(String path) {
    this.rename = disc.contains(disc.retrieveParentPath(path));
    return this.rename;
  }

  /**
   * Returns the file to be copied(if copy is true) or moved(if copy is false).
   */
  private File toCopyMove(boolean copy) {
    if (copy) {
      try {
        return this.deepCopy(disc.fileAt(this.getArguments()[0]));
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
      }
    }
    return disc.fileAt(this.getArguments()[0]);
  }

  /**
   * Returns a deep copy of file.
   */
  private File deepCopy(File file) throws InvalidFileNameException,
      InvalidPathException {
    if (file instanceof TextFile) { // Deep copy for text file.
      TextFile textFile = new TextFile(file.getName());
      textFile.setContents(file.fileContents());
      return textFile;
    } else { // Deep copy for directory.
      // Checking if directory is null or doesn't have any contents.
      if (file == null || ((Directory) file).isEmpty()) {
        return new Directory(file.getName());
      } else {
        // Recursively copying all the contents.
        Directory copy = new Directory(file.getName());
        for (File f : ((Directory) file).getContents()) {
          copy.addFile(deepCopy(f));
        }
        return copy;
      }
    }
  }

  /**
   * Renames file.
   */
  private void renameFile(File file) {
    String[] inParts = Builder.pathToArray(this.getArguments()[1]);
    String newName = inParts[inParts.length - 1];
    file.renameTo(newName);
  }

  /**
   * Adds toMove to destination, deleting toMove from its original location in
   * the process.
   */
  private void moveFile(File toMove, String destination) {
    try {
      Directory originalParent = (Directory) toMove.getParentFile();
      originalParent.deleteFile(toMove);
      disc.addFile(toMove, destination);
    } catch (InvalidPathException ex) {
      System.out.println(ex.getMessage());
    }
  }

  /**
   * Adds toCopy to destination.
   */
  private void copyFile(File toCopy, String destination) {
    try {
      disc.addFile(toCopy, destination);
    } catch (InvalidPathException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

  /**
   * Copies/moves file to destination.
   * 
   * @param copy true if file is to be copied, false if file is to be moved.
   */
  protected void copyMove(boolean copy) {
    File file = this.toCopyMove(copy); // File to copy/move.

    // Destination for file.
    File destination = disc.fileAt(this.getArguments()[1]);

    if (this.rename) {
      // Renaming file and copying/moving it to destination.
      this.renameFile(file);
      String path = disc.retrieveParentPath(this.getArguments()[1]);
      if (copy) {
        this.copyFile(file, path);
      } else {
        this.moveFile(file, path);
      }
    } else if (file instanceof TextFile && destination instanceof TextFile) {
      // Renaming file and copying/moving it to destination.
      this.renameFile(file);
      if (copy) {
        this.copyFile(file, destination.getParentPath());
      } else {
        this.moveFile(file, destination.getParentPath());
      }
    } else {
      // Moving/copying file to destination.
      if (copy) {
        this.copyFile(file, destination.getPath());
      } else {
        this.moveFile(file, destination.getPath());
      }
    }
  }

}
