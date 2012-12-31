/**
 * 
 */
package csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

/**
 * @author U434983
 *
 */
public class CSVFactory {

	private Map<String, Class<? extends TableReader>> readers;
	private Map<String, Class<? extends TableWriter>> writers;
	
	private static CSVFactory factory = null;
	
	/**
	 * Returns the factory for rading/writing tables.
	 * @return factory
	 */
	public static CSVFactory getFactory() {
		if (factory == null) {
			factory = new CSVFactory();
		}
		return factory;
	}
	
	/**
	 * Creates the factory and initializes.
	 */
	protected CSVFactory() {
		init();
	}

	/**
	 * Initializes teh factory.
	 */
	protected void init() {
		initReaderMap();
		initWriterMap();
		register(MimeTypeInfo.CSV_INFO);
		register(MimeTypeInfo.EXCEL_INFO);
	}
	
	/**
	 * Initializes the reader map.
	 */
	protected void initReaderMap() {
		if (readers != null) return;
		readers = new HashMap<String, Class<? extends TableReader>>();
	}
	
	/**
	 * Initializes the writer map.
	 */
	protected void initWriterMap() {
		if (writers != null) return;
		writers = new HashMap<String, Class<? extends TableWriter>>();
	}
	
	/**
	 * Registers a new MIME type.
	 * @param mimeTypeInfo the infor to register
	 */
	public void register(MimeTypeInfo mimeTypeInfo) {
		String types[] = mimeTypeInfo.getMimeTypes();
		Class<? extends TableReader> reader = mimeTypeInfo.getReaderClass();
		Class<? extends TableWriter> writer = mimeTypeInfo.getWriterClass();
		for (int i=0; i<types.length; i++) {
			if (reader != null) readers.put(types[i], reader);
			if (writer != null) writers.put(types[i], writer);
		}
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file filename
	 * @return reader class instance to be used
	 */
	public TableReader getReader(String file)  throws IOException {
		return getReader(new File(file));
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file file
	 * @return reader class instance to be used
	 */
	public TableReader getReader(File file) throws IOException {
		String mimeType = getMimeType(file);
		if (mimeType == null) throw new IllegalArgumentException("No MIME type found: "+file.getAbsolutePath());
		TableReader reader = getMimeTypeReader(mimeType);
		reader.setInputStream(new FileInputStream(file));
		return reader;
	}
	
	/**
	 * Returns a reader for the given MIME type.
	 * @param mimeType MIME type
	 * @return reader to be used
	 */
	public TableReader getMimeTypeReader(String mimeType) {
		if (mimeType == null) throw new IllegalArgumentException("NULL MIME type");
		Class<? extends TableReader> readerClass = readers.get(mimeType);
		if (readerClass == null) throw new IllegalArgumentException("Cannot find reader class for: "+mimeType);
		try {
			TableReader reader = readerClass.newInstance();
			return reader;
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create reader instance: ", e);
		}		
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file filename
	 * @return reader class instance to be used
	 */
	public TableWriter getWriter(String file) throws IOException {
		return getWriter(new File(file));
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file file
	 * @return reader class instance to be used
	 */
	public TableWriter getWriter(File file) throws IOException {
		String mimeType = getMimeType(file);
		if (mimeType == null) throw new IllegalArgumentException("No MIME type found: "+file.getAbsolutePath());
		TableWriter writer = getMimeTypeWriter(mimeType);
		writer.setOutputStream(new FileOutputStream(file));
		return writer;
	}
	
	/**
	 * Returns a writer for the given MIME type.
	 * @param mimeType MIME type
	 * @return writer to be used
	 */
	public TableWriter getMimeTypeWriter(String mimeType) {
		if (mimeType == null) throw new IllegalArgumentException("NULL MIME type");
		Class<? extends TableWriter> writerClass = writers.get(mimeType);
		if (writerClass == null) throw new IllegalArgumentException("Cannot find writer class for: "+mimeType);
		try {
			TableWriter writer = writerClass.newInstance();
			return writer;
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create writer instance: ", e);
		}		
	}
	
	/**
	 * Returns the MIME type for the given file.
	 * @param file file to check
	 * @return MIME type of file
	 */
	public String getMimeType(File file) {
		return new MimetypesFileTypeMap().getContentType(file);
	}
}
