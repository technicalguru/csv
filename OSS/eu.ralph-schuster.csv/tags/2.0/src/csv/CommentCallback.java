/**
 * 
 */
package csv;

/**
 * Will be called when a commen is discovered in an input stream
 * @author Ralph Schuster
 * @see TableReader#registerCommentCallBack(CommentCallback)
 */
public interface CommentCallback {

	/**
	 * Informs about a comment appeared in underlying stream.
	 * @param reader the reader that notifies
	 * @param comment comment
	 * @param row line number
	 * @param cell cell number in row (might be -1 if not applicable)
	 */
	public void comment(TableReader reader, String comment, int row, int cell);
	
}
