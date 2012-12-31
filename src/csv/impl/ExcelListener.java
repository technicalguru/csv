/**
 * 
 */
package csv.impl;

import org.apache.poi.ss.usermodel.Row;

/**
 * Notifies about Excel based events.
 * @author Ralph Schuster
 *
 */
public interface ExcelListener {

	/**
	 * Notifies about rows created.
	 * Beware that only new Excel rows will be notified. Excel usually
	 * contains blank rows in a sheet that will not be notified by an
	 * Excel writer.
	 * @param writer ExcelWriter that notifies
	 * @param row rows index that was created
	 * @see ExcelWriter#getOrCreateRow(int)
	 */
	public void rowCreated(ExcelWriter writer, Row row);
	
}
