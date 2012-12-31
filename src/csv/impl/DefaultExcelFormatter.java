/**
 * 
 */
package csv.impl;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Default implementation of an ExcelFormatter.
 * This class provides a default implementation that provides some basic
 * functionality to emphasize the header row in a sheet by a bold font and
 * formatting hyperlinks in cells.
 * You can derive from this implementation to change formatting, e.g. just
 * setting another color or font size.
 * @author Ralph Schuster
 *
 */
public class DefaultExcelFormatter implements ExcelFormatter {

	/** A default instance that does no formatting at all */
	public static final ExcelFormatter INSTANCE = new DefaultExcelFormatter();
	/** Arial font name */
	public static final String DEFAULT_FONT_NAME = "Arial";
	/** Color Black */
	public static final short DEFAULT_FONT_COLOR = IndexedColors.BLACK.getIndex();
	/** Color Blue */
	public static final short HYPERLINK_FONT_COLOR = IndexedColors.BLUE.getIndex();
	/** Font Size 10 */
	public static final short DEFAULT_FONT_SIZE = 10;
	/** date format "dd.mm.yyyy hh:mm" */
	public static final String DEFAULT_DATE_FORMAT = "dd.mm.yyyy hh:mm";
	/** integer format "0" */
	public static final String DEFAULT_INTEGER_FORMAT = "0";
	/** real format "0.00" */
	public static final String DEFAULT_REAL_FORMAT = "0.00";

	private boolean emphasizeFirstRow;
	private Font defaultBoldFont;
	private Font defaultPlainFont;
	private Font defaultHyperlinkFont;
	private String defaultFontName;
	private short defaultFontSize;
	private short defaultFontColor;
	private short defaultHyperlinkColor;
	
	/**
	 * Default constructor.
	 * This is without any formatting.
	 */
	public DefaultExcelFormatter() {
		this(false);
	}

	/**
	 * Constructor for defining the emphasizing of header rows.
	 * @param emphasizeFirstRow whether row 0 shall be set in bold font
	 * @see #getFont(ExcelWriter, int, int, Object)
	 */
	public DefaultExcelFormatter(boolean emphasizeFirstRow) {
		this(emphasizeFirstRow, null, null, null, null);
	}


	/**
	 * Constructor for defining the various properties.
	 * @param emphasizeFirstRow whether row 0 shall be set in bold font
	 * @param defaultFontName font name of default font
	 * @param defaultFontSize font size to be used
	 * @param defaultFontColor color to be used for font
	 * @param defaultHyperlinkColor color for hyperlinks to be used
	 * @see #getFont(ExcelWriter, int, int, Object)
	 */
	public DefaultExcelFormatter(boolean emphasizeFirstRow, String defaultFontName, Short defaultFontSize, Short defaultFontColor, Short defaultHyperlinkColor) {
		this.emphasizeFirstRow = emphasizeFirstRow;
		defaultBoldFont = null;
		defaultPlainFont = null;
		defaultHyperlinkFont = null;
		this.defaultFontName = defaultFontName != null ? defaultFontName : DEFAULT_FONT_NAME;
		this.defaultFontSize = defaultFontSize != null ? defaultFontSize : DEFAULT_FONT_SIZE;
		this.defaultFontColor = defaultFontColor != null ? defaultFontColor : DEFAULT_FONT_COLOR;
		this.defaultHyperlinkColor = defaultHyperlinkColor != null ? defaultHyperlinkColor : HYPERLINK_FONT_COLOR;		
	}

	/**
	 * Sets the cell style.
	 * This implementations calls various other methods to define
	 * the style of the cell.
	 * @param writer writer that requires the information
	 * @param cell cell to be formatted
	 * @param value value in cell
	 * @see #getFormat(ExcelWriter, int, int, Object)
	 * @see #getBackgroundColor(ExcelWriter, int, int, Object)
	 * @see #getFillPattern(ExcelWriter, int, int, Object)
	 * @see #getForegroundColor(ExcelWriter, int, int, Object)
	 * @see #getFont(ExcelWriter, int, int, Object)
	 * @see #setBorders(ExcelWriter, CellStyle, int, int, Object)
	 * @see #getAlign(ExcelWriter, int, int, Object)
	 * @see #getHyperlink(ExcelWriter, int, int, Object)
	 */
	@Override
	public void setStyle(ExcelWriter writer, Cell cell, Object value) {
		int row = cell.getRowIndex();
		int column = cell.getColumnIndex();
		
		CellStyle rc = writer.getWorkbook().createCellStyle();
		if (value instanceof String) rc.setWrapText(true);
		
		// data format
		Short format = getFormat(writer, row, column, value);
		if (format != null) rc.setDataFormat(format);
		
		// Colors
		Short fg = getForegroundColor(writer, row, column, value);
		if (fg != null) {
			rc.setFillForegroundColor(fg);		
			Short fill = getFillPattern(writer, row, column, value);
			if (fill != null) rc.setFillPattern(fill);
		}
		Short bg = getBackgroundColor(writer, row, column, value);
		if (bg != null) rc.setFillBackgroundColor(bg);
		
		// Font
		Font font = getFont(writer, row, column, value);
		if (font != null) rc.setFont(font);
		
		// Borders
		setBorders(writer, rc, row, column, value);
		
		// Alignment
		Short align = getAlign(writer, row, column, value);
		if (align != null) rc.setAlignment(align);
		
		// set style
		cell.setCellStyle(rc);
		
		// Set a hyperlink
		Hyperlink link = getHyperlink(writer, row, column, value);
		if (link != null) cell.setHyperlink(link);

	}
	
	/**
	 * Returns a hyperlink object when the given cell shall be linked.
	 * Notice that you should return a blue underlined font in {@link #getFont(ExcelWriter, int, int, Object)}
	 * when you return a hyperlink here.
	 * @param writer the calling writer
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return hyperlink object for the cell
	 */
	public Hyperlink getHyperlink(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Finalizes the workbook.
	 * This method is called immediately before the {@link ExcelWriter} writes the
	 * complete workbook to the underlying output stream.
	 * This implementation just sets all columns to auto fit.
	 * @param writer the calling writer
	 * @param rowCount the number of rows in the selected sheet
	 * @param columnCount the number of columns modified in the selected sheet
	 */
	@Override
	public void finalize(ExcelWriter writer, int rowCount, int columnCount) {
		Sheet sheet = writer.getSheet();
		for (int i=0; i<=columnCount; i++) sheet.autoSizeColumn(i);
	}

	/**
	 * Returns the alignment to be used.
	 * This implementation returns null.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return alignment index for Excel or null if no alignment is required
	 */
	public Short getAlign(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Returns the display format.
	 * The format of the value. This implementation sets format for dates and numbers.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return format index for Excel or null if no formatting is required
	 * @see #getDateFormat(int, int, Object)
	 * @see #getIntegerFormat(int, int, Object)
	 * @see #getRealFormat(int, int, Object)
	 */
	public Short getFormat(ExcelWriter writer, int row, int column, Object value) {
		if (value instanceof Date) {
			CreationHelper createHelper = writer.getWorkbook().getCreationHelper();
			return createHelper.createDataFormat().getFormat(getDateFormat(row, column, value));
		}
		if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Short)) {
			DataFormat format = writer.getWorkbook().createDataFormat();
			return format.getFormat(getIntegerFormat(row, column, value));
		}
		if ((value instanceof Double) || (value instanceof Float)) {
			DataFormat format = writer.getWorkbook().createDataFormat();
			return format.getFormat(getRealFormat(row, column, value));
		}
		
	    return null;
	}

	/**
	 * Returns the default format for dates.
	 * This implementation returns {@link #DEFAULT_DATE_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_DATE_FORMAT
	 */
	public String getDateFormat(int row, int column, Object value) {
		return DEFAULT_DATE_FORMAT;
	}
	
	/**
	 * Returns the default format for shorts, integers and longs.
	 * This implementation returns {@link #DEFAULT_INTEGER_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_INTEGER_FORMAT
	 */
	public String getIntegerFormat(int row, int column, Object value) {
		return DEFAULT_INTEGER_FORMAT;
	}
	
	/**
	 * Returns the default format for real and float numbers.
	 * This implementation returns {@link #DEFAULT_REAL_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_REAL_FORMAT
	 */
	public String getRealFormat(int row, int column, Object value) {
		return DEFAULT_REAL_FORMAT;
	}
	/**
	 * Returns the background color for the specified cell.
	 * This implementation returns null (default background color).
	 * You can use IndexedColors.LIGHT_GREEN.getIndex() to return the color.
	 * Notice that background colors is somehow misleading as foreground and background
	 * color build up a cell's background (behind the text itself).
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return color index for Excel or null
	 */
	public Short getBackgroundColor(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Returns the fill pattern for the background.
	 * This implementation returns CellStyle.SOLID_FOREGROUND if a foreground color was set.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return the fill pattern or null if default pattern shall be applied.
	 */
	public Short getFillPattern(ExcelWriter writer, int row, int column, Object value) {
		if (getForegroundColor(writer, row, column, value) != null) return CellStyle.SOLID_FOREGROUND;
		return null;
	}

	/**
	 * Returns the foreground color for the specified cell.
	 * This implementation returns null (default foreground color).
	 * You can use IndexedColors.LIGHT_GREEN.getIndex() to return the color.
	 * This is the correct implementation if you want to set the cell's color.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return color index for Excel or null
	 */
	public Short getForegroundColor(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Returns the correct font for the cell.
	 * This implementation will return bold font for the first row if required and
	 * hyperlink fonts for hyperlink cells.
	 * An overwritten implementation could look like this:
	 * <pre>
	 * font = writer.getWorkbook().createFont();
	 * font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	 * font.setColor(IndexedColors.BLACK.getIndex());
	 * font.setFontHeightInPoints((short)10);
	 * font.setFontName("Arial");
	 * </pre>
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return correct font or null if no special font is required.
	 * @see #getBoldFont(Workbook, int, int, Object)
	 * @see #getPlainFont(Workbook, int, int, Object)
	 * @see #getHyperlinkFont(Workbook, int, int, Object)
	 */
	public Font getFont(ExcelWriter writer, int row, int column, Object value) {
		if (emphasizeFirstRow && (row ==0)) return getBoldFont(writer.getWorkbook(), row, column, value);
		if (getHyperlink(writer, row, column, value) != null) {
			return getHyperlinkFont(writer.getWorkbook(), row, column, value);
		}
		return getPlainFont(writer.getWorkbook(), row, column, value);
	}
	
	/**
	 * Returns the font size to be used.
	 * @return the default font size
	 * @see #DEFAULT_FONT_SIZE
	 */
	public short getDefaultFontSize() {
		return defaultFontSize;
	}
	
	/**
	 * Returns the font color to be used in non-hyperlink cells.
	 * @return the font color
	 * @see #DEFAULT_FONT_COLOR
	 */
	public short getDefaultFontColor() {
		return defaultFontColor;
	}
	
	/**
	 * Returns the font color to be used for hyperlinks.
	 * @return the hyperlink color
	 */
	public short getDefaultHyperlinkColor() {
		return defaultHyperlinkColor;
	}
	
	/**
	 * Returns the font name to be used.
	 * @return the font name
	 */
	public String getDefaultFontName() {
		return defaultFontName;
	}
	
	/**
	 * Returns the setting of emphasizing the header row.
	 * @return the emphasizeFirstRow
	 */
	public boolean isEmphasizeFirstRow() {
		return emphasizeFirstRow;
	}

	/**
	 * Sets the property of emphasizing header rows.
	 * @param emphasizeFirstRow the emphasizeFirstRow to set
	 */
	public void setEmphasizeFirstRow(boolean emphasizeFirstRow) {
		this.emphasizeFirstRow = emphasizeFirstRow;
	}

	/**
	 * Returns the bold font used for header rows.
	 * This implementation returns the font returned by 
	 * {@link #getDefaultBoldFont(Workbook)}.
	 * @param row the row that this font will be used for
	 * @param column the column that this font will be used for
	 * @param value the value that this font will be used for
	 * @param workbook the workbook for creation
	 * @return the bold Font for this cell
	 * @see #getDefaultBoldFont(Workbook)
	 */
	public Font getBoldFont(Workbook workbook, int row, int column, Object value) {
		return getDefaultBoldFont(workbook);
	}

	/**
	 * Returns the default bold font.
	 * This implementation returns the font defined by {@link #getDefaultFontName()},
	 * {@link #getDefaultFontSize()} and {@link #getDefaultFontColor()} with bold weight.
	 * @param workbook workbook object for creation
	 * @return default bold font
	 */
	public Font getDefaultBoldFont(Workbook workbook) {
		if (defaultBoldFont == null) {
			defaultBoldFont = workbook.createFont();
			defaultBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			defaultBoldFont.setColor(getDefaultFontColor());
			defaultBoldFont.setFontHeightInPoints(getDefaultFontSize());
			defaultBoldFont.setFontName(getDefaultFontName());
		}
		return defaultBoldFont;
	}
	
	/**
	 * Returns the default font used for normal cells.
	 * This implementation returns the font defined by ,
	 * {@link #getDefaultPlainFont(Workbook)}.
	 * @param row the row that this font will be used for
	 * @param column the column that this font will be used for
	 * @param value the value that this font will be used for
	 * @param workbook the workbook for creation
	 * @return the font for this cell
	 */
	public Font getPlainFont(Workbook workbook, int row, int column, Object value) {
		return getDefaultPlainFont(workbook);
	}

	/**
	 * Returns the default font used for normal cells.
	 * This implementation returns the font defined by {@link #getDefaultFontName()},
	 * {@link #getDefaultFontSize()} and {@link #getDefaultFontColor()} with normal weight.
	 * @param workbook the workbook for creation
	 * @return the default plain font
	 */
	public Font getDefaultPlainFont(Workbook workbook) {
		if (defaultPlainFont == null) {
			defaultPlainFont = workbook.createFont();
			defaultPlainFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			defaultPlainFont.setColor(getDefaultFontColor());
			defaultPlainFont.setFontHeightInPoints(getDefaultFontSize());
			defaultPlainFont.setFontName(getDefaultFontName());
		}
		return defaultPlainFont;
	}
	
	/**
	 * Returns the font to be used for hyperlinks.
	 * This implementation returns the font defined by 
	 * {@link #getDefaultHyperlinkFont(Workbook)}. 
	 * @param workbook the workbook for creation
	 * @param row the row that this font will be used for
	 * @param column the column that this font will be used for
	 * @param value the value that this font will be used for
	 * @return font object
	 */
	public Font getHyperlinkFont(Workbook workbook, int row, int column, Object value) {
		return getDefaultHyperlinkFont(workbook);
	}
	
	/**
	 * Returns the font to be used for hyperlinks.
	 * This implementation returns the font defined by {@link #getDefaultFontName()},
	 * {@link #getDefaultFontSize()} and {@link #getDefaultHyperlinkColor()}. This font will
	 * be underlined with normal weight.
	 * @param workbook the workbook for creation
	 * @return font object for hyperlinks
	 */
	public Font getDefaultHyperlinkFont(Workbook workbook) {
		if (defaultHyperlinkFont == null) {
			defaultHyperlinkFont = workbook.createFont();
			defaultHyperlinkFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			defaultHyperlinkFont.setColor(getDefaultHyperlinkColor());
			defaultHyperlinkFont.setFontHeightInPoints(getDefaultFontSize());
			defaultHyperlinkFont.setFontName(getDefaultFontName());
			defaultHyperlinkFont.setUnderline(Font.U_SINGLE);
		}
		return defaultHyperlinkFont;
	}
	
	/**
	 * Sets the borders in the cell style.
	 * This implementation does nothing (no borders).
	 * If you want to set borders, you could do this:
	 * <pre>
	 *  short color = IndexedColors.BLACK.getIndex();
	 *  short thick = CellStyle.BORDER_THIN;
	 *  style.setBorderBottom(thick);
	 *  style.setBottomBorderColor(color);
	 *  style.setBorderLeft(thick);
	 *  style.setLeftBorderColor(color);
	 *  style.setBorderRight(thick);
	 *  style.setRightBorderColor(color);
	 *  style.setBorderTop(thick);
	 *  style.setTopBorderColor(color);
	 *  </pre>
	 * @param writer writer that requires the information
	 * @param style the cell style to be modified
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 */
	public void setBorders(ExcelWriter writer, CellStyle style, int row, int column, Object value) {		
	}
}
