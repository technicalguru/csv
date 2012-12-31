/**
 * 
 */
package csv.impl;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Hyperlink;

/**
 * @author U434983
 *
 */
public interface ExcelFormatter {

	public CellStyle getStyle(ExcelWriter writer, int row, int column, Object value);
	public void finalize(ExcelWriter writer, int rowCount, int columnCount);
	public Hyperlink getHyperlink(ExcelWriter writer, int row, int column, Object value);
	
}
