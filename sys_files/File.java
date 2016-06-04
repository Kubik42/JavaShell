package sys_files;

import java.util.HashMap;

import exceptions.InvalidFileNameException;
import exceptions.InvalidPathException;

/**
 * Represents a bare-bones file object.
 */
public abstract class File {

  private String name; // File name.
  private FileSystem disc; // FileSystem instance.

  // Invalid special characters.
  private final static String SPECIAL_CHARS = " `!#&*()-+={}[]|;:\\'\"<>,?";
  // Root symbol.
  private final static String ROOT = "/";

  // Parent of this file; root has no parent (equivalent to null).
  private File parent;

  // File attributes.
  private static HashMap<String, Boolean> attributes =
      new HashMap<String, Boolean>();
  static {
    attributes.put("Read-only", false);
    attributes.put("Hidden", false);
  }

  /**
   * Initializes new root File.
   */
  protected File() {
    this.name = "";
    this.parent = null;
  }

  /**
   * Initializes new File with file name (if pathName is not a path) in the
   * current directory. Otherwise, initializes a new File at pathName.
   * 
   * @param pathName name or path for this File object.
   * @throws InvalidFileNameException if pathName is an invalid name.
   * @throws InvalidPathException if pathName is a path that is invalid.
   */
  public File(String pathName) throws InvalidFileNameException,
      InvalidPathException {

    this.createFileSystem(); // Creating a FileSystem instance.

    // Converting path to absolute, checking if pathName is a path first.
    String absPathName = pathName;
    if (FileSystem.isPath(pathName)) {
      absPathName = disc.toAbsolutePath(pathName);
      this.createFileFromPath(absPathName); // Creating a File object from path.
    } else {
      this.createFile(absPathName, null); // Creating a File object.
    }
  }

  /**
   * Initializes new File with file name in the directory specified by path.
   * 
   * @param fileName name for this File object.
   * @param path path of the directory to be containing this File object.
   * @throws InvalidFileNameException if fileName is an invalid name.
   * @throws InvalidPathException is path is an invalid path.
   */
  public File(String fileName, String path) throws InvalidFileNameException,
      InvalidPathException {

    this.createFileSystem(); // Creating a FileSystem instance.

    // Converting path to absolute.
    String absPathName = disc.toAbsolutePath(path);
    this.createFile(fileName, absPathName); // Creating a File object.
  }

  /**
   * Initializes a FileSystem instance.
   */
  private void createFileSystem() {
    disc = FileSystem.getInstance();
  }

  /**
   * Creates file with file name and parent indicated by path.
   */
  private void createFile(String fileName, String path)
      throws InvalidFileNameException {

    // Checking if file name is valid.
    if (!this.containsIllegals(fileName)) {
      // Setting file name.
      if (fileName != null) {
        this.name = fileName;
        // Setting parent.
        if (path == null) {
          this.parent = disc.getCurrentDirectory();
        } else {
          this.parent = disc.fileAt(path);
        }
      } else {
        throw new InvalidFileNameException("File name cannot be null.");
      }
    } else {
      throw new InvalidFileNameException(fileName
          + ": File name cannot contains special characters: " + SPECIAL_CHARS);
    }
  }

  /**
   * Creates file from path.
   */
  private void createFileFromPath(String path) throws InvalidFileNameException {

    // Converting path into an array of directory elements.
    String[] inParts = Builder.pathToArray(path);

    // Creating file using file name and extracted parent path.
    this.createFile(inParts[inParts.length - 1],
        disc.retrieveParentPath(path));
  }

  /**
   * Returns whether or not fileName contains illegal characters.
   */
  private boolean containsIllegals(String fileName) {
    for (int i = 0; i < SPECIAL_CHARS.length(); i++) {
      if (fileName.indexOf(SPECIAL_CHARS.charAt(i)) != -1) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a string representation of this File object.
   */
  @Override
  public String toString() {

    // Making a string that will be containing all file attributes.
    String fileAttributes = "";
    for (String attribute : attributes.keySet()) {
      if (attributes.get(attribute)) {
        // If there are multiple attributes, separating them by a comma.
        if (fileAttributes.length() != 0) {
          fileAttributes += ", ";
        }
        fileAttributes += attribute;
      }
    }
    return ("File--------------------------------------------------------\n"
        + "Name          " + this.name + "\n" + "Type of file  "
        + this.getFileType() + "\n" + "File path     " + this.getPath() + "\n"
        + "Attributes    " + fileAttributes);
  }

  /**
   * Returns whether or not this File object is equivalent to other.
   */
  @Override
  public boolean equals(Object other) {
    // Just paths are compared since no two files can have the same path.
    if (other != null && other instanceof File) {
      return this.getPath().equals(((File) other).getPath());
    }
    return false;
  }

  /**
   * Returns the name of this file.
   * 
   * @return name of this File object.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Renames this file to the specified name.
   * 
   * @param newName new name for this File object.
   */
  public void renameTo(String newName) {
    this.name = newName;
  }

  /**
   * Returns the parent object of this file. If there is no parent (ie. the file
   * is the root) returns null.
   * 
   * @return a File object or null representing the parent of this File object.
   */
  public File getParentFile() {
    return this.parent;
  }

  /**
   * Sets the parent of this file to newParent.
   * 
   * @param newParent new parent File object for this File object.
   */
  public void setParent(File newParent) {
    this.parent = newParent;
  }

  /**
   * Returns the path to this file.
   * 
   * @return the path to this File object.
   */
  public String getPath() {
    // If the parent is root.
    if (FileSystem.equalsToRoot(this)) {
      return ROOT;
    } else {
      // To avoid double forwards slashes. Checking if the parent file is the
      // root file.
      if (!(ROOT.equals(getParentPath()))) {
        return this.getParentPath() + "/" + this.name;
      }
      return this.getParentPath() + this.name;
    }
  }

  /**
   * Returns the path of the parent file.
   * 
   * @return the path of the parent object of this File object.
   */
  public String getParentPath() {
    // Checking if the parent file is the root file.
    if (FileSystem.equalsToRoot(this.parent)) {
      return "/";
    } else {
      return this.parent.getPath();
    }
  }

  /**
   * Returns whether or not this file is read-only.
   * 
   * @return true if this File object is read-only, otherwise false.
   */
  public boolean isReadOnly() {
    return attributes.get("Read-only");
  }

  /**
   * Returns whether or no this file is hidden.
   * 
   * @return true if this File object is hidden, otherwise false.
   */
  public boolean isHidden() {
    return attributes.get("Hidden");
  }

  /**
   * Sets file read-only attribute to true if its currently set to false, and
   * vice versa.
   */
  public void switchReadOnly() {
    if (this.isReadOnly()) {
      attributes.put("Read-only", false);
    } else {
      attributes.put("Read-only", true);
    }
  }

  /**
   * Sets file hidden attribute to true if its currently set to false, and vice
   * versa.
   */
  public void switchHidden() {
    if (this.isHidden()) {
      attributes.put("Hidden", false);
    } else {
      attributes.put("Hidden", true);
    }
  }

  /**
   * Returns whether or not this file exists in the file system.
   * 
   * @return true if this File object exists, otherwise false.
   */
  public boolean exists() {
    return disc.contains(this.getPath());
  }

  /**
   * Removes this file from the file system.
   */
  public void delete() {
    disc.deleteFile(this.getPath());
  }

  /**
   * Returns the file type.
   * 
   * Requires a subclass.
   * 
   * @return the file type of this File object.
   */
  public abstract String getFileType();

  /**
   * Returns the contents of this file.
   * 
   * Requires a subclass.
   * 
   * @return string representation of the contents of this File object.
   */
  public abstract String fileContents();

  /**
   * Returns whether or not this file is empty.
   * 
   * Requires a subclass.
   * 
   * @return true if this File object is empty (no contents), otherwise false.
   */
  public abstract boolean isEmpty();

}
