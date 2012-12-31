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
