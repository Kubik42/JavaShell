package sys_files;

import java.util.ArrayList;
import java.util.Arrays;

import exceptions.InvalidFileNameException;
import exceptions.InvalidPathException;

/**
 * Represents a folder-like object used to store, retrieve, and delete various
 * File objects.
 */
public class Directory extends File {

  // List of File objects contained within this Directory.
  private ArrayList<File> contents = new ArrayList<File>();

  /**
   * Initializes new root Directory.
   */
  protected Directory() {
    super();
  }

  /**
   * Initializes new Directory with directory name (if pathName is not a path)
   * in the current directory. Otherwise, initializes a new Directory at
   * pathName.
   * 
   * @param pathName name or path for this Directory object.
   * @throws InvalidFileNameException if pathName is an invalid name.
   * @throws InvalidPathException if pathName is a path that is invalid.
   */
  public Directory(String pathName) throws InvalidFileNameException,
      InvalidPathException {
    super(pathName);
  }

  /**
   * Initializes new Directory with directory name in the directory specified by
   * path.
   * 
   * @param dirName name for this Directory object.
   * @param path path of the directory to be containing this Directory object.
   * @throws InvalidFileNameException if dirName is an invalid name.
   * @throws InvalidPathException if path is an invalid path.
   */
  public Directory(String dirName, String path)
      throws InvalidFileNameException, InvalidPathException {
    super(dirName, path);
  }

  /**
   * Initializes new Directory with directory name and contents in the directory
   * specified by path.
   * 
   * @param dirName name for this Directory object.
   * @param path path of the directory to be containing this Directory object.
   * @param contents contents for this Directory object.
   * @throws InvalidFileNameException if dirName is an invalid name.
   * @throws InvalidPathException if path is an invalid path.
   */
  public Directory(String dirName, String path, File[] contents)
      throws InvalidFileNameException, InvalidPathException {
    super(dirName, path);
    this.setContents(contents);
  }

  /**
   * Returns the file type of this directory.
   */
  @Override
  public String getFileType() {
    return "File folder";
  }

  /**
   * Returns the contents of this directory in a string format.
   */
  @Override
  public String fileContents() {

    // Lists all files/directories in a single line, each separated by a space.
    String dirContents = "";
    if (this.contents.size() != 0) {
      for (File file : this.contents) {
        dirContents += (file.getName() + " ");
      }
    }
    return dirContents.trim();
  }

  /**
   * Returns an array of contents of this directory where each element is a name
   * of a File object stored within this directory.
   * 
   * @return array of String representing the contents stored within this
   *         Directory.
   */
  public String[] getContentArray() {

    // Transferring the names of all files to an ArrayList,
    ArrayList<String> tempContents = new ArrayList<String>();
    for (File f : this.contents) {
      tempContents.add(f.getName());
    }

    // Converting tempContents from ArrayList to Array.
    String[] contents = new String[tempContents.size()];
    return tempContents.toArray(contents);
  }

  /**
   * Returns an array of contents of this directory where each element is a File
   * object stored within this directory.
   * 
   * @return array of File object representing the contents stored within this
   *         Directory.
   */
  public File[] getContents() {

    // Converting contents from ArrayList to Array.
    File[] asArray = new File[this.contents.size()];
    return this.contents.toArray(asArray);
  }

  /**
   * Sets the contents of this directory to the ones specified.
   * 
   * @param newContents new array of File objects for this Directory.
   */
  public void setContents(File[] newContents) {

    // Converting newContents from Array to ArrayList such that new contents
    // can be added in the future.
    this.contents = new ArrayList<File>(Arrays.asList(newContents));
  }

  /**
   * Appends the specified file to this directory. If file already exists in
   * this directory, replaces the old file with the new one.
   * 
   * @param file a File object to be added to this Directory.
   */
  public void addFile(File file) {
    if (this.containsFile(file)) {
      int index = this.contents.indexOf(file);
      this.contents.remove(index);
    }
    this.contents.add(file);
    file.setParent(this);
  }

  /**
   * Returns whether or not this directory is empty (has no contents).
   * 
   * @return true if this Directory has no contents, otherwise false.
   */
  public boolean isEmpty() {
    return this.contents.size() == 0;
  }

  /**
   * Returns whether or not this directory contains the specified File object.
   * 
   * @param file some File object.
   * @return true if this directory contains the specified File object.
   */
  public boolean containsFile(File file) {
    return this.contents.contains(file);
  }

  /**
   * Returns whether or not this directory contains the specified file.
   * 
   * @param fileName name of some File object.
   * @return true if this directory contains the specified File object.
   */
  public boolean contains(String fileName) {
    return Arrays.asList(this.getContentArray()).contains(fileName);
  }

  /**
   * Deletes file from this directory.
   * 
   * @param file some File object to be deleted.
   */
  public void deleteFile(File file) {

    if (!this.isEmpty()) {
      // Retrieving the index of file in the contents of this directory.
      int fileIndex =
          Arrays.asList(this.getContentArray()).indexOf(file.getName());

      // Removing file from contents by setting it to null.
      ArrayList<File> oldContents = this.contents;
      oldContents.remove(fileIndex);

      // Giving this directory new contents where the specified file is excluded
      // from.
      File[] newContents = oldContents.toArray(new File[oldContents.size()]);
      this.setContents(newContents);
    }
  }

  /**
   * Deletes a file with fileName from this directory.
   * 
   * @param fileName name of some File object to be deleted.
   */
  public void delete(String fileName) {
    File toDelete = FileSystem.getInstance().fileAt(fileName);
    this.deleteFile(toDelete);
  }

}
