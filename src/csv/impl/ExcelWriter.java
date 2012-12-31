/**
 * 
 */
package csv.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author U434983
 *
 */
public class ExcelWriter extends AbstractTableWriter {

	private Workbook workbook;
	private Sheet sheet;
	private int rowNum;
	private int maxColumns;
	private Set<ExcelListener> excelListeners = new HashSet<ExcelListener>();
	private ExcelFormatter formatter;
	
	/**
	 * 
	 */
	public ExcelWriter() {
	}

	/**
	 * @param out
	 */
	public ExcelWriter(OutputStream out) {
		super(out);
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public ExcelWriter(File file) throws IOException {
		super(file);
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public ExcelWriter(String file) throws IOException {
		super(file);
	}

	/* (non-Javadoc)
	 * @see csv.TableWriter#printRow(java.lang.Object[])
	 */
	@Override
	public void printRow(Object[] columns) throws IOException {
		// Create the row
		Row row = getSheet().createRow(rowNum);
		for (int i=0; i<columns.length; i++) {
			Cell cell = row.createCell(i);
			Object o = columns[i];
			if (o != null) {
				if (o instanceof Date) {
					cell.setCellValue((Date)o);
				} else if (o instanceof Double) cell.setCellValue((Double)o);
				else if (o instanceof Boolean) cell.setCellValue((Boolean)o);
				else cell.setCellValue(o.toString());
				
				if (i > maxColumns) maxColumns = i;
			}
			
			CellStyle style = getStyle(rowNum, i, o);
			if (style != null) {
				cell.setCellStyle(style);
			}
			
			Hyperlink link = getHyperlink(rowNum, i, o);
			if (link != null) {
				cell.setHyperlink(link);
			}
		}
		notifyExcelListeners(row);
		rowNum++;
	}

	/**
	 * @return the workbook
	 */
	public Workbook getWorkbook() {
		if (workbook == null) workbook = new HSSFWorkbook();
		return workbook;
	}

	/**
	 * @return the sheet
	 */
	public Sheet getSheet() {
		if (sheet == null) {
			sheet = getWorkbook().createSheet();
			sheet.setSelected(true);
			rowNum = 0;
			maxColumns = -1;
		}
		return sheet;
	}

	/* (non-Javadoc)
	 * @see csv.impl.AbstractTableWriter#close()
	 */
	@Override
	public void close() {
		try {
			if (formatter != null) formatter.finalize(this, rowNum, maxColumns);
			getWorkbook().write(getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.close();
	}

	/**
	 * Registers an Excel Listener
	 * @param l the listener
	 */
	public void registerExcelListener(ExcelListener l) {
		excelListeners.add(l);
	}

	/**
	 * Unregisters an ExcelListener
	 * @param l the listener
	 */
	public void unregisterExcelListener(ExcelListener l) {
		excelListeners.remove(l);
	}

	/**
	 * Notifies all Excel Listeners about the row.
	 * @param row the row that was created
	 */
	protected void notifyExcelListeners(Row row) {
		for (ExcelListener l : excelListeners) {
			l.rowCreated(this, row);
		}
	}
	
	/**
	 * @return the formatter
	 */
	public ExcelFormatter getFormatter() {
		return formatter;
	}

	/**
	 * @param formatter the formatter to set
	 */
	public void setFormatter(ExcelFormatter formatter) {
		this.formatter = formatter;
	}

	protected CellStyle getStyle(int row, int column, Object value) {
		if (formatter != null) return formatter.getStyle(this, row, column, value);
		return DefaultExcelFormatter.INSTANCE.getStyle(this, row, column, value);
	}

	protected Hyperlink getHyperlink(int row, int column, Object value) {
		if (formatter != null) return formatter.getHyperlink(this, row, column, value);
		return null;
	}
}
