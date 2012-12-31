/**
 * 
 */
package csv;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Introduces an interface for other implementations
 * of table writing interfaces.
 * @author Ralph Schuster
 *
 */
public interface TableWriter {
	
	/**
	 * Sets the underlying writer.
	 * @param out output stream to be used.
	 */
	public void setOutputStream(OutputStream out);
	
	/**
	 * Prints the columns into the table writer.
	 * @param columns columns to be written in row
	 * @throws IOException when an exception occurs
	 */
	public void printRow(Object[] columns) throws IOException;
	
    /**
     * Prints a comment into the stream.
     * Note that not all implementations support comments.
     * @param comment comment to write
     * @throws IOException when an exception occurs
     */
    public void printComment(String comment) throws IOException;
    
    /**
     * Prints a comment into the stream.
     * Note that not all implementations support comments.
     * @param comment comment to write
     * @param row index of row for comment
     * @param column index of column for comment
     * @throws IOException when an exception occurs
     */
    public void printComment(String comment, int row, int column) throws IOException;

	/**
	 * Closes the writer.
	 */
	public void close();
}
