/**
 * 
 */
package csv;

/**
 * Will be called when a commen is discovered
 * @author U434983
 *
 */
public interface CommentCallback {

	/**
	 * Informs about a comment appeared in underlying stream.
	 * @param reader the reader that notifies
	 * @param comment comment
	 * @param line line number
	 */
	public void comment(TableReader reader, String comment, int line);
	
}
