/**
 * 
 */
package csv;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Introduces an interface for other implementations
 * of table writing interfaces.
 * @author U434983
 *
 */
public interface TableWriter {
	
	/**
	 * Sets the underlying writer.
	 * @param out writer to be used.
	 */
	public void setOutputStream(OutputStream out);
	
	/**
	 * Prints the columns into the table writer.
	 * @param columns
	 * @throws IOException
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
	 * Closes the writer.
	 */
	public void close();
}
