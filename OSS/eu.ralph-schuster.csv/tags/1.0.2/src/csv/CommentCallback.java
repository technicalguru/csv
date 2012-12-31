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

	public void comment(CSVReader reader, String comment, int line);
	
}
