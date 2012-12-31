/**
 * 
 */
package csv;

import csv.impl.CSVReader;
import csv.impl.CSVWriter;
import csv.impl.ExcelReader;
import csv.impl.ExcelWriter;


/**
 * Contains information about what reader and writer classes are responsible
 * for a specific MIME type.
 * @author Ralph Schuster
 *
 */
public class MimeTypeInfo {

	/**
	 * The default information for CSV files.
	 */
	public static final MimeTypeInfo CSV_INFO = new MimeTypeInfo(
			new String[] {
					"text/csv",
					"text/comma-separated-values"
			}, 
			CSVReader.class, 
			CSVWriter.class
	);
	/**
	 * The default information for Excel files.
	 */
	public static final MimeTypeInfo EXCEL_INFO = new MimeTypeInfo(
			new String[] {
					"application/excel",
					"application/x-excel",
					"application/x-msexcel",
					"application/vnd.ms-excel"
			}, 
			ExcelReader.class, 
			ExcelWriter.class
	);
	private String mimeTypes[];
	private Class<? extends TableReader> readerClass;
	private Class<? extends TableWriter> writerClass;
	
	/**
	 * Constructor.
	 * @param mimeType MIME type being registered
	 * @param readerClass class responsible for reading such files
	 * @param writerClass class responsible for writing such files
	 */
	public MimeTypeInfo(String mimeType, Class<? extends TableReader> readerClass, Class<? extends TableWriter> writerClass) {
		this(new String[] { mimeType }, readerClass, writerClass);
	}

	/**
	 * Constructor.
	 * @param mimeTypes multiple MIME types being registered
	 * @param readerClass class responsible for reading such files
	 * @param writerClass class responsible for writing such files
	 */
	public MimeTypeInfo(String mimeTypes[], Class<? extends TableReader> readerClass, Class<? extends TableWriter> writerClass) {
		this.mimeTypes = mimeTypes;
		this.readerClass = readerClass;
		this.writerClass = writerClass;
	}

	/**
	 * Returns the MIME types that this information provides information for.
	 * @return the mimeTypes
	 */
	public String[] getMimeTypes() {
		return mimeTypes;
	}

	/**
	 * Returns the responsible reader class.
	 * @return the readerClass
	 */
	public Class<? extends TableReader> getReaderClass() {
		return readerClass;
	}

	/**
	 * Returns the responsible writer class.
	 * @return the writerClass
	 */
	public Class<? extends TableWriter> getWriterClass() {
		return writerClass;
	}

	
}
