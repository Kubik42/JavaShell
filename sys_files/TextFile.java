package sys_files;

import exceptions.InvalidFileNameException;
import exceptions.InvalidPathException;

/**
 * Representation of a text file document.
 */
public class TextFile extends File {

  private String contents; // Contents of this file.

  /**
   * Initializes new TextFile with file name (if pathName is not a path) and
   * empty contents in the current directory. Otherwise, initializes a new 
   * TextFile at pathName.
   * 
   * @param pathName name or path for this File object.
   * @throws InvalidFileNameException if pathName is an invalid name.
   * @throws InvalidPathException if pathName is a path that is invalid.
   */
  public TextFile(String pathName) throws InvalidFileNameException,
      InvalidPathException {
    super(pathName);
    this.contents = "";
  }

  /**
   * Initializes new TextFile with file name and empty contents in the 
   * directory specified by path.
   * 
   * @param fileName name for this TextFile object.
   * @param path path of the directory to be containing this TextFile object.
   * @throws InvalidFileNameException if fileName is an invalid name.
   * @throws InvalidPathException if path is an invalid path.
   */
  public TextFile(String fileName, String path)
      throws InvalidFileNameException, InvalidPathException {
    super(fileName, path);
    this.contents = "";
  }

  /**
   * Initializes new TextFile with file name and contents in the directory
   * specified by path.
   * 
   * @param fileName name for this TextFile object.
   * @param path path of the directory to be containing this TextFile object.
   * @throws InvalidFileNameException if fileName is an invalid name.
   * @throws InvalidPathException
   */
  public TextFile(String fileName, String path, String contents)
      throws InvalidFileNameException, InvalidPathException {
    super(fileName, path);
    this.contents = contents;
  }

  /**
   * Returns the file type of this text file.
   */
  @Override
  public String getFileType() {
    return "Text Document";
  }

  /**
   * Returns the contents of this TextFile object.
   */
  @Override
  public String fileContents() {
    return this.contents;
  }

  /**
   * Sets the contents of this text file to the ones specified.
   * 
   * @param newContents new text for this TextFile object.
   */
  public void setContents(String newContents) {
    this.contents = newContents;
  }

  /**
   * Returns whether or not this text file is empty.
   * 
   * @return true if this TextFile object has empty contents, otherwise false.
   */
  public boolean isEmpty() {
    String fileContents = this.contents.trim(); // Removing all whitespace.
    return fileContents.equals("");
  }

}
