/**
 * 
 */
package csv.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import csv.TableWriter;

/**
 * @author U434983
 *
 */
public abstract class AbstractTableWriter implements TableWriter {

    private PrintWriter writer;
    private OutputStream outputStream;
    
	/**
	 * 
	 */
	public AbstractTableWriter() {
		init();
	}

    /**
     * Create a new CSVWriter from an existing OutputStream.
     * @param out - the stream to write CSV values to.
     */
    public AbstractTableWriter(OutputStream out) {
        setOutputStream(out);
		init();
    }
    
    /**
     * Create a new CSVWriter, with automatic line flushing.
     * @param file - file to write CSV data to.
     * @throws IOException - when the file could not be created
     */
    public AbstractTableWriter(File file) throws IOException {
        this(new FileOutputStream(file));
    }
    
    /**
     * Create a new CSVWriter, with automatic line flushing.
     * @param file - file to write CSV data to.
     * @throws IOException - when the file could not be created
     */
    public AbstractTableWriter(String file) throws IOException {
        this(new FileOutputStream(file));
    }
    
	/**
	 * @see csv.TableWriter#setOutputStream(java.io.OutputStream)
	 */
	@Override
	public void setOutputStream(OutputStream out) {
		if (outputStream != null) throw new IllegalArgumentException("Output stream already set");
		this.outputStream = out;
	}

	/**
	 * General initialization.
	 */
	protected void init() {
	}
	
	/**
	 * @return the writer
	 */
	public PrintWriter getWriter() {
		if (writer == null) writer = new PrintWriter(outputStream);
		return writer;
	}

	/**
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Does nothing by default.
	 */
	public void printComment(String comment) throws IOException {
	}
	
	/**
	 * Closes the writer.
	 */
	public void close() {
		try {
			getOutputStream().close();
			if (writer != null) writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
