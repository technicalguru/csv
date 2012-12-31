/**
 * 
 */
package csv.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Implements Excel reading.
 * @author U434983
 *
 */
public class ExcelReader extends AbstractTableReader {

	private Workbook workbook;
	private Sheet sheet;
	private Row currentRow;
	private Row lastDeliveredRow;
	private int firstRow;
	private int lastRow;
	private int rowNum;
	
	/**
	 * Default constructor.
	 */
	public ExcelReader() {
	}

	public ExcelReader(File file) throws FileNotFoundException {
		super(file);
	}

	public ExcelReader(InputStream in) {
		super(in);
	}

	public ExcelReader(String file) throws FileNotFoundException {
		super(file);
	}

	
	/* (non-Javadoc)
	 * @see csv.impl.AbstractTableReader#open()
	 */
	@Override
	public void open() {
		super.open();
		try {
			workbook = WorkbookFactory.create(getInputStream());
			selectSheet(0);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create Excel workbook", e);
		}
	}

	/**
	 * Returns the workbook.
	 * @return workbook
	 */
	public Workbook getWorkbook() {
		return workbook;
	}
	
	/**
	 * Select the given sheet to be read from.
	 * @param index index of sheet
	 * @return sheet selected
	 */
	public Sheet selectSheet(int index) {
		sheet = workbook.getSheetAt(index);
		firstRow = sheet.getFirstRowNum();
		rowNum = firstRow;
		lastRow = sheet.getLastRowNum();
		currentRow = null;
		return sheet;
	}
	
	/**
	 * Returns the current sheet.
	 * @return the current sheet.
	 */
	public Sheet getSheet() {
		return sheet;
	}
	
	/**
	 * Returns the last delivered row.
	 * You need to call this after {@link #hasNext()} but before {@link #next()}.
	 * @return
	 */
	public Row getLastExcelRow() {
		return lastDeliveredRow;
	}
	
	/* (non-Javadoc)
	 * @see csv.impl.AbstractTableReader#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		rowNum = firstRow;
		currentRow = null;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (currentRow == null) retrieveNextRow();
		return currentRow != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Object[] next() {
		if (hasNext()) {
			List<Object> columns = new ArrayList<Object>();
			for (Cell cell: currentRow) {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					columns.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if(DateUtil.isCellDateFormatted(cell)) {
						columns.add(cell.getDateCellValue());
					} else {
						columns.add(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_BLANK:
					columns.add(null);
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					columns.add(cell.getBooleanCellValue());
					break;
				}
			}
			Object row[] = columns.toArray();
			lastDeliveredRow = currentRow;
			currentRow = null;
			
			incrementLineCount();
			incrementRowCount();
			return row;
		}
		throw new IllegalStateException("No more rows");
	}

	protected void retrieveNextRow() {
		while (rowNum <= lastRow) {
			currentRow = sheet.getRow(rowNum);
			boolean blank = true;
			for (Cell cell: currentRow) {
				if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					blank = false;
					break;
				}
			}
			rowNum++;
			if (!blank) break;
			currentRow = null;
		}
	}
}
