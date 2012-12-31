/**
 * 
 */
package csv.impl;

import org.apache.poi.ss.usermodel.Row;

/**
 * Notifies about Excel based events.
 * @author U434983
 *
 */
public interface ExcelListener {

	public void rowCreated(ExcelWriter writer, Row row);
	
}
