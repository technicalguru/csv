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
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Implements Excel reading.
 * This class reads Excel sheets like a stream, meaning
 * delivering rows one by one from the current sheet.
 *  * Use this reader if you want to load an Excel file by creating a {@link java.io.File}
 * and passing it to the constructor.
 * <p>
 * Example:
 * </p>
 * <p>
 * <pre>
java.io.File f = new java.io.File("excel-test.xls");
ExcelReader in = new ExcelReader(f);
while (in.hasNext()) {
    Object columns[] = in.next();
    // Do something here
}
in.close();
</pre>
 * </p>
 * @author Ralph Schuster
 * @see #selectSheet(int)
 * @see #selectSheet(String)
 */
public class ExcelReader extends AbstractTableReader {

	private Workbook workbook;
	private FormulaEvaluator formulaEvaluator = null;
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

	/**
	 * Constructor for reading from a file.
	 * @param file file to read from
	 * @throws FileNotFoundException when file does not exist
	 */
	public ExcelReader(File file) throws FileNotFoundException {
		super(file);
	}

	/**
	 * Constructor to read from an existing stream.
	 * @param in input stream to be used
	 */
	public ExcelReader(InputStream in) {
		super(in);
	}

	/**
	 * Constructor to read from an existing workbook.
	 * @param workbook the workbook be used
	 */
	public ExcelReader(Workbook workbook) {
		this.workbook = workbook;
	}

	/**
	 * Constructor for reading from a file.
	 * @param file file to read from
	 * @throws FileNotFoundException when file does not exist
	 */
	public ExcelReader(String file) throws FileNotFoundException {
		super(file);
	}

	
	/**
	 * Opens the stream by retrieving the workbook and selecting the first sheet.
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
		if (workbook == null) open();
		return workbook;
	}
	
	
	/**
	 * Select the given sheet to be read from.
	 * @param name name of sheet
	 * @return sheet selected
	 */
	public Sheet selectSheet(String name) {
		return selectSheet(workbook.getSheet(name));
	}
	
	/**
	 * Select the given sheet to be read from.
	 * @param sheet sheet to be selected
	 * @return sheet selected
	 */
	public Sheet selectSheet(Sheet sheet) {
		if (this.sheet != sheet) {
			this.sheet = sheet;
			firstRow = sheet.getFirstRowNum();
			rowNum = firstRow;
			lastRow = sheet.getLastRowNum();
			currentRow = null;
		}
		return this.sheet;
	}
	
	/**
	 * Select the given sheet to be read from.
	 * @param index index of sheet
	 * @return sheet selected
	 */
	public Sheet selectSheet(int index) {
		return selectSheet(workbook.getSheetAt(index));
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
	 * This is the row delivered by last call to {@link #next()}.
	 * @return the last row delivered by {@link #next()}
	 */
	public Row getLastExcelRow() {
		return lastDeliveredRow;
	}
	
	/**
	 * Resets the reader by resetting the current row index 
	 * @see csv.impl.AbstractTableReader#reset()
	 * @see #getRowCount()
	 */
	@Override
	public void reset() {
		super.reset();
		rowNum = firstRow;
		currentRow = null;
	}

	/**
	 * Returns whether there is a row to be read in the current sheet.
	 * This implementation stops reading when last row from a sheet was read.
	 * You might need to manually select the next sheet if you want to read more
	 * rows from other sheets.
	 * @return true if a row is available in current sheet.
	 * @see java.util.Iterator#hasNext()
	 * @see #selectSheet(int)
	 */
	@Override
	public boolean hasNext() {
		if (currentRow == null) retrieveNextRow();
		return currentRow != null;
	}

	/**
	 * Returns the next row.
	 * This method increases the internal row index and delivers the next row in the sheet.
	 * Values in the array are Java objects depending on the cell type. If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @return values in row
	 * @see java.util.Iterator#next()
	 * @see #getRowCount()
	 */
	@Override
	public Object[] next() {
		if (hasNext()) {
			Object row[] = getValues(currentRow);
			lastDeliveredRow = currentRow;
			currentRow = null;
			
			incrementLineCount();
			incrementRowCount();
			return row;
		}
		throw new IllegalStateException("No more rows");
	}

	/**
	 * Returns the row at the given index.
	 * Values in the array are Java objects depending on the cell type. If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param rowNum row index to read
	 * @return values of row
	 */
	public Object[] getValues(int rowNum) {
		Row row = getSheet().getRow(rowNum);
		return getValues(row);
	}
	
	/**
	 * Returns the row as Java objects.
	 * Values in the array are Java objects depending on the cell type. If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @return values in row
	 * @param row row to read
	 */
	public Object[] getValues(Row row) {
		if (row == null) return null;
		List<Object> columns = new ArrayList<Object>();
		for (Cell cell: row) {
			columns.add(getValue(cell));
		}
		return columns.toArray();
	}
	
	/**
	 * Returns the value of the specified cell.
	 * If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param rownum row index
	 * @param cellNum column index
	 * @return value of cell
	 */
	public Object getValue(int rownum, int cellNum) {
		Row row = getSheet().getRow(rowNum);
		return getValue(row, cellNum);
	}
	
	/**
	 * Returns the value of the specified cell.
	 * If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param row row object
	 * @param cellNum column index
	 * @return value of cell
	 */
	public Object getValue(Row row, int cellNum) {
		if (row == null) return null;
		Cell cell = row.getCell(cellNum);
		return getValue(cell);
	}
	
	/**
	 * Returns the value of the specified cell.
	 * If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param cell cell object
	 * @return value of cell
	 */
	public Object getValue(Cell cell) {
		if (cell == null) return null;
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return cell.getNumericCellValue();
			}
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return evaluateCellValue(cell);
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue();
		}
		return null;
	}
	
	/**
	 * Returns the evaluated cell content.
	 * This assumes the cell contains a formula.
	 * @param cell cell to evaluate
	 * @return cell value
	 */
	public Object evaluateCellValue(Cell cell) {
		FormulaEvaluator evaluator = getFormulaEvaluator();
		CellValue value = evaluator.evaluate(cell);
		switch (value.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return value.getStringValue();
		case Cell.CELL_TYPE_NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				return DateUtil.getJavaDate(value.getNumberValue());
			} else {
				return value.getNumberValue();
			}
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_BOOLEAN:
			return value.getBooleanValue();
		case Cell.CELL_TYPE_ERROR:
			return value.getErrorValue();
		default:
			System.out.println("type="+cell.getCellType());
		}
		return cell.getCellFormula();
	}
	
	/**
	 * Returns a formula evaluator for the current workbook.
	 * This is for convinience.
	 * @return the formula evaluator
	 */
	public FormulaEvaluator getFormulaEvaluator() {
		if (formulaEvaluator == null) {
			formulaEvaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
		}
		return formulaEvaluator;
	}
	
	/**
	 * Retrieves the next row from the current sheet.
	 * The row is then internally stored for evaluation of {@link #hasNext()}
	 * and {@link #next()}. Blank rows are skipped.
	 */
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
