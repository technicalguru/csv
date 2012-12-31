/**
 * 
 */
package csv;

import java.io.InputStream;
import java.util.Iterator;

/**
 * Introduces an interface for other implementations
 * of table reading interfaces.
 * @author Ralph Schuster
 *
 */
public interface TableReader extends Iterator<Object[]>{

	/**
	 * Sets the underlying reader.
	 * @param in input stream to be used
	 */
	public void setInputStream(InputStream in);
	
	/**
	 * Tells the reader whether the underlying stream will treat
	 * first row as header row.
	 * @param hasHeaderRow true if there is a header row.
	 */
	public void setHasHeaderRow(boolean hasHeaderRow);
	
	/**
	 * Tells whether the underlying stream has a header row or not
	 * @return true if there is a header row.
	 */
	public boolean hasHeaderRow();
	
	/**
	 * Returns the header row.
	 * @return header row if such was defined.
	 */
	public Object[] getHeaderRow();
	
	/**
	 * Returns the value in column with specified name
	 * @param name name of column (from header row)
	 * @param row row of values
	 * @return value in row for specified column.
	 */
	public Object get(String name, Object row[]);

	/**
	 * Returns the column index of given column name.
	 * The first column with given name will be returned.
	 * @param name name of column
	 * @return index of column or -1 if it does not exist.
	 */
	public int getColumnIndex(String name);

	/**
	 * Opens the reader or resets it.
	 */
	public void open();
	
	/**
	 * Resets the reader.
	 */
	public void reset();
	
	/**
	 * Closes the reader.
	 */
	public void close();
	
	/**
	 * Registers a comment callback.
	 * The callback will be executed when a comment is detected in input.
	 * Note that not all implementations actually support comments.
	 * @param callback callback to be registered
	 */
	public void registerCommentCallBack(CommentCallback callback);
	
	/**
	 * Unregisters a comment callback.
	 * Note that not all implementations actually support comments.
	 * @param callback callback to be unregistered
	 */
	public void unregisterCommentCallBack(CommentCallback callback);
	
	/**
	 * Sets the minimum number of columns to be returned by {@link #next()}.
	 * @param length number of columns
	 */
	public void setMinimumColumnCount(int length);
}
