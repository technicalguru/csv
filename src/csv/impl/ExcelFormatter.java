/**
 * 
 */
package csv.impl;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Interface for formatting Excel sheets
 * @author Ralph Schuster
 *
 */
public interface ExcelFormatter {

	/**
	 * Sets the cell style.
	 * @param writer writer that requires the information
	 * @param cell cell to be formatted
	 * @param value value in cell
	 */
	public void setStyle(ExcelWriter writer, Cell cell, Object value);
	
	/**
	 * Finalizes the workbook.
	 * This method is called immediately before the {@link ExcelWriter} writes the
	 * complete workbook to the underlying output stream.
	 * @param writer the calling writer
	 * @param rowCount the number of rows in the selected sheet
	 * @param columnCount the number of columns modified in the selected sheet
	 */
	public void finalize(ExcelWriter writer, int rowCount, int columnCount);
	
}
