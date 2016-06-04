package sys_files;

import java.util.Arrays;

import exceptions.InvalidPathException;

/**
 * Provides an interface to a file system and is the factory for objects to
 * store and access files in the file system.
 * 
 * @author  
 */
public class FileSystem {

  private static FileSystem instance; // FileSystem is created only once.
  private Directory root = new Directory(); // The root directory.
  private Directory currentDirectory; // Current working directory.

  private final static String ROOT = "/"; // Root symbol.

  private int totalDirectories = 0; // Total number of directories stored.
  private int totalTextFiles = 0; // Total number of text files stored.

  /**
   * Initializes new FileSystem. Private in case someone tries to create a 
   * FileSystem instance.
   */
  private FileSystem() {
    this.currentDirectory = root;
  }

  /**
   * Returns an instance of FileSystem. If the there is no instance yet, then
   * initializes a new FileSystem first.
   * 
   * @return FileSystem instance.
   */
  public static FileSystem getInstance() {
    // Initializes an instance of FileSystem if needed.
    if (instance == null) {
      instance = new FileSystem();
    }
    return instance;
  }

  /**
   * Returns a string representation of this FileSystem object.
   */
  @Override
  public String toString() {
    return ("Type:         " + this.getSystemType() + "\n" + "Total files:  "
        + this.numFiles() + "\n" + "\t     Directories("
        + this.numDirectories() + ")" + "\n" + "\t      TextFiles("
        + this.numTextFiles() + ")");
  }

  /**
   * Returns the root object.
   * 
   * @return Directory object associated with the root.
   */
  public Directory getRoot() {
    return this.root;
  }

  /**
   * Returns whether or not file is equivalent to root object.
   * 
   * @param file some File object.
   * @return true if file is equivalent to root object.
   */
  public static boolean equalsToRoot(File file) {
    return file.getParentFile() == null;
  }

  /**
   * Returns the type of this file system.
   * 
   * @return string representing the type of this FileSystem.
   */
  public String getSystemType() {
    return "Local Disc";
  }

  /**
   * Returns the total number of files stored.
   * 
   * @return total number of File objects stored within this FileSystem.
   */
  private int numFiles() {
    return this.numDirectories() + this.numTextFiles();
  }

  /**
   * Returns the total number of directories stored.
   */
  private int numDirectories() {
    return this.totalDirectories;
  }

  /**
   * Returns the total number of text files stored.
   */
  private int numTextFiles() {
    return this.totalTextFiles;
  }

  /**
   * Returns the current working directory.
   * 
   * @return Directory object representing the current working directory.
   */
  public Directory getCurrentDirectory() {
    return this.currentDirectory;
  }

  /**
   * Sets the current working directory to the one specified.
   * 
   * @param newDirectory Directory object representing a new working directory.
   */
  public void setCurrentDirectory(Directory newDirectory) {
    this.currentDirectory = newDirectory;
  }

  /**
   * Returns the path of the current working directory
   * 
   * @return path to the current working directory.
   */
  public String getPath() {
    return this.currentDirectory.getPath();
  }

  /**
   * Returns whether or not the specified string is a path. A path is defined as
   * a string which contains one or more forward slashes.
   * 
   * @param s any string.
   * @return true if s is a path, otherwise false.
   */
  public static boolean isPath(String s) {
    return s.contains("/");
  }

  /**
   * Returns the parent path from path. The parent path excludes the last
   * element in path.
   * 
   * @param path some path.
   * @return a parent path of path.
   */
  public String retrieveParentPath(String path) {
    // Retrieving parent path.
    String[] inParts = Builder.pathToArray(path);
    String[] parentPath = Arrays.copyOfRange(inParts, 0, inParts.length - 1);
    String pp = Builder.rebuildPath(parentPath);

    // Checking if path starts at root.
    if (path.startsWith(ROOT)) {
      pp = this.getPath() + pp;
    }

    // Checking if the parent path is equivalent to root.
    if ("".equals(pp)) {
      return ROOT;
    } else {
      return pp;
    }
  }

  /**
   * Returns whether or not file specified by path exists. If only the name of
   * the file is given, returns whether or not that file exists in the current
   * working directory.
   * 
   * @param pathName path or the name of some File object.
   * @return true if the File object specified by path exists, otherwise false.
   */
  public boolean contains(String pathName) {
    // If there is a File object associated with path, then it must exist.
    if (this.fileAt(pathName) != null) {
      return true;
    }
    return false;
  }

  /**
   * Transforms the specified path into an absolute path and returns the result.
   * 
   * @param pathName some path.
   * @return absolute path representation of pathName.
   * @throws InvalidPathException if pathName is an invalid path.
   */
  public String toAbsolutePath(String pathName) throws InvalidPathException {

    // Since the current directory will be continuously changed while validating
    // / pathName, storing a backup of it.
    Directory backup = this.getCurrentDirectory();

    // No need to convert a root symbol to absolute path.
    if (ROOT.equals(pathName)) {
      return pathName;
    }

    // Checking if pathName starts at root.
    if (pathName.indexOf(ROOT) == 0) {
      this.setCurrentDirectory(root);
    }

    try {
      // Transforming pathName into an absolute path by changing the current
      // directory according to each individual element in pathName.
      String absPath = this.followPath(pathName);
      // Changing current directory back to what it was before the check.
      this.setCurrentDirectory(backup);
      return absPath;
    } catch (InvalidPathException ex) {
      this.setCurrentDirectory(backup);
      throw ex;
    }
  }

  /**
   * Follows path using current directory.
   */
  private String followPath(String path) throws InvalidPathException {
    String[] inParts = Builder.pathToArray(path);
    String toAdd = "";

    // Iterating through every directory in path and setting it as the current
    // directory.
    for (int i = 0; i < inParts.length; i++) {
      // Checking if p is a parent or a normal directory.
      if ("..".equals(inParts[i])) {
        Directory parent = (Directory) this.currentDirectory.getParentFile();
        if (parent != null) {
          this.setCurrentDirectory(parent);
        }
      } else if (!".".equals(inParts[i])) {
        if (i != inParts.length - 1) {
          this.setTo(inParts[i]);
        } else {
          toAdd = inParts[i];
        }
      }
    }
    // Checking if a separator is required.
    if (this.getPath().endsWith(ROOT)) {
      return this.getPath() + toAdd;
    }
    return this.getPath() + ROOT + toAdd;
  }

  /**
   * Sets current directory to the directory specified by fileName.
   */
  private void setTo(String fileName) throws InvalidPathException {
    // Getting the index of p in contents of the current directory.
    String[] contents = this.currentDirectory.getContentArray();
    int index = Arrays.asList(contents).indexOf(fileName);

    // Checking if fileName exists.
    if (index != -1) {
      File next = this.currentDirectory.getContents()[index];
      if (next instanceof Directory) {
        this.setCurrentDirectory((Directory) next);
      } else {
        throw new InvalidPathException("Cannot set directory to a file.");
      }
    } else {
      throw new InvalidPathException(fileName + ": Directory does not exist.");
    }
  }

  /**
   * Returns the File object associated with the specified path. If the file
   * cannot be found, returns null.
   * 
   * @param path a path to some File object.
   * @return a File object associated with path if it exists, otherwise null.
   */
  public File fileAt(String path) {
    try {
      // Converting path to absolute.
      String absPath = this.toAbsolutePath(path);
      if (ROOT.equals(absPath)) { // No need to search for root file.
        return root;
      } else {
        return this.getFile(Builder.pathToArray(absPath), root);
      }
    } catch (InvalidPathException ex) { // If file does not exist.
      return null;
    }
  }

  /**
   * Returns the File object associated with the specified path; starting the
   * search from dir.
   */
  private File getFile(String[] path, Directory dir) {

    // Do the search if path is not empty, otherwise return null.
    if (path.length > 0) {
      // Iterates through every file in dir and compare them to path.
      for (int i = 0; i < dir.getContents().length; i++) {
        // First element from path is taken and compared.
        if (path[0].equals(dir.getContentArray()[i])) {
          // If something matches, stop the search once reaching the end of
          // path, otherwise recursively check for the next element in path.
          if (path.length == 1) {
            return dir.getContents()[i];
          } else {
            // New path where the previous element is excluded.
            String[] newPath = Arrays.copyOfRange(path, 1, path.length);
            // Continue checking, now starting in the matching child directory.
            return this.getFile(newPath, (Directory) dir.getContents()[i]);
          }
        }
      }
    }
    return null;
  }

  /**
   * Deletes the file specified by path. If only the name of the file is given,
   * deletes that file from the current directory. The file is only deleted if
   * it exists.
   * 
   * @param pathName a path or the name of some File object.
   */
  public void deleteFile(String pathName) {

    // Only deleting if the path actually exists.
    if (this.contains(pathName)) {

      // Retrieving the File object to be deleted.
      File file = this.fileAt(pathName);

      // Retrieving the parent of that File object.
      Directory parent = (Directory) file.getParentFile();

      // Deleting the file.
      parent.deleteFile(file);
    }
  }

  /**
   * Returns whether or not the file system is empty.
   * 
   * @return true if there are no File objects stored within this FileSystem,
   *         otherwise false.
   */
  public boolean isEmpty() {
    return (this.totalDirectories + this.totalTextFiles) == 0;
  }

  /**
   * Adds file to the current working directory.
   * 
   * @param file some File object.
   */
  public void addFile(File file) {
    this.currentDirectory.addFile(file);

    // Accounting for the newly added file.
    if (file instanceof Directory) {
      this.totalDirectories++;
    } else if (file instanceof TextFile) {
      this.totalTextFiles++;
    }
  }

  /**
   * Adds file to the specified path. If file already exists at the path,
   * replaces the old file.
   * 
   * @param file some File object.
   * @param path represents the location to where the specified File object
   *        should be added.
   * @throws InvalidPathException if path is not a valid path.
   */
  public void addFile(File file, String path) throws InvalidPathException {

    // Checking if the specified path exists.
    if (this.contains(path)) {

      // Converting path to absolute path.
      String absPath = this.toAbsolutePath(path);

      // Retrieving the parent of the file from path.
      Directory parent = (Directory) this.fileAt(absPath);

      // Adding the file.
      parent.addFile(file);

      // Accounting for the newly added file.
      if (file instanceof Directory) {
        this.totalDirectories++;
      } else if (file instanceof TextFile) {
        this.totalTextFiles++;
      }
    } else {
      throw new InvalidPathException("The path does not exist:\n" + path);
    }
  }

  /**
   * Completely clears the file system.
   */
  public void cleanUp() {
    root = null;
    this.currentDirectory = null;
    root = new Directory();
    this.currentDirectory = root;
  }

}
