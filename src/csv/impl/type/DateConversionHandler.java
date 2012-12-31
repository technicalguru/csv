/**
 * 
 */
package csv.impl.type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import csv.TypeConversionHandler;

/**
 * A conversion handler for dates. The default implementation
 * can recognize these date strings: dd/MM/yyyy, dd.MM.yyyy, 
 * dd/MM/yy, dd.MM.yy, yyyy/MM/dd, yyyy.MM.dd. Please not that
 * for a specific date string the first suitable format will be used.
 * @author ralph
 *
 */
public class DateConversionHandler implements TypeConversionHandler {

	public static final DateConversionHandler INSTANCE = new DateConversionHandler();
	
	private static final String DEFAULT_DATE_FORMATS[] = new String[] {
		"dd/MM/yyyy",
		"dd.MM.yyyy",
		"dd/MM/yy",
		"dd.MM.yy",
		"yyyy/MM/dd",
		"yyyy.MM.dd"
	};
	
	private String parsingFormats[] = null;
	private DateFormat parsingFormatters[] = null;
	private String printFormat = null;
	private DateFormat printFormatter = null;
	
	/**
	 * Constructor.
	 */
	public DateConversionHandler() {
	}

	/**
	 * Returns th type java.util.Date.
	 * @see csv.TypeConversionHandler#getTypes()
	 */
	@Override
	public String[] getTypes() {
		return new String[] { "java.util.Date" };
	}

	/**
	 * Converts string to date.
	 * This method tries to parse the given string by checking each
	 * possible date format. If no format applies then the original
	 * string will be returned
	 * @param s string to be parsed
	 * @return date 
	 * @see csv.TypeConversionHandler#toObject(java.lang.String)
	 */
	@Override
	public Object toObject(String s) {
		// Get all parsing formats and return the first possible
		DateFormat formats[] = getParsingFormatters();
		for (DateFormat format : formats) {
			try {
				Date rc = format.parse(s);
				
				// Return the date if successful
				return rc;
			} catch (ParseException e) {
				// Ignore, just try next
			}
		}
		
		// Return the original value
		return s;
	}

	/**
	 * Converts the date to its string representation.
	 * @param o date to be converted
	 * @return string representation of date
	 * @see csv.TypeConversionHandler#toString(java.lang.Object)
	 */
	@Override
	public String toString(Object o) {
		if (o == null) return null;
		
		// Assuming this is a date
		if (o instanceof Date) {
			return getPrintFormatter().format((Date)o);
		}
		
		// Return from toString method
		return o.toString();
	}

	/**
	 * Returns the date formatters created from our date formatters.
	 * @return array of formatters to be used (never null!)
	 */
	public DateFormat[] getParsingFormatters() {
		if (parsingFormatters == null) {
			String formats[] = getParsingFormats();
			parsingFormatters = new DateFormat[formats.length];
			for (int i=0; i<formats.length; i++) {
				parsingFormatters[i] = new SimpleDateFormat(formats[i]);
			}
		}
		return parsingFormatters;
	}
	
	/**
	 * Returns the formats that will be used.
	 * This method returns default formats when no formats were set
	 * @return the format strings used for parsing dates (never null!).
	 */
	public String[] getParsingFormats() {
		if (parsingFormats == null) parsingFormats = DEFAULT_DATE_FORMATS;
		return parsingFormats;
	}

	/**
	 * Sets the parsing date formats to be used.
	 * @param parsingFormats the parsingFormats to set
	 */
	public void setParsingFormats(String[] parsingFormats) {
		this.parsingFormats = parsingFormats;
		parsingFormatters = null;
		printFormatter = null;
	}

	/**
	 * Returns the printing format.
	 * This method will return the first parsing format if no format was set.
	 * @return the printFormat
	 */
	public String getPrintFormat() {
		if (printFormat == null) printFormat = getParsingFormats()[0];
		return printFormat;
	}

	/**
	 * Sets the format used for printing.
	 * @param printFormat the printFormat to set
	 */
	public void setPrintFormat(String printFormat) {
		this.printFormat = printFormat;
		printFormatter = null;
	}

	/**
	 * Returns the print formatter created from the print format.
	 * @return print formatter
	 */
	public DateFormat getPrintFormatter() {
		if (printFormatter == null) printFormatter = new SimpleDateFormat(getPrintFormat());
		return printFormatter;
	}
	
}
