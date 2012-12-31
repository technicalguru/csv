/**
 * 
 */
package csv;

import csv.impl.CSVReader;
import csv.impl.CSVWriter;
import csv.impl.ExcelReader;
import csv.impl.ExcelWriter;


/**
 * @author U434983
 *
 */
public class MimeTypeInfo {

	public static final MimeTypeInfo CSV_INFO = new MimeTypeInfo(
			new String[] {
					"text/csv",
					"text/comma-separated-values"
			}, 
			CSVReader.class, 
			CSVWriter.class
	);
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
	 * Default Constructor
	 */
	public MimeTypeInfo(String mimeType, Class<? extends TableReader> readerClass, Class<? extends TableWriter> writerClass) {
		this(new String[] { mimeType }, readerClass, writerClass);
	}

	/**
	 * Default Constructor
	 */
	public MimeTypeInfo(String mimeTypes[], Class<? extends TableReader> readerClass, Class<? extends TableWriter> writerClass) {
		this.mimeTypes = mimeTypes;
		this.readerClass = readerClass;
		this.writerClass = writerClass;
	}

	/**
	 * @return the mimeTypes
	 */
	public String[] getMimeTypes() {
		return mimeTypes;
	}

	/**
	 * @return the readerClass
	 */
	public Class<? extends TableReader> getReaderClass() {
		return readerClass;
	}

	/**
	 * @return the writerClass
	 */
	public Class<? extends TableWriter> getWriterClass() {
		return writerClass;
	}

	
}
