/**
 * 
 */
package csv.impl;

import java.util.Date;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author U434983
 *
 */
public class DefaultExcelFormatter implements ExcelFormatter {

	public static final ExcelFormatter INSTANCE = new DefaultExcelFormatter();

	/**
	 * Default constructor
	 */
	public DefaultExcelFormatter() {
	}

	/**
	 * Returns the cell style.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return
	 */
	@Override
	public CellStyle getStyle(ExcelWriter writer, int row, int column, Object value) {
		CellStyle rc = writer.getWorkbook().createCellStyle();
		if (value instanceof String) rc.setWrapText(true);
		
		// data format
		Short format = getFormat(writer, row, column, value);
		if (format != null) rc.setDataFormat(format);
		
		// Colors
		Short bg = getBackgroundColor(writer, row, column, value);
		if (bg != null) rc.setFillBackgroundColor(bg);
		Short fill = getFillPattern(writer, row, column, value);
		if (fill != null) rc.setFillPattern(fill);
		Short fg = getBackgroundColor(writer, row, column, value);
		if (fg != null) rc.setFillForegroundColor(fg);
		
		// Font
		Font font = getFont(writer, row, column, value);
		if (font != null) rc.setFont(font);
		
		// Borders
		setBorders(writer, rc, row, column, value);
		
		// Alignment
		Short align = getAlign(writer, row, column, value);
		if (align != null) rc.setAlignment(align);
		
		
		return rc;
	}
	
	/**
	 * Note that you should return a blue underlined font in {@link #getFont(ExcelWriter, int, int, Object)}
	 * when you return a hyperlink here.
	 * @see csv.impl.ExcelFormatter#getHyperlink(csv.impl.ExcelWriter, int, int, java.lang.Object)
	 */
	@Override
	public Hyperlink getHyperlink(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Just sets all column on auto fit.
	 */
	@Override
	public void finalize(ExcelWriter writer, int rowCount, int columnCount) {
		Sheet sheet = writer.getSheet();
		for (int i=0; i<=columnCount; i++) sheet.autoSizeColumn(i);
	}

	/**
	 * Returns the alignment
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return
	 */
	public Short getAlign(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Returns the format
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return
	 */
	public Short getFormat(ExcelWriter writer, int row, int column, Object value) {
		if (value instanceof Date) {
			CreationHelper createHelper = writer.getWorkbook().getCreationHelper();
			return createHelper.createDataFormat().getFormat("dd.mm.yyyy hh:mm");
		}
		if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Short)) {
			DataFormat format = writer.getWorkbook().createDataFormat();
			return format.getFormat("0");
		}
		if ((value instanceof Double) || (value instanceof Float)) {
			DataFormat format = writer.getWorkbook().createDataFormat();
			return format.getFormat("0.00");
		}
		
	    return null;
	}

	/**
	 * Use IndexedColors.LIGHT_GREEN.getIndex();
	 * @param writer
	 * @param row
	 * @param column
	 * @return
	 */
	public Short getBackgroundColor(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Use CellStyle.SOLID_FOREGROUND;
	 * @param writer
	 * @param row
	 * @param column
	 * @return
	 */
	public Short getFillPattern(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Use IndexedColors.LIGHT_GREEN.getIndex();
	 * @param writer
	 * @param row
	 * @param column
	 * @return
	 */
	public Short getForegroundColor(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Use:
	 * <pre>
	 * font = writer.getWorkbook().createFont();
	 * font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	 * font.setColor(IndexedColors.BLACK.getIndex());
	 * font.setFontHeightInPoints((short)10);
	 * font.setFontName("Arial");
	 * </pre>
	 * @param writer
	 * @param row
	 * @param column
	 * @return
	 */
	public Font getFont(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}
	
	/**
	 * Returns a plain black Arial font to be used for normal text.
	 * @param workbook workbook object
	 * @param fontSize font size in points
	 * @return font object
	 */
	public static Font getDefaultFont(Workbook workbook, short fontSize) {
		Font plainFont = workbook.createFont();
		plainFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		plainFont.setColor(IndexedColors.BLACK.getIndex());
		plainFont.setFontHeightInPoints(fontSize);
		plainFont.setFontName("Arial");
		return plainFont;
	}
	
	/**
	 * Returns a blue underlined Arial font to be used for hyperlinks.
	 * @param workbook workbook object
	 * @param fontSize font size in points
	 * @return font object
	 */
	public static Font getHyperlinkFont(Workbook workbook, short fontSize) {
		Font hyperlinkFont = workbook.createFont();
		hyperlinkFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
		hyperlinkFont.setFontHeightInPoints(fontSize);
		hyperlinkFont.setFontName("Arial");
		hyperlinkFont.setUnderline(Font.U_SINGLE);
		return hyperlinkFont;
	}
	
	/**
	 * Use this:
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
	 * @param writer
	 * @param style
	 * @param row
	 * @param column
	 */
	public void setBorders(ExcelWriter writer, CellStyle style, int row, int column, Object value) {		
	}
}
